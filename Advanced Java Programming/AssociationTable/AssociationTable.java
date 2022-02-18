import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
/*
 * AssociationTable class is a generic class implements the Association Table logics
 * the keys type should be comparable in order to prevent duplicate keys
 */
public class AssociationTable<T extends Comparable<T>, E> {
	/*
	 * private attribute for the class
	 * the keys and values should be correlated by index
	 */
	private ArrayList<T> keys;
	private ArrayList<E> values;
	
	//default constructor
	public AssociationTable() {
		this.keys = new ArrayList<T>();
		this.values = new ArrayList<E>();
	}
	
	/*
	 * constructor out of an existing lists of keys and values
	 * in this case the sizes of the lists should be the same
	 */
	public AssociationTable(ArrayList<T> keys, ArrayList<E> values) throws IllegalArgumentException {
		if(keys.size() != values.size()) {
			throw new IllegalArgumentException("lists sized are not equal");
		}
		this.keys = new ArrayList<T>();
		this.values = new ArrayList<E>();
		
		for(int i = 0; i < keys.size(); i++) {
			this.add(keys.get(i), values.get(i));
		}
	}
	
	/*
	 * add method is an instance method. it gets a key and a value as parameters and add them to the appropriate indexes in the lists
	 * if the key already exists, the value will be updated with the new value
	 */
	public void add(T key, E value) {
		/*
		 * the table is sorted at all time. so when insert new entry to the table, it will iterate the keys until it will found a bigger key.
		 * if found a bigger key, the new entry will be inserted with its index and this will insert it before the bigger key.
		 * if the key already exists in the table the value will be updated
		 */
		for(int i = 0; i < this.keys.size(); i++) {
			if(this.keys.get(i).compareTo(key) > 0) {
				System.out.println("adding the following row to the middle of table:\nto index: " + i + "\nstudent:\n" + key.toString() + "\nphone: " + value + "\n");
				this.keys.add(i, key);
				this.values.add(i, value);
				return;
			}else if(this.keys.get(i).compareTo(key) == 0) {
				System.out.println("updating the row:\nin index: " + i + "\nstudent:\n" + this.keys.get(i) + "\nold phone: " + this.values.get(i) + "\nnew phone: " + value + "\n");
				this.values.set(i, value);
				return;
			}
		}
		//if finish the loop and no return, it means the new entry should be insert to the end of the table
		System.out.println("adding the following row to the end of the table:\nstudent:\n" + key.toString() + "\nphone: " + value + "\n");
		this.keys.add(key);
		this.values.add(value);
	}
	
	/*
	 * get method is a default getter for a table.
	 * it gets a key is parameter and returns the value for this key.
	 * if it doesn't exists it will return null
	 */
	public E get(T key) {
		for(int i = 0; i < this.keys.size(); i++) {
			T currentKey = this.keys.get(i);
			if(currentKey.compareTo(key) == 0) {
				return this.values.get(this.keys.indexOf(currentKey));
			}
		}
		return null;
	}
	
	/*
	 * contains method is a default boolean method for a table.
	 * it gets a key is parameter and returns rather the key exists in the table or not
	 */
	public Boolean contains(T key) {
		for(T currentKey : keys) {
			if(currentKey.compareTo(key) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * remove method is a default delete method for a table.
	 * it gets a key is parameter and delete the key from the table and its value
	 * this method returns rather the value was deleted or not exists in the table
	 */
	public Boolean remove(T key) {
		for(int i = 0; i < this.keys.size(); i++) {
			T currentKey = this.keys.get(i);
			if(currentKey.compareTo(key) == 0) {
				System.out.println("removing the following row from the table:\nstudent:\n" + currentKey.toString() + "\nphone: " + this.values.get(i) + "\n");
				this.values.remove(i);
				this.keys.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * size is a default size method for a table.
	 * it returns the number of entries currently in the table as int.
	 */
	public int size() {
		return this.keys.size();
	}
	
	/*
	 * keyIterator is a default iterator method for a table.
	 * it returns an iterator instance, that includes all of the table's rows as an Entry instances.
	 * the iterator is created with TreeMap in order to insure that the order of the map will be the same for good.
	 */
	public Iterator<Entry<T, E>> keyIterator() {
		TreeMap<T, E> tempTm = new TreeMap<T, E>();
		for(int i = 0; i < this.keys.size(); i++) {
			tempTm.put(this.keys.get(i), this.values.get(i));
		}
		return tempTm.entrySet().iterator();
	}
	
	/*
	 * print method is used for debugging
	 */
	public void print() {
		for(int i = 0; i < this.keys.size(); i++) {
			System.out.println(this.keys.get(i).toString());
			System.out.println(this.values.get(i));
		}
	}
}
