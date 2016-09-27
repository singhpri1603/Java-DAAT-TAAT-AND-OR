import java.util.*;
import java.io.*;

public class Retrieving {
	
	PrintWriter writer;
	
	int taatAndCounter=0;
	int taatAndOptCounter=0;
	int taatOrCOunter=0;
	int taatOrOptCounter=0;
	int daatAndCounter=0;
	int daatOrCounter=0;
	
	Retrieving(PrintWriter writer){
		this.writer=writer;
	}
	
	Retrieving(){
		
	}
	
	//////get top k//////
	
	void getTopK(ArrayList<kterms> topk, int k){
		Collections.sort(topk, new CustomComparator());
		//System.out.println(topk);
		//System.out.println("FUNCTION: getTopK "+k);
		writer.println("FUNCTION: getTopK "+k);
		//System.out.print("Result:");
		writer.print("Result:");
		for(int i=0;i<k;i++){
			if(i==k-1)
				writer.println(topk.get(i).term);
			else
				writer.print(topk.get(i).term+", ");
		}
		//System.out.println();
		//writer.println();
	}
	
	
	/////get postings/////
	
	
	String getPostings(HashMap<String, Value> index, String queryTerm){
		String returning;
		Value value = index.get(queryTerm);
		
		if(value==null){
			writer.println("FUNCTION: getPostings "+queryTerm);
			//System.out.println("term not found");
			writer.println("term not found");
			returning=null;
		}
		else{
		
			LinkedList<LLNode> sortedDocID=value.sortedDocID;
			LinkedList<LLNode> sortedFreq=value.sortedFreq;
			//System.out.println();
			//writer.println();
			//System.out.println("FUNCTION: getPostings "+queryTerm);
			writer.println("FUNCTION: getPostings "+queryTerm);
			
			
			
			//System.out.print("Ordered by doc IDs: ");
			writer.print("Ordered by doc IDs: ");
			for(int i=0;i<sortedDocID.size();i++){
                if(i==sortedDocID.size()-1)
                    //System.out.print(sortedDocID.get(i).docID+", ");
                    writer.println(sortedDocID.get(i).docID);
                else
                    writer.print(sortedDocID.get(i).docID+", ");
			}
			//System.out.println();
			//writer.println();
			
			//System.out.print("Ordered by TF: ");
			writer.print("Ordered by TF: ");
			for(int i=0;i<sortedFreq.size();i++){
                if(i==sortedDocID.size()-1)
                    //System.out.print(sortedDocID.get(i).docID+", ");
                    writer.println(sortedFreq.get(i).docID);
                else
                    writer.print(sortedFreq.get(i).docID+", ");
			}
			//System.out.println("\n\n");
			//writer.println("\n\n");
			
			returning=queryTerm;
		}
		
		return returning;
		
	}
	
	
	/////term at a time AND/////
	
	
	void termAtATimeQueryAnd(HashMap<String, Value> index, String[] queryTerm){
		long startTime=System.nanoTime();
		if(queryTerm.length>0){
			LinkedList<LLNode> and=index.get(queryTerm[0]).sortedFreq;
			
			for (int i=1;i<queryTerm.length;i++){
				LinkedList<LLNode> list=index.get(queryTerm[i]).sortedFreq;
				and=andQuery(and,list);
			}
			
			ArrayList<kterms> al=new ArrayList<kterms>();
			for(int i=0;i<queryTerm.length;i++){
				kterms kt = new kterms(index.get(queryTerm[i]).noOfDocs,queryTerm[i]);
				al.add(kt);
			}
			
			Collections.sort(al, new CustomComparator2());
			
			//System.out.println(al);
			
			LinkedList<LLNode> and2=index.get(al.get(0).term).sortedFreq;
			
			for (int i=1;i<al.size();i++){
				LinkedList<LLNode> list=index.get(al.get(i).term).sortedFreq;
				and2=andOptQuery(and2,list);
			}
			
			/// sorting the and list
			
			Collections.sort(and, new CustomComparator3());
			
			long timeElapsed=System.nanoTime()-startTime;
			float time= (float)timeElapsed;
			time=time/1000000000;
			
			/*System.out.print("FUNCTION: termAtATimeQueryAnd ");
			for(int i=0; i<queryTerm.length;i++){
				if(i==queryTerm.length-1)
					System.out.println(queryTerm[i]);
				else
					System.out.print(queryTerm[i]+", ");
			}*/
			writer.print("FUNCTION: termAtATimeQueryAnd ");
			for(int i=0; i<queryTerm.length;i++){
				if(i==queryTerm.length-1)
					writer.println(queryTerm[i]);
				else
					writer.print(queryTerm[i]+", ");
			}	
			
			//System.out.println(and.size()+" documents are found");
			writer.println(and.size()+" documents are found");
			//System.out.println(taatAndCounter+" comparisons are made");
			writer.println(taatAndCounter+" comparisons are made");
			//System.out.println(time+" seconds are used");
			writer.println(time+" seconds are used");
			//System.out.println(taatAndOptCounter+" comparisons are made with optimization (optional bonus part)");
			writer.println(taatAndOptCounter+" comparisons are made with optimization (optional bonus part)");
			
			
			/*System.out.print("Result: ");
			for(int i=0; i<and.size();i++){
				if(i==and.size()-1)
					System.out.println(and.get(i).docID);
				else
					System.out.print(and.get(i).docID+", ");
			}
			*/
			writer.print("Result: ");
			for(int i=0; i<and.size();i++){
				if(i==and.size()-1)
					writer.println(and.get(i).docID);
				else
					writer.print(and.get(i).docID+", ");
			}
		
		
			
			/*System.out.print(and2);
			writer.print(and2);
			System.out.println("\n\n");
			writer.println("\n\n");*/
			//System.out.println(index.get(al.get(0).term).sortedFreq);
		}
	}
	
