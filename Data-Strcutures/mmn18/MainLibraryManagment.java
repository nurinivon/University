package mmn18;
import java.util.*;

/*
 * this class is the main class for the Library exercise.
 * this class is only the interface for the program.
 * in this class we are using the Library, Book, Subscription - Methods and Objects.
 */

public class MainLibraryManagment {
	
	//global Library Object
	static Library myLibrary = new Library();
	
	//the main method of the class and the exercise
	public static void main(String[] args) {
		System.out.println ("\nWelcome to My Library");
		//variable that will holds the user input
		String userChoice;
		//variable that will keep us in the interface
		boolean userWantExitCode = false;
		//this loop will continue until the user will want to exit
		while(!userWantExitCode) {
			System.out.println ("\nWhat Would You Like To Do?(enter the appropriate number)");
			System.out.println ("1. Manage Library Using Statements");
			System.out.println ("2. Get Statements Explanations");
			System.out.println ("\n\n9.Exit Code");
			
			//scan users statement
			userChoice = new Scanner(System.in).nextLine();
			
			//a call to managingStatments method
			if(userChoice.equals("1")) {
				managingStatments();
			}else {
				//a call to getStatementsExplanations method
				if(userChoice.equals("2")) {
					getStatementsExplanations();
				}else {
					//will exit the code
					if(userChoice.equals("9")) {
						userWantExitCode = true;
					}else {
						System.out.println ("Invalid Choice");
					}
				}
			}
		}
		System.out.println ("\nBye Bye!");
	}//end of method main
	
