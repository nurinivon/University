/*
 * SalariedEmployee is a sub class for Employee. it is an employee which his earnings calculated by weekly salary
 */
public class SalariedEmployee extends Employee {
	//unique attribute for SalariedEmployee object in addition to Employee
	private double weeklySalary;
	//constants variables used by this class
	private final double MIN_WEEKLY_SALARY = 0.0;
	
	//constructor for SalariedEmployee object, first construct an Employee object and only then initializing SalariedEmployee's attribute
	public SalariedEmployee(String firstName, String lastName, String SSN, int year, int month, int day, double weeklySalary) {
		super(firstName, lastName, SSN, year, month, day);
		if(weeklySalary < MIN_WEEKLY_SALARY) {
			throw new IllegalArgumentException("weekley salay must be >= 0.0");
		}
		this.weeklySalary = weeklySalary;
	}
	
	//setter for SalariedEmployee attribute
	public void setWeeklySalary(double weeklySalary) {
		if(weeklySalary < MIN_WEEKLY_SALARY) {
			throw new IllegalArgumentException("weekley salay must be >= 0.0");
		}
		this.weeklySalary = weeklySalary;
	}
	
	//getter for SalariedEmployee attribute
	public double getWeeklySalary() {
		return this.weeklySalary;
	}
	
	//the implementation of abstract method earnings unique to SalariedEmployee
	public double earnings() {
		return this.weeklySalary;
	}
	
	//the implementation of toString method uses the base string from the Employee class and extends it accordingly
	public String toString() {
		return String.format("Salaried Employee: %s%nWeekly Salary: $%,.2f",super.toString(), this.getWeeklySalary());
	}
}
