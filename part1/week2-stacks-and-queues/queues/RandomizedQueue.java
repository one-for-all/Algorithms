/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array = (Item[]) new Object[1];
    private int arraySize = 1;
    private int count = 0;

    public RandomizedQueue() {
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    private void resize(int newSize) {
        /* Pack items into a new array of size newSize
         */

        // Check new array size is enough to put all items
        assert newSize >= count;

        // Create new array with items
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = 0; i < count; i++)
            newArray[i] = array[i];

        array = newArray;
        arraySize = newSize;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        // Resize array if no space at the end
        if (count >= arraySize)
            resize(count * 2);

        // Insert the item at the end
        array[count] = item;
        count++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int randomIdx = StdRandom.uniform(count);
        Item item = array[randomIdx];
        array[randomIdx] = array[count - 1];
        array[count - 1] = null;

        count--;
        if (count <= arraySize / 4)
            resize(arraySize / 2 + 1);

        return item;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        int randomIdx = StdRandom.uniform(count);
        return array[randomIdx];
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int current = 0;
        private final Item[] shuffledArray;

        public ArrayIterator() {
            int[] permutation = StdRandom.permutation(count);
            shuffledArray = (Item[]) new Object[count];
            for (int i = 0; i < count; i++)
                shuffledArray[i] = array[permutation[i]];
        }

        public boolean hasNext() {
            if (current >= count)
                return false;
            return true;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return shuffledArray[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static void testIsEmpty() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        assert queue.isEmpty();
        queue.enqueue(1);
        assert !queue.isEmpty();
        queue.dequeue();
        assert queue.isEmpty();
    }

    private static void testSample() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        assert queue.sample() == 1;
    }

    private static void testDequeue() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        int item = queue.dequeue();
        assert item == 1;
    }

    private static void testMultipleEnqueue() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        for (int i = 0; i < 10; i++) {
            int item = queue.sample();
            assert item == 1 || item == 2 || item == 3;
        }
    }

    private static void testMultipleDequeue() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(5);
        queue.enqueue(10);
        int a = queue.dequeue();
        int b = queue.dequeue();
        int c = queue.dequeue();

        // Sort the values (insertion sort)
        int[] values = { a, b, c };
        for (int i = 1; i < values.length; i++) {
            for (int j = i; j > 0 && values[j] < values[j - 1]; j--) {
                int tmp = values[j - 1];
                values[j - 1] = values[j];
                values[j] = tmp;
            }
        }

        // Check result
        int[] groundTruth = { 1, 5, 10 };
        for (int i = 0; i < values.length; i++)
            assert values[i] == groundTruth[i];
    }

    private static void testIterator() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(5);
        queue.enqueue(10);

        for (int item : queue)
            assert item == 1 || item == 5 || item == 10;
    }

    public static void main(String[] args) {
        testIsEmpty();
        testSample();
        testDequeue();
        testMultipleEnqueue();
        testMultipleDequeue();
        testIterator();
    }
}
