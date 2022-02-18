import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
/*
 * Controller is the class that uses as controller to handle the GUI's events
 */
public class Controller {
	//The following nodes are attached to the GUI nodes
    @FXML
    private Text inputStr;
    @FXML
    private Text resultStr;
    //constant variable for the class
    private final String DISABLED_MESSAGE = "Buttons are disabled. Press 'AC' for new calculation.";
    
    //private attributes for the controller class
    private static Calculator myCalc = new Calculator(); //the calc instance is static for the whole class
    private Boolean disabled = false; //a flag to disable button's click events
    
    /*
     * buttonClicked is the method most of the buttons is calling. it adds the text shows on the button to the input string on the screen.
     * 
     * NOTE: after pressing '=', all buttons are disable except 'AC'
     */
    public void buttonClicked(ActionEvent e) {
    	String text = ((Button)e.getSource()).getText();
    	if(!disabled) {
    		myCalc.addInput(text);
        	inputStr.setText(designStr(myCalc.getInput()));
    	}else {
    		JOptionPane.showMessageDialog(null, DISABLED_MESSAGE);
    	}
    }
    
    /*
     * ACClicked is the method called by clicking 'AC' button.
     * it clears the input from the screen and from the calculator instance.
     * it also enables the buttons.
     */
    public void ACClicked(ActionEvent e) {
    	myCalc.clear();
    	inputStr.setText(myCalc.getInput());
    	resultStr.setText("");
    	disabled = false;
    }
    
    /*
     * DELClicked is the method called by clicking 'DEL' button.
     * it deletes the last character from the screen and from the calculator instance.
     */
    public void DELClicked(ActionEvent e) {
    	if(!disabled) {
    		myCalc.delete();
        	inputStr.setText(designStr(myCalc.getInput()));
    	}else {
    		JOptionPane.showMessageDialog(null, DISABLED_MESSAGE);
    	}
    }
    
    /*
     * changeSignClicked is the method called by clicking '+/-' button.
     * a sign will be wrapped with '()'
     */
    public void changeSignClicked(ActionEvent e) {
    	if(!disabled) {
    		myCalc.changeSign();
    		inputStr.setText(designStr(myCalc.getInput()));
    	}else {
    		JOptionPane.showMessageDialog(null, DISABLED_MESSAGE);
    	}
    }
    
    /*
     * resultClicked is the method called by clicking '=' button.
     * it calculates the result for the current input and show it on the screen.
     */
    public void resultClicked(ActionEvent e) {
    	if(!disabled) {
        	resultStr.setText(myCalc.getResult());
        	disabled = true;
    	}else {
    		JOptionPane.showMessageDialog(null, DISABLED_MESSAGE);
    	}
    }
    
    /*
     * designStr is the method to design the string before it shows on the UI.
     * at the calculator instance the string is saved "1+1"
     * and on the screen it will appear "1 + 1" (with spaces).
     * a sign (+) or (-) will not be spaced from the number (-)9
     */
    public String designStr(String str) {
    	//next two lines are made to prevent design a sign rather than an operator
    	str = str.replace("(+)", "(&)");
    	str = str.replace("(-)", "(@)");
    	
    	//design the operators
    	str = str.replace("+", " + ");
    	str = str.replace("-", " - ");
    	str = str.replace("/", " / ");
    	str = str.replace("*", " * ");
    	
    	//switch back the signs
    	str = str.replace("(&)", "(+)");
    	str = str.replace("(@)", "(-)");
    	return str;
    }
}
