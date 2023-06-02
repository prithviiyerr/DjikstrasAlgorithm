// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, including the Internet, either
// modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the supplied grading code.
//
// <prithvi iyer>
// <prithvii>
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SSAD {
	static int theVerts;
	static int startV;
	static buildList l;
	static theList[] dTable;
	static minHeap ecco;
	static String[] nums;
	static theList[] hasWent;
	
	static theList p;
	static int pk;
/**
 * main method
 * @param args
 * @throws IOException
 */
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(args[0], "r");
		FileWriter fw = new FileWriter(args[1]);
		String s = raf.readLine();
		int count = 0;
		fw.write("  Node  | Successors\n");
		fw.write("  ----------------------------------------------------------------------\n");
		do {
			if(s.contains("Number of vertices:")) {
				String curr = s;
				String verts[] = curr.split("Number of vertices:");
				theVerts = Integer.parseInt(verts[1].strip());
				l = new buildList(theVerts);
				nums = new String[theVerts];
			}
			if(s.contains("Start vertex:")) {
				String curr = s;
				String start[] = curr.split("Start vertex:");
				startV = Integer.parseInt(start[1].strip());
			}
			if(s.contains("|")) {
				String curr = s;
				String start[] = curr.split("\\|");
				nums[count] = start[0];
				count++;
				String w = start[0].strip();
				String wates[] = start[1].strip().split("\\s+");
			
				int theIndex = Integer.parseInt(w);
				for(int a=0;a<theVerts;a++) {
					if(Integer.parseInt(wates[a])!=0) {
						theList t = new theList(a,Integer.parseInt(wates[a]));
						l.addOn(theIndex, t);
					}
				}
			}
			s = raf.readLine();
		}
		while(s!=null);
		
		for(int b=0; b<theVerts; b++) {
			fw.write("\t   "+nums[b]+"|\t"); //"|\t\t"
			for(int c=0; c<l.getList().get(b).size(); c++) {
				if(l.getList().get(b).size()-1<c) {
					fw.write(l.getList().get(b).get(c)+"");
				}
				else {
					fw.write(l.getList().get(b).get(c)+"\t");  // tab
				}
			}
			fw.write("\n");
		}
		fw.write("\n"+"  Start vertex is: "+startV+"\n\n");
		dTable = new theList[theVerts];
		for(int x=0; x<theVerts; x++ ) {
			dTable[x] = new theList(x, Integer.MAX_VALUE);   
		}
		ecco = new minHeap(theVerts);
		ecco.getThings()[startV+1].setWeight(0);
		dTable[startV].setWeight(0);
		ecco.buildHeap();
	
		hasWent = new theList[theVerts];
		for(int a=0; a<theVerts; a++) {
			hasWent[a] = new theList(a, -1);
		}
		
		do {
			theList toDelete = ecco.deleteMin();
			if(toDelete == null) {
				break;
			}
			String thisString = "";
			
			for(int b=0; b< l.getList().get(toDelete.getIndex()).size(); b++) {
				theList p = l.getList().get(toDelete.getIndex()).get(b);
				int pk = toDelete.getWeight() + p.getWeight();
				thisString = toDelete.getPath() + " " + p.getIndex();
				
				if (hasWent[p.getIndex()].getWeight()==-1) {
					hasWent[p.getIndex()] = p;
					dTable[p.getIndex()].setWeight(pk);
					dTable[p.getIndex()].setPath(thisString);
					
					for(int i=1; i<ecco.getSize()+1; i++) {
						if(ecco.getThings()[i].getIndex()==p.getIndex()) {
							ecco.getThings()[i].setWeight(pk);
							ecco.getThings()[i].setPath(thisString);
							ecco.buildHeap();
							break;
						}
					}
				}
				else if(hasWent[p.getIndex()].getWeight()>-1 && dTable[p.getIndex()].getWeight()>pk) {
					hasWent[p.getIndex()] = p;
					dTable[p.getIndex()].setWeight(pk);
					dTable[p.getIndex()].setPath(thisString);
					for(int v=1; v<ecco.getSize()+1; v++) {
						if(p.getIndex() == ecco.getThings()[v].getIndex()) {
							ecco.getThings()[v].setWeight(pk);
							ecco.getThings()[v].setPath(thisString);
							ecco.buildHeap();
							break;
						}
					}
				}
			}
			
			
			
			
		}
		while(!ecco.isEmpty());
		dTable[startV].setWeight(0);
		dTable[startV].setPath("");
		
		for(int y=0; y<dTable.length; y++) {
		
		}
	
		fw.write("           Total\n");
		fw.write("  Dest | Weight | Path\n");
		fw.write("  ----------------------------------------------------------------------\n");
		for(int b=0; b<dTable.length; b++) {
			fw.write("\t   "+nums[b]+"|  "); 
			
			
				if(dTable[b].getWeight() == Integer.MAX_VALUE) {
					fw.write("inf");
				}
				else {
					if(dTable[b].getWeight() < 100) {
						fw.write(" "+String.valueOf(dTable[b].getWeight()));
					}
					else if(dTable[b].getWeight() == 0){
						fw.write("    "+String.valueOf(dTable[b].getWeight()));
					}
					else {
						fw.write(String.valueOf(dTable[b].getWeight()));
					}
				}
				fw.write(" |");
				fw.write("   "+dTable[b].getPath());
				fw.write("\n");
			
			
		}
		
		raf.close();
		fw.close();

	}
	
	
	

}
