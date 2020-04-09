README:
======

Short Project # 10: Implementation of Tries

Authors : 
----------------- 
	 Sri Chaitra Kareddy          	SXK180037
	
How to compile and run the code:
-------------------------------
	1) The command prompt path should be in "src" directory
	2) javac -cp . sxk180037/Trie.java
	3) java  -cp . sxk180037/Trie
	4) The input is given in this manner:
		You start inserting the words.Once the word is inserted, it will indicate whether or not the word is inserted and the word number.When you type "End" it will allow you to start searching for the word in the trie.

Operations in Code:
-------------------
The following methods are written for each Hashing Method:
put(x)
	calls the method insert(x), which creates a childnodesMap for each character.Also stories the word number in a hashmap.
get(x)
	gets the word by calling the search method which returns a boolean value based on whether the word is found or not.

remove(x):
Removes a word from the trie.

prefixCount(x): returns how many words in the dictionary start with the prefix.

startsWith(x):Returns if there is any word in the trie that starts with the given prefix.
