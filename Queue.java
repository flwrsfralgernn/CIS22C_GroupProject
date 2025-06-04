/**
 * The Queue class definition
 * @author Prajwal Agrawal
 * CIS 22C, Lab 5
 * @param <T> the generic data stored in the Queue
 */
import java.util.NoSuchElementException;

// Implement the Queue class using starter code and instructions above.

public class Queue<T> implements Q<T> {
    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private int size;
    private Node front;
    private Node end;

    /**
     * Constructs an empty Queue.
     */
    public Queue() {
        this.size = 0;
        this.front = null;
        this.end = null;
    }

    /**
     * Constructs a Queue from an array of data.
     * @param data the data to store in the Queue
     */
    public Queue(T[] data) {
        this();
        if (data == null) {
            return;
        }

        for (T elem : data) {
            enqueue(elem);
        }
    }

    /**
     * Constructs a copy of the given Queue.
     * @param other the Queue to copy
     */
    public Queue(Queue<T> other) {
        this();
        if (other == null) {
            return;
        }
        
        Node node = other.front;
        while (node != null) {
            enqueue(node.data);
            node = node.next;
        }
    }

    /**
     * Returns the value stored at the front of the Queue.
     * @return the value at the front of the queue
     * @throws NoSuchElementException when the precondition is violated
     */
    public T getFront() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }

    /**
     * Returns the size of the Queue.
     * @return the size from 0 to n
     */
    public int getSize() {
        return size;
    }

    /**
     * Determines whether a Queue is empty.
     * @return whether the Queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Inserts a new value at the end of the Queue.
     * @param data the new data to insert
     * @postcondition a new node at the end of the Queue
     */
    public void enqueue(T data) {
        Node node = new Node(data);
        if (isEmpty()) {
            front = node;
        } else {
            end.next = node;
        }
        end = node;
        size++;
    }

    /**
     * Removes the front element in the Queue.
     * @precondition !isEmpty()
     * @throws NoSuchElementException when the precondition is violated
     * @postcondition the front element has been removed
     */
    public void dequeue() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        front = front.next;
        size--;
    }

    /**
     * Returns a string representation of the Queue.
     * @return a string of the form "A B C D E F G H I J K L \n"
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node node = front;
        while (node != null) {
            sb.append(node.data + " ");
            node = node.next;
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Determines whether two Queues are equal.
     * @param other the other Queue to compare
     * @return whether the two Queues are equal
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Queue)) {
            return false;
        }
        Queue<T> otherQ = (Queue<T>) other;
        if (this.size != otherQ.size) {
            return false;
        }
        Node node = this.front;
        Node otherNode = otherQ.front;
        while (node != null) {
            if (!node.data.equals(otherNode.data)) {
                return false;
            }
            node = node.next;
            otherNode = otherNode.next;
        }
        return true;
    }
}
