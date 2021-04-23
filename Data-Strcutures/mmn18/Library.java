package mmn18;
import java.util.*; 

/*
 * this class is here to define the library object we will use as the library for the exercise.
 */

public class Library {
	/*library is made out of two hash tables, 1 for books, 1 for subscriptions
	 * those hash tables are both chained hash tables that Separate chaining with linked lists
	 */
	
	/*the book hash table uses not hash function. book id is made out of 2 letters and 4 digits number.
	 * the book key will be defined by is 4 digits number inside is id.
	 * the hash table max size will be 10,000. 
	 * number of possible books ids is 6,760,000.
	 * the load factor for this table will be 676 for every 6,760,000 books.
	 * under the assumption of random and uniform distribution, this load factor is good enough to say that average case method will all be
	 * complexity - O(1)
	 */
	Hashtable<Integer, LinkedList<Book>> booksTable;
	
	/*the subscriptions hash table uses the next hash function. h(subscriberID) = subscriberID % 1000.
	 * the subscription key will be defined by is 3 last digits of its id.
	 * the hash table max size will be 1,000. 
	 * the load factor for this table will be 10 for every 10,000 subscriptions.
	 * under the assumption of random and uniform distribution, this load factor is good enough to say that average case method will all be
	 * complexity - O(1)
	 */
	Hashtable<Integer, LinkedList<Subscription>> subscriptionsTable;
	
	//the constructor for library object
	//complexity - O(1)
	public Library() {
		this.booksTable = new Hashtable<Integer, LinkedList<Book>>();
		this.subscriptionsTable = new Hashtable<Integer, LinkedList<Subscription>>();
	}//end of constructor
	
	//instance method to add new book to the library
	//Complexity O(1)
	public void addBookToLibrary(String bookID) {
		//we ignore to first letters in the book id, and the book key in the table will be defined by its 4 digits number
		int bookIntegerID = Integer.parseInt(bookID.substring(2));
		Book newBook = new Book(bookID);
		//if this key do not contain a linked list already, we will create new one
		if(this.booksTable.get(bookIntegerID) == null) {
			LinkedList<Book> newBookList = new LinkedList<Book>();
			newBookList.add(newBook);
			this.booksTable.put(bookIntegerID, newBookList);
			//else, we will add the new book to the exist list.
		}else {
			this.booksTable.get(bookIntegerID).add(newBook);
		}
		
	}//end of method addBookToLibrary
	
	//instance method to add new subscription to the library
	//complexity - O(1)
	public void addSubscription(String lastName, String subscriberID) {
		//a call to the subscription hash function
		int subscriberHashID = subscriptionHashFunction(subscriberID);
		Subscription newSubscription = new Subscription(lastName, subscriberID); 
		//if there isn't subscription list initialized already in the current key position, we will initialize new one, with the new subscription
		if(this.subscriptionsTable.get(subscriberHashID) == null) {
			//creating new subscription list
			LinkedList<Subscription> currentSubscriptions = new LinkedList<Subscription>();
			//add the new subscription object to the list
			currentSubscriptions.add(newSubscription);
			//put the list in the correct place in the hash table
			this.subscriptionsTable.put(subscriberHashID, currentSubscriptions);
			//else, we will just add the new subscription to his list in his key.
		}else {
			this.subscriptionsTable.get(subscriberHashID).add(newSubscription);
		}
	}//end of method addSubscription
	
