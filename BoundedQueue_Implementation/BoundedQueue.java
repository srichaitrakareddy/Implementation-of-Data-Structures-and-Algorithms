package sxk180037;
/**
 * @author SRI CHAITRA KAREDDY
 */

import java.util.Scanner;

public class BoundedQueue<T> {

    private final Integer capacity;
    private T[] queueArray;
    private Integer head, tail, size;


    public BoundedQueue(int size) {
        queueArray = (T[]) new Object[size];
        capacity = size;
        clear();
    }

    //add the element to the queue.

    public boolean offer(T element) {

        if (size() == capacity)
            return false;
        else {
            queueArray[tail] = element;
            tail = (tail + 1) % capacity;
            size++;
            return true;
        }
    }

    //removing the front element

    public T poll() {
        T value = peek();
        if (value != null) {
            head = (head + 1) % capacity;
            size--;
        }

        return value;
    }

    //returning the front element

    public T peek() {
        if (isEmpty())
            return null;
        else
            return queueArray[head];
    }

    //return the size of the queue.

    public int size() {

        return size;
    }

    //check if the queue is empty

    public boolean isEmpty() {
        if (size() == 0)
            return true;
        else
            return false;
    }

    // clear the queue

    public void clear() {
        head = 0;
        tail = 0;
        size = 0;
    }

    //printing the queue.

    public void printList() {
        System.out.print(this.size() + ": ");

        for (int i = head, count=0; count < size() ; i = (i+1)%capacity) {
            System.out.print(queueArray[i] + " ");
            count++;
        }
        System.out.println();
    }

    //adding all the elements of the queue to a new array.
    public void toArray(T[] arr) {
        if(arr.length < size()) {
            System.out.println("array size is less than the queue size which is: " + size());
            return;
        }

        for (int i = head, count=0; count < size() ; i = (i+1)%capacity) {
            arr[count] = queueArray[i];
            count++;
        }
    }

    private void printArray(T[] new_Array) {
        for(int i=0; i<new_Array.length; i++) {
            System.out.print(new_Array[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n=0;
        System.out.println("Enter the required capacity:");
        if(in.hasNext()){
            n = in.nextInt();
        }

        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        BoundedQueue<Integer> q = new BoundedQueue<>(n);
        for (int i = 1; i <= n; i++) {
            q.offer(Integer.valueOf(i));
        }
        q.printList();



        whileloop: while (in.hasNext()) {
            System.out.println("1-Offer an element to the Queue");
            System.out.println("2-Removing an element at the front of the queue(poll)");
            System.out.println("3-Return the front element(peek)");
            System.out.println("4-Return the size of the queue.");
            System.out.println("5-Clear the Queue.");
            System.out.println("5-Filling user supplied array with the queue elements.");
            int option = in.nextInt();
            switch (option) {
                case 1: // add an element (offer)
                    if (q.offer(in.nextInt())) {

                    } else {
                        System.out.println("Cannot add, limit exceeded.");
                    }
                    break;
                case 2: // Remove element (pop)
                    if (q.peek()!=null) {
                        System.out.println(q.poll());
                    } else {
                        System.out.println("Queue is empty");
                    }
                    break;
                case 3: // front element (peek)
                    if (q.peek()!=null) {
                        System.out.println(q.peek());
                    } else {
                        System.out.println("Queue is empty");
                    }
                    break;
                case 4: // size of the queue
                    System.out.println(q.size());
                    break;
                case 5: // clear the queue
                    q.clear();
                    System.out.println("Queue is cleared.");
                    break;
                case 6: // add elements to a new array
                    Integer[] new_Array = new Integer[5];
                    q.toArray(new_Array);
                    System.out.println("The array is " );
                    q.printArray(new_Array);
                    break;

                default: // exiting the loop
                    System.out.println("Exit");
                    break whileloop;
            }
            q.printList();
        }

    }

}
