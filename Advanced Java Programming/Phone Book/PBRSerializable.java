import java.io.Serializable;

/*
 * PBRSerializable class stands for PhoneBookRecordSerializable.
 * PhoneBookRecord class is not Serializable, so this class is very similar to PhoneBookRecord only just it is Serializable and therefor could be exported as objects stream
 */
public class PBRSerializable  implements Serializable {
	//efault attribute for Serializable class
	private static final long serialVersionUID = 1L;
	//private attributed for the class
	private String name;
	private String phone;
	//default constructor
	public PBRSerializable() {
		
	}
	//a constructor created from PhoneBookRecord
	public PBRSerializable(PhoneBookRecord rec) {
		this.name = rec.getName();
		this.phone = rec.getPhone();
	}
	//constructor made out of strings
	public PBRSerializable(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
	//default getters
	public String getName() {
		return this.name;
	}
	
	public String getPhone() {
		return this.phone;
	}
	//default setters
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	//overrides to string method
	public String toString() {
		return "Name: " + this.name + "\nPhone: " + this.phone;
	}

}
