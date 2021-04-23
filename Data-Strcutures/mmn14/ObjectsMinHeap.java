package mmn14;

/*
 * this class defines the ObjectsMinHeap which is our special heap contain the heap size
 * and an array of MinHeapObject;
 * this class contains all the standard minimum heap methods, only just, adjusted to work
 * on MinHeapObjects instead of integers.
 */
public class ObjectsMinHeap {
	
	//ObjectsMinHeap properties
	int size;
	MinHeapObject[] array;
	final int maxArraySize =100;
	
	//the constructor for ObjectsMinHeap object
	//complexity: O(1)
	public ObjectsMinHeap() {
		this.size = 0;
		this.array = new MinHeapObject[maxArraySize];
	}//end of constructor
	
	/*
	 * instance insert new object to the heap.
	 * complexity: O(log(n))
	 */
	public void insert(MinHeapObject obj) {
		//stop the heap from growing more than allowed
		if(this.size < maxArraySize) {
			//insert the new object to the bootom of the heap
			this.size++;
			this.array[size-1] = obj;
			//heapify it from bottom up to his right spot
			this.bottomUpHeapify(size-1);//(O(log(n))
		}
	}//end of method insert
	
	/*
	 * instance recursive method that get an index of element in the heap and slides him up
	 * in the heap to his right place in legal heap.
	 * complexity: O(log(n))
	 */
	public void bottomUpHeapify(int index) {
		//cant slide up the head of the heap, stopping condition for the recursive method
		if(index > 0) {
			//parent of the current index
			int parent = (index-1) / 2;
			//if the index is smaller than his parent, we need to switch them
			if(this.array[parent].value > this.array[index].value) {
				//swap them
				this.swap(index, parent);
				//recursive call for the next index
				this.bottomUpHeapify(parent);
			}
		}//end of stopping condition
	}//end of method bottomUpHeapify
	
	/*
	 * an instance recursive heapify method adjusted to invoked by ObjectsMinHeap object.
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
		if(this.size == 0 || this.isLeaf(index) || (this.array[index].value <= this.left(index) && this.array[index].value <= this.right(index))) {
			return;
		}else {
			//check which of his children is the smallest
			if(this.left(index) > this.right(index)) {
				 //if the right child is smaller, swap them and keep heapify
				this.swap(index, (index * 2) + 2);
				this.heapify((index * 2) + 2);
			}else {
				//if the left child is smaller, swap them and keep heapify
				this.swap(index, (index * 2) + 1);
				this.heapify((index * 2) + 1);
			}
		}
	}//end of method heapify
	
	/*
	 * an instance method invoked by ObjectsMinHeap object.
	 * the method gets an index, return true if its a leaf and heap, false if not.
	 * complexity: O(1)
	 */
	public boolean isLeaf(int index) {
		if(index >= (this.size / 2) && index < this.size) {
			return true;
		}else {
			return false;
		}
	}//end of method isLeaf
	
	/*
	 * an instance method invoked by ObjectsMinHeap object.
	 * the method gets an index and return the value inside his left child
	 * complexity: O(1)
	 */
	public int left(int index) {
		return (this.array[(index * 2) + 1].value);
	}//end of method left
	
	/*
	 * an instance method invoked by ObjectsMinHeap object.
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
		if((index *2)+2<this.size) {
			return (this.array[(index * 2) + 2].value);
		}else {
			return Integer.MAX_VALUE;
		}
	}//end of method right
	
	/*
	 * an instance method invoked by ObjectsMinHeap object.
	 * the method gets 2 elements indexes and switch there places.
	 * complexity: O(1)
	 */
	public void swap(int fIndex,int sIndex) {
		MinHeapObject key = this.array[fIndex];
		this.array[fIndex] = this.array[sIndex];
		this.array[sIndex] = key;
	}//end of method swap
	
	/*
	 * an instance method invoked by ObjectsMinHeap object.
	 * the method gets an element index, it put the element at the end of the array
	 * reduce the size of the heap by 1.
	 * all the other methods works with the size property and not with array.length,
	 * so the element will stay there but there will be no reference to him.
	 * heapify the [index] element to his right place.
	 * complexity: O(log(n))
	 */
	public void delete(int index) {
		this.swap(index, this.size - 1);
		this.size--;
		this.heapify(index);
	}//end of method delete
}//end of class ObjectsMinHeap
