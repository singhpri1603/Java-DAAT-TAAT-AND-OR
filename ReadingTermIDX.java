import java.io.*;
import java.util.*;

public class ReadingTermIDX {
	int k=5;
	
	PrintWriter writer;
	
	ReadingTermIDX(PrintWriter writer, int k){
		this.writer=writer;
		this.k=k;
	}
	
	String line = null;
	
	HashMap<String, Value> reading(String file)
	{
		ArrayList<kterms> topk=new ArrayList<kterms>();
		HashMap<String, Value> index= new HashMap<String, Value>();
		try{
			FileReader fileReader=new FileReader(file);
			BufferedReader br=new BufferedReader(fileReader);
			
			
			while ((line=br.readLine())!=null)
			{
				
				//System.out.println(line);
				String[] part1=line.split("\\\\c");
				String term = part1[0];
				
				String[] part2= part1[1].split("\\\\m");
				int noOfDocs=Integer.parseInt(part2[0]);
				
				//making array list for top k terms
				
				kterms kt =new kterms(noOfDocs,term);
				topk.add(kt);
				Value values=new Value(noOfDocs);
				
				String docsRaw=part2[1];
				//System.out.println(term+"\n"+noOfDocs);//+"\n"+docsRaw);
				String[] split1=docsRaw.split("\\[");
				String[] split2=split1[1].split("\\]");
				String docs = split2[0];
				
				int posting[][]=new int[noOfDocs][2];
				
				//System.out.println(docs);
				//LinkedList<LLNode> list = new LinkedList<LLNode>();
				String[] docsplit=docs.split(", ");
				for(int i =0; i<docsplit.length;i++){
					String[] splitagain=docsplit[i].split("/");
					int docID=Integer.parseInt(splitagain[0]);
					int frequency=Integer.parseInt(splitagain[1]);
					LLNode node = new LLNode(docID,frequency);
					values.addDocID(node);
					
					//Adding to the array
					posting[i][0]=docID;
					posting[i][1]=frequency;
					//list.add(node);
				}
				
				//for(int j=0;j<posting.length;j++)
					//System.out.print(" "+posting[j][0]+" "+posting[j][1]);
				//System.out.println();
				
				
				//Sorting the array
				
			    int tmp1;
			    int tmp2;
			    for(int j=0;j<posting.length-1;j++){
			    	for (int i = 1; i < posting.length - j; i++) {
			            if (posting[i-1][1] < posting[i][1]) {
			                tmp1 = posting[i-1][0];
			                tmp2 = posting[i-1][1];
			                posting[i-1][0] = posting[i][0];
			                posting[i-1][1] = posting[i][1];
			                posting[i][0] = tmp1;
			                posting[i][1] = tmp2;
			                
			            }
			        }
			    }
			    //A new linked list with order of frequency
			    for(int i=0;i<posting.length;i++){
			    	LLNode node = new LLNode(posting[i][0],posting[i][1]);
					values.addFreq(node);
			    }
				//System.out.println(list);
				
				index.put(term, values);
				
			}
			
			br.close();
			//System.out.println(index);
			
			//calling top k
			Retrieving rt=new Retrieving(writer);
			rt.getTopK(topk,k);
		}
		
		catch(FileNotFoundException ex){
			System.out.println("file not found");
		}
		
		catch(IOException ex){
			System.out.println("Error");
		}
		
		return index;
	}
	

}
