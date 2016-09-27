
public class LLNode {
int docID;
int termFrequency;

LLNode(int docID, int termFrequency)
{
	this.docID=docID;
	this.termFrequency=termFrequency;
	
}
public String toString()
{
 StringBuilder sb = new StringBuilder();
 sb.append(docID).append(" ")
   .append(termFrequency).append(" ");
   
 return sb.toString();
}
}
