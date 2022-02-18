/*
 * ChosenWord is a class handles the ChosenWord objects.
 * ChosenWord objects contains the original word as string, and chars array initialize with '_' and each letters is discovered according to user's guesses.
 */
public class ChosenWord {
	private String originalWord;
	private char[] chars;
	
	/*
	 * constructor for ChosenWord object. has to be constructed out of a word as string.
	 * original word is being saved in the object
	 * the chars array initialize to '_' array
	 */
	public ChosenWord(String word) {
		this.originalWord = word;
		this.chars = new char[word.length()];
		for(int i = 0; i < this.chars.length; i++) {
			this.chars[i] = '_';
		}
	}
	
	/*
	 * isCompleted method checks if the chosen word is fully discovered and returns the appropriate boolean value.
	 */
	public Boolean isCompleted() {
		for(char c : this.chars) {
			if(c == '_') {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * print method prints the current word with letters already discovered and '_' char for those didnt discovered yet.
	 */
	public void print() {
		System.out.print("\n");
		for(char c : this.chars) {
			System.out.print(c + " ");
		}
		System.out.print("\n");
	}
	
	/*
	 * checkChar is a method that gets a char as input and check if the char appears in the chosen word.
	 * if the char exists in the word it will be shown on the chars array
	 */
	public void checkChar(char c) {
		char[] temp = originalWord.toCharArray();
		int i = 0;
		Boolean charFound = false;
		for(char tempC : temp) {
			if(Character.toLowerCase(tempC) == c) {
				charFound = true;
				this.chars[i] = tempC;
			}
			i++;
		}
		if(charFound) {
			System.out.println("\nGood Job !");
		}else {
			System.out.println("This char doenst exist :(");	
		}
	}
}
