
public class theList {
	int index, weight;
	String path = "";
	
	/**
	 * makes a data type with index and weight
	 * @param index
	 * @param weight
	 */
	public theList(int index, int weight) {
		this.index = index;
		this.weight = weight;
	}
	/**
	 * gets the index
	 * @return index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * gets the weight
	 * @return weight
	 */
	public int getWeight() {
		return weight;
	}
	/**
	 * converts to string
	 */
	public String toString() {
		return String.valueOf(index)+": "+String.valueOf(weight);
	}
	/**
	 * sets the weight
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	} 
	
	/**
	 * gets the path
	 * @return
	 */
	public String getPath()
	{
		return path;
	}
	/*8
	 * sets the path
	 */
	public void setPath(String s) {
		this.path = s;
	}
	

}
