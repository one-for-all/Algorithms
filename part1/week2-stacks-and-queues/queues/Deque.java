/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

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
        if (item == null)
            throw new IllegalArgumentException();

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
        if (item == null)
            throw new IllegalArgumentException();

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
        if (first == null)
            throw new NoSuchElementException();

        Item item = first.item;
        if (first == last) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }
        count--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null)
            throw new NoSuchElementException();

        Item item = last.item;
        if (first == last) {
            first = null;
            last = null;
        }
        else {
            last = last.prev;
            last.next = null;
        }
        count--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null)
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private static void testAddFirst() {
        Deque<Integer> deque = new Deque<>();
        int[] values = { 1, 2, 3 };
        for (int val : values)
            deque.addFirst(val);
        assert deque.size() == values.length;
        int size = deque.size();
        int idx = 0;
        for (int item : deque) {
            assert item == values[size - 1 - idx];
            idx++;
        }
    }

    private static void testAddLast() {
        Deque<Integer> deque = new Deque<>();
        int[] values = { 1, 2, 3 };
        for (int val : values)
            deque.addLast(val);
        int idx = 0;
        for (int item : deque) {
            assert item == values[idx];
            idx++;
        }
    }

    private static void testRemoveFirst() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(5);
        deque.addFirst(6);
        int first = deque.removeFirst();
        assert first == 6;
        int second = deque.removeFirst();
        assert second == 5;
    }

    private static void testRemoveLast() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(5);
        deque.addFirst(6);
        int last = deque.removeLast();
        assert last == 5;
        int secondLast = deque.removeLast();
        assert secondLast == 6;
    }

    private static void testIsEmpty() {
        Deque<Integer> deque = new Deque<>();
        assert deque.isEmpty();
        deque.addFirst(1);
        deque.addLast(6);
        assert !deque.isEmpty();
        deque.removeFirst();
        deque.removeLast();
        assert deque.isEmpty();
    }

    private static void testSize() {
        Deque<Integer> deque = new Deque<>();
        assert deque.size() == 0;
        deque.addLast(5);
        assert deque.size() == 1;
        deque.addFirst(6);
        assert deque.size() == 2;
    }

    private static void testIterator() {
        Deque<Double> deque = new Deque<>();
        Double[] values = { 1.0, 1.5, 2.0 };
        for (double val : values)
            deque.addLast(val);
        int idx = 0;
        for (double item : deque) {
            assert item == values[idx];
            idx++;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        testAddFirst();
        testAddLast();
        testRemoveFirst();
        testRemoveLast();
        testIsEmpty();
        testSize();
        testIterator();
    }
}
