/*
 * HourlyEmployee is a sub class for Employee. it is an employee which his earnings calculated by the amount of hours he worked and wage (payment per hour)
 */
public class HourlyEmployee extends Employee {
	//unique attributes for HourlyEmployee object in addition to Employee
	private double wage;
	private double hours;
	//constants variables used by this class
	private final int STANDARD_WEEKLY_HOURS = 40;
	private final double MIN_WEEKLY_HOURS = 0.0;
	private final double MAX_WEEKLY_HOURS = 168.0;
	private final double MIN_WAGE = 0.0;
	private final double OVERTIME_MULTIPLE = 1.5;
	
	//constructor for HourlyEmployee object, first construct an Employee object and only then initializing HourlyEmployee's attributes
	public HourlyEmployee(String firstName, String lastName, String SSN, int year, int month, int day, double wage, double hours) {
		super(firstName, lastName, SSN, year, month, day);
		if(wage < MIN_WAGE) {
			throw new IllegalArgumentException("hourly wage must be >= 0.0");
		}
		if((hours < MIN_WEEKLY_HOURS) || (hours > MAX_WEEKLY_HOURS)) {
			throw new IllegalArgumentException("hours worked must be >= 0.0 and <= 168.0");
		}
		this.wage = wage;
		this.hours = hours;
	}

	//setters for HourlyEmployee attributes
	public void setWage(double wage) {
		if(wage < MIN_WAGE) {
			throw new IllegalArgumentException("hourly wage must be >= 0.0");
		}
		this.wage = wage;
	}
	
	public void setHours(double hours) {
		if((hours < MIN_WEEKLY_HOURS) || (hours > MAX_WEEKLY_HOURS)) {
			throw new IllegalArgumentException("hours worked must be >= 0.0 and <= 168.0");
		}
		this.hours = hours;
	}
	
	//getters for HourlyEmployee attributes
	public double getWage() {
		return this.wage;
	}
	
	public double getHours() {
		return this.hours;
	}
	
	//the implementation of abstract method earnings unique to HourlyEmployee, overtime taken under consideration
	public double earnings() {
		if(getHours() <= STANDARD_WEEKLY_HOURS) {
			return getWage() * getHours();
		}else {
			return STANDARD_WEEKLY_HOURS * getHours() + (getHours() - STANDARD_WEEKLY_HOURS) * getWage() * OVERTIME_MULTIPLE;
		}
	}
	
	//the implementation of toString method uses the base string from the Employee class and extends it accordingly
	public String toString() {
		return String.format("Hourly Employee: %s%nHourly Wage: $%,.2f%nHours Worked: %,.2f",super.toString(), this.getWage(), this.getHours());
	}
}
