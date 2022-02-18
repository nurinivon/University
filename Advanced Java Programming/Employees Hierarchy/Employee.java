/*
 * Employee class is an abstract class contains the shared attributes of all the employees' types
 */
public abstract class Employee {
	//shared attributes of all the employees' types
	//all of those are final as they cannot be replaced
	private final String firstName;
	private final String lastName;
	private final String SSN;
	private final BirthDate birthDate;
	
	//constructor for Employee, all employees should those attributes
	public Employee(String firstName, String lastName, String SSN, int year, int month, int day) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.SSN = SSN;
		this.birthDate = new BirthDate(year, month, day);
	}
	
	//getters for the employee's attributes
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getSSN() {
		return this.SSN;
	}

	public String getBirthDay() {
		return this.birthDate.toString();
	}
	
	public int getBirthdayMonth() {
		return this.birthDate.getMonth();
	}
	
	//base version of employee's toString method, each employee type is using this base string and extend it as needed.
	public String toString() {
		return String.format("%s %s%nSSN: %s%nBirthdate: %s", this.getFirstName(), this.getLastName(), this.getSSN(), this.getBirthDay());
	}
	
	//abstract method for earnings as all employees have earnings only just for employee type it is calculated differently
	public abstract double earnings();
}
