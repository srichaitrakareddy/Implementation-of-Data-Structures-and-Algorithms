package com.sxk180037;

/** Sample starter code for SP3.
 *  @author rbk
 */
/**
 * @author Sri Chaitra Kareddy
 */

import java.util.Random;

public class SP3 {
    public static Random random = new Random();
    public static int numTrials = 100;
    public static void main(String[] args) {
        int n = 128000000;
        int choice = 5;
        if(args.length > 0) { n = Integer.parseInt(args[0]); }
        if(args.length > 1) { choice = Integer.parseInt(args[1]); }
        int[] arr = new int[n];
        for(int i=0; i<n; i++) {
            arr[i] = i;
        }
        Timer timer = new Timer();
        switch(choice) {
            case 1:
                for(int i=0; i<numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort1(arr);
                }
                break;
            case 3:
                for(int i=0; i<numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort3(arr);
                }
                break;
            case 4:
                for(int i=0; i<numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort4(arr);
                }
                break;
            case 5:
                for(int i=0; i<numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSortTake5(arr);
                }
                break;// etc
        }
        timer.end();
        timer.scale(numTrials);

        System.out.println("n: "+n+"\n"+"Choice: " + choice + "\n" +"Average "+ timer);
    }

    public static void insertionSort(int[] arr) {
        insertionSort(arr,0,arr.length);
    }
    static void insertionSort(int[] arr, int p,int r) {
        for(int i=p+1;i<r;i++) {
            int temp = arr[i];
            int j = i-1;
            while(j>=p && temp < arr[j]) {
                arr[j+1] = arr[j];
                j= j-1;
                arr[j+1] = temp;
            }
        }
    }

    public static void mergeSort1(int[] arr) {
        mergeSort1(arr,0,arr.length-1);
    }
    static void mergeSort1(int[] arr, int p, int r) {
        if(p<r) {
            int q = (p+r)/2;
            mergeSort1(arr,p,q);
            mergeSort1(arr,q+1,r);
            merge1(arr,p,q,r);
        }
    }
    static void merge1(int[] arr,int p,int q,int r) {
        int[] L = new int[q-p+1];
        int[] R = new int[r-q];
        System.arraycopy(arr, p, L, 0, q-p+1);
        System.arraycopy(arr, q+1,R, 0, r-q);
        int i=0;
        int j=0;
        for(int k=p;k<=r;k++) {
            if(i>L.length -1 && j<R.length) {
                arr[k] = R[j++];
            } else if(j>R.length - 1 && i<L.length) {
                arr[k] = L[i++];
            } else {
                if( L[i]<=R[j] ) {
                    arr[k] = L[i++];
                } else {
                    arr[k] = R[j++];
                }
            }
        }
    }

    public static void mergeSort3(int[] arr) {
        int[] B = new int[arr.length];
        System.arraycopy(arr, 0, B, 0, arr.length);
        mergeSort3(arr,B, 0,arr.length-1);
    }

    static void mergeSort3(int[] arr, int[] B, int p, int r) {
        if(p<r) {
            int q = (p+r)/2;
            mergeSort3(B,arr,p,q);
            mergeSort3(B, arr,q+1,r);
            merge3(arr, B,p,q,r);
        }
    }

    static void merge3(int[] arr, int[] b, int p, int q, int r) {
        int i = p, j=q+1, k =p;
        while(i<=q && j<=r) {
            if (b[i] <= b[j])
                arr[k++] = b[i++];
            else
                arr[k++] = b[j++];
        }
        while(i<=q)
            arr[k++] = b[i++];
        while(j<=r)
            arr[k++] = b[j++];
    }

    public static void mergeSort4(int[] arr) {
        int[] b = new int[arr.length];
        System.arraycopy(arr, 0, b, 0, arr.length);
        mergeSort4(arr,b, 0,arr.length-1);
    }

    static void mergeSort4(int[] a, int[] b, int p, int r) {
        int t = 30;
        if(r-p<t)
            insertionSort(a, p, r);
        else {
            int q = (p+r)/2;
            mergeSort4(b,a, p, q);
            mergeSort4(b,a,q+1, r);
            merge3(a,b,p,q,r); //using merge3 because merge4 and merge3 are the same
        }
    }

    public static void mergeSortTake5(int[] A){
        int[] B = A.clone();
        mergeSortTake5(A, B);
    }

    private static void mergeSortTake5(int[] A, int[] B){
        int [] T = A;
        for(int i=1;i<A.length;i=2*i){
            for(int j=0;j<A.length-1;j+=2*i){
                int mid = Math.min(j+i-1, A.length-1);
                int right = Math.min(j+2*i-1, A.length-1);
                merge3(A, B, j, mid, right);
            }
            T = A;
            A = B;
            B = T;
        }
    }

    /** Timer class for roughly calculating running time of programs
     *  @author rbk
     *  Usage:  Timer timer = new Timer();
     *          timer.start();
     *          timer.end();
     *          System.out.println(timer);  // output statistics
     */

    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime-startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() { if(!ready) { end(); }  return elapsedTime; }

        public long memory()   { if(!ready) { end(); }  return memUsed; }

        public void scale(int num) { elapsedTime /= num; }

        public String toString() {
            if(!ready) { end(); }
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
        }
    }

    /** @author rbk : based on algorithm described in a book
     */


    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {

        public static void shuffle(int[] arr) {
            shuffle(arr, 0, arr.length-1);
        }

        public static<T> void shuffle(T[] arr) {
            shuffle(arr, 0, arr.length-1);
        }

        public static void shuffle(int[] arr, int from, int to) {
            int n = to - from  + 1;
            for(int i=1; i<n; i++) {
                int j = random.nextInt(i);
                swap(arr, i+from, j+from);
            }
        }

        public static<T> void shuffle(T[] arr, int from, int to) {
            int n = to - from  + 1;
            Random random = new Random();
            for(int i=1; i<n; i++) {
                int j = random.nextInt(i);
                swap(arr, i+from, j+from);
            }
        }

        static void swap(int[] arr, int x, int y) {
            int tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        static<T> void swap(T[] arr, int x, int y) {
            T tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        public static<T> void printArray(T[] arr, String message) {
            printArray(arr, 0, arr.length-1, message);
        }

        public static<T> void printArray(T[] arr, int from, int to, String message) {
            System.out.print(message);
            for(int i=from; i<=to; i++) {
                System.out.print(" " + arr[i]);
            }
            System.out.println();
        }
    }
}