	//instance method to remove subscription from the library
	//complexity - O(1)
	public void removeSubscription(String subscriberID) {
		//finding subscription key by hash function
		int subscriberHashID = subscriptionHashFunction(subscriberID);
		LinkedList<Subscription> currentSubscriptions = this.subscriptionsTable.get(subscriberHashID);
		//check if the subscription exist
		if(currentSubscriptions != null) {
			boolean foundSubscription = false;
			//loop thru the list to search for the correct subscription
			//under the assumption of random and uniform distribution, the list size will be 10 for every possible 10,000 ids
			//so we consider this loop's complexity as the average case - O(1)
			for(int i = 0; i < currentSubscriptions.size() && !foundSubscription; i++) {
				if(currentSubscriptions.get(i).subscriberID.equals(subscriberID)) {
					//when finding the correct subscription, we will return all of his book to the library
					foundSubscription = true;
					//loop for all of his books (max books 10)
					//complexity - O(1)
					for(int j = 0; j < currentSubscriptions.get(i).currentNumberOfBooks; j++) {
						//getting the current bookID
						String currentBookID = currentSubscriptions.get(i).currentBooksIDs[j];
						LinkedList<Book> tempBooksList = this.booksTable.get(Integer.parseInt(currentBookID.substring(2)));
						if(tempBooksList != null) {
							boolean foundBook = false;
							//going thru this book list to find the correct book
							//complexity - O(1) (as mentioned above for the assumptions of the books table methods)
							for(int k = 0; k < tempBooksList.size() && !foundBook; k++) {
								if(tempBooksList.get(k).bookID.equals(currentBookID)) {
									//return the book to the library
									foundBook = true;
									tempBooksList.get(k).returnBookToLibrary();
								}
							}//end of books list loop
						}
					}
					//removing the subscription from the library
					this.subscriptionsTable.remove(i);
				}
			}//end of loop to find the subscription inside his list
		}
	}//end of method removeSubscription
	
	//instance subscription object getter by subscriberID
	//complexity O(1), for the average case
	public Subscription getSubscription(String subscriberID) {
		int subscriberHashID = subscriptionHashFunction(subscriberID);
		LinkedList<Subscription> currentSubscriptions = this.subscriptionsTable.get(subscriberHashID);
		if(currentSubscriptions != null) {
			for(int i = 0; i < currentSubscriptions.size(); i++) {
				if(currentSubscriptions.get(i).subscriberID.equals(subscriberID)) {
					return currentSubscriptions.get(i);
				}
			}
		}//if there is no appropriate subscription in the library - returns null
		return null;
	}//end of subscription getter
	
	//instance Book object getter by bookID
	//complexity O(1), for the average case
	public Book getBook(String bookID) {
		LinkedList<Book> tempBooksList = this.booksTable.get(Integer.parseInt(bookID.substring(2)));
		if(tempBooksList != null) {
			for(int i = 0; i < tempBooksList.size(); i++) {
				if(tempBooksList.get(i).bookID.equals(bookID)) {
					return tempBooksList.get(i);
				}
			}
		}//if there is no appropriate book in the library - returns null
		return null;
	}//end of book getter
	
	//instance method to borrow book from library
	//complexity O(1)
	public void borrowBook(String subscriberID, String bookID) {
		//finding the subscription key with the hash function
		int subscriberHashID = subscriptionHashFunction(subscriberID);
		LinkedList<Subscription> currentSubscriptions = this.subscriptionsTable.get(subscriberHashID);
		if(currentSubscriptions != null) {
			boolean foundSubscription = false;
			//loop over the subscription list
			//complexity - O(1), under above mentioned assumptions, for the average case
			for(int i = 0; i < currentSubscriptions.size() && !foundSubscription; i++) {
				if(currentSubscriptions.get(i).subscriberID.equals(subscriberID)) {
					foundSubscription = true;
					//add the book to the book array
					//validation for max number of books will happen before calling this function, so if we got here, we are ready to borrow
					this.subscriptionsTable.get(subscriberHashID).get(i).borrowBook(bookID);
				}
			}
		}
		//finding the book by is id
		LinkedList<Book> tempBooksList = this.booksTable.get(Integer.parseInt(bookID.substring(2)));
		if(tempBooksList != null) {
			boolean foundBook = false;
			//loop over the book list
			//complexity - O(1), under above mentioned assumptions, for the average case
			for(int j = 0; j < tempBooksList.size() && !foundBook; j++) {
				if(tempBooksList.get(j).bookID.equals(bookID)) {
					foundBook = true;
					//set the ownerId property for the book
					this.booksTable.get(Integer.parseInt(bookID.substring(2))).get(j).borrowBook(subscriberID);
				}
			}
		}
	}//end of method borrowBook
	
