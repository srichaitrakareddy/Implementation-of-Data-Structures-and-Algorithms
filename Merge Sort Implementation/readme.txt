README:
======

Short Project : Divide and Conquer | Merge Sort

Authors  : 
----------------- 
	1) Sri Chaitra Kareddy.          	SXK180037

How to compile and run the code:
-------------------------------
For each entry please hardcode the values of n and choice, the choice values are
		1 for Merge Sort (Take 1)
		3 for Merge Sort (Take 3)
		4 for Merge Sort (Take 4)
		5 for Merge Sort (Take 5)


After Running the Program for various values of 'n' for different Algorithms and not running more than one algorithm at a time,
We get the following Observations -

Generated Table Results :
-----------------------


n/Algorithm		Merge Sort (Take 1)			Merge Sort (Take 3)			Merge Sort (Take 4)			Merge Sort (Take 5)
		Time (msec)	Memory (Used/Avail)	Time (msec)	Memory (Used/Avail)	Time (msec)	Memory (Used/Avail)	Time (msec)	Memory (Used/Avail)
  
  8000000 	 1294 msec 	  121 MB / 286 MB 	 2137 msec 	  94 MB / 252 MB 	 2046 msec 	  94 MB / 252 MB 	 1335 msec 	 2748 MB / 2792 MB

 16000000 	 2827 msec 	  157 MB / 253 MB 	 5836 msec 	  125 MB / 252 MB 	 5311 msec 	  125 MB / 252 MB 	 2824 msec 	  552 MB / 577 MB

 32000000 	 6083 msec 	  248 MB / 329 MB 	 5100 msec 	  247 MB / 252 MB 	 9059 msec 	  247 MB / 252 MB 	 5982 msec 	 2833 MB / 4040 MB 

 64000000 	 12518 msec 	  492 MB / 1150 MB 	 10494 msec 	  491 MB / 497 MB 	 17640 msec 	  491 MB / 497 MB 	 12601 msec 	  3442 MB / 3969 MB

 128000000 	 32534 msec 	  786 MB / 934 MB 	 30534 msec 	  724 MB / 914 MB 	 39286 msec 	  979 MB / 1230 MB 	 26105 msec 	  979 MB / 1230 MB
	
 256000000 	     - 		         - 		     - 			- 		     - 			- 		     - 			  -


At 256000000, we have got an Out Of Memory Exception
