/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStatsTiiming {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        Stopwatch stopwatch = new Stopwatch();
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("time elapsed: " + stopwatch.elapsedTime() + " seconds.");
    }
}
