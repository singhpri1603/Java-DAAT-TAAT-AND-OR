import java.util.*;
public class CustomComparator implements Comparator<kterms>{
	
	public int compare(kterms o1, kterms o2) {
		
		return (o1.noOfDocs-o2.noOfDocs)*(-1);
	}
        

}