	LinkedList<LLNode> andQuery(LinkedList<LLNode> list1,LinkedList<LLNode> list2){
		LinkedList<LLNode> and= new LinkedList<LLNode>();
		
		for(int i =0;i<list1.size();i++){
			for(int j=0;j<list2.size();j++){
				taatAndCounter++;
				if(list1.get(i).docID==list2.get(j).docID){
					and.add(list1.get(i));
					break;
				}
			}
		}
		
		return and;
	}
	
	LinkedList<LLNode> andOptQuery(LinkedList<LLNode> list1,LinkedList<LLNode> list2){
		LinkedList<LLNode> and= new LinkedList<LLNode>();
		
		for(int i =0;i<list1.size();i++){
			for(int j=0;j<list2.size();j++){
				taatAndOptCounter++;
				if(list1.get(i).docID==list2.get(j).docID){
					and.add(list1.get(i));
					break;
				}
			}
		}
		
		return and;
	}
	

	/////term at a time OR/////
	
	void termAtATimeQueryOr(HashMap<String, Value> index, String[] queryTerm, String[] queryTermOld){
		long startTime= System.nanoTime();
		if(queryTerm.length>0){
			LinkedList<LLNode> or=index.get(queryTerm[0]).sortedFreq;
			
			for (int i=1;i<queryTerm.length;i++){
				LinkedList<LLNode> list=index.get(queryTerm[i]).sortedFreq;
				or=orQuery(or,list);
			}
			/*System.out.println("FUNCTION: termAtATimeQueryOr "+queryTerm);
			writer.println("FUNCTION: termAtATimeQueryOr "+queryTerm);
			System.out.println("Result: "+or);
			writer.println("Result: "+or);*/
			
			ArrayList<kterms> al=new ArrayList<kterms>();
			for(int i=0;i<queryTerm.length;i++){
				kterms kt = new kterms(index.get(queryTerm[i]).noOfDocs,queryTerm[i]);
				al.add(kt);
			}
			
			Collections.sort(al, new CustomComparator2());
			
			//System.out.println(al);
			
			LinkedList<LLNode> or2=index.get(al.get(0).term).sortedFreq;
			
			for (int i=1;i<al.size();i++){
				LinkedList<LLNode> list=index.get(al.get(i).term).sortedFreq;
				or2=orOptQuery(or2,list);
			}
			
			Collections.sort(or, new CustomComparator3());
			
			long timeElapsed=System.nanoTime()-startTime;
			float time= (float)timeElapsed;
			time=time/1000000000;
			
			/*System.out.print("FUNCTION: termAtATimeQueryOr ");
			for(int i=0; i<queryTerm.length;i++){
				if(i==queryTerm.length-1)
					System.out.println(queryTerm[i]);
				else
					System.out.print(queryTerm[i]+", ");
			}*/
			writer.print("FUNCTION: termAtATimeQueryOr ");
			for(int i=0; i<queryTermOld.length;i++){
				if(i==queryTermOld.length-1)
					writer.println(queryTermOld[i]);
				else
					writer.print(queryTermOld[i]+", ");
			}	
			
			//System.out.println(or.size()+" documents are found");
			writer.println(or.size()+" documents are found");
			//System.out.println(taatOrCOunter+" comparisons are made");
			writer.println(taatOrCOunter+" comparisons are made");
			//System.out.println(time+" seconds are used");
			writer.println(time+" seconds are used");
			//System.out.println(taatOrOptCounter+" comparisons are made with optimization (optional bonus part)");
			writer.println(taatOrOptCounter+" comparisons are made with optimization (optional bonus part)");
			
			
			/*System.out.print("Result: ");
			for(int i=0; i<or.size();i++){
				if(i==or.size()-1)
					System.out.println(or.get(i).docID);
				else
					System.out.print(or.get(i).docID+", ");
			}*/
			
			writer.print("Result: ");
			for(int i=0; i<or.size();i++){
				if(i==or.size()-1)
					writer.println(or.get(i).docID);
				else
					writer.print(or.get(i).docID+", ");
			}
		
		
			
			/*System.out.print(or2);
			writer.print(or2);
			System.out.println("\n\n");
			writer.println("\n\n");*/
		}
	}
	
