/**
 * HashTable.java
 * @author Prajwal Agrawal
 * CIS 22C, Lab 14
 */
import java.util.ArrayList;

public class HashTable<T> {
    private int numElements;
    private ArrayList<LinkedList<T>> table;

    /** Constructors */

    /**
     * Construct a new HashTable
     * 
     * @param size the size of the HashTable
     * @precondition size > 0
     * 
     * @throws IllegalArgumentException when size <= 0
     */
    public HashTable(int size) throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        numElements = 0;
        table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<T>());
        }
    }

    /**
     * Construct a new HashTable with elements from the given array
     * 
     * @param array an array of elements to insert
     * @param size  the size of the HashTable
     * @precondition size > 0
     * 
     * @throws IllegalArgumentException when size <= 0
     */
    public HashTable(T[] array, int size) throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        numElements = 0;
        table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<T>());
        }
        if (array != null) {
            for (T item : array) {
                if (item != null) {
                    add(item);
                }
            }
        }
    }

    /** Accessors */

    /**
     * Returns the number of elements in the HashTable
     * 
     * @return the number of elements
     */
    public int getNumElements() {
        return numElements;
    }

    /**
     * Accesses a specified element in the table.
     * 
     * @param elem the element to locate
     * @return the bucket number where the element is located or -1 if it is not
     *         found.
     * @precondition elem != null
     * @throws NullPointerException when elem is null
     */
    public int find(T elem) throws NullPointerException {
        if (elem == null) {
            throw new NullPointerException("elem cannot be null");
        }
        int index = hash(elem);

        if (table.get(index).findIndex(elem) == -1) {
            return -1;
        }
        return index;

    }

    /**
     * Accesses a specified key in the Table.
     * 
     * @param elem the key to access
     * @precondition elem != null
     * 
     * @return the value associated with the key
     * @throws NullPointerException when elem is null
     */
    public T get(T elem) {
        if (elem == null) {
            throw new NullPointerException("elem cannot be null");
        }
        int index = hash(elem);
        LinkedList<T> bucket = table.get(index);

        if (bucket.findIndex(elem) == -1) {
            return null;
        }
        bucket.advanceIteratorToIndex(bucket.findIndex(elem));
        return bucket.getIterator();
    }

    /**
     * Computes the hash value in the table for a given Object
     * 
     * @param obj the Object
     * @return the index in the table
     */
    private int hash(T obj) {
        return obj.hashCode() % table.size();
    }

    /**
     * Counts the number of elements at this index
     * 
     * @param index the index in the table
     * @precondition 0 <= index < table.size()
     * 
     * @return the count of elements at this index
     * @throws IndexOutOfBoundsException when the precondition is violated
     */
    public int countBucket(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= table.size()) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        return table.get(index).getLength();
    }

    /**
     * Determines whether a specified element is in the table
     * 
     * @param elmt the element to locate
     * @return whether the element is in the table
     * @precondition elmt != null
     * 
     * @throws NullPointerException when the precondition is violated
     */
    public boolean contains(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("elmt cannot be null");
        }
        int index = hash(elmt);
        return table.get(index).findIndex(elmt) != -1;
    }

    /** Mutators */

    /**
     * Adds an item to the HashTable
     * 
     * @param item the item to add
     * @precondition item != null
     */
    public void add(T item) {
        int index = hash(item);
        table.get(index).addLast(item);
        numElements++;
    }
    
    /**
     * Removes the given element from the table
     * 
     * @param elmt the element to remove
     * @return whether elmt exists and was removed from the table
     * @precondition elmt != null
     * 
     * @throws NullPointerException when the precondition is violated
     */
    public boolean delete(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("elmt cannot be null");
        }
        int index = hash(elmt);
        LinkedList<T> bucket = table.get(index);
        int location = bucket.findIndex(elmt);
        if (location == -1) {
            return false;
        }
        bucket.advanceIteratorToIndex(location);
        bucket.removeIterator();
        numElements--;
        return true;
    }

    /**
     * Resets the hash table back to the empty state, as if the one argument constructor has just been called.
     */
    public void clear() {
        for (int i = 0; i < table.size(); i++) {
            table.get(i).clear();
        }
        numElements = 0;
    }

    /** Additional Methods */

    /**
     * Computes the load factor
     * 
     * @return the load factor
     */
    public double getLoadFactor() {

        if (numElements == 0 || table.size() == 0) {
            return 0.0;
        }

        return (double) numElements / table.size();
    }

    /**
     * Creates a String of all the elements in a bucket
     * 
     * @param index the index of the bucket
     * @precondition index >= 0 && index < table.size()
     * @throws IndexOutOfBoundsException when index is out of bounds
     * 
     * @return a String of all the elements in the bucket
     */
    public String bucketToString(int index) throws IndexOutOfBoundsException {
        return table.get(index).toString();
    }
  
    /**
     * Creates a String of the bucket number followed by a colon followed by the first element at each bucket followed by a new line. For empty buckets, add the bucket number followed by a colon followed by empty. 
     * 
     * @return a String of all first elements at each bucket
     */
    public String rowToString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < table.size(); i++) {
            LinkedList<T> bucket = table.get(i);
            result.append("Bucket " + i + ": ");
            if (bucket.isEmpty()) {
                result.append("empty\n");
            } else {
                result.append(bucket.getFirst() + "\n");
            }
        }
        return result.toString();
    }
    
    /**
     * Starting at the 0th bucket, and continuing in order until the last bucket,
     * concatenates all elements at all buckets into one String, with a new line
     * between buckets and one more new line at the end of the entire String.
     * 
     * @return a String of all elements in this HashTable
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < table.size(); i++) {
            LinkedList<T> bucket = table.get(i);
            if (!bucket.isEmpty()) {
                result.append(bucket.toString().trim() + " \n");
            }
        }
        if (result.toString().isEmpty()) {
            return "\n";
        } else {
            return result.toString() + "\n";
        }
    }
}
