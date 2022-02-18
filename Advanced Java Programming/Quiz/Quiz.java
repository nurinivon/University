import java.io.FileNotFoundException;

/*
 * Quiz class implements the quiz logic.
 * A Quiz instance has questions bank from which he sends the questions to the UI.
 */
public class Quiz {
	//constant variable for a quiz
	private final int SCORE_MULT = 100;

	//private properties of a Quiz instance
	private QuestionsBank qb;
	private int currentQuestionIndex;
	private int correctAnswers;
	
	/*
	 * The Quiz constructor could throw an exception as QuestionBank object handles external files.
	 */
	public Quiz() throws FileNotFoundException {
		this.qb = new QuestionsBank();
		this.currentQuestionIndex = -1;
		this.correctAnswers = 0;
	}
	
	/*
	 * hasNextQuestion is an instance method. it returns the Boolean value if the questions bank left anymore questions or not. 
	 */
	public Boolean hasNextQuestion() {
		return (this.currentQuestionIndex < (this.qb.getNumberOfQuestions() - 1));
	}
	
	/*
	 * getNextQuestion is an instance method. it creates a copy of the next question, shuffles its answers and return the copied instance.
	 */
	public Question getNextQuestion() {
		this.currentQuestionIndex++;
		Question q = new Question(this.qb.getQuestion(currentQuestionIndex));
		q.shuffleAnswers();
		return q;
	}
	
	/*
	 * isCorrectAnswer is an instance method. it gets an answer as string, and checks if this is the correct answer for the current question.
	 */
	public Boolean isCorrectAnswer(String answer) {
		return this.qb.isCorrectAnswer(this.currentQuestionIndex, answer);
	}
	/*
	 * addCorrectAnswer is an instance method that adding a correct answer to the instance's counter.
	 */
	public void addCorrectAnswer() {
		this.correctAnswers++;
	}
	
	/*
	 * getScore is an instance method it returns the score of the question according to the correct answers counter.
	 * the method handles an arithmetic exception as division by zero is possible in this case.
	 */
	public int getScore() {
		try {
			return SCORE_MULT * this.correctAnswers / this.qb.getNumberOfQuestions();
		}catch(ArithmeticException e) {
			return 0;
		}
	}
	
	/*
	 * restart method is an instance method. initialize the properties to the beginning values.
	 * in addition, the order of the questions will be changed.
	 */
	public void restart() {
		this.currentQuestionIndex = -1;
		this.correctAnswers = 0;
		this.qb.shuffleQuestions();
	}
}
