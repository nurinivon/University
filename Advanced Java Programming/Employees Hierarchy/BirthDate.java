import java.time.LocalDateTime;
/*
 * BirthDate is a class represents birth days objects.
 * it has three integers attributes: year, month, day
 */
public class BirthDate {
	//the birthday attributes
	private int year;
	private int month;
	private int day;
	//constant variables for this class
	private final int JANUARY = 1;
	private final int FEBRUARY = 2;
	private final int APRIL = 4;
	private final int JUNE = 6;
	private final int SEPTEMBER = 9;
	private final int NOVEMBER = 11;
	private final int DECEMBER = 12;
	private final int MAX_DAY_OF_MONTH = 31;
	private final int MIN_DAY_OF_MONTH = 0;
	private final int MAX_DAY_OF_FEBRUARY = 28;
	
	//constructor for BirthDate object, includes all dates validations
	public BirthDate(int year, int month, int day) {
		if(year > LocalDateTime.now().getYear()) {
			throw new IllegalArgumentException("human cannot be born in the future");
		}
		if(month < JANUARY || month > DECEMBER) {
			throw new IllegalArgumentException("ileagal month");
		}
		if(day < MIN_DAY_OF_MONTH || day > MAX_DAY_OF_MONTH) {
			throw new IllegalArgumentException("ileagal day of month");
		}else {
			if(month == FEBRUARY && day > MAX_DAY_OF_FEBRUARY) {
				throw new IllegalArgumentException("this month has only 28 days");
			}else {
				if(day == MAX_DAY_OF_MONTH && (month == APRIL || month == JUNE || month == SEPTEMBER || month == NOVEMBER)) {
					throw new IllegalArgumentException("this month has only 30 days");
				}
			}
		}
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	//getter for the month attribute
	public int getMonth() {
		return this.month;
	}
	
	//toString method returns the birth date in the format "d/mm/yyyy"
	public String toString() {
		return this.day + "/" + this.month + "/" + this.year;
	}
	
}
