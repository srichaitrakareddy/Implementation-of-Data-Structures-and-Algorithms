/**
 * Short Project: Hashing
 * Implemented Double Hashing and Cuckoo Hashing and analyzed the performance against HashSet
 * @author Sri Chaitra Kareddy
 */

package sxk180037;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * DoubleHashing
 * implements the methods for DoubleHashing
 */
class DoubleHashing<T> {
    //Entry class used to store the element and the deleted flag
    private class Entry<T> {
        T element;
        boolean isDeleted;
        //Entry class constructor
        public Entry(T element) {
            this.element = element;
            this.isDeleted = false;
        }
    }
    private int size;
    private Entry<T>[] hashTable;
    private Double loadFactor;
    //DoubleHashing constructor takes in the initial size for the hashtable and the loadfactor
    DoubleHashing(int tableSize, double lf) {
        this.size = 0;
        this.hashTable = new Entry[tableSize];
        this.loadFactor = lf;
    }

    /**
     * isPrime(x)
     * checks whether the number is prime or not and returns the answer as a boolean value
     * @param x, the number to check
     * @return true if x is prime else false
     */
    private boolean isPrime(int x) {
        for(int i = 2;i<x;i++) {
            if(x%i == 0)
                return false;
        }
        return true;
    }

    /**
     * getPrime(s)
     * returns the nearest prime number  less than s
     * @param s , the bound for the prime number
     * @return the nearest prime number less than s
     */
    private int getPrime(int s) {
        for(int i = s; i>0;i--) {
            if(isPrime(i))
                return i;
        }
        return -1;
    }

    /**
     * Method: hashCode2(x)
     * Description: The second hash function method that returns a secondary hashvalue
     * @param x , the value to be hashed
     * @return secondary hashcode for x
     */
    private int hashCode2(T x) {
        int prime = getPrime(hashTable.length);
        return (prime-(x.hashCode()%prime));
    }

    /**
     * hashCode1(x)
     * Manipulates the hashcode of x to get the primary hashcode for x
     * @param x, the value to be hashed
     * @return the primary hashcode for x
     */
    private int hashCode1(T x) {
        int h = x.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * indexOf(code, length)
     *  Calculates the index value for the given hashcode and the size of the hashtable
     * @param code the hashcode of the value
     * @param length the length of the hashtable
     * @return the index of the the particular hashcode in the table of size length
     */
    private int indexOf(int code, int length) {
        return code & (length -1);
    }

    /**
     * rehash()
     * The existing table size is doubled and values are rehashed in the new table of bigger size
     */
    public void rehash()
    {
        size=0;
        int presentSize = hashTable.length;
        Entry<T>[] oldHashTable = hashTable;
        hashTable= new Entry[2* oldHashTable.length];
        for (int i = 0; i < presentSize; i++) {
            if(oldHashTable[i] != null && oldHashTable[i].isDeleted == false)
                add(oldHashTable[i].element);
        }
    }

    /**
     * find(x)
     * returns the index of the value x in the hashtable
     * @param x, the value to search for
     * @return the index of the value x in the hashtable
     */
    private int find(T x) {
        int k = 0;
        int index1 = indexOf(hashCode1(x), hashTable.length);
        int index2;
        while(true) {
            index2 = (index1 + k*hashCode2(x)) % hashTable.length;
            if(index2<0) {
                k++;
                continue;
            }
            if( hashTable[index2] == null || (hashTable[index2]).element.equals(x)) {
                return index2;
            }
            else if (hashTable[index2].isDeleted == true)
                break;
            else
                k++;
            if(k!=0 && k>hashTable.length)
                rehash();
        }
        int spot = index2;
        if(hashTable[index2].isDeleted==true)
                return spot;
        return -1;
    }

    /**
     * contains(x)
     * checks whether the element x exists in the hashtable or not
     * @param x, the value to search for
     * @return true if x is present in the hashtable, false if otherwise
     */
    public boolean contains(T x) {
        int location = find(x);
        if(hashTable[location]!=null && hashTable[location].element == x && hashTable[location].isDeleted==false)
            return true;
        return false;
    }

    /**
     * add(x)
     * adds the value x if its not present in the hashtable
     * @param x, the value to add
     * @return true if x was successfully added into the hashtable, false otherwise
     */
    public boolean add(T x) {
        int location = find(x);
        if(location==-1 || (hashTable[location]!= null && hashTable[location].isDeleted==false))
            return false;
        else {
            hashTable[location] = new Entry<>(x);
            if(hashTable[location].isDeleted==true) {
                hashTable[location].isDeleted=false;
                size++;
            }
            //calls rehash method if load factor was reached
            if(size>hashTable.length * loadFactor)
                rehash();
            return true;
        }
    }

    /**
     * remove(x)
     * removes the element x from the hashtable if it exists
     * @param x, the element to delete
     * @return x if found and deleted, null otherwise
     */
    public T remove(T x) {
        int location = find(x);
        if(hashTable[location]!=null && hashTable[location].element==x) {
            size--;
            T result = hashTable[location].element;
            hashTable[location].isDeleted=true;
            return result;
        }
        return null;
    }
}
//DoubleHashing class ends

/**
 * CuckooHashing
 * implements the methods for CuckooHashing
 */
class CuckooHashing<T> {
    enum Status {
        FILLED, DELETED;
    }
    //Entry class used to store element and its status
    class Entry<T> {
        T element;
        Status status;
        //Entry class constructor
        public Entry(T element) {
            this.element = element;
            this.status = Status.FILLED;
        }
    }

