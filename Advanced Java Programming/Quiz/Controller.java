import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
 * Controller is the class that uses as controller to handle the GUI's events
 */
public class Controller {
	//auto initialized properties connected to the UI nodes
    @FXML
    private VBox welcomeVbox;
    @FXML
    private VBox scoreVbox;
    @FXML
    private VBox questionVbox;
    @FXML
    private Text question;
    @FXML
    private RadioButton rb1;
    @FXML
    private RadioButton rb2;
    @FXML
    private RadioButton rb3;
    @FXML
    private RadioButton rb4;
    @FXML
    private Text scoreNumber;
    
    //the class's constants variables
    private final String FILE_NOT_FOUND_MESSAGE = "Could not open the file 'exam.txt'";
    private final String CHOOSE_ANSWER_MESSAGE = "Please Choose One Answer";
    private final String CORRECT_ANSWER_MESSAGE = "Correct !";
    private final String WRONG_ANSWER_MESSAGE = "Wrong Answer";
    //private attributes for the controller
    private static Quiz quiz; //quiz is static, no need to initialize it more than once
    private ToggleGroup radioGroup = new ToggleGroup();
    
    /*
     * startQuiz is the method that handles the "start game" button's click event.
     * it creates a new quiz instance, it sets the the radio buttons to be under the same group and set the scene for the questions.
     * creation of a new quiz could throw an exception as it is made out of an external file so an exception handling is required.
     */
    public void startQuiz(ActionEvent e) {
    	try {
			quiz = new Quiz();
			rb1.setToggleGroup(radioGroup);
	    	rb2.setToggleGroup(radioGroup);
	    	rb3.setToggleGroup(radioGroup);
	    	rb4.setToggleGroup(radioGroup);
	    	initScene();
		} catch (FileNotFoundException fe) {
			JOptionPane.showMessageDialog(null, FILE_NOT_FOUND_MESSAGE);
		}
    }
    
    /*
     * initScene will be called after the game starts or after it restarts.
     * it hide all unnecessary content and move on to set the question.
     */
    public void initScene() {
    	scoreVbox.setVisible(false);
    	welcomeVbox.setVisible(false);
    	
    	setNextQuestion();
    }
    
    /*
     * setNextQuestion is a method that sets the UI according to the question's properties.
     * if there are no more questions than it will move on to the score scene.
     */
    public void setNextQuestion() {
    	if(quiz.hasNextQuestion()) {
        	radioGroup.selectToggle(null); //make sure previous selection is not cached
    		Question q = quiz.getNextQuestion();
    		question.setText(q.getQuestionStr());
    		rb1.setText(q.getAnswer(0));
    		rb2.setText(q.getAnswer(1));
    		rb3.setText(q.getAnswer(2));
    		rb4.setText(q.getAnswer(3));
        	questionVbox.setVisible(true);
    	}else {
    		setScorePage();
    	}
    }
    
    /*
     * submitAnswer is the method handles the "submit" button for each question.
     * it indicates after each submit if the user marked the correct answer or not and continues to the next question. 
     */
    public void submitAnswer(ActionEvent e) {
    	//check if there is a selection
    	if(radioGroup.getSelectedToggle() != null) {
    		String message;
    		RadioButton chosenRb = (RadioButton) radioGroup.getSelectedToggle();
    		if(quiz.isCorrectAnswer(chosenRb.getText())){
    			message = CORRECT_ANSWER_MESSAGE;
    			quiz.addCorrectAnswer(); //mark the answer as correct in the quiz instance
    		}else {
    			message = WRONG_ANSWER_MESSAGE;
    		}
    		int choice = JOptionPane.showConfirmDialog(null, message, null, JOptionPane.DEFAULT_OPTION);
    		if(choice == JOptionPane.OK_OPTION) {
    			setNextQuestion();
			}
    	}else {
    		JOptionPane.showMessageDialog(null, CHOOSE_ANSWER_MESSAGE);
    	}
    }
    
    /*
     * setScorePage is the method called after no more questions in the quiz.
     * it hides unnecessary content and show the appropriate one.
     */
    public void setScorePage() {
    	questionVbox.setVisible(false);
    	scoreNumber.setText(String.valueOf(quiz.getScore()));
    	scoreVbox.setVisible(true);
    }
    
    /*
     * restartQuiz handles the "restart" button's click event.
     * it restart the quiz (restart doesn't create new instance) and initialize the scene again.
     */
    public void restartQuiz(ActionEvent e) {
    	quiz.restart();
    	initScene();
    }

}