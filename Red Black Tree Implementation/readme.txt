README:
======

Short Project # 07: Red Black Trees 

Author: 
----------------- 
	1)Sri Chaitra Kareddy  		SXK180037

How to compile and run the code:
-------------------------------
	1) The command prompt path should be in "src" directory
	2) javac -cp . sxk180037/*.java
	3) java  -cp . sxk180037/RedBlackTree
	4) The input is given in this manner:
		for every integer to add, just type in the integer to be added
		for every integer to remove, enter the integer with a minus(hyphen) sign in front of it
		the input sequence must end with 0
		Sample Input: 1 3 2 6 8 4 5 -3 -5 0

Operations in Code:
-------------------
add(x)
	Adds x to tree. If tree contains a node with same key, replaces the element by x. 
	Returns true if x is a new element added to tree
remove(x)
	Removes x from tree. Returns x if found, otherwise returns null
find(x)
	Checks if x is present in the tree and returns the node, Otherwise returns the node at which the search fails	
transplant(destination, source)
	Transplants the node from source to destination, used as a helper for remove
fixup(x)
	Rebalances the tree after removal of a node is done
rotateLeft(x)
	Rotates the tree left at the node given
rotateRight(x)
	Rotates the tree right at the node given
get(x)
	Checks if there is an element that is equal to x in the tree and element in tree that is equal to x is returned, null otherwise.

contains(x)
	boolean method that searches for an element equal to x in the tree and returns true if found, otherwise returns false
min()
	returns the minimum element in the tree
max()
	returns the maximum element in the tree
toArray()
	returns an array with the elements of the tree arranged using in order traversal
