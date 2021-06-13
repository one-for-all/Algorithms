/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int count;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        Node node = new Node();
        node.item = item;
        if (first == null) {
            first = node;
            last = node;
        }
        else {
            Node oldFirst = first;
            oldFirst.prev = node;
            node.next = oldFirst;
            first = node;
        }
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        Node node = new Node();
        node.item = item;
        if (last == null) {
            first = node;
            last = node;
        }
        else {
            Node oldLast = last;
            oldLast.next = node;
            node.prev = oldLast;
            last = node;
        }
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        
    }

    // remove and return the item from the back
    public Item removeLast()

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()

    // unit testing (required)
    public static void main(String[] args) {

    }
}
