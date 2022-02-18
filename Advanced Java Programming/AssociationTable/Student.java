/*
 * Student class represent a student instances.
 * it implements the Comparable interface and it comparable thru the id attribute.
 */
public class Student implements Comparable<Student>{
	//private attributes for Student instance
	private String id;
	private String firstName;
	private String lastName;
	private int birthYear;
	
	/*
	 * only constructor for Student instance is with all attributes.
	 * this constructor could throw an exception as the id attribute should contain digits only, and at least one digit
	 */
	public Student(String id, String firstName, String lastName, int birthYear) throws Exception {
		if(id.length() == 0) {
			throw new Exception("id must include at least one char");
		}
		try {
			Integer.parseInt(id);
		}catch(Exception e) {
			throw new Exception("id must include obly digits");
		}
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthYear = birthYear;
	}
	
	//default getters
	public String getId() {
		return this.id;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public int getBirthYear() {
		return this.birthYear;
	}
	
	//default setters
	//setId could throw an exception as the id attribute should contain digits only, and at least one digit
	public void setId(String id) throws Exception {
		if(id.length() == 0) {
			throw new Exception("id must include at least one char");
		}
		try {
			Integer.parseInt(id);
		}catch(Exception e) {
			throw new Exception("id must include obly digits");
		}
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	
	//the implementation for compareTo for Student class
	public int compareTo(Student student) {
		return this.id.compareTo(student.getId());
	}
	
	//overrides toString method for Student class
	public String toString() {
		return "id: " + this.id + "\nname: " + this.firstName + " " + this.lastName + "\nbirthYear: " + this.birthYear;
	}

}