	/*
	 * this method is sub interface for the program, this were all the statements will be done.
	 * all of the methods will be work on the myLibrary variable.
	 * the complexity of this method is depend on the user choice,
	 * all of the methods here are from one of the linked classes (Book, Library, Subscription), and the complexities are details in each.
	 */
	public static void managingStatments() {
		//global Subscription to be used in the method
		Subscription currentSubscription;
		//global Book to be used in the method
		Book currentBook;
		//global String to keep the user statement
		String userStatement;
		//a variable that will keep us in the method until the user want to exit
		boolean userWantReturnLibrary = false;
		while(!userWantReturnLibrary) {
			System.out.println ("\nPlease Enter A Statement: (to return to main menu please enter '9')");
			//scan the user statement
			userStatement = new Scanner(System.in).nextLine();
			//9 means user wants to return to main menu
			if(userStatement.equals("9")) {
				userWantReturnLibrary = true;
			}else {
				/*
				 * the user should enter a statement separated with spaces
				 * we will split the statement to sub strings instead of spaces
				 * there should only 3 options for the substirngs length - 2,3,4.
				 * 2 - for insert new book, insert print max books holders, check which subscription holds specific book, check what books a subscriber holds
				 * 3 - insert new subscription, remove subscription
				 * 4 - borrow book, return book
				 */
				String[] splittedString = userStatement.split(" ");
				if(splittedString.length == 4) {
					//if the statement ends with '+', we need to borrow book
					if(splittedString[3].equals("+")) {
						//the book id will be stored in the third sub string
						currentBook = myLibrary.getBook(splittedString[2]);
						//the subscriber id will be stored in the 2nd sub string
						currentSubscription = myLibrary.getSubscription(splittedString[1]);
						//check if there is such book in the library
						if(currentBook != null) {
							//check if its not borrowed yet
							if(currentBook.currentOwnerID == null) {
								//check if there is such subscription in the library
								if(currentSubscription != null) {
									//check if the subscription doesn't have 10 books already
									if(currentSubscription.currentNumberOfBooks < 10) {
										//borrow the book, and print appropriate message
										myLibrary.borrowBook(splittedString[1], splittedString[2]);
										System.out.println ("You Asked To Borrow The Book " + splittedString[2] + " To The Subscription With ID " + splittedString[1] + " And Last Name " + splittedString[0]);
										System.out.println ("Book Borrowed Successfully");
									}else {
										//validation message for subscription that already has 10 books
										System.out.println ("You Asked To Borrow The Book " + splittedString[2] + " To The Subscription With ID " + splittedString[1] + " And Last Name " + splittedString[0]);
										System.out.println ("This Subscription Already Holds 10 Books, No More Books Can Be Borrowed");
									}
								}else {
									//not find the subscription
									System.out.println ("You Asked To Borrow The Book " + splittedString[2] + " To The Subscription With ID " + splittedString[1] + " And Last Name " + splittedString[0]);
									System.out.println ("No Such Subscription With ID: " + splittedString[1]);
								}
							}else {
								//book is already borrowed
								System.out.println ("Sorry Book Is Already Borrowed");
								System.out.println ("Please Return The Book To The Library First");
							}
						}else {
							//not find book
							System.out.println ("You Asked To Borrow The Book " + splittedString[2] + " To The Subscription With ID " + splittedString[1] + " And Last Name " + splittedString[0]);
							System.out.println ("No Such Book With ID: " + splittedString[2]);
						}
					}else {
						//if the statement ends with '-', we need to return book
						if(splittedString[3].equals("-")) {
							//the book id will be stored in the third sub string
							currentBook = myLibrary.getBook(splittedString[2]);
							//the subscriber id will be stored in the 2nd sub string
							currentSubscription = myLibrary.getSubscription(splittedString[1]);
							//check if there is such book in the library
							if(currentBook != null) {
								//check that he has a owner
								if(currentBook.currentOwnerID != null) {
									//check if there is such subscription in the library
									if(currentSubscription != null) {
										//return the book to the library, print the appropriate message
										myLibrary.returnBookToLibrary(splittedString[2]);
										System.out.println ("You Asked To Return The Book " + splittedString[2] + " From The Subscription With ID " + splittedString[1] + " And Lastname " + splittedString[0] + " To The Library.");
										System.out.println ("Book Returned Successfully");
									}else {
										//not find subscription
										System.out.println ("You Asked To Return The Book " + splittedString[2] + " From The Subscription With ID " + splittedString[1] + " And Lastname " + splittedString[0] + " To The Library.");
										System.out.println ("No Such Subscription With ID: " + splittedString[1]);
									}
								}else {
									//book is already in the library
									System.out.println ("You Asked To Return The Book " + splittedString[2] + " From The Subscription With ID " + splittedString[1] + " And Lastname " + splittedString[0] + " To The Library.");
									System.out.println ("This Book Is Not Borrowed");
								}
							}else {
								//not find book
								System.out.println ("You Asked To Return The Book " + splittedString[2] + " From The Subscription With ID " + splittedString[1] + " And Lastname " + splittedString[0] + " To The Library.");
								System.out.println ("No Such Book With ID: " + splittedString[2]);
							}
						}else {
							//invalid input
							System.out.println ("Invalid Statement");
						}
					}
				}else {
					//if length is 3, the user wants either add or remove a subscription
					if(splittedString.length == 3) {
						//if the 1st sub string is '+', the user wants to add new subscription to the library
						if(splittedString[0].equals("+")) {
							//check that the subscriber id is made out of 9 digits
							if(splittedString[2].length() == 9) {
								//check if the subscriber id already exist
								currentSubscription = myLibrary.getSubscription(splittedString[2]);
								if(currentSubscription == null) {
									//adding the subscription and print message
									myLibrary.addSubscription(splittedString[1], splittedString[2]);
									System.out.println ("You Asked To Add Subscribtion With Lastname " + splittedString[1] + " And ID " + splittedString[2] + " To The Library.");
									System.out.println ("Subscription Added Successfully.");
								}else {
									//subscription already exist
									System.out.println ("You Asked To Add Subscribtion With Lastname " + splittedString[1] + " And ID " + splittedString[2] + " To The Library.");
									System.out.println ("This Subscriber Already Exist");
								}
							}else {
								//validation message for +- 9 digits number
								System.out.println ("You Asked To Add Subscribtion With Lastname " + splittedString[1] + " And ID " + splittedString[2] + " To The Library.");
								System.out.println ("Invalid ID Number, Should Contain 9 Digits.");
							}
						}else {
							//if the 1st sub string is '-', the user wants to remove subscription from the library
							if(splittedString[0].equals("-")) {
								//check that the subscriber id is made out of 9 digits
								if(splittedString[2].length() == 9) {
									//remove the subscription and prin message
									myLibrary.removeSubscription(splittedString[2]);
									System.out.println ("You Asked To Remove Subscribtion With Lastname " + splittedString[1] + " And ID " + splittedString[2] + " From The Library.");
									System.out.println ("Subscription removed Successfully");
								}else {
									//validation message for +- 9 digits number
									System.out.println ("You Asked To Remove Subscribtion With Lastname " + splittedString[1] + " And ID " + splittedString[2] + " From The Library.");
									System.out.println ("Invalid ID Number, Should Contain 9 Digits.");
								}
							}else {
								//invalid input
								System.out.println ("Invalid Statement");
							}
						}
					}else {
						if(splittedString.length == 2) {
							//it can be start or with '?' or with '+', otherwise - invalid input
							if(splittedString[0].equals("?")) {
								//if the 2nd sub string is '!', the user wants us to print the subscribers who holds the max number of books
								if(splittedString[1].equals("!")) {
									//print appropriate message and call the printMaxBooksHolders method.
									System.out.println ("You Asked To Know Who Are The Subscribers That Holds The Max Number Of Books");
									myLibrary.printMaxBooksHolders();
								}else {
									//if the 2nd sub string length is 9, the user wants to see the books that this subscribers holds
									if(splittedString[1].length() == 9) {
										//check if there is such subscriber in the library
										currentSubscription = myLibrary.getSubscription(splittedString[1]);
										if(currentSubscription != null) {
											//print the appropriate message and print the subscription properties
											System.out.println ("You Asked To Know What Books Do The Subscriber With ID " + splittedString[1] + " Holds.");
											System.out.println ("The Answer Is:");
											currentSubscription.printSubscription();
										}else {
											//not find subscription
											System.out.println ("You Asked To Know What Books Do The Subscriber With ID " + splittedString[1] + " Holds.");
											System.out.println ("No Such Subscription In My Library");
										}
									}else {
										//if the 2nd sub string is length 6, the user wants to check which subscriber holds this book
										if(splittedString[1].length() == 6) {
											currentBook = myLibrary.getBook(splittedString[1]);
											//check if there is such book in the library
											if(currentBook != null) {
												//check if the book is borrowed
												if(currentBook.currentOwnerID != null) {
													//get the book's owner and print his properties
													currentSubscription = myLibrary.getSubscription(currentBook.currentOwnerID);
													System.out.println ("You Asked To Know Which Subscriber Holds The Book: " + splittedString[1] + ".");
													System.out.println ("The Subscriber Is:");
													currentSubscription.printSubscription();
												}else {
													//book is not borrowed, is in the library
													System.out.println ("You Asked To Know Which Subscriber Holds The Book: " + splittedString[1] + ".");
													System.out.println ("This Book Is Not Borrowed, Is In The Library");
												}
											}else {
												//not find book
												System.out.println ("You Asked To Know Which Subscriber Holds The Book: " + splittedString[1] + ".");
												System.out.println ("No Such Book In My Library");
											}
										}else {
											System.out.println ("Invalid Statement");
										}
									}
								}
							}else {
								//if the first sub string is '+', user wants to add new book to library
								if(splittedString[0].equals("+")) {
									//check validation book id needs to be made out of 2 letter and 4 digits 
									if(splittedString[1].length() == 6) {
										//check if this book id already exist
										currentBook = myLibrary.getBook(splittedString[1]);
										if(currentBook == null) {
											//add the book, print appropriate message
											myLibrary.addBookToLibrary(splittedString[1]);
											System.out.println ("You Asked To Insert The Book " + splittedString[1] + " To The Library.");
											System.out.println ("Book Inserted Successfully.");
										}else {
											//book id already exist
											System.out.println ("You Asked To Insert The Book " + splittedString[1] + " To The Library.");
											System.out.println ("This Book ID Already Exist");
										}
									}else {
										//wrong book id message
										System.out.println ("Invalid Statement");
										System.out.println ("Book ID Should Be Made Out Of 2 Capital Letters And 4 Digits Number. For Example: AA1234");
									}
								}else {
									System.out.println ("Invalid Statement");
								}
							}
						}else {
							System.out.println ("Invalid Statement");
						}
					}
				}
			}
			
		}//end of while
	}// end of method managingStatments
	
