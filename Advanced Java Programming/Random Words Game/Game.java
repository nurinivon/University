/*
 * Game is a class handles the Game objects.
 * Game object has number of guesses, chosen word object (randomly generated) and list of letters to choose from (a-z).
 */
public class Game {
	private int numOfGuesses;
	private ChosenWord currentChosenWord;
	private String unChosenChars;
	
	/*
	 * Game constructor print a message to the user.
	 * number of guesses initialize to 0, chosen is randomly generated, and unchosen chars are initialize to a-z.
	 */
	public Game() {
		System.out.println("Welcom To Random Words Game !\n\nTo complete the hidden word please choose one of the chars from the list below:");
		this.numOfGuesses = 0;
		currentChosenWord = new ChosenWord(WordsSet.generateRandomWord());
		this.unChosenChars = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
	}
	
	/*
	 * isDone is an instance method for game object.
	 * the game is done when the chosen word is fully discovered.
	 */
	public Boolean isDone() {
		return this.currentChosenWord.isCompleted();
	}
	
	/*
	 * printCurrentWord is an instance method for game object.
	 * it prints the current chosen word. letters that didnt discovered yet will be printed as '_'
	 */
	public void printCurrentWord() {
		this.currentChosenWord.print();
	}
	
	/*
	 * printUnchosenChars print the letters (a-z) that have not been chosen yet.
	 */
	public void printUnchosenChars() {
		System.out.println(this.unChosenChars);
	}
	
	/*
	 * printNumberOfGuesses is an instance method for game object.
	 * it prints the number of guesses the user tried to choose letters
	 */
	public void printNumberOfGuesses() {
		System.out.println("Number Of Guesses: " + this.numOfGuesses);
	}
	
	/*
	 * checkChar is a method that gets a char as input and check if the char appears in the chosen word.
	 * the method checks if the char is a letter - only a letter is a valid input
	 * a char that was chosen before will be removed from the unchosen chars, and could not be chosen again
	 * if the chars is valid it will be sent to be checked by the chosen word if it exist in it.
	 * 
	 * The Method Ignores Letters Case (A == a)
	 */
	public void checkChar(char c) {
		this.numOfGuesses++;
		if(Character.isLetter(c)) {
			c = Character.toLowerCase(c);
			if(unChosenChars.indexOf(c) != -1) {
				this.unChosenChars = String.join("", this.unChosenChars.split(String.valueOf(c)));
				this.unChosenChars = this.unChosenChars.replace("  ", " ");
				this.currentChosenWord.checkChar(c);
			}else {
				System.out.println("This character has already been chosen. Please choose another character.");
				return;
			}
		}else {
			System.out.println("Invalid Charachter");
			return;
		}
	}
}
