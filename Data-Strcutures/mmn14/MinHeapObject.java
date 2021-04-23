package mmn14;

/*
 * for the exercise we will need to remember for each object inside a heap, the index
 * of the sub array inside the input array which it came from.
 * in order to that the heap we are using wont be a integers heap,
 * it will be a MinHeapObjects heap.
 * MinHeapObject will contain a value on which all of the heap methods will work,
 * and also inputSourceIndex to remember the sub array inside the input array which it came from.
 */
public class MinHeapObject {
	int inputSourceIndex;
	int value;
	
	//the constructor for MinHeapObject.
	//complexity: O(1)
	public MinHeapObject(int inputSourceIndex,int value){
			this.inputSourceIndex=inputSourceIndex;
			this.value = value;
	}//end of constructor
}//end of class MinHeapObject
