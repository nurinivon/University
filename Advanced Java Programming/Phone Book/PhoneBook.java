import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * PhoneBook class is the implementation of hte phone book logics.
 * It contains the List of the Contacts and the method related to them
 * The Contacts list will be sorted by the Contact's name descending
 */
public class PhoneBook {
	//constant variables for the class
	private final String OUTPUT_FILE_NAME = "phone book.txt";
	//private atteributes of the class
	private ArrayList<PhoneBookRecord> records;
	
	//PhoneBook default constructor, initializes the contacts list
	public PhoneBook() {
		this.records = new ArrayList<PhoneBookRecord>();
	}
	
	/*
	 * add method gets a PhoneBookRecord and add it to the list.
	 * the Contact will be added to the appropriate index by his name.
	 */
	public void add(PhoneBookRecord newRecord){
		/*
		 * the list is sorted at all time. so when insert new contact to the list, it will iterate the contacts until it will found a contact which its name is "bigger".
		 * if found a "bigger" name, the new contact will be inserted with its index and this will insert it before the current contact.
		 */
		for(int i = 0; i < this.records.size(); i++) {
			if(this.records.get(i).getName().compareTo(newRecord.getName()) > 0) {
				this.records.add(i, newRecord);
				return;
			}
		}
		//if finish the loop and no return, it means the new contact should be insert to the end of the list
		this.records.add(newRecord);
	}
	
	/*
	 * delete gets a PhoneBookRecord and deletes it from the contact list
	 */
	public void delete(PhoneBookRecord record) {
		this.records.remove(record);
	}
	
	/*
	 * update method gets a PhoneBookRecord, and updates it with new name and new phone
	 */
	public void update(PhoneBookRecord record, String newName, String newPhone) {
		record.setName(newName);
		record.setPhone(newPhone);
	}
	
	/*
	 * get method is getting a string and returns an iterator over a PhoneBookRecord list which the name or the phone of in this list contains the String.
	 */
	public Iterator<PhoneBookRecord> get(String str) {
		//creating a temp list
		ArrayList<PhoneBookRecord> tempList = new ArrayList<PhoneBookRecord>();
		//iterating all of the records
		for(int i = 0; i < this.records.size(); i++) {
			//if the records's name or phone contains the String it will be added to the temp list
			if(this.records.get(i).getName().contains(str) || this.records.get(i).getPhone().contains(str)) {
				tempList.add(this.records.get(i));
			}
		}
		//return the iterator for the temp list
		return tempList.iterator();
	}
	
	/*
	 * importContacts method gets a file path for a file that contains an objects stream
	 * it iterates the objects in the file and adding them the phone book
	 */
	public void importContacts(File filePath) throws Exception, FileNotFoundException, IOException, ClassNotFoundException{
		//creating new file input stream
		FileInputStream fi = new FileInputStream(filePath);
		//creating new object input stream
		ObjectInputStream oi = new ObjectInputStream(fi);
		/*
		 * we expecting PBRSerializable objects - PBRSerializable = PhoneBookRecordSerializable
		 * PhoneBookRecord is not Serializable as it uses special attributes to be corralated to table view
		 */
		PBRSerializable currentObj;
		try {
			//iterate the objects in the file
			while((currentObj = (PBRSerializable) oi.readObject()) != null) {
				//creating PhoneBookRecord instances and adding each object to the phone book
				this.add(new PhoneBookRecord(currentObj.getName(), currentObj.getPhone()));
			}
		} catch (EOFException e) {
			//e.printStackTrace();
		}

		oi.close();
		fi.close();
	}
	
	/*
	 * exportContacts method gets a path to a directory and a string to filter the contacts.
	 * it creates a file contains the list of objects that their phone or their name is including the filter string
	 */
	public void exportContacts(File directoryPath, String filterInput) throws FileNotFoundException , IOException , ClassNotFoundException {
		//setting the path to the file as the user's chosen directory and the file name
		String filePath = directoryPath.getAbsolutePath() + "\\" + OUTPUT_FILE_NAME;
		//creating new file out stream
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		//creating new object out stream
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		//getting the contacts according to the filter string
		Iterator<PhoneBookRecord> it = this.get(filterInput);
		//iterate the objects
		while(it.hasNext()) {
			//each object is turning into PBRSerializable as PhoneBookRecord is not Serializable
			PhoneBookRecord entry = it.next();
			//writing the object to the file
			oos.writeObject(new PBRSerializable(entry));
		}
		oos.close();
		fos.close();
	}
}
