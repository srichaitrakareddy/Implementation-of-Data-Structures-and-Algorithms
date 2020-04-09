// code for SP5 (Binary Heap)
/**
 * SP 5: Binary Heap
 * @author Sri Chaitra Kareddy
 */
package  sxk180037;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

//BinaryHeap Class Begins
public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size; //number of elements

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
	pq = new Comparable[maxCapacity];
	size = 0;
    }

    // add method: resize pq if needed
    public boolean add(T x) {
	    if(size==pq.length)
	        return false;
	    else {
	        //add the element
	        pq[size]=x;
	        //check if the heap is maintained
	        percolateUp(size);
	        //increment the size
	        size++;
	        return true;

        }


    }

    //calls the add method.
    public boolean offer(T x) {
	return add(x);
    }
    // throw exception if pq is empty otherwise calls the poll method
    public T remove() throws NoSuchElementException {
	T result = poll();
	if(result == null) {
	    throw new NoSuchElementException("Priority queue is empty");
	} else {
	    return result;
	}
    }

    // return null if pq is empty
    public T poll() {
        if(size==0)
            return null;
        else {
            T temp= (T) pq[0];
            pq[0]=pq[size-1];
            size--;
            percolateDown(0);
            return temp;


        }
    }
    
    public T min() { 
	return peek();
    }

    // return null if pq is empty
    public T peek() {
	  if (size==0)
	      return  null;
	  else return (T) pq[0];
    }

    int parent(int i) {
	return (i-1)/2;
    }

    int leftChild(int i) {
	return 2*i + 1;
    }

    /** pq[index] may violate heap order with parent */
    void percolateUp(int index) {
        T x = (T) pq[index];
        while(index>0 && (pq[parent(index)].compareTo(x)==1)) {
            pq[index] = pq[parent(index)];
            index = parent(index);
            pq[index]=x;
        }

    }

    /** pq[index] may violate heap order with children */
    void percolateDown(int index) {
        T x = (T) pq[index];
        int c = (2*index)+1;
        while(c<=size-1) {
            if((c<size-1) && (pq[c].compareTo(pq[c+1])==1))
                c++;
            if(pq[c].compareTo(x)>0)
                break;
            pq[index] = pq[c];
            index = c;
            c = (2*index)+1;
            pq[index]=x;
        }
    }


    int compare(Comparable a, Comparable b) {
	return ((T) a).compareTo((T) b);
    }
    
    /** Create a heap.  Precondition: none. */
    void buildHeap() {
	for(int i=parent(size-1); i>=0; i--) {
	    percolateDown(i);
	}
    }

    public boolean isEmpty() {
	return size() == 0;
    }

    public int size() {
	return size;
    }

    // Resize array to double the current size
    void resize() {
    }
    



    public static void main(String[] args) {
	Integer[] arr = {0,9,7,5,3,1,8,6,4,2};
	BinaryHeap<Integer> h = new BinaryHeap(arr.length);

	System.out.print("Before:");
	for(Integer x: arr) {
	    h.offer(x);
	    System.out.print(" " + x);
	}
	System.out.println();

	for(int i=0; i<arr.length; i++) {
	    arr[i] = h.poll();
	}

	System.out.print("After :");
	for(Integer x: arr) {
	    System.out.print(" " + x);
	}
	System.out.println();
    }
}
