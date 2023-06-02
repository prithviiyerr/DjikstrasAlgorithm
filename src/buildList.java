import java.util.ArrayList;
import java.util.LinkedList;

public class buildList {
	private int vertices;
	private ArrayList<LinkedList<theList>> l;
	/**
	 * constructor that makes the adjacency list
	 * @param a
	 */
	public buildList(int a) {
		this.vertices = a;
		l = new ArrayList<LinkedList<theList>>(vertices);
		for(int b=0; b<vertices; b++) {
			l.add(new LinkedList<theList>());
		}
	}
	/**
	 * gets the list
	 * @return l
	 */
	public ArrayList<LinkedList<theList>> getList() {
		return l;
	}
	/**
	 * adds to the list
	 * @param ind  index
	 * @param m  the entry
	 * @return true or false
	 */
	public boolean addOn(int ind, theList m) {
		if(l.get(ind).add(m)) {
			return true;
		}
		return false;
	}
	
	
	

}
