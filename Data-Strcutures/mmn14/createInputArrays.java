package mmn14;
import java.util.Scanner;

/*
 * this class contains three methods to create input arrays for MergeAndSortKSeries.merge method.
 */

public class createInputArrays {
	
	//this method returns the input as the example values from the book
	public static int[][] defaultInput(){
		int[][] inputArrays = new int[][] {{3, 7, 18, 32}, {6, 27, 35}, {14, 28, 75, 96, 107}};
		return inputArrays;
	}//end of method defaultInput
	
	/*
	 * this method returns a random input.
	 * number of sub arrays: 0-10
	 * each sub array length: 0-10
	 * values 0-100
	 */
	public static int[][] randomInput(){
		//random number of sub arrays
		int k = (int) (Math.random() * 10);
		//define the input array
		int[][] inputArrays = new int[k][];
		for (int i = 0; i < k; i++) {
			//random each sub array length
			int ni = (int) (Math.random() * 10);
			inputArrays[i] = new int [ni]; 
			for(int j = 0; j < ni; j++) {
				//random value
				inputArrays[i][j] = (int) (Math.random() * 100);
			}//end of inner loop
		}//end of outer loop
		return inputArrays;
	}//end of method randomInput
	
	
	 //this method has made in order to let the user manually create the input arrays 
	public static int[][] manualInput(){
		//create new scanner
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		//boolean which will keep us in the loop until the user will input correct value
		boolean userKInput = false;
		int k = 0;
		//a loop which will keep us the user will input correct value
		while(!userKInput) {
			System.out.println ("Please enter the number of series you would like to add(0-10)");
			k = scan.nextInt();
			//arbitrary 0-10 sub arrays
			if (k >= 0 && k <= 10) {
				userKInput = true;
			}else {
				System.out.println ("wrong input, please try again");
			}
		}//end of while
		//initializing input arrays
		int[][] inputArrays = new int[k][];
		/*
		 * the next loop will go thru all the sub arrays, for each - it will ask the user to define 
		 * the length of it, and then the values inside.
		 */
		for (int i = 0; i < k; i++) {
			//boolean which will keep us in the loop until the user will input correct value
			boolean userLengthInput = false;
			int length = 0;
			//a loop which will keep us the user will input correct value
			while(!userLengthInput) {
				System.out.println ("Please enter the length" + "(0-10)" + "of series of index"+ i);
				length = scan.nextInt();
				//arbitrary length 0-10
				if (length >= 0 && length <= 10) {
					userLengthInput = true;
				}else {
					System.out.println ("wrong input, please try again");
				}
			}//end of while
			//initialize the current array
			inputArrays[i] = new int[length];
			//a loop to define each sub arrays' values
			for(int j = 0; j < length; j++) {
				//boolean which will keep us in the loop until the user will input correct value
				boolean userNewValue = false;
				int value = 0;
				//a loop which will keep us the user will input correct value
				while (!userNewValue) {
					System.out.println ("please enter new value (0-1000)");
					value = scan.nextInt();
					//arbitrary value 0-1000
					if (value >= 0 && value <= 1000) {
						userNewValue = true;
					}else {
						System.out.println ("wrong input, please try again");
					}
				}//end of while
				inputArrays[i][j] = value;
			}//end of inner loop
		}//end of outer loop
		return inputArrays;
	}//end of method manualInput
}//end of class createInputArrays