	LinkedList<LLNode> orQuery(LinkedList<LLNode> list1,LinkedList<LLNode> list2){
		
		
		for(int i =0;i<list2.size();i++){
			int x=0;
			for(int j=0;j<list1.size();j++){
				taatOrCOunter++;
				if(list1.get(j).docID==list2.get(i).docID)
					x=1;
			}
			if(x==0)
				list1.add(list2.get(i));
			
		}
		
		return list1;
	}
	
LinkedList<LLNode> orOptQuery(LinkedList<LLNode> list1,LinkedList<LLNode> list2){
		
		
		for(int i =0;i<list2.size();i++){
			int x=0;
			for(int j=0;j<list1.size();j++){
				taatOrOptCounter++;
				if(list1.get(j).docID==list2.get(i).docID)
					x=1;
			}
			if(x==0)
				list1.add(list2.get(i));
			
		}
		
		return list1;
	}
	
	//@SuppressWarnings("unused")
	/////Doc at a time query AND/////
	
	void docAtATimeQueryAnd(HashMap<String, Value> index, String[] queryTerm){
		
		if(queryTerm.length==1){
			long startTime=System.nanoTime();
			LinkedList<LLNode> and=new LinkedList<LLNode>();
			and= index.get(queryTerm[0]).sortedDocID;
			
			long timeElapsed=System.nanoTime()-startTime;
			float time= (float)timeElapsed;
			time=time/1000000000;
		    
		   /* System.out.print("FUNCTION: docAtATimeQueryAnd ");
			for(int i=0; i<queryTerm.length;i++){
				if(i==queryTerm.length-1)
					System.out.println(queryTerm[i]);
				else
					System.out.print(queryTerm[i]+", ");
			}*/
			writer.print("FUNCTION: docAtATimeQueryAnd ");
			for(int i=0; i<queryTerm.length;i++){
				if(i==queryTerm.length-1)
					writer.println(queryTerm[i]);
				else
					writer.print(queryTerm[i]+", ");
			}	
			
			//System.out.println(and.size()+" documents are found");
			writer.println(and.size()+" documents are found");
			//System.out.println(daatAndCounter+" comparisons are made");
			writer.println(daatAndCounter+" comparisons are made");
			//System.out.println(time+" seconds are used");
			writer.println(time+" seconds are used");
			
			
			/*System.out.print("Result: ");
			for(int i=0; i<and.size();i++){
				if(i==and.size()-1)
					System.out.println(and.get(i).docID);
				else
					System.out.print(and.get(i).docID+", ");
			}*/
			
			writer.print("Result: ");
			for(int i=0; i<and.size();i++){
				if(i==and.size()-1)
					writer.println(and.get(i).docID);
				else
					writer.print(and.get(i).docID+", ");
			}
			
		}
		
		else{
		long startTime=System.nanoTime();
		LinkedList<Integer> and=new LinkedList<Integer>();
		
		int[] position=new int[queryTerm.length];
		//ArrayList<ListIterator<LLNode>> iteratorList= new ArrayList<ListIterator<LLNode>>();
		int[][] trackingArray=new int[queryTerm.length][2];
		
		
		//filling list iterator
		for(int i=0;i<queryTerm.length;i++){
			//ListIterator<LLNode> listIterator= index.get(queryTerm[i]).sortedDocID.listIterator();
			//iteratorList.add(listIterator);
			position[i]=0;
		}
		
		//filling tracking array
		for(int i=0;i<queryTerm.length;i++){
			
			trackingArray[i][0]= index.get(queryTerm[i]).sortedDocID.get(0).docID;
			trackingArray[i][1]=i;
		}
		
		trackingArray=arraySort(trackingArray);
	    
	    and=daatAndQuery(index, and, position, trackingArray, queryTerm);
	    
	    long timeElapsed=System.nanoTime()-startTime;
		float time= (float)timeElapsed;
		time=time/1000000000;
	    
	    /*System.out.print("FUNCTION: docAtATimeQueryAnd ");
		for(int i=0; i<queryTerm.length;i++){
			if(i==queryTerm.length-1)
				System.out.println(queryTerm[i]);
			else
				System.out.print(queryTerm[i]+", ");
		}*/
		writer.print("FUNCTION: docAtATimeQueryAnd ");
		for(int i=0; i<queryTerm.length;i++){
			if(i==queryTerm.length-1)
				writer.println(queryTerm[i]);
			else
				writer.print(queryTerm[i]+", ");
		}	
		
		//System.out.println(and.size()+" documents are found");
		writer.println(and.size()+" documents are found");
		//System.out.println(daatAndCounter+" comparisons are made");
		writer.println(daatAndCounter+" comparisons are made");
		//System.out.println(time+" seconds are used");
		writer.println(time+" seconds are used");
		
		
		/*System.out.print("Result: ");
		for(int i=0; i<and.size();i++){
			if(i==and.size()-1)
				System.out.println(and.get(i).intValue());
			else
				System.out.print(and.get(i).intValue()+", ");
		}*/
		
		writer.print("Result: ");
		for(int i=0; i<and.size();i++){
			if(i==and.size()-1)
				writer.println(and.get(i).intValue());
			else
				writer.print(and.get(i).intValue()+", ");
		}
		}
	
	  /*  for(int i=0;i<and.size();i++){
	    	System.out.print(and.get(i).toString()+" ");
	    	writer.print(and.get(i).toString()+" ");
	    	
	    }
	    System.out.println();
	    writer.println();*/
	    
	}
	    
	
	//daat and query function
	
