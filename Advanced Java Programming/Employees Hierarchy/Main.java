/*
 * Nuri Nivon 2021
 * The following program is implementation of the employees types inheritance tree
 */
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * Main class is the is main class of the program and it contain the main method for the program.
 */
public class Main {
	//constant variable
	private static final double BIRTHDAY_BONUS = 200.0;
	
	public static void main(String[] args) {
		//the employees list initialized as the main employee type list
		ArrayList<Employee> employeesList = new ArrayList<Employee>();
		//each employee in the list is a different type of employee all inherited from main employee class
		employeesList.add(new SalariedEmployee("Steve", "Jobs", "12345", 1955, 2, 24, 1000.0));
		employeesList.add(new BasePlusCommissionEmployee("Jeff", "Bezos", "54321", 1964, 1, 12, 12000.0, 0.5, 1500.0));
		employeesList.add(new CommissionEmployee("Elon", "Musk", "999999", 1971,6, 28, 100000.0, 0.7));
		employeesList.add(new HourlyEmployee("Mark", "Zuckerberg", "66666", 1984, 5, 14, 20.0, 55));
		employeesList.add(new PieceWorker("Warren", "Buffet", "77777", 1930, 8, 30, 22, 180.0));
		/*
		 * iterate the employees list and for each we call the same methods polymorphically
		 * only just those methods are implemented differently in each class and executed by dynamic binding
		 */
		for(Employee e : employeesList) {
			System.out.print("Employee Details:\n" + e.toString() + "\nEmployee Current Earnings:\n$");
			//employees having their birthdays this month get's 200 dollar bonus to their earnigns
			if(e.getBirthdayMonth() == LocalDate.now().getMonthValue()) {
				System.out.println(e.earnings() + BIRTHDAY_BONUS);
			}else {
				System.out.println(e.earnings());
			}
			System.out.print("\n");
		}
	}
}
