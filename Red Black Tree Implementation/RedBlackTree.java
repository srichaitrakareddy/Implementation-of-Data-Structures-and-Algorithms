
/** Starter code for Red-Black Tree
 */

/**
 * SP 07: Red Black Trees as an extension of Binary Search Tree
 * @author Sri Chaitra Kareddy
 * @author Meet Shah
 *
 */
package sxk180037;

import java.util.*;

//RedBlackTree class begins
public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        boolean isRed() {
            return color == RED;
        }

        boolean isBlack() {
            return color == BLACK;
        }
    }

    //HashMap to store the parents of nodes
    private Map<Entry<T>,Entry<T>> parentsMap;
    private Entry<T> root;
    int size;
    private Stack<Entry<T>> stack;

    RedBlackTree() {
        super();
        parentsMap = new HashMap<>();
        root = null;
        stack = new Stack<>();
        size = 0;
    }

    /**
     * find(x)
     * Description: Used as a helper method to find x or the parent of x if x isnt found
     * @param x - the element to search for
     * @return the parent of x
     */
    public Entry<T> find(T x) {
        stack.push(null);
        return (Entry<T>) find(root, x);
    }

    /**
     * find(node, x)
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
                    stack.push(tempNode);
                    tempNode = (Entry<T>) tempNode.left;
                }
            }
            else if(x.equals(tempNode.element))
                break;
            else if(tempNode.right == null )
                break;
            else {
                stack.push(tempNode);
                tempNode = (Entry<T>) tempNode.right;
            }
        }
        return tempNode;
    }

    /**
     * contains(x)
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
     * get(x)
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
     * add(x)
     * Description: method is called to insert a value (x) into the tree, After a node is added the tree is then rotated so that te colors of the red black tree satisfy the conditions
     * @param x - the value to insert
     * @return boolean value true if the node was inserted else false
     */
    public boolean add(T x) {

        if(root == null) {
            root = new Entry<>(x, null, null);
            root.color = BLACK;
            parentsMap.put(root, null);
            size = 1;
        }
        else {
            Entry<T> parent = find(x);
            //Case 1: when x is already present, just replace the value
            if(parent.element.equals(x)) {
                parent.element = x;
                return false;
            }
            //Case 2: when x is not present, insert it as a child
            else {
                Entry<T> newEntry = new Entry<>(x, null, null);
                parentsMap.put(newEntry, parent);
                if (x.compareTo(parent.element) < 0)
                    parent.left = newEntry;
                else
                    parent.right = newEntry;
                newEntry.color = RED;
                while(newEntry!=root && parent.color==RED) {
                    if(parentsMap.get(parent).left == parent) {
                        Entry<T> uncle = (Entry<T>) parentsMap.get(parent).right;
                        if(uncle!=null && uncle.color == RED) {
                            uncle.color = BLACK;
                            parent.color = BLACK;
                            parentsMap.get(parent).color = RED;
                            newEntry = parentsMap.get(parent);
                        }
                        else if(parent.right == newEntry) {
                            Entry<T> temp = parent;
                            rotateLeft(parent);
                            newEntry = temp;
                        }
                        else {
                            parent.color = BLACK;
                            parentsMap.get(parent).color = RED;
                            rotateRight(parent);
                        }
                    }
                    else {
                        Entry<T> uncle = (Entry<T>) parentsMap.get(parent).left;
                        if(uncle!=null && uncle.color == RED) {
                            uncle.color = BLACK;
                            parent.color = BLACK;
                            parentsMap.get(parent).color = RED;
                            newEntry = parentsMap.get(parent);
                        }
                        else if(parent.left == newEntry) {
                            Entry<T> temp = parent;
                            rotateRight(parent);
                            newEntry = temp;
                        }
                        else {
                            parent.color = BLACK;
                            parentsMap.get(parent).color = RED;
                            rotateLeft(parent);
                        }
                    }
                }
                root.color = BLACK;
            }
        }
        size++;
        return true;
    }

    /**
     * rotateLeft(x)
     * Description: This method rotates the tree towards the left at the node given
     * @param x, the node at which to rotate
     */
    private void rotateLeft(Entry<T> x) {
        if(parentsMap.get(x)!=null) {
            if(x == parentsMap.get(x).left)
                parentsMap.get(x).left = x.right;
            else
                parentsMap.get(x).right = x.right;
            parentsMap.put((Entry<T>) x.right, parentsMap.get(x));
            parentsMap.put(x, (Entry<T>) x.right);
            if(x.right.left!=null)
                parentsMap.put((Entry<T>) x.right.left,x);
            x.right = x.right.left;
            parentsMap.get(x).left = x;
        }
        else {
            Entry<T> rightNode = (Entry<T>) root.right;
            root.right = rightNode.left;
            parentsMap.put((Entry<T>) rightNode.left, root);
            parentsMap.put(root, rightNode);
            rightNode.left = root;
            root = rightNode;
        }
    }

    /**
     * rotateRight(x)
     * Description: This method rotates the tree towards the right at the node given
     * @param x, the node at which to rotate
     */
    private void rotateRight(Entry<T> x) {
        if(parentsMap.get(x)!=null) {
            if(x == parentsMap.get(x).left)
                parentsMap.get(x).left = x.left;
            else
                parentsMap.get(x).right = x.left;
            parentsMap.put((Entry<T>) x.left, parentsMap.get(x));
            parentsMap.put(x, (Entry<T>) x.left);
            if(x.left.right!=null)
                parentsMap.put((Entry<T>) x.left.right, x);
            x.left = x.left.right;
            parentsMap.get(x).right=x;
        }
        else {
            Entry<T> leftNode = (Entry<T>) root.left;
            root.left = root.left.right;
            parentsMap.put((Entry<T>) leftNode.right, root);
            parentsMap.put(root, leftNode);
            leftNode.right = root;
            root = leftNode;
        }
    }

    /**
     * transplant(destination, source)
     * Description: This method transplants the source node into the destination node, does the same thing that splice method does for a BST
     * @param target, the node to transplant values into
     * @param source, the node which the values are to be copied from
     */
    private void transplant(Entry<T> target, Entry<T> source) {
        if(parentsMap.get(target)==null)
            root = source;
        else if(target == parentsMap.get(target).left)
            parentsMap.get(target).left = source;
        else
            parentsMap.get(target).right = source;
        parentsMap.put(source, parentsMap.get(target));
    }

    /**
     * remove(x)
     * Description: removes the node with value x from tree, after that calls the splice method to readjust the tree
     * @param x - the value to remove
     * @return the value that was removed
     */
    public T remove(T x) {
        //if theres no node to delete
        if(root == null)
            return null;
        Entry<T> node = find(x);
        if(node == root && node.left ==null && node.right ==null){
            root = null;
            size =0;
            return x;
        }
        //if the node to delete is not present
        if(node.element!=x)
            return null;
        Entry<T> xref, y=node;
        boolean ori_color = y.color;
        //if right child exists for the node to be deleted
        if(node.left==null ) {
            xref= (Entry<T>) node.right;
            transplant(node, (Entry<T>) node.right);
        }
        // if only left child exists for the node to be deleted
        else if(node.right==null) {
            xref = (Entry<T>) node.left;
            transplant(node, (Entry<T>) node.left);
        }
        //if the node to be deleted has both the subtrees
        else{
            y = (Entry<T>) find(node.right, x);
            ori_color = y.color;
            xref = (Entry<T>) y.right;
            if(parentsMap.get(y)==node)
                parentsMap.put(xref, y);
            else {
                transplant(y, (Entry<T>) y.right);
                y.right = node.right;
                parentsMap.put((Entry<T>) y.right,y);
            }
            transplant(node, y);
            y.left = node.left;
            parentsMap.put((Entry<T>) y.left, y);
            y.color = node.color;
        }
        if(ori_color==BLACK)
            fixup(xref);
        size--;
        return x;
    }

    /**
     *fixup(x)
     * Description: This method rebalances the colors of the red black trees after deletion
     * @param x, the node to start the rebalancing at
     */
    private void fixup(Entry<T> x) {
        Entry<T> wc1=null, wc2 =null;
        while(x!=root && x.color==BLACK) {
            // if the node is the left child
            if(parentsMap.get(x).left == x) {
                //get the sibling of the node
                Entry<T> w = (Entry<T>) parentsMap.get(x).right;
                if(w!=null) {
                    wc1 = (Entry<T>) w.left;
                    wc2 = (Entry<T>) w.right;
                }
                //if sibling has RED color, then shift the RED color towards the parent and rotate the tree
                if(w.color==RED) {
                    w.color=BLACK;
                    parentsMap.get(x).color=RED;
                    rotateLeft(parentsMap.get(x));
                    w = (Entry<T>) parentsMap.get(w).right;
                }
                //if both the siblings children have BLACK color, then change sibling's color to RED and continue going up the tree
                else if(wc1!=null && wc2!=null && wc1.color==BLACK && wc2.color ==BLACK) {
                    w.color= RED;
                    x = parentsMap.get(x);
                    continue;
                }
                //if the sibling's right child has BLACK color, then color the child BLACK, make sibling RED and rotate the tree
                else if(wc2!=null && wc2.color==BLACK) {
                    if(wc1!=null)
                        wc1.color = BLACK;
                    w.color= RED;
                    rotateRight(w);
                    w= (Entry<T>) parentsMap.get(x).right;
                }
                //if sibling's right child has RED color, then color the sibling as its parent, color parent BLACK and rotate
                if(wc2!=null && wc2.color==RED){
                    w.color = parentsMap.get(x).color;
                    parentsMap.get(x).color = BLACK;
                    wc2.color = BLACK;
                    rotateLeft(parentsMap.get(x));
                    x=root;
                }
            }
            //if the node is the right child follow a mirrorized method to the above
            else {
                Entry<T> w = (Entry<T>) parentsMap.get(x).left;
                if(w!=null) {
                    wc1 = (Entry<T>) w.left;
                    wc2 = (Entry<T>) w.right;
                }
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    parentsMap.get(x).color = RED;
                    rotateRight(parentsMap.get(x));
                    w = (Entry<T>) parentsMap.get(w).left;
                } else if (wc1 != null && wc2 != null && wc1.color == BLACK && wc2.color == BLACK) {
                    w.color = RED;
                    x = parentsMap.get(x);
                    continue;
                } else if (wc1 != null && wc1.color == BLACK) {
                    if (wc2 != null)
                        wc2.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = (Entry<T>) parentsMap.get(x).left;
                }
                if(wc1!=null && wc1.color == RED) {
                    w.color = parentsMap.get(x).color;
                    parentsMap.get(x).color = BLACK;
                    wc1.color = BLACK;
                    rotateRight(parentsMap.get(x));
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    /**
     * min()
     * Description: returns the minimum value that was stored in the tree
     * @return minimum value in that tree
     */
    public T min() {
        if(size == 0)
            return null;
        Entry<T> minEntry = root;
        //minimum value is the left most child of the tree, hence traverse through the left subtree and return element
        while(minEntry.left != null)
            minEntry = (Entry<T>) minEntry.left;
        return minEntry.element;
    }

    /**
     * max()
     * Description: returns the maximum value the was stored in the tree
     * @return maximum value in that tree
     */
    public T max() {
        if(size == 0)
            return null;
        Entry<T> maxEntry = root;
        //maximum value is the right most child of the tree, hence traverse through the right subtree and return element
        while(maxEntry.right != null)
            maxEntry = (Entry<T>) maxEntry.right;
        return maxEntry.element;
    }

    /**
     * toArray()
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
                tempEntry = (Entry<T>) tempEntry.left;
            }
            tempEntry = inOrderStack.pop();
            arr[i] = tempEntry.element;
            i++;
            tempEntry = (Entry<T>) tempEntry.right;
        }
        return arr;
    }

    /**
     * printRBTree()
     * Description: calls the print function starting at the root
     */
    public void printRBTree() {
        if(root==null) {
            System.out.println("There are no elements in the Red Black Tree");
            return;
        }
        printRBTree(root);
        System.out.println();
    }

    /**
     * printRBTree(node)
     * Description: prints the inorder traversal of the tree starting at the node
     * @param node, the node at which to start the inorder traversal
     */
    private void printRBTree(Entry<T> node) {
        if(node != null) {
            printRBTree((Entry<T>) node.left);
            System.out.print(" " + node.element+" (");
            if(node.color ==  RED)
                System.out.print("RED)");
            else
                System.out.print("BLACK)");
            if(parentsMap.get(node)!=null)
                System.out.print(" (P: "+parentsMap.get(node).element+")");
            else
                System.out.print(" (Root)");
            printRBTree((Entry<T>) node.right);
        }
        return;
    }

    //the main method
    public static void main(String[] args) {
        RedBlackTree<Integer> t = new RedBlackTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printRBTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printRBTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size-1; i++)
                    System.out.print(arr[i] + " ");
                System.out.println();
                return;
            }
        }
    }
    // main method ends
}
//RedBlackTree class ends

