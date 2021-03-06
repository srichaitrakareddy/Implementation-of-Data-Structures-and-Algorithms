/*
 *  Sukruti Puranam Hosudurg 		    SXP180014
 *  Shailaja Shrishail Patangi		    SXP177331
 *  Sri Chaitra Kareddy 			    SXK180037
 *  Balnur Dauletbekkyzy Duisenbayeva     BDD180001
 * 
 */

package sxk180037;

import java.util.Random;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SkipList<T extends Comparable<? super T>> {
	static final int PossibleLevels = 33;
	Entry<T> head, tail; // dummy nodes head and tail
	int size; // size of the skipList
	int maxLevel;
	Entry<T>[] last; // used by find()
	int[] spans; // used by find() to store spans while traversing levels
	Random random;

	static class Entry<E> {
		E element;
		Entry<E>[] next;
		Entry<E> prev;
		int span[];
		private int height;

		public Entry(E x, int lev) {
			element = x;
			next = new Entry[lev];
			span = new int[lev];
			height = lev;
		}

		public E getElement() {
			return element;
		}
	}

	// Constructor
	public SkipList() {
		head = new Entry<T>(null, PossibleLevels);
		tail = new Entry<T>(null, PossibleLevels);
		size = 0;
		maxLevel = 1;
		last = new Entry[PossibleLevels];
		spans = new int[PossibleLevels];
		// Arrays.fill(spans, 1);
		random = new Random();
		// setting head.next to tail
		for (int i = 0; i < PossibleLevels; i++) {
			head.next[i] = tail;
		}
		tail.prev = head;
	}

	// Helper function to search for element x
	public void find(T x) {
		Entry<T> temp = head;
		int i = maxLevel - 1;
		int span = 0;
		while (i >= 0) {
			while (temp.next[i] != null && temp.next[i].element != null && x.compareTo(temp.next[i].element) > 0) {
				span += temp.span[i] + 1;
				temp = temp.next[i];
			}
			last[i] = temp;
			// Total distance between nodes when jumping levels
			spans[i] = span;
			i--;
		}
	}

	// Choose the Level of an entry
	public int chooseLevel() {
		int level = 1 + Integer.numberOfTrailingZeros(random.nextInt());
		if (level > maxLevel)
			maxLevel = level;
		return level;
	}

	// Add x to list. If x already exists, reject it. Returns true if new node is
	// added to list
	public boolean workingAdd(T x) {
		int level = chooseLevel();
		// Exit if already exist
		if (contains(x))
			return false;
		Entry<T> ent = new Entry<T>(x, level);
		for (int i = 0; i < level; i++) {
			ent.next[i] = last[i].next[i];
			ent.span[i] = last[i].span[i] == 1 ? 1 : last[i].span[i] - spans[i] + 1;
			last[i].next[i] = ent;
			last[i].span[i] = last[i].span[i] == 1 ? 1 : spans[i];
		}
		ent.next[0].prev = ent;
		ent.prev = last[0];
		size += 1;
		return true;
	}

	// Add x to list. If x already exists, reject it. Returns true if new node is
	// added to list
	public boolean add(T x) {
		int level = chooseLevel();

		// Exit if already exist
		if (contains(x))
			return false;

		Entry<T> ent = new Entry<T>(x, level);
		for (int i = 0; i < level; i++) {
			boolean isTailNode = last[i].next[i].equals(tail);
			ent.next[i] = last[i].next[i];
			ent.span[i] = isTailNode ? size - spans[0] + spans[i] : spans[i] + last[i].span[i] - spans[0];
			last[i].next[i] = ent;
			last[i].span[i] = spans[0] - spans[i];
		}
		ent.next[0].prev = ent;
		ent.prev = last[0];
		size += 1;

		for (int i = level; i < PossibleLevels; i++) {
			if (i < maxLevel) {
				last[i].span[i] += 1;
			} else {
				head.span[i] += 1;
			}
		}

		return true;
	}

	// Find smallest element that is greater or equal to x
	public T ceiling(T x) {
		if (contains(x))
			return x;
		return last[0].next[0].element;
	}

	// Does list contain x?
	public boolean contains(T x) {
		find(x);
		if (last[0].next[0].element == null)
			return false;

		return x.compareTo(last[0].next[0].element) == 0;
	}

	// Return first element of list
	public T first() {
		return head.next[0].element;
	}

	// Find largest element that is less than or equal to x
	public T floor(T x) {
		if (contains(x))
			return x;
		return last[0].element;
	}

	// Return element at index n of list. First element is at index 0.
	public T get(int n) {
		if (n < 0 || n >= size)
			throw new NoSuchElementException();
		return getLog(n);
	}

	// O(n) algorithm for get(n)
	public T getLinear(int n) {
		Entry<T> temp = head;
		for (int i = 0; i <= n; i++) {
			temp = temp.next[0];
		}
		return temp.element;
	}

	// Optional operation: Eligible for EC.
	// O(log n) expected time for get(n). Requires maintenance of spans, as
	// discussed in class.
	public T getLog(int n) {
		Entry<T> temp = head;
		int i = maxLevel - 1;
		n = n + 1;
		int distance = 0;

		while (i >= 0) {
			while (distance + temp.span[i] + 1 <= n) {
				distance += temp.span[i] + 1;
				if (distance == n) {
					temp = temp.next[i];
					return temp == null ? null : temp.element;
				}
				temp = temp.next[i];
			}
			i--;
		}
		return null;
	}

	// Is the list empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// Iterate through the elements of list in sorted order
	public Iterator<T> iterator() {
		return new SkipListIterator();
	}

	protected class SkipListIterator implements Iterator<T> {

		Entry<T> cursor;
		boolean ready;

		SkipListIterator() {
			cursor = head;
			ready = false;
		}

		public boolean hasNext() {
			return cursor.next[0].element != null;
		}

		public T next() {
			if (cursor.next == null)
				throw new NoSuchElementException();
			cursor = cursor.next[0];
			ready = true;
			return cursor.element;
		}

		public void remove() {
			if (!ready)
				throw new IllegalStateException();
			find(cursor.element);
			Entry<T> ent = last[0].next[0];
			int len = ent.next.length;
			for (int i = 0; i < len; i++) {
				last[i].next[i] = ent.next[i];
			}
			ent.next[0].prev = last[0];
			size -= 1;
			cursor = ent.prev;
			ready = false; // next() should be called atleast once inorder to call remove()
		}

	}

	// Return last element of list
	public T last() {
		return tail.prev.element;
	}

	// Optional operation: Reorganize the elements of the list into a perfect skip
	// list
	// Not a standard operation in skip lists. Eligible for EC.
	public void rebuild() {
		// computing maxLevel required for rebuild()
		maxLevel = (int) (Math.log10(size) / Math.log10(2)) + 1;
		int position = 0;
		Entry<T>[] previous = new Entry[maxLevel];
		int[] powersOfTwo = new int[maxLevel];

		for (int i = 0; i < maxLevel; i++) {
			previous[i] = head;
			powersOfTwo[i] = (int) Math.pow(2, i);
		}

		// p is the first entry
		Entry<T> p = head.next[0];

		// While p reaches tail
		while (p != null) {
			position++;

			// For odd positions, head positioned at 0.
			if (!(position % 2 == 0)) {
				// exact no of entries in new p.next[] = 1 :)
				p.height = 1;

				Entry<T>[] newNext = new Entry[1];
				int[] newSpan = new int[1];

				// only correctly referencing at lowest level
				newNext[0] = p.next[0];
				newSpan[0] = 1;
				// replacing the old next with the new next[]
				p.next = newNext;
				p.span = newSpan; // & old span with new span[]

				previous[0].next[0] = p;
				previous[0] = p;
			}

			// For even positions, need to recreate next[] and span[]
			// with proper height of each entry
			else {
				int exponent = maxLevel - 1;

				while (position % powersOfTwo[exponent] != 0)
					exponent--;

				// exact no of entries in new p.next[] = exponent + 1
				p.height = exponent + 1;

				Entry<T>[] newNext = new Entry[p.height];
				int[] newSpan = new int[p.height];

				// only correctly referencing at lowest level
				newNext[0] = p.next[0];
				newSpan[0] = p.span[0];
				// replacing the old next with the new next[]
				p.next = newNext;
				p.span = newSpan; // & old span with new span[]

				// so filling up entries in top-down manner
				// in all such entries which points to p
				while (exponent > -1) {
					// Entry at level exponent which points to p = p
					previous[exponent].next[exponent] = p;
					previous[exponent].span[exponent] = powersOfTwo[exponent];

					previous[exponent] = p; // ...for some future entry
					exponent--;
				}
			}
			p = p.next[0]; // moving on to the immediate next
		}

		for (int i = 0; i < maxLevel; i++) {
			int in = maxLevel - 1 - i;

			if (previous[in] != tail) {
				previous[in].next[in] = tail;

				// When size = 2^maxLevel - 1
				if (size() == Math.pow(2, maxLevel) - 1) {
					// in perfect skip list span[i] = 2^i
					previous[in].span[in] = powersOfTwo[in];
				}
				// When size != 2^maxLevel - 1
				else {
					int prevDist = 0;
					Entry<T> q = head;

					while (q.next[in] != null && q.next[in].element != null
							&& q.next[in].element.compareTo(previous[in].element) <= 0) {

						prevDist += q.span[in]; // summing up distance strode
						q = q.next[in]; // on the same level
					}
					previous[in].span[in] = size + 1 - prevDist;
				}
			}
		}
	}

	// Remove x from list. Removed element is returned. Return null if x not in list
	public T remove(T x) {
		if (!contains(x))
			return null;
		Entry<T> ent = last[0].next[0];
		int len = ent.next.length;
		for (int i = 0; i < len; i++) {
			last[i].next[i] = ent.next[i];
			last[i].span[i] += ent.span[i];
		}
		ent.next[0].prev = last[0];
		int index = len;
		// Adjust remaining nodes from level to maxLevel
		while (index < maxLevel) {
			last[index].span[index] -= 1;
			index++;
		}
		size -= 1;
		return ent.element;
	}

	public void printList() {
		Entry<T> temp = head;
		while (!temp.equals(tail)) {
			System.out.println(" element " + temp.element + " Level Size " + temp.next.length);
			int n = temp.next.length;
			for (int i = 0; i < n; i++) {
				System.out.print("i " + i + " span " + temp.span[i] + " ");
			}
			System.out.println();
			temp = temp.next[0];
		}
	}

	// Return the number of elements in the list
	public int size() {
		return size;
	}

}
