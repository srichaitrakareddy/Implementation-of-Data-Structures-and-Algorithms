README:
======

Short Project # 08: Hashing Implementation and Comparison with Java HashSet 

Authors : 
----------------- 
	1) Sri Chaitra Kareddy          	SXK180037

How to compile and run the code:
-------------------------------
	1) The command prompt path should be in "src" directory
	2) javac -cp . com.sxk180037/Hashing.java
	3) java  -cp . com.sxk180037/Hashing
	4) The input is given in this manner:
		Pick 1 for Double Hashing or 2 for Cuckoo Hashing
		Hardcode the values of n1 for number of additions and n2 for the number of elements we need to check the distinct count

Operations in Code:
-------------------
The following methods are written for each Hashing Method:
add(x)
	adds and element to the hashtable at the index derived from its hashcode
remove(x)
	removes the element from the hashtable by marking it as deleted
contains(x)
	returns true when the elment is present in the hashtable, false otherwise
find(x)
	Helper method used to find the index of the value x
rehash()
	Used to increase the hashtable size and rehash when the size reaches a certain threshold given by the load factor

The Hashing class contains 4 different variations of this method:
distinctElements(T arr[])
	This method returns the count of distinct elements in the array by using the hashtables

Report:
-------

  Number			DoubleHashing					CuckooHashing				HashSet

		   0.5 LoadFactor	   0.75 LoadFactor	   0.5 LoadFactor	   0.75 LoadFactor	
		
   1000		Time: 89 msec.		Time: 67 msec.		Time: 2 msec.		Time: 1 msec.		Time: 0 msec.	
		Memory: 4 MB / 252 MB   Memory: 4 MB / 252 MB	Memory: 5 MB / 252 MB	Memory: 5 MB / 252 MB	Memory: 4 MB / 252 MB

   10000	Time: 7579 msec.	Time: 7338 msec.	Time: 7 msec.		Time: 3 msec.		Time: 1 msec.
		Memory: 5 MB / 252 MB.	Memory: 6 MB / 252 MB.	Memory: 9 MB / 252 MB.	Memory: 9 MB / 252 MB.	Memory: 7 MB / 252 MB.
						
  100000	Time: 333023 msec.	Time: 374731 msec.	Time: 42 msec.		Time: 23 msec.		Time: 23 msec.
		Memory: 10 MB / 252 MB.	Memory: 17 MB / 252 MB	Memory: 14 MB / 252 MB.	Memory: 27 MB / 252 MB.	Memory: 25 MB / 252 MB

  1000000		  -			-		Time: 242 msec.		Time: 196 msec.		Time: 143 msec.
								Memory: 90 MB / 252 MB	Memory: 190 MB / 252 MB	Memory: 157 MB / 335 MB

At 1000000 numbers, Double Hashing never ends.


DistinctNumbers			DoubleHashing					CuckooHashing				HashSet

		   0.5 LoadFactor	   0.75 LoadFactor	   0.5 LoadFactor	   0.75 LoadFactor

100		Time: 0 msec.		Time: 0 msec.		Time: 0 msec.		Time: 0 msec.		Time: 0 msec.
		Memory: 4 MB / 252 MB	Memory: 4 MB / 252 MB	Memory: 5 MB / 252 MB	Memory: 5 MB / 252 MB	Memory: 4 MB / 252 MB

1000		Time: 7 msec.		Time: 7 msec.		Time: 0 msec.		Time: 1 msec.		Time: 0 msec.
		Memory: 7 MB / 252 MB.	Memory: 7 MB / 252 MB.	Memory: 10 MB / 252 MB.	Memory: 10 MB / 252 MB.	Memory: 7 MB / 252 MB.						

10000		Time: 1529 msec.	Time: 1548 msec.	Time: 2 msec.		Time: 2 msec:		Time: 1 msec.
		Memory: 25 MB / 252 MB.	Memory: 25 MB / 252 MB.	Memory: 23 MB / 252 MB	Memory: 23 MB / 252 MB	Memory: 23 MB / 252 MB




