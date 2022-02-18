/*
 * WordsSet is a class holds the words list and contains the method of generating a random word out of it.
 */
public class WordsSet {
	//the words list for the game
	private static final String[] set = {"Hello", "Java", "Programming", "Sky", "Baby", "Scanner", "Exception", "Engineering", "English", "Money", "Zoom", "Character", "Story", "Antidisestablishmentarianism", "Israel", "Jerusalem"};
	
	/*
	 * generateRandomWord is a method that generated a random word out of the words list and return it.
	 */
	public static String generateRandomWord() {
		int randomIndex = (int) (Math.random() * set.length);
		return set[randomIndex];
	}
}
