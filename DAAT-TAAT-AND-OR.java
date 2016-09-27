import java.util.*;
import java.io.*;

public class CSE535Assignment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String termIDX= args[0];
		String output= args[1];
		int k=Integer.parseInt(args[2]);
		String query=args[3];
		
		//termIDX= "src/"+termIDX;
		//output= "src/"+output;
		//query= "src/"+query;
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(output, "UTF-8");
			
			
			HashMap<String, Value> index= new HashMap<String, Value>();
			ReadingTermIDX demo = new ReadingTermIDX(writer, k);
			index=demo.reading(termIDX);
			
			try{
				FileReader fileReader=new FileReader(query);
				BufferedReader br=new BufferedReader(fileReader);
				String line=null;
				while ((line=br.readLine())!=null){
					String[] queryTermOld=line.split(" ");
					String[] queryTermOld1=new String[queryTermOld.length];
					
					for(int i=0; i<queryTermOld.length;i++)
						queryTermOld1[i]=queryTermOld[i];
					
					Retrieving rt=new Retrieving(writer);
					for(int i=0;i<queryTermOld1.length;i++)
						queryTermOld1[i]=rt.getPostings(index, queryTermOld1[i]);
					List<String> list = new ArrayList<String>();

				    for(String s : queryTermOld1) {
				       if(s != null && s.length() > 0) {
				          list.add(s);
				       }
				    }

				    String[] queryTerm = list.toArray(new String[list.size()]);
					if(queryTerm.length==0){
						writer.print("FUNCTION: termAtATimeQueryAnd ");
						for(int i=0; i<queryTermOld.length;i++){
							if(i==queryTermOld.length-1)
								writer.println(queryTermOld[i]);
							else
								writer.print(queryTermOld[i]+", ");
						}	
						writer.println("terms not found");
						
						writer.print("FUNCTION: termAtATimeQueryOr ");
						for(int i=0; i<queryTermOld.length;i++){
							if(i==queryTermOld.length-1)
								writer.println(queryTermOld[i]);
							else
								writer.print(queryTermOld[i]+", ");
						}	
						writer.println("terms not found");
						
						writer.print("FUNCTION: docAtATimeQueryAnd ");
						for(int i=0; i<queryTermOld.length;i++){
							if(i==queryTermOld.length-1)
								writer.println(queryTermOld[i]);
							else
								writer.print(queryTermOld[i]+", ");
						}	
						writer.println("terms not found");
						
						writer.print("FUNCTION: docAtATimeQueryOr ");
						for(int i=0; i<queryTermOld.length;i++){
							if(i==queryTermOld.length-1)
								writer.println(queryTermOld[i]);
							else
								writer.print(queryTermOld[i]+", ");
						}	
						writer.println("terms not found");
					}
					
					else if(queryTermOld.length>queryTerm.length){
						
						writer.print("FUNCTION: termAtATimeQueryAnd ");
						for(int i=0; i<queryTermOld.length;i++){
							if(i==queryTermOld.length-1)
								writer.println(queryTermOld[i]);
							else
								writer.print(queryTermOld[i]+", ");
						}	
						
						writer.println("terms not found");
						
						rt.termAtATimeQueryOr(index,queryTerm, queryTermOld);
						
						writer.print("FUNCTION: docAtATimeQueryAnd ");
						for(int i=0; i<queryTermOld.length;i++){
							if(i==queryTermOld.length-1)
								writer.println(queryTermOld[i]);
							else
								writer.print(queryTermOld[i]+", ");
						}	
						
						writer.println("terms not found");
						
						rt.docAtATimeQueryOr(index, queryTerm, queryTermOld);
						
					}
					
					else{
				
					rt.termAtATimeQueryAnd(index, queryTerm);
					rt.termAtATimeQueryOr(index,queryTerm, queryTermOld);
					rt.docAtATimeQueryAnd(index, queryTerm);
					rt.docAtATimeQueryOr(index, queryTerm, queryTermOld);
					}
					
				}
				br.close();
			}
			catch(FileNotFoundException ex){
				System.out.println("file not found");
			}
			
			catch(IOException ex){
				System.out.println("Error");
			}
			
			
			
			
			writer.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
