package mmn18;

/*
 * this class define the book object that we will use in the library
 */

public class Book {
	//each book has those properties, id and current ownerID (if null - it means the book is in the library and not borrowed)
	String bookID;
	String currentOwnerID;
	
	//Book constructor, must have ID, and ownerId initialize to null.
	//complexity - O(1)
	public Book(String newBookID) {
		this.bookID = newBookID;
		this.currentOwnerID = null;
	}// end of Book Constructor
	
	//instance method when borrow a book to remember the ownerID
	//complexity - O(1)
	public void borrowBook(String ownerID) {
		this.currentOwnerID = ownerID;
	}// end of method borrowBook
	
	//instance method when return book to library is ownerID turns to null
	//complexity - O(1)
	public void returnBookToLibrary() {
		this.currentOwnerID = null;
	}// end of method returnBookToLibrary

}//end of class Book
