/** @author
 *  Binary search tree 
 **/
package sxk180037;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

//class BinarySearchTree begins
public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root, temp;
    int size;
    Stack<Entry<T>> parentsStack = new Stack<>();

    public BinarySearchTree() {
        root = null;
        size = 0;
    }


    /**
     * Method: find(x)
     * Description: Used as a helper method to find x or the parent of x if x isnt found
     * @param x - the elment to search for
     * @return the parent of x
     */
    public Entry<T> find(T x) {
        parentsStack.push(null);
        return find(root, x);
    }

    /**
     * Method: find(node, x)
     * Description: Helper method used to traverse through the tree using a stack to keep track of the parents of x
     * @param tempNode - the node to start searching from
     * @param x - the value to search for in the tree
     * @return the parent node of x
     */
    public Entry<T> find(Entry<T> tempNode, T x) {
        if(tempNode == null || tempNode.element.equals(x))
            return tempNode;
        while(true) {
            if(x.compareTo(tempNode.element) < 0) {
                if(tempNode.left == null)
                    break;
                else {
                    parentsStack.push(tempNode);
                    tempNode = tempNode.left;
                }
            }
            else if(x.equals(tempNode.element))
                break;
            else if(tempNode.right == null )
                break;
            else {
                parentsStack.push(tempNode);
                tempNode = tempNode.right;
            }
        }
        return tempNode;
    }

    /**
     * Method: contains(x)
     * Description: this method takes in an input value x and returns true if x is present in the tree else false
     * @param x - the value to check whether exists or not
     * @return true if x is in tree else return false
     */
    public boolean contains(T x) {
        Entry<T> findAns = find(x);
        if(findAns == null || !findAns.element.equals(x))
            return false;
        else
            return true;
    }

    /**
     * Method: get(x)
     * Description: if x is present in tree, the value is returned else null
     * @param x - the value to get
     * @return the value x if present else null
     */
    public T get(T x) {
        if(contains(x))
            return x;
        else
            return null;
    }

    /**
     * Method: add(x)
     * Description: inserts the value x in the BST after finding the proper place to insert it. If tree contains a node with same key, replace element by x
     * @param x - the value to insert
     * @return true if the value was inserted , false otherwise
     */
    public boolean add(T x) {
        //when tree is empty the new value must be inserted and becomes the root node
        if(size == 0) {
            root = new Entry(x, null, null);
            size = 1;
            return true;
        }
        else {
            temp = find(x);
            //Case 1: when x is already present, just replace the value
            if(temp.element.equals(x)) {
                temp.element = x;
                return false;
            }
            //Case 2: when x is not present, insert it as a child
            else if (x.compareTo(temp.element) < 0)
                temp.left = new Entry<>(x, null, null);
            else temp.right = new Entry<>(x, null, null);
            size++;
        }
        return true;
    }

    /**
     * Method: remove(x)
     * Description: removes the node with value x from tree, after that calls the splice method to readjust the tree
     * @param x - the value to remove
     * @return the value that was removed
     */
    public T remove(T x) {
        //when tree is empty, we cannot remove element
        if(root == null)
            return null;
        temp = find(x);
        //if element is not present, we cannot remove it
        if(temp.element.compareTo(x) != 0)
            return null;
        //when the element to be removed has at most one subtree
        if(temp.left == null || temp.right == null)
            splice(temp);
        //when element to be removes has two children
        else {
            parentsStack.push(temp);
            Entry<T> minRight = find(temp.right, x);
            temp.element = minRight.element;
            splice(minRight);
        }
        size--;
        return x;
    }

    /**
     * Method: splice(node)
     * Description: readjusts the tree from the node that was given
     * Precondition:
     * 1. node has at-most one child
     * 2. Stack ancestors has path from root to parent of t
     * @param tempEntry - the node to readjust the tree from
     */
    private void splice(Entry<T> tempEntry) {
        Entry<T> parent = parentsStack.peek();
        Entry<T> child = tempEntry.left!=null ? tempEntry.left : tempEntry.right;
        if(parent == null)
            root = child;
        else if(parent.left == tempEntry)
            parent.left = child;
        else
            parent.right = child;
    }

    /**
     * Method: min()
     * Description: returns the minimum value that was stored in the tree
     * @return minimum value in that tree
     */
    public T min() {
        if(size == 0)
            return null;
        Entry<T> minEntry = root;
        //minimum value in a BST is the left most child of the tree, hence traverse through the left subtree and return element
        while(minEntry.left != null)
            minEntry = minEntry.left;
        return minEntry.element;
    }

    /**
     * Method: max()
     * Description: returns the maximum value the was stored in the tree
     * @return maximum value in that tree
     */
    public T max() {
        if(size == 0)
            return null;
        Entry<T> maxEntry = root;
        //maximum value in a BST is the right most child of the tree, hence traverse through the right subtree and return element
        while(maxEntry.right != null)
            maxEntry = maxEntry.right;
        return maxEntry.element;
    }

    // TODO: Create an array with the elements using in-order traversal of tree
    /**
     * Method: toArray()
     * Description: creates an returns an array which stores the nodes of the tree in the inorder fashion
     * @return the array that has the nodes stored in inorder fashion
     */
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        if(root == null)
            return null;
        Stack<Entry<T>> inOrderStack = new Stack<>();
        Entry<T> tempEntry = root;
        int i = 0;
        while(tempEntry != null || inOrderStack.size() > 0) {
            while(tempEntry != null) {
                inOrderStack.push(tempEntry);
                tempEntry = tempEntry.left;
            }
            tempEntry = inOrderStack.pop();
            arr[i] = tempEntry.element;
            i++;
            tempEntry = tempEntry.right;
        }
        return arr;
    }


// Start of Optional problem 2

    /** Optional problem 2: Iterate elements in sorted order of keys
     Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
        return null;
    }

    // Optional problem 2.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem 2.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem 2.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem 2.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

// End of Optional problem 2

    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }


    public void printTree() {
        //System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
        if(node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
}

