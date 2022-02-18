/*
 * Nuri Nivon 2021
 * The following program is an implementation of Association Table.
 */
import java.util.Iterator;
import java.util.Map.Entry;
/*
 * Main class is the main class of this program.
 * it contains the main method for the program and it is the iterface of it.
 */
public class Main {
	public static void main(String[] args) {
		//exceptions could be thrown with constructing a student or constructing an Association Table
		try {
			AssociationTable<Student, String> myAT = new AssociationTable<Student, String>();
			Student s1 = new Student("333","Albert","Einstein",1879);
			Student s2 = new Student("111","Isaac ","Newton ",1643);
			Student s3 = new Student("222","Leonhard","Euler",1707);
			//insert 1st 3 students to the table
			myAT.add(s1, "03-8590242");
			myAT.add(s2, "02-9667891");
			myAT.add(s3, "077-030-3323");
			//add 4th student to the table
			myAT.add(new Student("444","Leonardo","Fibonacci",1170), "08-9428909");
			//remove the student with id:"111" from the table
			if(!myAT.remove(s2)) {
				System.out.println("this student doesn't exist");
			}
			
			//updating the telephone of the student with id:"333"
			myAT.add(s1, "*EMC");
			
			//iterate the the Association Table and print the entries
			System.out.println("current association table is:");
			Iterator<Entry<Student, String>> it = myAT.keyIterator();
			while(it.hasNext()) {
				 Entry<Student, String> entry = it.next();
				 System.out.println(entry.getKey().toString());
				 System.out.println("phone: " + entry.getValue() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
