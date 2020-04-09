package sxk180037;
/**
 * @author SRI CHAITRA KAREDDY
 */
//to traverse the list in both directions.
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DoublyLinkedList<T> extends SinglyLinkedList<T>
{

    static class Entry<E> extends SinglyLinkedList.Entry<E>
    {
        Entry<E> prev;
        Entry(E x, Entry<E> next, Entry<E> prev)
        {
            super(x, next);
            this.prev = prev;
        }
    }


    public DoublyLinkedList() //constructor
    {
        head=new Entry<>(null,null,null);
        tail=head;
        size=0;
    }

    public ListIterator<T> iterator() { return new DLLIterator(); }

    protected class DLLIterator extends SLLIterator implements ListIterator<T>  // Iterator
    {
        DLLIterator()
        {
            cursor = (Entry<T>) head;
            ready = false;
        }



        public boolean hasPrevious() {
            Entry<T> node = (Entry<T>) cursor;
            return node.prev != null;
        }

        public T previous() {
            Entry<T> node = (Entry<T>) cursor;
            node=node.prev;
            cursor=node;				//pointing the cursor one step backwards.
            return node.element;
        }

        public void add(T x) {
            Entry<T> newnode=new Entry<>(x,null,null);
            Entry<T> currentnode=(Entry<T>) cursor;
            newnode.next=currentnode.next;
            newnode.prev=currentnode;
            if(currentnode.next!=null) {
                ((Entry<T>)currentnode.next).prev = newnode;
            }
            currentnode.next=newnode;
            cursor=newnode;
        }

        public void remove(){
            Entry<T> cursor1=(Entry<T>) cursor;
            cursor1.prev.next=cursor1.next;
            ((Entry<T>)cursor1.next).prev=cursor1.prev;
            cursor1=cursor1.prev;
            cursor=cursor1;
        }
        //overriding the abstract methods.
        public int nextIndex() {
            // TODO Auto-generated method stub
            return 0;
        }

        public int previousIndex() {
            // TODO Auto-generated method stub
            return 0;
        }

        public void set(T e) {
            // TODO Auto-generated method stub

        }
    }//end of DLLIterator class

    public void add(T x) {
        add(new Entry<>(x, null,null));
    }

    //adding at any point of the linked list.

    public void add(Entry<T> newnode) {
        tail.next = newnode;
        newnode.prev=(Entry<T>) tail;
        tail = tail.next;
        size++;
    }


    public static void main(String[] args) throws NoSuchElementException {
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);

        }

        DoublyLinkedList<Integer> dlst=new  DoublyLinkedList<>();
        for(int i=1; i<=n; i++) {
            dlst.add(Integer.valueOf(i));
        }
        dlst.printList();
        ListIterator<Integer> iter = (ListIterator<Integer>) dlst.iterator();
        Scanner in = new Scanner(System.in);
        System.out.println("1- Increment the cursor by one step.");
        System.out.println("2- Add an element after the current position.");
        System.out.println("3- Move the cursor to the previous position.");
        System.out.println("4- Remove an element pointing to the cursor");
        whileloop:
        while(iter.hasNext()) {

            int com = in.nextInt();

            switch(com) {
                case 1:  // Moving a step to the next element.
                    if (iter.hasNext()) {
                        System.out.println("Cursor is at element "+iter.next());
                        break;
                    } else {
                        System.out.println("No element found next.");
                        break;
                    }

                case 2:  // adding an  element
                    System.out.println("Enter an element to insert:");
                    int x=in.nextInt();
                    iter.add(x);
                    System.out.println("Element inserted.");
                    dlst.printList();
                    break;

                case 3:
                    if(iter.hasPrevious()) {
                        System.out.println("Cursor changed to previous. "+iter.previous());
                        break;
                    }
                    else {
                        System.out.println("There is no previous node.");
                        break;
                    }

                case 4:
                    iter.remove();
                    dlst.printList();

            }

        }


        dlst.printList();

    }//end of the main method.
}//end of the DLL CLASS

