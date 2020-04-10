/**Binary Search Implementation: Recursive
 Binary Search : Given a sorted set of elements, we go by dividing the array into 2 parts recursively until we find the
 element that we are looking for.Sorting the elements will take a different complexity.If given an unsorted array.
 Edge cases : start is greater than the end value given the given data is incorrect so return false.
 StackOverFlow error if the element is not there.
 */
import java.util.*;
public class Solution {

public static boolean binarySearch(int [] arr,int start,int end, int element){
        if(start>end){
        return false;
        }
        //Also can do it as mid=(start+end)>>>1 (unsigned right operator)
        int mid=(start+end)/2;
        //Integer overflow : It is caused when we have large numbers then start+end is lost.
        //Avoiding Integer Overflow : start+((end-start)/2)

        if(arr[mid]==element){
        return true;
        } else if(element <arr[mid]){
        return binarySearch(arr,start,mid,element);
        }
        else{
        return binarySearch(arr,mid,end,element);
        }
        }

public static void main(String[] args){
        int[] a={1,2,3,4,5,6,7,8,9,10};
        boolean result= binarySearch(a,10,1,10);
        }
        }