    int k;
    int tableSize;
    int size;
    Entry<T>[] hashTable;
    double loadFactor;
    int threshold;

    //Cuckoo Hashing constructor takes in the loadfactor
    CuckooHashing(double lf) {
        this.size = 0;
        this.k = 2;
        this.tableSize = 1024;
        this.hashTable = new Entry[tableSize];
        this.threshold = tableSize;
        this.loadFactor = lf;
    }

    /**
     * hash(h)
     * hashed the given value
     * @param h, the value to be hashed
     * @return the hashcode of h
     */
    static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * indexOf(h, length)
     * finds the index of the given code h in the hashtable of size length
     * @param h the hashcode to return the index of
     * @param length length of the hashtable
     * @return the index of the hashcode h in the table of size length
     */
    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * hashFunction(i, x)
     * returns the hashcode for x in either of the ways described by i
     * @param i the choise of hashfunction
     * @param x the value to hash
     * @return the hashvalue of x in the choice of hashing i
     */
    private int hashFunction(int i, T x) {
        switch(i) {
            case 1: return indexFor(hash(x.hashCode()), tableSize);
            default: return 1+x.hashCode()%9;
        }
    }

    /**
     * rehash()
     * doubles the size of the hashtable and rehashes the existing values into it
     */
    private void rehash() {
        Entry[] e = new Entry[size];
        for (int i = 0, j = 0; i < tableSize; i++) {
            if (hashTable[i] != null && hashTable[i].status == Status.FILLED) {
                e[j++] = hashTable[i];
                hashTable[i] = null;
            }
        }
        tableSize = 2 * tableSize;
        size = 0;
        hashTable = new Entry[tableSize];
        for (int i = 0; i < e.length; i++) {
            add((T) e[i].element);
        }
    }

    /**
     * contains(x)
     * tests whether element is present in the hashtable or not
     * @param x the element to search for
     * @return true if element exists in the hashtable, false otherwise
     */
    public boolean contains(T x) {
        for(int i = 1;i<=k;i++) {
            int loc = hashFunction(i, x);
            if(hashTable[loc] == null)
                return false;
            if(hashTable[loc].status == Status.DELETED)
                return false;
            if(hashTable[loc].element==x)
                return true;
        }
        return false;
    }
    /**
     * add(x)
     * adds a value into the hashtable if it doesn't already exist
     * @param x the value to add into the hashtable
     * @return true if the element was successfully added, false otherwise
     */
    public boolean add(T x) {
        //calls rehash if loadfactor is reached
        if(size/(double)tableSize > loadFactor)
            rehash();
        //returns false if x already exists
        if(contains(x))
            return false;
        for(int i = 1; i<=k ; i++) {
            if(hashTable[hashFunction(i, x)] == null) {
                hashTable[hashFunction(i, x)] = new Entry<>(x);
                size++;
                return true;
            }
            else if (hashTable[hashFunction(i,x)].status==Status.DELETED) {
                hashTable[hashFunction(i, x)] = new Entry(x);
                size++;
                return true;
            }
        }
        int i = 1, count = 0;
        while(count++ < threshold) {
            int loc = hashFunction(i, x);
            if(hashTable[loc]!=null) {
                if(hashTable[loc].status == Status.DELETED) {
                    hashTable[loc] = new Entry(x);
                    size++;
                    return true;
                }
                else {
                    T temp = hashTable[loc].element;
                    hashTable[loc].element = x;
                    x = temp;
                }
                i = i == k ? 1 : i + 1;
            }
        }
        rehash();
        return false;
    }