	//this method will contain explanations for how to manage the library using the statements
	//complexity O(1)
	public static void getStatementsExplanations() {
		//variable to keep the user choice
		String userChoice;
		//variable that will keep us in the loop until user want to return the main method
		boolean userWantReturnLibrary = false;
		while(!userWantReturnLibrary) {
			//those are The Only Propper Methods Can Be Done In This Library
			System.out.println ("\nThe Following Are The Only Propper Methods Can Be Done In This Library.");
			System.out.println ("(enter the method number in order to see the statement to invoke it)");
			System.out.println ("1. insert new book to library");
			System.out.println ("2. insert new subscription to library");
			System.out.println ("3. delete subscription from library");
			System.out.println ("4. subscriber wants to borrow book");
			System.out.println ("5. subscriber wants to return a book to the library");
			System.out.println ("6. see what books do a subscriber have");
			System.out.println ("7. find the subscriber which holds a specific book");
			System.out.println ("8. find the subscribers who hold the max number of books");
			System.out.println ("9. return to library");
			
			//scan users next choice
			userChoice = new Scanner(System.in).nextLine();
			
			/*
			 * for all the next statements, for each chosen method from the list, the program will print the statement structure.
			 */
			if(userChoice.equals("1")) {
				System.out.println ("In order to insert new book to library, use the next statement:");
				System.out.println ("+ <book_id>");
			}else {
				if(userChoice.equals("2")) {
					System.out.println ("In order to insert new subscription to library, use the next statement:");
					System.out.println ("+ <subscriber_lasname> <subscriber_id>");
				}else {
					if(userChoice.equals("3")) {
						System.out.println ("In order to delete subscription from library, use the next statement:");
						System.out.println ("- <subscriber_lasname> <subscriber_id>");
					}else {
						if(userChoice.equals("4")) {
							System.out.println ("In order to invoke borrow method, use the next statement:");
							System.out.println ("<subscriber_lasname> <subscriber_id> <book_id> +");
						}else {
							if(userChoice.equals("5")) {
								System.out.println ("In order to return book to the library, use the next statement:");
								System.out.println ("<subscriber_lasname> <subscriber_id> <book_id> -");
							}else {
								if(userChoice.equals("6")) {
									System.out.println ("In order to see what books do a subscriber have, use the next statement:");
									System.out.println ("? <subscriber_id>");
								}else {
									if(userChoice.equals("7")) {
										System.out.println ("In order to find the subscriber which holds a specific book, use the next statement:");
										System.out.println ("? <book_id>");
									}else {
										if(userChoice.equals("8")) {
											System.out.println ("In order to find the subscribers who hold the max number of books, use the next statement:");
											System.out.println ("? !");
										}else {
											if(userChoice.equals("9")) {
												userWantReturnLibrary = true;
											}else {
												System.out.println ("Invalid Choice");
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}//end of method getStatementsExplanations
}//end of class MainLibraryManagment
