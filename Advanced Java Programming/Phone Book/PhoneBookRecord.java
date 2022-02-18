import javafx.beans.property.SimpleStringProperty;
/*
 * PhoneBookRecord class represents the a single instance in the phone book
 * it has a name and phone
 */
public class PhoneBookRecord {
	/*
	 * private attributes of this class
	 * the attributes are from Type SimpleStringProperty which allows to connect them to a UI table view
	 */
	private SimpleStringProperty name;
	private SimpleStringProperty phone;
	
	/*
	 *  a constructor for PhoneBookRecord instance throws exception as it contain validations on the attributes
	 */
	public PhoneBookRecord(String name, String phone) throws Exception {
		//a name cannot be empty
		if(name.isEmpty()) {
			throw new Exception("name cannot be empty");
		}
		// a phone cannot be empty
		if(phone.isEmpty()) {
			throw new Exception("phone cannot be empty");
		}
		//phone can contain digits or the char '-'
		for(int i = 0; i < phone.length(); i++) {
			if(!Character.isDigit(phone.charAt(i)) && phone.charAt(i) != '-') {
				throw new Exception("phone can be made of digits or '-' only");
			}
		}
		this.name = new SimpleStringProperty(name);
		this.phone = new SimpleStringProperty(phone);
	}
	
	//default getters
	public String getName() {
		return name.get();
	}
	
	public String getPhone() {
		return phone.get();
	}
	
	//default setters
	public void setPhone(String newPhone) {
		phone.set(newPhone);
	}
	
	public void setName(String newName) {
		name.set(newName);
	}
	//overrides to string method
	public String toString() {
		return "Name: " + this.getName() + "\nPhone: " + this.getPhone();
	}
}
