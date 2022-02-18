/*
 * BasePlusCommissionEmployee is a sub class for CommissionEmployee. is an employee which his earnings calculated by the amount he sold and is  commission rate in addition to base salary
 */
public class BasePlusCommissionEmployee extends CommissionEmployee{
	////unique attribute for BasePlusCommissionEmployee object in addition to CommissionEmployee
	private double baseSalary;
	//constant variable
	private final double MIN_BASE_SALARY = 0.0;
	
	//constructor for BasePlusCommissionEmployee object, first construct an CommissionEmployee object and only then initializing BasePlusCommissionEmployee's attributes
	public BasePlusCommissionEmployee(String firstName, String lastName, String SSN, int year, int month, int day, double grossSales, double commissionRate, double baseSalary) {
		super(firstName, lastName, SSN, year, month, day, grossSales, commissionRate);
		if(baseSalary < MIN_BASE_SALARY) {
			throw new IllegalArgumentException("base salary must be >= 0.0");
		}
		this.baseSalary = baseSalary;
	}
	
	//setter for BasePlusCommissionEmployee attribute
	public void setBaseSalary(double baseSalary) {
		if(baseSalary < MIN_BASE_SALARY) {
			throw new IllegalArgumentException("base salary must be >= 0.0");
		}
		this.baseSalary = baseSalary;
	}
	
	//getter for BasePlusCommissionEmployee attribute
	public double getBaseSalary() {
		return this.baseSalary;
	}
	
	//the implementation of abstract method earnings unique to BasePlusCommissionEmployee
	public double earnings() {
		return getBaseSalary() + super.earnings();
	}
	
	//the implementation of toString method uses the base string from the CommissionEmployee class and extends it accordingly
	public String toString() {
		return String.format("Base-Salaried %s%nBase Salary: $%,.2f", super.toString(), this.getBaseSalary());
	}
}