	LinkedList<Integer> daatAndQuery(HashMap<String, Value> index, LinkedList<Integer> and, int[] position, int[][] trackingArray, String[] queryTerm){
		
	    boolean x=false;
	    
	    //check if all terms are equal
	    for(int i=0; i<trackingArray.length-1;i++){
	    	daatAndCounter++;
	    	if(trackingArray[i][0]==trackingArray[i+1][0]){
	    		if(i==trackingArray.length-2)
	    			x=true;
	    	}
	    	else
	    		break;
	    }
	    
	    
	    //if all terms equal... add to and list.. increment iterator and refill array
	    if(x==true){
	    	and.add(trackingArray[0][0]);
	    	boolean y=true;
	    	for(int i=0;i<trackingArray.length;i++){
	    		if(position[i]>=index.get(queryTerm[i]).sortedDocID.size()-1){
	    			y=false;
	    			break;
	    		}
	    	}
	    	if(y){
	    		for(int i=0;i<trackingArray.length;i++){
	    			position[i]=position[i]+1;
	    			trackingArray[i][0]= index.get(queryTerm[i]).sortedDocID.get(position[i]).docID;
	    			
	    			//LLNode temp2=iteratorList.get(trackingArray[i][1]).previous();
	    			//iteratorList.get(trackingArray[i][1]).remove();
	    			//trackingArray[i][0]=temp.docID;
	    		}
	    		trackingArray=arraySort(trackingArray);
	    		and=daatAndQuery(index, and, position, trackingArray, queryTerm);
	    	}
	    	
	    	//and=daatAndQuery(index, and, position, trackingArray, queryTerm);
	    }
	    
	    
	    //if all terms not equal.. increment iterator of smallest docid and place next docid in array
	    else{
	    	int a=trackingArray[0][1];
	    	
	    	if(position[a]<index.get(queryTerm[a]).sortedDocID.size()-1){
	    	//if(iteratorList.get(trackingArray[0][1]).hasNext()){
	    		//LLNode temp= index.get(queryTerm[trackingArray[0][1]]).sortedDocID.get(position[trackingArray[0][1]]+1).docID;
	    		//LLNode temp= iteratorList.get(trackingArray[0][1]).next();
	    		position[a]=position[a]+1;
	    		trackingArray[0][0]=index.get(queryTerm[trackingArray[0][1]]).sortedDocID.get(position[a]).docID;
	    		trackingArray[0][1]=trackingArray[0][1];
	    		
	    		trackingArray=arraySort(trackingArray);
	    		
	    		/*for(int i=1;i<trackingArray.length;i++){// placing new docid at correct position in array
	    			daatAndCounter++;
	    			if(tempdocID<=trackingArray[i][0]&&i==1){
	    				
    					trackingArray[0][0]=tempdocID;
    					trackingArray[0][1]=tempindex;
    				}
    			
    			
    			 
    			else if(trackingArray[i][0]<=tempdocID){
    				if(i==trackingArray.length-1){
	    				int j=0;
    					for(j=1;j<=i;j++){
    						trackingArray[j-1][0]=trackingArray[j][0];
    						trackingArray[j-1][1]=trackingArray[j][1];
    					}
    					trackingArray[j-1][0]=tempdocID;
    					trackingArray[j-1][1]=tempindex;
	    			}
    				continue;
    			}
    			else if(trackingArray[i][0]<tempdocID&&i==1){
    				trackingArray[0][0]=trackingArray[1][0];
    				trackingArray[0][1]=trackingArray[1][1];
    				
    				trackingArray[1][0]=tempdocID;
    				trackingArray[1][1]=tempindex;
    			}
    			
    			else if(trackingArray[i][0]>tempdocID){
    					int j=0;
    					for(j=1;j<i;j++){
    						trackingArray[j-1][0]=trackingArray[j][0];
    						trackingArray[j-1][1]=trackingArray[j][1];
    					}
    					trackingArray[j-1][0]=tempdocID;
    					trackingArray[j-1][1]=tempindex;
    				}
    			break;
    				
	    				*/	
	    			
	    		
	    		
	    		and=daatAndQuery(index, and, position, trackingArray, queryTerm);
	    	}
	    }
	    
	    return and;
	    
	}
	