    /**
     * remove(x)
     * removes the element from the hashtable
     * @param x the element to remove
     * @return the element x if removal was successful, null otherwise
     */
    public T remove(T x) {
        if(contains(x)) {
            T e = null;
            for (int i = 1; i <= k; i++) {
                int loc = hashFunction(i, x);
                if (hashTable[loc] != null) {
                    if (hashTable[loc].element == x) {
                        e = (T) hashTable[loc].element;
                        hashTable[loc].status = Status.DELETED;
                        size--;
                        return e;
                    }
                }
            }
        }
        return null;
    }
}
//CuckooHashing class ends

/**
 * Hashing
 *creates objects for the DoubleHashing and CuckooHashing classes and analyses their performance against Java's HashSet
 */
public class Hashing {
    /**
     * Method: distinctElementsDH75(arr)
     * Description: attempts to add an array of randomized integers into the DoubleHashing hashtable with loadfactor 0.75 and counts the distinct elements
     * @param arr the randomized array of integers
     * @param <T>
     * @return the count of distinct elements in arr
     */
    static <T> int distinctElementsDH75(T arr[]) {
        int count = 0;
        DoubleHashing<T> dh = new DoubleHashing(arr.length, 0.75);
        for(int i = 0;i<arr.length;i++) {
            if(!dh.contains(arr[i])) {
                dh.add(arr[i]);
                count++;
            }
        }
        return count;
    }

    /**
     * distinctElementsDH5(arr)
     * attempts to add an array of randomized integers into the DoubleHashing hashtable with loadfactor 0.5 and counts the distinct elements
     * @param arr the randomized array of integers
     * @param <T>
     * @return the count of distinct elements in arr
     */
    static <T> int distinctElementsDH5(T arr[]) {
        int count = 0;
        DoubleHashing<T> dh = new DoubleHashing(arr.length, 0.5);
        for(int i = 0;i<arr.length;i++) {
            if(!dh.contains(arr[i])) {
                dh.add(arr[i]);
                count++;
            }
        }
        return count;
    }

    /**
     * distinctElementsCH5(arr)
     * attempts to add an array of randomized integers into the CuckooHashing hashtable with loadfactor 0.5 and counts the distinct elements
     * @param arr the randomized array of integers
     * @param <T>
     * @return the count of distinct elements in arr
     */
    static <T> int distinctElementsCH5(T arr[]) {
        int count = 0;
        CuckooHashing<T> ch = new CuckooHashing<>( 0.5);
        for(int i = 0;i<arr.length;i++) {
            if(!ch.contains(arr[i])) {
                ch.add(arr[i]);
                count++;
            }
        }
        return count;
    }

