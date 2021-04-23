package mmn18;

/*
 * this class define the subscription object that we will use in the library
 */

public class Subscription {
	
	//the properties for subscription object
	String lastName;
	String subscriberID;
	//an array that will contain the ids of the book that this subscription holds
	String[] currentBooksIDs;
	//counter for the number of books the subscription hold
	int currentNumberOfBooks;
	//as mentioned in the exercise, a subscription can hold max 10 books
	final int maxAllowedBooks = 10;
	
	
	//constructor for subscription object
	//complexity - O(1)
	public Subscription(String lastName, String subscriberID) {
		this.lastName = lastName;
		this.subscriberID = subscriberID;
		//the array initialize to be length of 10
		this.currentBooksIDs = new String[maxAllowedBooks];
		//when constructing new object, he doesnt have any books
		this.currentNumberOfBooks = 0;
	}//end of Subscription constructor
	
	
	//instance method when borrow a book, save is id to the IDs array, and count it with currentNumberOfBooks
	//complexity - O(1)
	public void borrowBook(String bookID) {
		//only if the subscription doesnt have 10 books yet
		if(this.currentNumberOfBooks < maxAllowedBooks) {
			this.currentBooksIDs[currentNumberOfBooks] = bookID;
			this.currentNumberOfBooks++;
		}
	}//end of method borrowBook
	
	/*instance method when return book to library.
	 * search the book in the books array, remove it from the array.
	 * the counter count 1 down.
	 * complexity - O(1)
	 */
	public void returnBookToLibrary(String bookID) {
		//this boolean is here to help insure we are going just until we find the appropriate book
		boolean foundRelevantBook = false;
		//goes over the book array
		//Max complexity - O(10)
		//complexity - O(1)
		for(int i = 0; i < this.currentNumberOfBooks && !foundRelevantBook; i++) {
			if(this.currentBooksIDs[i].equals(bookID)) {
				foundRelevantBook = true;
				//the while loop just continue the for loop to remove the object from the array
				while(i < currentNumberOfBooks - 1) {
					this.currentBooksIDs[i] = this.currentBooksIDs[i +1 ];
				}
				//when removing a book from the array the last spot in the array should remain empty
				this.currentBooksIDs[currentNumberOfBooks] = null;
				this.currentNumberOfBooks--;
			}
		}
	}//end of method returnBookToLibrary
	
	//instance method to print the subscription data
	//ccomplexity - O(1)
	public void printSubscription() {
		System.out.println("Subscriber Last Name: " + this.lastName);
		System.out.println("Subscriber ID: " + this.subscriberID);
		System.out.println("Subscriber Number Of Books In Hold: " + this.currentNumberOfBooks);
		System.out.println("Books:");
		//loop to print the subscription books
		//Max complexity - O(10)
		//complexity - O(1)
		for(int i = 0; i < this.currentNumberOfBooks; i++) {
			System.out.print(this.currentBooksIDs[i]);
			if(i < this.currentNumberOfBooks - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("");
	}//end of method printSubscription
}//end of class Subscription
