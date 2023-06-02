
public class minHeap {
	private int size;
	private theList[] things;
	private buildList l;
	public static final int DEFCAP = 10;
	
	/**
	 * default constructor for minheap
	 */
	public minHeap() {
		this.size = 0;
		things = new theList[DEFCAP+1];
		for(int a=1; a<DEFCAP+1; a++) {
			things[a] = new theList(a, Integer.MAX_VALUE);
			size++;		
		}
	}
	/**
	 * constructor with size parameter
	 * @param s  size
	 */
	public minHeap(int s) {
		this.size = 0;
		things = new theList[s+1];
		for(int a=1; a<s+1; a++) {
			things[a] = new theList(a-1, Integer.MAX_VALUE);
			size++;		
		}
	}
	/**
	 * constructor with array parameter
	 * @param t  array of theLists
	 */
	public minHeap(theList[] t) {
		this.things = t;
	}
	/**
	 * checks if heap is empty
	 * @return true or false
	 */
	public boolean isEmpty() {
		if(size==0) {
			return true;
		}
		return false;
	}
	
	/**
	 * sift max values down to move min values up
	 * @param hole  child
	 */
	private void siftDown(int hole) {
		int child;
		theList tmp = things[hole];
		for (; hole * 2 <= size; hole = child) {
			child = hole * 2;
			if (child != size && things[child + 1].getWeight() < tmp.getWeight()) {
				child++;
			}
			if (things[child].getWeight() <= tmp.getWeight()) {
				things[hole] = things[child];
			} 
			else {
				break;
			}

		}
		things[hole] = tmp;
	}
	/**
	 * find the min value
	 * @return  the min value
	 */
	public theList findMin() {
		if( isEmpty( ) )
            throw new RuntimeException( );
        return things[ 1 ];
	}
	/**
	 * deletes the minimum value from heap
	 * @return value deleted
	 */
	public theList deleteMin() {
		if ( isEmpty( ) )
			throw new RuntimeException( );
			theList minItem = things[ 1 ];
			things[ 1 ] = things[ size ];
			size--;
			siftDown( 1 );
			if (minItem.getWeight() == Integer.MAX_VALUE) {
				return null;
			}
			return minItem;
	}
	/**
	 * builds the heap
	 */
	public void buildHeap() {
		for ( int idx = size / 2; idx > 0; idx-- )
			siftDown( idx );
	}
	/**
	 * gets the array of theLists
	 * @return
	 */
	public theList[] getThings() {
		return things;
	}
	/**
	 * gets the size
	 * @return size
	 */
	public int getSize() {
		return size;
	}
	

}
