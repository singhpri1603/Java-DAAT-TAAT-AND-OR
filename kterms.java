
public class kterms  {
	
	int noOfDocs;
	String term;
	
	kterms(int noOfDocs,String term){
		this.noOfDocs=noOfDocs;
		this.term=term;
	}
	
	public String toString()
	{
	 StringBuilder sb = new StringBuilder();
	 sb.append(noOfDocs).append(" ")
	   .append(term).append(" ");
	   
	 return sb.toString();
	}

}
