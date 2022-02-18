/*
 * CommissionEmployee is a sub class for Employee. it is an employee which his earnings calculated by the amount he sold and is  commission rate
 */
public class CommissionEmployee extends Employee {
	//unique attributes for CommissionEmployee object in addition to Employee
	private double grossSales;
	private double commissionRate;
	//constants variables used by this class
	private final double MIN_GROSS_SALES = 0.0;
	private final double MIN_COMMISSION_RATE = 0.0;
	private final double MAX_COMMISSION_RATE = 1.0;
	
	//constructor for CommissionEmployee object, first construct an Employee object and only then initializing CommissionEmployee's attributes
	public CommissionEmployee(String firstName, String lastName, String SSN, int year, int month, int day, double grossSales, double commissionRate) {
		super(firstName, lastName, SSN, year, month, day);
		if((commissionRate <= MIN_COMMISSION_RATE) || (commissionRate >= MAX_COMMISSION_RATE)) {
			throw new IllegalArgumentException("commission rate must be > 0.0 and < 1.0");
		}
		if(grossSales < MIN_GROSS_SALES) {
			throw new IllegalArgumentException("gross sales must be >= 0.0");
		}
		this.grossSales = grossSales;
		this.commissionRate = commissionRate;
	}
	
	//setters for CommissionEmployee attributes
	public void setGrossSales(double grossSales) {
		if(grossSales < MIN_GROSS_SALES) {
			throw new IllegalArgumentException("gross sales must be >= 0.0");
		}
		this.grossSales = grossSales;
	}
	
	public void setCommissionRate(double commissionRate) {
		if((commissionRate <= MIN_COMMISSION_RATE) || (commissionRate >= MAX_COMMISSION_RATE)) {
			throw new IllegalArgumentException("commission rate must be > 0.0 and < 1.0");
		}
		this.commissionRate = commissionRate;
	}
	
	//getters for CommissionEmployee attributes
	public double getGrossSales() {
		return this.grossSales;
	}
	
	public double getCommissionRate() {
		return this.commissionRate;
	}
	
	//the implementation of abstract method earnings unique to CommissionEmployee
	public double earnings() {
		return getCommissionRate() * getGrossSales();
	}
	
	//the implementation of toString method uses the base string from the Employee class and extends it accordingly
	public String toString() {
		return String.format("Commission Employee: %s%nGross Sales: $%,.2f%nCommission Rate: %,.2f",super.toString(), this.getGrossSales(), this.getCommissionRate());
	}
}