		//function for sorting array
	    
	    int[][] arraySort(int[][] trackingArray){
	    	int tmp1;
		    int tmp2;
		    for(int j=0;j<trackingArray.length;j++){
		    	for (int i = 1; i < trackingArray.length - j; i++) {
		            if (trackingArray[i-1][0] > trackingArray[i][0]) {
		                tmp1 = trackingArray[i-1][0];
		                tmp2 = trackingArray[i-1][1];
		                trackingArray[i-1][0] = trackingArray[i][0];
		                trackingArray[i-1][1] = trackingArray[i][1];
		                trackingArray[i][0] = tmp1;
		                trackingArray[i][1] = tmp2;
		            }
		    	}
		    }
		    
		    return trackingArray;
	    }
	    
	    
	    
	    /////Doc at a time OR/////
	    
	    void docAtATimeQueryOr(HashMap<String, Value> index, String[] queryTerm, String[] queryTermOld){
	    	if(queryTerm.length==1){
				long startTime=System.nanoTime();
				LinkedList<LLNode> and=new LinkedList<LLNode>();
				and= index.get(queryTerm[0]).sortedDocID;
				
				long timeElapsed=System.nanoTime()-startTime;
				float time= (float)timeElapsed;
				time=time/1000000000;
			    
			   /* System.out.print("FUNCTION: docAtATimeQueryOr ");
				for(int i=0; i<queryTerm.length;i++){
					if(i==queryTerm.length-1)
						System.out.println(queryTerm[i]);
					else
						System.out.print(queryTerm[i]+", ");
				}*/
				writer.print("FUNCTION: docAtATimeQueryOr ");
				for(int i=0; i<queryTerm.length;i++){
					if(i==queryTerm.length-1)
						writer.println(queryTerm[i]);
					else
						writer.print(queryTerm[i]+", ");
				}	
				
				//System.out.println(and.size()+" documents are found");
				writer.println(and.size()+" documents are found");
				//System.out.println(daatAndCounter+" comparisons are made");
				writer.println(daatAndCounter+" comparisons are made");
				//System.out.println(time+" seconds are used");
				writer.println(time+" seconds are used");
				
				
				/*System.out.print("Result: ");
				for(int i=0; i<and.size();i++){
					if(i==and.size()-1)
						System.out.println(and.get(i).docID);
					else
						System.out.print(and.get(i).docID+", ");
				}*/
				
				writer.print("Result: ");
				for(int i=0; i<and.size();i++){
					if(i==and.size()-1)
						writer.println(and.get(i).docID);
					else
						writer.print(and.get(i).docID+", ");
				}
				
			}
	    	else{
	    	
	    	long startTime=System.nanoTime();
			
			LinkedList<Integer> or=new LinkedList<Integer>();
			int[] position=new int[queryTerm.length];
			//ArrayList<ListIterator<LLNode>> iteratorList= new ArrayList<ListIterator<LLNode>>();
			int[][] trackingArray=new int[queryTerm.length][2];
			
			
			//filling list iterator
			for(int i=0;i<queryTerm.length;i++){
				//ListIterator<LLNode> listIterator= index.get(queryTerm[i]).sortedDocID.listIterator();
				//iteratorList.add(listIterator);
				position[i]=0;
			}
			
			//filling tracking array
			for(int i=0;i<queryTerm.length;i++){
				
				trackingArray[i][0]= index.get(queryTerm[i]).sortedDocID.get(0).docID;
				trackingArray[i][1]=i;
			}
			
			trackingArray=arraySort(trackingArray);
			
			int flag=trackingArray.length;
		    
		    or=daatOrQuery(index, or, position, trackingArray, queryTerm, flag);
		    
		    long timeElapsed=System.nanoTime()-startTime;
			float time= (float)timeElapsed;
			time=time/1000000000;
		    
		    
			writer.print("FUNCTION: docAtATimeQueryOr ");
			for(int i=0; i<queryTermOld.length;i++){
				if(i==queryTermOld.length-1)
					writer.println(queryTermOld[i]);
				else
					writer.print(queryTermOld[i]+", ");
			}	
			
			//System.out.println(or.size()+" documents are found");
			writer.println(or.size()+" documents are found");
			//System.out.println(daatOrCounter+" comparisons are made");
			writer.println(daatOrCounter+" comparisons are made");
			//System.out.println(time+" seconds are used");
			writer.println(time+" seconds are used");
			
			
			
			
			writer.print("Result: ");
			for(int i=0; i<or.size();i++){
				if(i==or.size()-1)
					writer.println(or.get(i).intValue());
				else
					writer.print(or.get(i).intValue()+", ");
			}
		
	    	}
		    
		   /* for(int i=0;i<or.size();i++){
		    	System.out.print(or.get(i).toString()+" ");
		    	writer.print(or.get(i).toString()+" ");*/
		    
		}
		    
		
		//daat or query function
		
