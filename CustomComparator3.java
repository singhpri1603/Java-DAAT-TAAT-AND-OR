import java.util.Comparator;
public class CustomComparator3 implements Comparator<LLNode>{
	
	public int compare(LLNode o1, LLNode o2) {
		
		return (o1.docID-o2.docID);
	}
        

}
