/**
SP10:Implementation of Tries.
author:Sri Chaitra Kareddy.
*/
package sxk180037;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Trie<V> {

    int value; //word number
    private Map<String,Number> hmap=new HashMap<>();
    //Entry class begins here.
    private class Entry {
        //data structure to store child nodes.
        private HashMap<Character, Entry> childNodesMap;
        private boolean isWord;

         Entry(){
            childNodesMap = new HashMap<>();
        }

        public void setIsWord(boolean flag){
            this.isWord = flag;
        }

        public boolean getIsWord(){
            return this.isWord;
        }

        public void setChildNode(Entry node, char ch){
            childNodesMap.put(ch, new Entry());
        }

        public Entry getChildNode(char ch){
            return childNodesMap.get(ch);
        }
        //Entry class ends here.
    }

    private Entry root;
    private int size;
    private boolean isWord;


    public Trie() {
        root = new Entry();
        size = 0;
    }

    /**
     * method:insert(x):
     *  Description:Inserts a word into the trie. */
    public void insert(String word) {
        Entry tempRoot = root;
        for(int i=0; i<word.length(); i++){
            char ch = word.charAt(i);
            Entry childNode = tempRoot.getChildNode(ch);
            if(childNode == null){
                tempRoot.setChildNode(new Entry(), ch);
            }
            tempRoot = tempRoot.getChildNode(ch);
        }

        tempRoot.setIsWord(true);
    }
    /**
     * method: Entry getLastNodeOfSearch(String word, int i, Entry currNode)
     *  Description:Keeps traversing thrrough each node to get the last node.
     *  */
    public Entry getLastNodeOfSearch(String word, int i, Entry currNode){
        if(i == word.length() || currNode==null){
            return currNode;
        }

        char ch = word.charAt(i);
        return getLastNodeOfSearch(word, i+1, currNode.getChildNode(ch));
    }
    /**
     * method:search(x):
     *  Description:Returns if the word is in the trie.
     *  */
    public boolean search(String word) {
        Entry lastNodeOfSearch = getLastNodeOfSearch(word, 0, root);
        return lastNodeOfSearch!=null && lastNodeOfSearch.getIsWord();
    }
    /**
     * method:search(x):
     *  Description:Returns if there is any word in the trie that starts with the given prefix.
     *  */
    public boolean startsWith(String prefix) {
        Entry lastNodeOfSearch = getLastNodeOfSearch(prefix, 0, root);
        return lastNodeOfSearch!=null;
    }

    /**private V put(Iterator<Character> iter, V value) {
        return null;
    }

    private V get(Iterator<Character> iter) {
        return null;
    }
     **/


    // public methods

    /**
     * Method: put(x)
     * Description: calls insert(x) for word insertion
     * @param s
     * @param value
     * @return word number after insertion.
     */
    public int put(String s, int value) {
        insert(s);
        hmap.put(s,value);
        System.out.println("Word Inserted at :"+ value);
        return value;
    }

    /**
     * Method:get(s): makes a call for search(x)
     * Description: checks if a word is present in the trie.
     * @param s
     * @return word number if the word is present in the trie
     */

    public int get(String s) {
        boolean val=search(s);
        if(val==true){
             int a=(int) hmap.get(s);
             System.out.println("Found");
             return a;
        }
        else {
            return -1;
        }

    }
    /** remove a string*/

    private int remove(Iterator<Character> iter) {
        return remove(iter,root);
    }

    public int remove(String s) {
        StringIterator iter = new StringIterator(s);
        return remove(iter);
       
    }
    /**
     * Method: remove
     * @param iter, iterator that visit every element of given input
     * @param cur, current node that is being visited
     * @return null if get given iter does not exists ,else value if iter is present and get removed
     */
    private int remove(Iterator<Character> iter, Entry cur) {
        if(!iter.hasNext()) {
             int val = value;
            if(val == 0) return 0;
            value = 0;
            size--;
            return cur.childNodesMap.size() == 0 ? val : 0;
        }

        char ch = iter.next();
        Entry node = cur.childNodesMap.get(ch);
        if(node == null) {
            return 0;
        }
        boolean removeEntry;
        int val = remove(iter, node);
         if(value==0)
              removeEntry = false;
        else
            removeEntry=true;
        if(removeEntry) {
            cur.childNodesMap.remove(ch);
        }
        return cur.childNodesMap.size() == 0 && value == 0 ? val : null;
    }

    // How many words in the dictionary start with this prefix?
    public int prefixCount(String s) {

        StringIterator iter = new StringIterator(s);
        Entry start = prefixCount(iter, root);
        if(start==null) // If the prefix is not in the trie, return 0.
            return 0;

        return dfs(start);
    }
    /**
     * @param cur, current node that is being visited
     * @return number of words that are stored in a tree whose root is cur
     */
    private int dfs(Entry cur) {
        if(cur.childNodesMap.size() == 0) { //leaf nodes form a root
            return value == 0 ? 0 : 1;
        }

        int res = 0;
        for(Entry entry : cur.childNodesMap.values()) {
            res += dfs(entry);
        }

        // mid level nodes in the tree form a word
        if(value!=0) {
            res +=1;
        }

        return res;
    }

    /**
     * @param iter, iterator that visit every element of given input
     * @param cur, current node that is being visited
     * @return null if get given prefix does not exists ,else entry that match the prefix
     */
    private Entry prefixCount(Iterator<Character> iter, Entry cur) {
        if(!iter.hasNext()) {
            return cur;
        }

        char ch = iter.next();
        Entry node = cur.childNodesMap.get(ch);
        if(node == null) {
            return null;
        }
        return prefixCount(iter, node);
    }
    
    public int size() {
        return size;
    }

    public static class StringIterator implements Iterator<Character> {
        char[] arr;  int index;
        public StringIterator(String s) { arr = s.toCharArray(); index = 0; }
        public boolean hasNext() { return index < arr.length; }
        public Character next() { return arr[index++]; }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    //main begins here.
    public static void main(String[] args) {
        Trie<Integer> trie = new Trie<>();
        int wordno = 0;
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            String s = in.next();
            if(s.equals("End")) { break; }
            wordno++;
            trie.put(s, wordno);
        }

        while(in.hasNext()) {
            String s = in.next();
            Integer val = trie.get(s);
            System.out.println(s + "\t" + val);
        }
    }
    //main ends here.
} //Trie class ends