import java.util.*;

public class Value {
	
	int noOfDocs;
	LinkedList<LLNode> sortedDocID = new LinkedList<LLNode>();
	LinkedList<LLNode> sortedFreq = new LinkedList<LLNode>();
	
	Value(int noOfDocs){
		this.noOfDocs= noOfDocs;
		
	}
	
	void addDocID(LLNode node){
		sortedDocID.add(node);
	}
	
	void addFreq(LLNode node){
		sortedFreq.add(node);
	}
	
	public String toString()
	{
	 StringBuilder sb = new StringBuilder();
	 sb.append(noOfDocs).append(" ")
	   .append(sortedDocID).append(" ")
	   .append(sortedFreq).append(" ");
	   
	 return sb.toString();
	}

}
