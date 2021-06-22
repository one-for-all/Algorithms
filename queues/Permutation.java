/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]); // Number of items to output
        int n = 0; // Number of items we have obtained so far
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String value = StdIn.readString();
            n++;

            if (n <= k) // Insert first k elements
                queue.enqueue(value);
            else {
                // With probability k/n, replace an item in the queue with
                // the new item
                boolean insert = StdRandom.bernoulli((double) k / n);
                if (insert) {
                    queue.dequeue();
                    queue.enqueue(value);
                }
            }
        }

        assert queue.size() == k;
        for (String val : queue)
            StdOut.println(val);
    }
}