    /**
     * distinctElementsCH75(arr)
     * attempts to add an array of randomized integers into the CuckooHashing hashtable with loadfactor 0.75 and counts the distinct elements
     * @param arr the randomized array of integers
     * @param <T>
     * @return the count of distinct elements in arr
     */
    static <T> int distinctElementsCH75(T arr[]) {
        int count = 0;
        CuckooHashing<T> ch = new CuckooHashing<>( 0.75);
        for(int i = 0;i<arr.length;i++) {
            if(!ch.contains(arr[i])) {
                ch.add(arr[i]);
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("Select the number corresponding to the hashing method required:");
            System.out.println("1. Double Hashing");
            System.out.println("2. Cuckoo Hashing");
            //hardcoded value for the number of integers to be inserted
            int n1 = 1000000;
            //hardcoded value for number of intergers in randomized array to check distinct of
            int n2 = 100;
            int choice = input.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Double Hashing");
                    //creates objects of DoubleHashing with different load factors
                    DoubleHashing<Integer> dhlf5 = new DoubleHashing(n1, 0.5);
                    DoubleHashing<Integer> dhlf75 = new DoubleHashing(n1, 0.75);
                    Set<Integer> hashSet = new HashSet<>();
                    Timer dht5 = new Timer();
                    Timer dht75 = new Timer();
                    System.out.println("Addition of "+n1+" numbers to Double Hashing 0.5lf started...");
                    dht5.start();
                    for(int i=1 ; i<= n1;i++)
                        dhlf5.add(i);
                    dht5.end();
                    System.out.println("Addition of "+n1+" numbers to Double Hashing 0.5lf ended...");
                    System.out.println("Addition of "+n1+" numbers to Double Hashing 0.75lf started...");
                    dht75.start();
                    for(int i=1 ; i<= n1;i++)
                        dhlf75.add(i);
                    dht75.end();
                    System.out.println("Addition of "+n1+" numbers to Double Hashing 0.75lf ended...");
                    Timer hst = new Timer();
                    System.out.println("Addition of "+n1+" numbers to HashSet started...");
                    hst.start();
                    for( int i=1; i<=n1; i++)
                        hashSet.add(i);
                    hst.end();
                    System.out.println("Addition of "+n1+" numbers to HashSet ended...");
                    //generating the random array for checking distinct
                    Random randomNumberGenerator = new Random();
                    Integer[] arr = new Integer[n2];
                    for(int i = 0;i<n2;i++)
                        arr[i] = randomNumberGenerator.nextInt(100);
                    Timer dhdistinct5 = new Timer(), dhdistinct75=new Timer(), hsdistinct=new Timer();
                    System.out.println("Distinct checking in "+n2+" numbers in Double Hashing 0.5lf started...");
                    dhdistinct5.start();
                    int count = distinctElementsDH5(arr);
                    dhdistinct5.end();
                    System.out.println("Distinct checking in "+n2+" numbers in Double Hashing 0.5lf ended...");
                    System.out.println("Distinct checking in "+n2+" numbers in Double Hashing 0.75lf started...");
                    dhdistinct75.start();
                    count = distinctElementsDH75(arr);
                    dhdistinct75.end();
                    System.out.println("Distinct checking in "+n2+" numbers in Double Hashing 0.75lf started...");
                    hashSet = new HashSet<>();
                    System.out.println("Distinct checking in "+n2+" numbers in HashSet started...");
                    hsdistinct.start();
                    count = 0;
                    for(int i = 0;i<arr.length;i++) {
                        if(!hashSet.contains(arr[i])) {
                            hashSet.add(arr[i]);
                            count++;
                        }
                    }
                    hsdistinct.end();
                    System.out.println("Distinct checking in "+n2+" numbers in HashSet ended...");
                    System.out.println("******");
                    System.out.println("******");
                    System.out.println("PERFORMANCE REPORT ON ADDITION OF "+n1+" INTEGERS");
                    System.out.println("Double Hashing with load factor 0.5");
                    System.out.println(dht5);
                    System.out.println("Double Hashing with load factor 0.75");
                    System.out.println(dht75);
                    System.out.println("JavaHashSet");
                    System.out.println(hst);
                    System.out.println("******");
                    System.out.println("PERFORMANCE REPORT ON DISTINCT NUMBER COUNT ON "+n2+" NUMBERS");
                    System.out.println("Double Hashing with load factor 0.5");
                    System.out.println(dhdistinct5);
                    System.out.println("Double Hashing with load factor 0.75");
                    System.out.println(dhdistinct75);
                    System.out.println("JavaHashSet");
                    System.out.println(hsdistinct);
                    System.out.println("******");
                    System.out.println("******");
                    break;
                }
                case 2: {
                    System.out.println("Cuckoo Hashing");
                    //creates objects of CuckooHashing with different load factors
                    CuckooHashing<Integer> chlf5 = new CuckooHashing<>(0.5);
                    CuckooHashing<Integer> chlf75 = new CuckooHashing<>(0.75);
                    Set<Integer> hashSet = new HashSet<>();
                    Timer cht5 = new Timer();
                    Timer cht75 = new Timer();
                    System.out.println("Adding of "+n1+" numbers to Cuckoo Hashing 0.5lf started...");
                    cht5.start();
                    for(int i=1 ; i<= n1;i++)
                        chlf5.add(i);
                    cht5.end();
                    System.out.println("Adding of "+n1+" numbers to Cuckoo Hashing 0.5lf ended...");
                    System.out.println("Adding of "+n1+" numbers to Cuckoo Hashing 0.75lf started...");
                    cht75.start();
                    for(int i=1 ; i<= n1;i++)
                        chlf75.add(i);
                    cht75.end();
                    System.out.println("Adding of "+n1+" numbers to Cuckoo Hashing 0.75lf ended...");
                    Timer hst = new Timer();
                    System.out.println("Adding of "+n1+" numbers to HashSet started...");
                    hst.start();
                    for( int i=1; i<=n1; i++)
                        hashSet.add(i);
                    hst.end();
                    System.out.println("Adding of "+n1+" numbers to HashSet ended...");
                    Random randomNumberGenerator = new Random();
                    Integer[] arr = new Integer[n2];
                    for(int i = 0;i<n2;i++)
                        arr[i] = randomNumberGenerator.nextInt(100);
                    Timer chdistinct5 = new Timer(), chdistinct75=new Timer(), hsdistinct=new Timer();
                    System.out.println("Distinct checking in "+n2+" numbers in Cuckoo Hashing 0.5lf started...");
                    chdistinct5.start();
                    int count = distinctElementsCH5(arr);
                    chdistinct5.end();
                    System.out.println("Distinct checking in "+n2+" numbers in Cuckoo Hashing 0.5lf ended...");
                    System.out.println("Distinct checking in "+n2+" numbers in Cuckoo Hashing 0.75lf started...");
                    chdistinct75.start();
                    count = distinctElementsCH75(arr);
                    chdistinct75.end();
                    System.out.println("Distinct checking in "+n2+" numbers in Cuckoo Hashing 0.75lf started...");
                    hashSet = new HashSet<>();
                    System.out.println("Distinct checking in "+n2+" numbers in HashSet started...");
                    hsdistinct.start();
                    count = 0;
                    for(int i = 0;i<arr.length;i++) {
                        if(!hashSet.contains(arr[i])) {
                            hashSet.add(arr[i]);
                            count++;
                        }
                    }
                    hsdistinct.end();
                    System.out.println("Distinct checking in "+n2+" numbers in HashSet ended...");
                    System.out.println("******");
                    System.out.println("******");
                    System.out.println("PERFORMANCE REPORT ON ADDITION OF "+n1+" INTEGERS\n");
                    System.out.println("Cuckoo Hashing with load factor 0.5");
                    System.out.println(cht5);
                    System.out.println("Cuckoo Hashing with load factor 0.75");
                    System.out.println(cht75);
                    System.out.println("JavaHashSet");
                    System.out.println(hst);
                    System.out.println("******");
                    System.out.println("PERFORMANCE REPORT ON DISTINCT NUMBER COUNT OF "+n2+" NUMBERS");
                    System.out.println("Cuckoo Hashing with load factor 0.5");
                    System.out.println(chdistinct5);
                    System.out.println("Cuckoo Hashing with load factor 0.75");
                    System.out.println(chdistinct75);
                    System.out.println("JavaHashSet");
                    System.out.println(hsdistinct);
                    System.out.println("******");
                    System.out.println("******");
                    break;
                }
                default: {
                    System.out.println("Invalid option.Run Again.");
                    break;
                }
            }
            System.out.println("Do you want to continue?(Y/N)");
            String cont = input.next();
            if (cont.charAt(0) == 'Y')
                continue;
            else
                break;
        }
    }
}
//end of hashing class