		LinkedList<Integer> daatOrQuery(HashMap<String, Value> index, LinkedList<Integer> or, int[] position, int[][] trackingArray, String[] queryTerm, int flag){
			
		 
		    //if all terms not equal.. increment iterator of smallest docid and place next docid in array
		    if(trackingArray[0][0]>=0){
			if(or.size()==0)
				or.add(trackingArray[0][0]);
				
			if(trackingArray[0][0]!=or.getLast())
				or.add(trackingArray[0][0]);
		    }
				
		    	int a=trackingArray[0][1];
		    	
		    	if(flag==1){
		    		
			    		trackingArray[0][0]=index.get(queryTerm[trackingArray[0][1]]).sortedDocID.get(position[a]-1).docID;
			    		flag=flag-1;
			    		or=daatOrQuery(index, or, position, trackingArray, queryTerm, flag);
		    		
		    	}
		    	
		    	if(flag>1){
		    	
		    	if(position[a]<index.get(queryTerm[a]).sortedDocID.size()){
		    	//if(iteratorList.get(trackingArray[0][1]).hasNext()){
		    		//LLNode temp= index.get(queryTerm[trackingArray[0][1]]).sortedDocID.get(position[trackingArray[0][1]]+1).docID;
		    		//LLNode temp= iteratorList.get(trackingArray[0][1]).next();
		    		
		    		int tempdocID=index.get(queryTerm[trackingArray[0][1]]).sortedDocID.get(position[a]).docID;
		    		position[a]=position[a]+1;
		    		int tempindex=trackingArray[0][1];
		    		
		    		for(int i=1;i<flag;i++){// placing new docid at correct position in array
		    			daatOrCounter++;
		    			if(tempdocID<=trackingArray[i][0]&&i==1){
		    				
		    					trackingArray[0][0]=tempdocID;
		    					trackingArray[0][1]=tempindex;
		    				}
		    			
		    			
		    			 
		    			else if(trackingArray[i][0]<=tempdocID){
		    				if(i==flag-1){
			    				int j=0;
		    					for(j=1;j<=i;j++){
		    						trackingArray[j-1][0]=trackingArray[j][0];
		    						trackingArray[j-1][1]=trackingArray[j][1];
		    					}
		    					trackingArray[j-1][0]=tempdocID;
		    					trackingArray[j-1][1]=tempindex;
			    			}
		    				continue;
		    			}
		    			else if(trackingArray[i][0]<tempdocID&&i==1){
		    				trackingArray[0][0]=trackingArray[1][0];
		    				trackingArray[0][1]=trackingArray[1][1];
		    				
		    				trackingArray[1][0]=tempdocID;
		    				trackingArray[1][1]=tempindex;
		    			}
		    			
		    			else if(trackingArray[i][0]>tempdocID){
		    					int j=0;
		    					for(j=1;j<i;j++){
		    						trackingArray[j-1][0]=trackingArray[j][0];
		    						trackingArray[j-1][1]=trackingArray[j][1];
		    					}
		    					trackingArray[j-1][0]=tempdocID;
		    					trackingArray[j-1][1]=tempindex;
		    				}
		    			break;
		    				
		    					
		    			
		    		}
		    		
		    		
		    		or=daatOrQuery(index, or, position, trackingArray, queryTerm, flag);
		    	}
		    	else
		    	{
		    		trackingArray[0][0]=-1;
		    		int x=trackingArray[0][0];
		    		int y=trackingArray[0][1];
		    		
		    		for(int i=1;i<flag;i++){
		    			trackingArray[i-1][0]=trackingArray[i][0];
		    			trackingArray[i-1][1]=trackingArray[i][1];
		    		}
		    		
		    		trackingArray[flag-1][0]=x;
		    		trackingArray[flag-1][1]=y;
		    		flag=flag-1;
		    		
		    		
		    		
		    		or=daatOrQuery(index, or, position, trackingArray, queryTerm, flag);
		    	}
		    	
		    	}
		    	
				
				
		    return or;
		    
		}
		
}
