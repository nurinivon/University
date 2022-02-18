/*
 * Nuri Nivon 2021
 * The following program is a game which generate a random word out of words list.
 * The user has to guess the word letter by letter until he completes the whole word.
 */
import java.util.Scanner;

/*
 * Main class is the is main class of the game and it creates the interface for the game.
 */
public class Main {
	/*
	 * main method creates the interface for the game.
	 * when it starts a new game is created and a random word is been generated, when the users completes the word he can start a new game or exit.
	 */
	public static void main(String args[]) {
		Boolean userWantsToPLay = true;
		Scanner scan = new Scanner(System.in);
		Game currentGame;
		while(userWantsToPLay) {
			currentGame = new Game();
			while(!currentGame.isDone()) {
				System.out.println("\nPlease choose a charcter");
				currentGame.printCurrentWord();
				currentGame.printUnchosenChars();
				char c = scan.next().charAt(0);
				currentGame.checkChar(c);
			}
			System.out.println("You Completed The Hidden Word !");
			currentGame.printNumberOfGuesses();
			currentGame.printCurrentWord();
			System.out.println("\nFor New Game Please Enter '1'");
			String userInput = scan.next();
			userWantsToPLay = (userInput.length() == 1 && userInput.charAt(0) == '1');
		}
		System.out.println("Bye Bye");
		scan.close();
	}
}
