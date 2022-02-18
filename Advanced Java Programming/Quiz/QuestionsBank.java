import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * QuestionsBank class is the implementation for a questions list of a quiz.
 * The QuestionsBank is created out of an external file of questions.
 */
public class QuestionsBank {
	//constant variables for this class
	private final int QUESTION_LINES = 5;
	private final String EXAM_PATH = "exam.txt"; //the path to .txt contains the questions
	
	//private questions list
	private ArrayList<Question> questions;
	
	/*
	 * QuestionsBank constructor throws an exception as it handles external file.
	 * the exception is thrown and not handled here to allow handling it on the UI side
	 * questions file logic:
	 * a question should be made out of 5 lines in the text file.
	 * first line is question itself.
	 * second line is the correct answer
	 * rest 3 is some wrong answers
	 * 
	 * follow the above logic as no validation could be done regarding this logic.
	 */
	public QuestionsBank() throws FileNotFoundException{
		//init the questions list
		this.questions = new ArrayList<Question>();
		//read the file using scanner
		Scanner input = new Scanner(new File(EXAM_PATH));
		Question q = new Question();
		//lines counter
		int i = 0;
		//iterate the file line by line
		while(input.hasNextLine()) {
			String st = input.nextLine();
			//check line is not empty
			if(!st.isBlank()) {
				//every 5 lines we create a new question instance
				if(i % QUESTION_LINES == 0) {
					q = new Question();
					q.setQuestionStr(st);
				}else {
					//if it's not the fifth line than it is an answer
					q.addAnswer(st);
					//after adding the last answer we add the question to the questions list
					if(i % QUESTION_LINES == 1) {
						this.questions.add(q);
					}
				}
				i++;
			}
		}
		//close scanner
		input.close();
	}
	
	/*
	 * getter for the questions list size
	 */
	public int getNumberOfQuestions() {
		return this.questions.size();
	}
	
	/*
	 * getter for a question instance by index
	 */
	public Question getQuestion(int index) {
		return this.questions.get(index);
	}
	
	/*
	 * isCorrectAnswer is an instance method. it gets the question's index and an answer as a string and return the Boolean value if this is the correct answer or not.
	 */
	public Boolean isCorrectAnswer(int questionIndex, String answer) {
		return this.questions.get(questionIndex).isCorrectAnswer(answer);
	}
	
	/*
	 * shuffleQuestions is an instance method. it changes the order of the questions in the questions list.
	 */
	public void shuffleQuestions() {
		Collections.shuffle(this.questions);
	}
}
