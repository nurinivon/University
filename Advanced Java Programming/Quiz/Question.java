import java.util.ArrayList;
import java.util.Collections;

/*
 * Question class handles Questions objects
 */
public class Question {
	
	//constants variables for this class
	private final int CORRECT_ANSWER_INDEX = 0;
	
	//private attributes of Question instance
	private String questionStr;
	private ArrayList<String> answers;
	
	//Question constructor initialize the answers list.
	public Question() {
		this.answers = new ArrayList<String>();
	}
	
	//a duplication constructor
	public Question(Question q) {
		this.questionStr = q.questionStr;
		this.answers = new ArrayList<String>(q.getAnswers());
	}
	
	//setter for the questionsStr attribute
	public void setQuestionStr(String str) {
		this.questionStr = str;
	}
	
	//add answer to the answers list
	public void addAnswer(String answer) {
		this.answers.add(answer);
	}
	
	//getter for the questionsStr attribute
	public String getQuestionStr() {
		return this.questionStr;
	}
	
	//get an answer by index
	public String getAnswer(int index) {
		return this.answers.get(index);
	}
	
	//getter for the answers list
	public ArrayList<String> getAnswers(){
		return this.answers;
	}
	
	//shuffleAnswers is a method that changes the order of the answers in the answers list.
	public void shuffleAnswers() {
		Collections.shuffle(this.answers);
	}
	
	/*
	 * isCorrectAnswer is an instance method that checks if an answer is the corrects answer for a question.
	 * It is a correct answer if it equals to the first answer in the answers list.
	 */
	public Boolean isCorrectAnswer(String answer) {
		return this.answers.get(CORRECT_ANSWER_INDEX).equals(answer);
	}
	
	/*
	 * this method used for debugging
	 */
	public void printAnswers() {
		for(String temp : answers) {
			System.out.println(temp);
		}
	}
}
