/**
 * Defines a doubly-linked list class
 * @author Prajwal Agrawal
 * CIS 22C Lab 4
 */
import java.util.NoSuchElementException;

public class LinkedList<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**** CONSTRUCTORS ****/

    /**
     * Instantiates a new LinkedList with default values
     * 
     * @postcondition the list is set up and empty
     */
    public LinkedList() {
        this.length = 0;
        this.first = null;
        this.last = null;
        this.iterator = null;
    }

    /**
     * Converts the given array into a LinkedList
     * 
     * @param array the array of values to insert into this LinkedList
     * @postcondition the linked list contains all the elements of the array
     */
    public LinkedList(T[] array) {
        this();
        if (array == null || array.length == 0) {
            return;
        }

        this.first = new Node(array[0]);
        this.last = this.first;
        this.length = 1;
        for (int i = 1; i < array.length; i++) {
            this.addLast(array[i]);
        }

    }

    /**
     * Instantiates a new LinkedList by copying another List
     * 
     * @param original the LinkedList to copy
     * @postcondition a new List object, which is an identical,
     *                but separate, copy of the LinkedList original
     */
    public LinkedList(LinkedList<T> original) {
        this();
        if (original == null || original.isEmpty()) {
            return;
        }
        Node current = original.first;
        while (current != null) {
            this.addLast(current.data);
            current = current.next;
            this.length++;
        }
        this.iterator = null;
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     * 
     * @precondition the list is not empty
     * @return the value stored at node first
     * @throws NoSuchElementException if the list is empty
     */
    public T getFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     * 
     * @precondition the list is not empty
     * @return the value stored in the node last
     * @throws NoSuchElementException if the list is empty
     */
    public T getLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }
        return last.data;
    }

    /**
     * Returns the data stored in the iterator node
     * 
     * @precondition the iterator is not null
     * @return the data stored in the iterator node
     * @throw NullPointerException if the iterator is null
     */
    public T getIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("The iterator is null");
        }
        return iterator.data;
    }

    /**
     * Returns the current length of the LinkedList
     * 
     * @return the length of the LinkedList from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the LinkedList is currently empty
     * 
     * @return whether the LinkedList is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns whether the iterator is offEnd, i.e. null
     * 
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return (iterator == null);
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     * 
     * @param data the data to insert at the front of the LinkedList
     * @postcondition the new element is added to the front of the list
     */
    public void addFirst(T data) {

        Node newNode = new Node(data);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        length++;
    }

    /**
     * Creates a new last element
     * 
     * @param data the data to insert at the end of the LinkedList
     * @postcondition the new element is added to the end of the list
     */
    public void addLast(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;

        }
        length++;

    }

    /**
     * Inserts a new element after the iterator
     * 
     * @param data the data to insert
     * @precondition the iterator is not null
     * @throws NullPointerException if the iterator is null
     */
    public void addIterator(T data) throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("The iterator is null");
        }
        Node newNode = new Node(data);

        // Link the new node into the list
        newNode.next = iterator.next;
        newNode.prev = iterator;

        // Link the iterator to the new node
        iterator.next = newNode;

        // Link the new node to the next node
        if (newNode.next != null) {
            newNode.next.prev = newNode;
        }

        length++;
    }

    /**
     * removes the element at the front of the LinkedList
     * 
     * @precondition the list is not empty
     * @postcondition the element at the front of the list is removed
     * @throws NoSuchElementException if the list is empty
     */
    public void removeFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }
        Node nodeToRemove = first;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        nodeToRemove.next = null;
        nodeToRemove.prev = null;
        length--;
    }

    /**
     * removes the element at the end of the LinkedList
     * 
     * @precondition the list is not empty
     * @postcondition the element at the end of the list is removed
     * @throws NoSuchElementException if the list is empty
     */
    public void removeLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }
        Node nodeToRemove = last;
        last = last.prev;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        nodeToRemove.next = null;
        nodeToRemove.prev = null;
        length--;
    }

    /**
     * removes the element referenced by the iterator
     * 
     * @precondition the iterator is not null
     * @postcondition the element referenced by the iterator is removed and the
     *                iterator is set to null
     * @throws NullPointerException if the iterator is null
     */
    public void removeIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("The iterator is null");
        }

        Node nodeToRemove = iterator;

        if (nodeToRemove.prev != null) {
            nodeToRemove.prev.next = nodeToRemove.next;
        } else {
            first = nodeToRemove.next;
        }

        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = nodeToRemove.prev;
        } else {
            last = nodeToRemove.prev;
        }

        nodeToRemove.next = null;
        nodeToRemove.prev = null;

        this.iterator = null;
        length--;
    }

    /**
     * places the iterator at the first node
     * 
     * @postcondition The iterator is at the first node
     */
    public void positionIterator() {
        this.iterator = first;
    }

    /**
     * Moves the iterator one node towards the last
     * 
     * @precondition the iterator is not null
     * @postcondition the iterator is at the next node
     * @throws NullPointerException if the iterator is null
     */
    public void advanceIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("The iterator is null");
        }
        iterator = iterator.next;
    }

    /**
     * Moves the iterator one node towards the first
     * 
     * @precondition the iterator is not null
     * @postcondition the iterator is at the previous node
     * @throws NullPointerException if the iterator is null
     */
    public void reverseIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("The iterator is null");
        }
        iterator = iterator.prev;

    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * Re-sets LinkedList to empty as if the
     * default constructor had just been called
     */
    public void clear() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the LinkedList to a String, with each value separated by a blank
     * line At the end of the String, place a new line character
     * 
     * @return the LinkedList as a String
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Node temp = first;
        while (temp != null) {
            result.append(temp.data + " ");
            temp = temp.next;
        }
        return result.toString() + "\n";
    }

    /**
     * Determines whether the given Object is
     * another LinkedList, containing
     * the same data in the same order
     * 
     * @param obj another Object
     * @return whether there is equality
     */
    @SuppressWarnings("unchecked") // good practice to remove warning here
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkedList)) {
            return false;
        }

        LinkedList<T> other = (LinkedList<T>) obj;
        if (this.length != other.length) {
            return false;
        }

        Node thisNode = this.first;
        Node otherNode = other.first;

        while (thisNode != null) {

            if (thisNode.data == null || otherNode.data == null) {
                if (thisNode.data != otherNode.data) {
                    return false;
                }
            } else if (!thisNode.data.equals(otherNode.data)) {
                return false;
            }

            thisNode = thisNode.next;
            otherNode = otherNode.next;
        }
        
        return true;
    }

    /**
     * Moves all nodes in the list towards the end
     * of the list the number of times specified
     * Any node that falls off the end of the list as it
     * moves forward will be placed the front of the list
     * For example: [1, 2, 3, 4, 5], numMoves = 2 -> [4, 5, 1, 2 ,3]
     * For example: [1, 2, 3, 4, 5], numMoves = 4 -> [2, 3, 4, 5, 1]
     * For example: [1, 2, 3, 4, 5], numMoves = 7 -> [4, 5, 1, 2 ,3]
     * 
     * @param numMoves the number of times to move each node.
     * @precondition numMoves >= 0
     * @postcondition iterator position unchanged (i.e. still referencing
     *                the same node in the list, regardless of new location of Node)
     * @throws IllegalArgumentException when numMoves < 0
     */
    public void spinList(int numMoves) throws IllegalArgumentException {
        if (numMoves < 0) {
            throw new IllegalArgumentException("numMoves cannot be negative");
        }

        if (first == null || first == last || numMoves == 0) {
            return; // no need to spin if list is empty, has one element, or no moves
        }

        numMoves = numMoves % length; // optimize the number of moves to within the list length
        if (numMoves == 0) {
            return; // no need to spin if numMoves is a multiple of length
        }

        for (int i = 0; i < numMoves; i++) {
            Node temp = last;
            removeLast();
            addFirst(temp.data);
        }
    }

    /**
     * Splices together two LinkedLists to create a third List
     * which contains alternating values from this list
     * and the given parameter
     * For example: [1,2,3] and [4,5,6] -> [1,4,2,5,3,6]
     * For example: [1, 2, 3, 4] and [5, 6] -> [1, 5, 2, 6, 3, 4]
     * For example: [1, 2] and [3, 4, 5, 6] -> [1, 3, 2, 4, 5, 6]
     * 
     * @param list the second LinkedList
     * @return a new LinkedList, which is the result of
     *         interlocking this and list
     * @postcondition this and list are unchanged
     */
    public LinkedList<T> altLists(LinkedList<T> list) {
        LinkedList<T> result = new LinkedList<>();

        if (list == null || list.first == null) {
            return this;
        }

        if (first == null) {
            return list;
        }

        Node currentThis = first;
        Node currentList = list.first;
        while (currentThis != null || currentList != null) {
            if (currentThis != null) {
                result.addLast(currentThis.data);
                currentThis = currentThis.next;
            }
            if (currentList != null) {
                result.addLast(currentList.data);
                currentList = currentList.next;
            }
        }
        return result;
    }

    
    /**
     * Returns each element in the LinkedList along with its numerical position from 1 to n, 
     * followed by a newline. Returns the numbered LinkedList elements as a String.
     * 
     * @return a String with the elements of the LinkedList, numbered from 1 to n
     */
    public String numberedListString() {
        StringBuilder result = new StringBuilder();
        Node temp = first;
        int count = 1;
        while (temp != null) {
            result.append(count + ". " + temp.data + "\n");
            temp = temp.next;
            count++;
        }
        return result.toString() + "\n";
    }

    /** MORE METHODS */

    /**
     * Searches the LinkedList for a given element's index
     * 
     * @param data the data whose index to locate
     * @return the index of the data or -1 if the data is not contained in the LinkedList
     */
    public int findIndex(T data) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.data == data || current.data.equals(data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Advances the iterator to location within the LinkedList specified by the given index
     * 
     * @param index the index at which to place the iterator
     * @throws IndexOutOfBoundsException when index is out of bounds
     */
    public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        positionIterator();
        for (int i = 0; i < index; i++) {
            advanceIterator();
        }
    }

}
