import java.util.ArrayList;
/*
 * Calculator class is implementing the calculator logic.
 * The calculator handles the following operator only: '+' , '-' , '/' , '*'
 */
public class Calculator {
	
	//constant variables for the class
	private final String SYNTAX_ERROR = "Syntax Error";
	private final String MATH_ERROR = "Math Error";
	
	//private attribute for a calculator instance
	private String currentInput;
	
	//Calculator constructor initialize input to empty string
	public Calculator() {
		this.currentInput = "";
	}
	
	//basic setter for the currentInput attribute
	public void setInput(String str) {
		this.currentInput = str;
	}
	
	//basic getter for the currentInput attribute
	public String getInput() {
		return this.currentInput;
	}
	
	/*
	 * addInput is a method that adds a string to the currentInput.
	 * the method returns the currentInput after adding
	 */
	public void addInput(String str) {
		this.currentInput = this.currentInput + str;
	}
	
	/*
	 * deleteChar is a private method that delete single char from the end of the currentInput attribute.
	 */
	private void deleteChar() {
		if(this.currentInput.length() > 0) {
			this.currentInput = this.currentInput.substring(0, this.currentInput.length() - 1);
		}
	}
	
	/*
	 * delete is a public method allow to delete a char from currentInput attribute.
	 * if the last input is (-) or (+) it will delete all of it
	 */
	public void delete() {
		System.out.println(this.currentInput);
		if(this.currentInput.length() > 0) {
			if(this.currentInput.charAt(this.currentInput.length() - 1) == ')') {
				//4 chars need to be deleted including space
				this.deleteChar();
				this.deleteChar();
				this.deleteChar();
				this.deleteChar();
			}else {
				this.deleteChar();
			}
		}
	}
	
	/*
	 * clear is the method that clear current input from the calculator instance.
	 */
	public void clear() {
		this.currentInput = "";
	}
	
	/*
	 * changeSign is a method that adds a (-) or (+) to currentInput.
	 * if the last input is (-) or (+) it will not add a new one, it will replace the existing one.
	 * a sign is the only operand that will be saved with space (at the start)
	 * the method returns the new currentInput
	 * 
	 */
	public void changeSign() {
		if(this.currentInput.charAt(this.currentInput.length() - 1) == ')') {
			if(this.currentInput.charAt(this.currentInput.length() - 2) == '-') {
				this.deleteChar();
				this.deleteChar();
				this.addInput("+)");
			}else {
				this.deleteChar();
				this.deleteChar();
				this.addInput("-)");
			}
		}else {
			this.addInput(" (-)");
		}
	}
	
