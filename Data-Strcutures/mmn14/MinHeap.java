package mmn14;

/*
 * this class define the common minimum heap object and the common heap methods.
 */
public class MinHeap {
	//the heap properties: size, and array of number
	int size;
	int[] array;
	
	//constructor for heap object from integers array. O(1)
	public MinHeap(int[] arr) {
		//size of the heap as the size of the array
		this.size = arr.length;
		//the array initialize to the input array
		this.array = arr;
	}//end of constructor
	
	/*
	 * instance void method invoked by MinHeap object. sorting his array property as a heap.
	 * going from the first element which is not a leaf all the way to the head of the heap.
	 * for each - heapify it to the right place.
	 * complexity: O(n)
	 */
	public void makeHeap() {
		for(int i = this.size / 2; i >= 0; i--) {
			this.heapify(i);
			
		}
	}//end of method makeHeap
	
	/*
	 * an instance recursive heapify method adjusted to invoked by MinHeap object.
	 * this method is legal only if the object invoked it is a legal heap except from one junction
	 * which his index is the given index.
	 * complexity: O(log(n))
	 */
	public void heapify(int index) {
		/*
		 * stopping conditions for the recursive function:
		 * 1 - size of heap is 0;
		 * 2 - current element is a leaf
		 * 3 - current element is smaller than both of his children
		 */
		if(this.size == 0 || this.isLeaf(index) || (this.array[index] <= this.left(index) && this.array[index] <= this.right(index) )) {
			return;
		//check which of his children is the smallest
		}else if(this.left(index) > this.right(index)){
			//if the right child is smaller, swap them and keep heapify
			this.swap(index, (index * 2) + 2 );
			this.heapify((index * 2) + 2);
		}else {
			//if the right child is smaller, swap them and keep heapify
			this.swap(index, (index * 2) + 1 );
			this.heapify((index * 2) + 1);
		}
	}//end of method heapify
	
	/*
	 * an instance method invoked by MinHeap object.
	 * the method gets 2 elements indexes and switch there places.
	 * complexity: O(1)
	 */
	public void swap(int firstIndex, int secondIndex) {
		int temp = this.array[firstIndex];
		this.array[firstIndex] = this.array[secondIndex];
		this.array[secondIndex] = temp;
	}//end of method swap
	
	/*
	 * an instance method invoked by MinHeap object.
	 * the method gets an index and return the value inside his left child
	 * complexity: O(1)
	 */
	public int left(int index) {
		return this.array[(index * 2) + 1];
	}//end of method left
	
	/*
	 * an instance method invoked by MinHeap object.
	 * the method gets an index and return the value inside his right child
	 * right method needs a fallback option in case a parent object has only left child.
	 * for this case the use of left and right methods are only to find minimum out of parent
	 * and his 2 children inside of minimum heap, so for this case the fallback option will be
	 * a maximun number, that ensure us that if there is no right child at all, so no use will
	 * be done with it.
	 * it is possible that this fallback will not suit all of the heap cases, but for our 
	 * personal case, it will ensure us correctness. 
	 * complexity: O(1)
	 */
	public int right(int index) {
		if((index * 2) + 2 < this.size)
			return this.array[(index * 2) + 2];
		return Integer.MAX_VALUE;
	}//end of method right
	
	/*
	 * an instance method invoked by MinHeap object.
	 * the method gets an index, return true if its a leaf and false if not.
	 * complexity: O(1)
	 */
	public boolean isLeaf(int index) {
		if(index <  this.size && index >= this.size / 2)
			return true;
		return false;
	}//end of method isLeaf
	
	/*
	 * an instance method invoked by MinHeap object.
	 * the method gets an element index, it put the element at the end of the array
	 * reduce the size of the heap by 1.
	 * all the other methods works with the size property and not with array.length,
	 * so the element will stay there but there will be no reference to him.
	 * heapify the [index] element to his right place.
	 * complexity: O(log(n))
	 */
	public void delete(int index) {
		//cant delete from null
		if(this.size <= 0)
			return;
		this.swap(index, this.size - 1);
		this.size--;
		this.heapify(index);//O(log(n))
	}//end of method delete
}//end of class MinHeap
