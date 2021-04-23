package mmn14;
import java.util.Scanner;

/*
 * this class contains the methods for the exercise.
 * in order to fully understand this class, please take a look first at MinHeap Class, MinHeapObject Class,
 * ObjectsMinHeap class, than this class and finish with createInputArrays class.
 */
public class MergeAndSortKSeries {
	
	/*
	 * method main is used as user interface,
	 * this method calls merge method with 3 options of input:
	 * 1. default input - this is the example input from the book.
	 * 2. random input - a function create a random input.
	 * 3. manual input - the will ask the user to create his own inputs.
	 */
	public static void main(String[] args) {
		System.out.println ("\nWelcome to MergeAndSortKSeries");
		//initialize variables
		int[][] inputArrays;
		int userChoice;
		//initialize input from user
		Scanner scan = new Scanner(System.in);
		//boolean variable which will hold us in the method until user wants to exit
		boolean userWantExit = false;
		while(!userWantExit) {
			System.out.println ("\nwhat would like to do?(enter the appropriate number)");
			System.out.println ("1. Merge Using default inputArrays");
			System.out.println ("2. Merge Using random inputArrays");
			System.out.println ("3. Merge Using manual inputArrays");
			System.out.println ("\n\n9.Exit Code");
			//get input from the user
			userChoice = scan.nextInt();
			if(userChoice == 1) {
				inputArrays = createInputArrays.defaultInput();
				merge(inputArrays);
			}
			if(userChoice == 2) {
				inputArrays = createInputArrays.randomInput();
				merge(inputArrays);
			}
			if(userChoice == 3) {
				inputArrays = createInputArrays.manualInput();
				merge(inputArrays);
			}
			if(userChoice == 9) {
				userWantExit = true;
			}
			if(userChoice != 1 && userChoice != 2 && userChoice != 3 && userChoice != 9) {
				System.out.println ("wrong input, please try again");
			}
			
		}//end of while
		System.out.println ("\nBye Bye!");
		scan.close();
	}//end of method main
	
	/*
	 * merge method gets an arrays of arrays as an input, and return one sorted array from all the elements.
	 * a condition for this method to create correct sorted array is:
	 * each sub array inside input is sorted as a heap.
	 * the method gets an arrays of arrays as an input and turns every sub array to minimum heap.
	 * then create a special heap called ObjectsMinHeap, which is a minimum heap of MinHeapObject.
	 * this special Object contains a value on which all of the standard heap method will work, and the index of his source heap that it was taken from.
	 * our main heap is entering into a loop, in each iteration, the head of the heap is extracted into the output array, and the next value from the same source sub heap is entering to the heap instead.
	 * in each iteration the heap contains the k minimum values out of all the input heaps, and the head of heap is the always the minimum,
	 * so in each iteration the output array gets the minimum value from main heap - which keeps output array sorted for all time.
	 * complexity: (lines)
	 * 				83-97: O(1)
	 * 			   99-109: O(n * k)
	 * 			  111-122: O(1)
	 * 			  123-131: O(k * log(n))
	 * 				  146: O(1)
	 * 			  147-162: O(n * log(n))
	 * 			  167-169: O(n)
	 * 				  170: O(1)
	 * 
	 * 				total: max(O(n * k), O(n * log(n)))
	 * 		 in this case: O(n * log(n))
	 */
	public static int[] merge(int[][] inputArrays) {
		//i and j are indexes variables which we will use a lot during the method, defined here and initialized when need
		int i;
		int j;
		//k is number of arrays which we will use in this method, initialized to 0
		int k = inputArrays.length;
		//n is the total number of elements in the input arrays, initialized to 0
		int n = 0;
		//create new minimum heap array with number of heaps as number of input arrays
		MinHeap[] heapsInputArray = new MinHeap[k];
		
		/*
		 *the next two loops is about to count the total number of elements in our input, and create the minimum heaps accordingly.
		 *for each sub array inside input we construct a minimum heap and makeHeap to it. 
		 *complexity: O(n * k)
		 */
		System.out.print("The input was:");
		for(i = 0; i < inputArrays.length; i++) { //O(k)
			//for each sub array in input we construct minimum heap
			heapsInputArray[i] = new MinHeap(inputArrays[i]);
			heapsInputArray[i].makeHeap(); //O(ni)
			System.out.println("");
			for(j = 0; j < inputArrays[i].length; j++) {
				//count the elements
				n++;
				System.out.print(inputArrays[i][j]+" ");
			}//end of inner loop
		}//end of outer loop
		
		//after counting all the elements, we can define and initialize the output array to be in length of n. 
		int[] output = new int[n];
		
		//define our main heap which will send the values to the output array
		ObjectsMinHeap mainHeap = new ObjectsMinHeap();
		
		/*
		 * going thru all the first elements inside each sub heap and save them to our main.
		 *for each element we also save his souce heap index using our pre defined object - MinHeapObject.
		 *after we save each element, we delete him from his original heap.
		 *complexity: O(k * log(n))
		 */
		for(i = 0; i < heapsInputArray.length; i++) {//O(k)
			//this condition is here to avoid empty heaps
			if(heapsInputArray[i].array.length > 0) {
				//insert new object to our main heap
				mainHeap.insert(new MinHeapObject(i, heapsInputArray[i].array[0]));//O(log(k))
				//delete the value from the source heap
				heapsInputArray[i].delete(0);//O(log(ni))
			}
		}//end of loop
		
		/*
		 * the next loop works the following:
		 * 1 - set the first clear spot at output as the head of the heap
		 * 2 - check if there are more elements in the source sub heap in the input
		 * 3 - if not - we can delete this object from our heap, heap gets shorted by 1.
		 * 4 - if do - we replace the value of the head of the main heap with the current first value of his source sub heap
		 * 		than, we delete that first value from the sub heap, and than we heapify our main heap.
		 * 5 - i++
		 * complexity: we are going thru all the elements in the input exactly once, for each
		 * 			   in the worst case, we will have to invoke delete method which is O(log(ni))
		 * 			total complexity: O(n*log(n))
		 */
		//initializing i to 0 before entering the loop
		i = 0;
		while(mainHeap.size > 0) {
			//insert into output array
			output[i] = mainHeap.array[0].value;
			//checking if there are elements left in the source sub heap
			if(heapsInputArray[mainHeap.array[0].inputSourceIndex].size == 0) {
				mainHeap.delete(0);
			}else {
				//replace the value with the first element in the source sub heap
				mainHeap.array[0].value = heapsInputArray[mainHeap.array[0].inputSourceIndex].array[0];
				//delete first element from sub heap
				heapsInputArray[mainHeap.array[0].inputSourceIndex].delete(0);//O(log(ni))
				//heapify new object to his correct place in the main heap
				mainHeap.heapify(0);//O(log(k))
			}
			i++;
		}//end of while
		
		//printing the output
		//complexity: O(n)
		System.out.println("\n\nThe output is:");
		for(i = 0; i < output.length; i++) {
			System.out.print(output[i] + " ");
		}
		return output;
	}//end of method merge
}//end of class MergeAndSortKSeries