	/*
	 * getResult is that calculates the result of the currentInput is a math expression.
	 * the method returns the result as a string.
	 * the calculation considers also strong operators('*', '/') before weak operators('+','-').
	 * 
	 * Definitions:
	 * operand - a sign, operator of number
	 * block - two numbers need to be calculated with an operator with all the signs before them. example ["-","+","-","1","*","+","+","2"]
	 * 
	 * Algorithm:
	 * 1. create operands list from the input string. example: ["1","+","-","1"]
	 * 2. iterate the list until one operand left
	 * 3. 	find the next strong strong block ('*' or '/') and calculates it
	 * 4. 	remove the block calculated and place the result instead
	 * 5.	when no more strong blocks, calculate the first block in the list
	 * 6. 	remove the block calculated and place the result instead
	 * 7. 	when only one number in the list - calculate is sign and place it as one operand
	 * 8.return the result (if the result is complete number it will return with no precision)
	 */
	public String getResult() {
		//temp string to save the operand
		String tempStr = "";
		//initialize the operand list
		ArrayList<String> operands = new ArrayList<String>();
		/*
		 * first loop:
		 * iterate the currentinput char by char and create the operands list
		 */
		for(int i = 0; i < this.currentInput.length(); i++) {
			char c = this.currentInput.charAt(i);
			//an operand could be made with a lot of digits. ex: "14.5"
			if(Character.isDigit(c)) {
				tempStr = tempStr + c;
			}else if(c == '.') {//two '.' could not be shown on the same operand. ex: "14.5.3"
				if(tempStr.contains(".")) {
					return SYNTAX_ERROR;
				}
				tempStr = tempStr + c;
			}else if(isOperator(c)){
				//math expression cannot end with an operator
				if(i == (this.currentInput.length() - 1)) {
					return SYNTAX_ERROR;
				}
				//add the operand to the list
				if(!tempStr.isBlank()) {
					operands.add(tempStr);
					tempStr = "";
				}
				//add the operator as an operator to the list
				tempStr = tempStr + c;
				operands.add(tempStr);
				tempStr = "";
			}
		}
		//add last operand
		if(!tempStr.isBlank()) {
			operands.add(tempStr);
		}
		
		//the calculation start with assumption that there are strong operators: '*', '/'
		Boolean strongOperatorsLeft = true;
		/*
		 * second loop:
		 * iterate the operands list and calculate the result block by block
		 * in each iteration a block is removes until one operand is left and this is the result
		 */
		while (operands.size() > 1) {
			double x1 = 0.0;
			double x2 = 0.0;
			double mult1 = 1.0;
			double mult2 = 1.0;
			double tempResult;
			int startBlockIndex;
			int endBlockIndex;
			Boolean x1Found = false;
			Boolean x2Found = false;
			//the following is a break point nextCalc
			nextCalc:
			//checking for strong blocks
			if(strongOperatorsLeft) {
				//iterate the list until strong block is found
				for(int i = 0; i < operands.size(); i++) {
					if(isStrongOperator(operands.get(i))) {
						/*
						 * math expression cant start with strong operator
						 * math expression cant end with strong operator
						 * before a strong operator should be a number
						 */
						if((i == 0) || (i == (operands.size() - 1)) || (isOperator(operands.get(i - 1)))) {
							return SYNTAX_ERROR;
						}
						//init the variables
						x2Found = false;
						mult1 = 1.0;
						mult2 = 1.0;
						//first number in the block is the number before the operator
						x1 = Double.parseDouble(operands.get(i - 1));
						/*
						 * iterate back on the operands to find the start of the block.
						 * on each iteration if it is '-' we will change sign for the mult1 variable.
						 */
						for(startBlockIndex = i - 2; startBlockIndex >= 0 && isWeakOperator(operands.get(startBlockIndex)); startBlockIndex--) {
							if(operands.get(startBlockIndex).equals("-")) {
								mult1 *= -1.0;
							}
						}
						x1 *= mult1; //change sign to x1 if needed
						/*
						 * loop forward on the operands to find x2
						 */
						for(endBlockIndex = i + 1; endBlockIndex < operands.size() && !x2Found; endBlockIndex++) {
							//if we found strong operator it is syntax error. ex: ["*","+","-","*"]
							if(isStrongOperator(operands.get(endBlockIndex))) {
								return SYNTAX_ERROR;
							}
							//change sign for the mult2 variable if needed
							if(isWeakOperator(operands.get(endBlockIndex))) {
								if(operands.get(endBlockIndex).equals("-")) {
									mult2 *= -1.0;
								}
							}else {
								//when found a number it is x2
								x2 = Double.parseDouble(operands.get(endBlockIndex)) * mult2; //change sign to x2 if needed
								x2Found = true;
							}
						}
						//if we finish loop the operands and not found x2, its syntax error. ex: ["*","-","+"]
						if(!x2Found) {
							return SYNTAX_ERROR;
						}
						//calculate the result of the block
						if(operands.get(i).equals("/")) {
							if(x2 == 0.0) {
								return MATH_ERROR;
							}
							tempResult = x1 / x2;
						}else {
							tempResult = x1 * x2;
						}
						//remove all the block from start to end
						startBlockIndex++;
						while(endBlockIndex > startBlockIndex) {
							endBlockIndex--;
							operands.remove(endBlockIndex);
						}
						//add the result(absolute value) to the appropriate place in the operands list
						operands.add(startBlockIndex, String.valueOf(Math.abs(tempResult)));
						//a '-' will be added separately
						if(tempResult < 0.0) {
							operands.add(startBlockIndex, "-");
						}
						//break point break two loops
						break nextCalc;
					}
				}
				//if we reach end of list and not found strong operators we will switch the flag
				strongOperatorsLeft = false;
			}else {//if no more strong operators
				//init varaibles
				x1Found = false;
				x2Found = false;
				mult1 = 1.0;
				mult2 = 1.0;
				int i;
				int j;
				//iterate the operands list from start until found a number. this is x1
				for(i = 0; i < operands.size() && !x1Found; i++) {
					//if it is '-' change sign to mult1
					if(isWeakOperator(operands.get(i))) {
						if(operands.get(i).equals("-")) {
							mult1 *= -1.0;
						}
					}else {
						//when found x1, change is sign if needed
						x1 = Double.parseDouble(operands.get(i)) * mult1;
						x1Found = true;
					}
				}
				//if not found a number its syntax error. ex: ["+","-","+"]
				if(!x1Found) {
					return SYNTAX_ERROR;
				}
				//iterate the operands from x1 forward looking for x2
				for(j = i; j < operands.size() && !x2Found; j++) {
					//if it is '-' change sign to mult2
					if(isWeakOperator(operands.get(j))) {
						if(operands.get(j).equals("-")) {
							mult2 *= -1.0;
						}
					}else {
						//when found x2, change is sign if needed
						x2 = Double.parseDouble(operands.get(j)) * mult2;
						x2Found = true;
					}
				}
				//if not found x2 it means we have one number in the least and this is the last iteration
				if(!x2Found) {
					if(i == j) {//if x1 is the last operand in the list
						tempResult = x1;
						//remove all operands from start until j
						while(j > 0) {
							j--;
							operands.remove(j);
						}
						//add the result includes the sign as this is the last iteration
						operands.add(0, String.valueOf(tempResult));
					}else {
						//x1 is not the last operand but no more numbers -> syntax error. ex: ["+","2","-","+"]
						return SYNTAX_ERROR;
					}
				}else {
					//if found x2, calculate the result
					tempResult = x1 + x2;
					//remove all operands from start until j
					while(j > 0) {
						j--;
						operands.remove(j);
					}
					//add the result NOT includes the sign (absolute value) as may be more iterations
					operands.add(0, String.valueOf(Math.abs(tempResult)));
					//a '-' will be added separately
					if(tempResult < 0.0) {
						operands.add(0, "-");
					}
				}
			}
		}
		//finish the loop when operands list size is 1
		//the result will be the only operand left
		double result = Double.parseDouble(operands.get(0));
		/*
		 * returns the result as string
		 * if the result is complete number it will return with no precision
		 */
		if(result % 1 == 0) {
			return String.valueOf((int)result);
		}else {
			return String.valueOf(result);
		}
	}
	
	/*
	 * the following are private methods to indicate if a string or a char is:
	 * and operator at all.
	 * or
	 * strong operators: '/' or '*'
	 * weak operators: '+' or '-'
	 */
	private Boolean isStrongOperator(String s) {
		return (s.equals("/") || s.equals("*"));
	}
	private Boolean isWeakOperator(String s) {
		return (s.equals("+") || s.equals("-"));
	}
	private Boolean isOperator(char c) {
		return (c == '+' || c == '-' || c == '/' || c == '*');
	}
	private Boolean isOperator(String s) {
		return (s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*"));
	}
}
