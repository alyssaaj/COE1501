// Alyssa Jordan
// alj81@pitt.edu
// 10/29/19

/******************************************************************************
 *  Compilation:  javac IndexPQ.java
 *  Execution:    java IndexPQ
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented or Maximum-oriented indexed PQ implementation using a binary heap.
 *  This is a modification on IndexMinPQ.java and IndexMaxPQ.java from the textbook
 ******************************************************************************/

import java.util.*;


public class IndexPQ{
	private int maxN;        // maximum number of elements on PQ
    private int n;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Apartment[] keys;      // keys[i] = priority of i
    private Boolean min_or_max; // true if min, false if max


    // Initializes an empty indexed priority queue with indices between 0 and maxN-1
	public IndexPQ(int maxN, Boolean min_or_max) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        this.min_or_max = min_or_max;
        n = 0;
        //keys = (Apartment[]) new Comparable[maxN + 1];    // make this of length maxN??
        keys = new Apartment[maxN+1];
        pq   = new int[maxN + 1];
        qp   = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            qp[i] = -1;
    }

    // prints all the Apartments (not in priority order)
    public void printAllApartments(){
    	System.out.println("All the Apartments: ");
    	for (int i = 1; i<=n; i++){
    		Apartment aa = keys[pq[i]];
    		aa.printApartment();
    	}
    }

    // Returns true if this priority queue is empty
    public boolean isEmpty() {
        return n == 0;
    }

    // Is i an index on this priority queue?
    public boolean contains(int i) {
        if (i < 0 || i >= maxN) throw new IllegalArgumentException();
        return qp[i] != -1;
    }

    // Returns the Apartment object belonging to a specific index
    public Apartment getAptByIndex(int i){
    	if (i < 0 || i >= maxN) throw new IllegalArgumentException();
    	return keys[i];
    }

    // Returns the number of keys on this priority queue
    public int size() {
        return n;
    }


    // Associates key with index i
    public void insert(int i, Apartment key) {
        if (i < 0 || i >= maxN) throw new IllegalArgumentException();
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }


    // Returns an index associate with a minimum or maximum key
    public int highestPriorityIndex() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    // Returns a minimum or maximum key.
    public Apartment highestPriorityKey() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return keys[pq[1]];
    }

    // Removes a minimum or maximum key and return its associated index
    public int delHighestPriority() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        int highest_priority = pq[1];
        Apartment aa = keys[highest_priority];
    	aa.printApartment();
        exch(1, n--);
        sink(1);
        assert highest_priority == pq[n+1];
        qp[highest_priority] = -1;        // delete
        keys[highest_priority] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed
        return highest_priority;
    }

    // Returns the key associated with index i
    public Apartment keyOf(int i) {
        if (i < 0 || i >= maxN) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return keys[i];
    }

    // Change the key associated with index i to the specified value.
    public void changeKey(int i, Apartment key) {
        if (i < 0 || i >= maxN) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    // Change the key associated with index i to the specified value.
    public void change(int i, Apartment key) {
        changeKey(i, key);
    }

    //Remove the key associated with index i.
    public void delete(int i) {
        if (i < 0 || i >= maxN) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    /*************** General Helper Functions *****************/
    private boolean greater_or_less(int i, int j) {
    	if (min_or_max){
    		return keys[pq[i]].getPrice().compareTo(keys[pq[j]].getPrice()) > 0;
    	}
    	else{
    		return keys[pq[i]].getSqft().compareTo(keys[pq[j]].getSqft()) < 0;
    	}

    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    /*************** Heap Helper Functions *****************/
    private void swim(int k) {
        while (k > 1 && greater_or_less(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater_or_less(j, j+1)) j++;
            if (!greater_or_less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }



}