	//instance method to return book to the library
	//complexity O(1)
	public void returnBookToLibrary(String bookID) {
		//subscriberID initialize to null
		String subscriberID = null;
		//finding the book list by the book id
		LinkedList<Book> tempBooksList = this.booksTable.get(Integer.parseInt(bookID.substring(2)));
		if(tempBooksList != null) {
			boolean foundBook = false;
			//loop for the book list
			//complexity O(1)
			for(int j = 0; j < tempBooksList.size() && !foundBook; j++) {
				if(tempBooksList.get(j).bookID.equals(bookID)) {
					foundBook = true;
					//when found the book, we will save the subscriber id, and after, we will return it to the library
					subscriberID = this.booksTable.get(Integer.parseInt(bookID.substring(2))).get(j).currentOwnerID;
					this.booksTable.get(Integer.parseInt(bookID.substring(2))).get(j).returnBookToLibrary();
				}
			}
		}
		
		//after finding the subscriber id
		if(subscriberID != null) {
			//finding the subscriptions list with the subscriptions hash function
			int subscriberHashID = subscriptionHashFunction(subscriberID);
			LinkedList<Subscription> currentSubscriptions = this.subscriptionsTable.get(subscriberHashID);
			if(currentSubscriptions != null) {
				boolean foundSubscription = false;
				//loop for the subscriptions list
				//complexity O(1)
				for(int i = 0; i < currentSubscriptions.size() && !foundSubscription; i++) {
					if(currentSubscriptions.get(i).subscriberID.equals(subscriberID)) {
						foundSubscription = true;
						//when found the subscription - return the appropriate book to the library
						this.subscriptionsTable.get(subscriberHashID).get(i).returnBookToLibrary(bookID);
					}
				}
			}
		}
	}//end of method returnBookToLibrary
	
	//instance method to find the subscriptions hold the max number of books and print them
	//complexity O(n)
	public void printMaxBooksHolders() {
		//initialize the max holders list - this will be the output list
		LinkedList<Subscription> currentMaxBooksHolders = new LinkedList<Subscription>();
		//initialize max number of books
		int currentMaxNumber = 0;
		//variable holds all the keys in use in the subscriptions table
		Enumeration<Integer> enu = this.subscriptionsTable.keys(); 
		//loop over all the keys in use
		//complexity O(n)
		while(enu.hasMoreElements()) {
			//the current key
			int i = enu.nextElement();
			if(this.subscriptionsTable.get(i) != null) {
				//loop over the current subscription list
				//complexity O(1)
				for(int j = 0; j < this.subscriptionsTable.get(i).size(); j++) {
					//if the current subscription holds as much books as the currentMaxNumber, we will add it to the output list
					if(this.subscriptionsTable.get(i).get(j).currentNumberOfBooks == currentMaxNumber && currentMaxNumber != 0) {
						currentMaxBooksHolders.add(this.subscriptionsTable.get(i).get(j));
					}else {
						//if the current subscription holds more books than currentMaxNumber, we will clear the ourpur list, and add it instead
						if(this.subscriptionsTable.get(i).get(j).currentNumberOfBooks > currentMaxNumber) {
							currentMaxBooksHolders.clear();
							currentMaxBooksHolders.add(this.subscriptionsTable.get(i).get(j));
							//currentMaxNumber will be as much as the current subscription holds.
							currentMaxNumber = this.subscriptionsTable.get(i).get(j).currentNumberOfBooks;
						}
					}
				}
			}
		}// end of loop over the keys
		
		//if there are more than 1 subscriptions holding at least 1 book
		if(currentMaxBooksHolders.size() > 0) {
			//print all the properties
			System.out.println("The Max Number Of Books Is: " + currentMaxNumber);
			System.out.println("There Are " + currentMaxBooksHolders.size() + " Subscribers Holding This Number Of Books.");
			System.out.println("The Subscribers Are:" + "\n");
			//loop over the output list
			//complexity O(n)
			for(int k = 0; k < currentMaxBooksHolders.size(); k++) {
				//print each subscription
				currentMaxBooksHolders.get(k).printSubscription();
				System.out.println("");
			}
		}else {
			//message when there are no subscription holding at least 1 book
			System.out.println("There Are No Books Holders In This Library");
		}
	}//end of method printMaxBooksHolders
	
	//static function, the hash function to handle subscribers id and return the key for the hash table.
	//complexity O(1)
	public static int subscriptionHashFunction(String subscriberID) {
		int subscriberIntegerID = Integer.parseInt(subscriberID);
		return (subscriberIntegerID % 1000);
	}//end of subscription hash function 
}//end of class Library
