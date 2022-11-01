/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] probs;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        probs = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                while (perc.isOpen(row, col)) {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                }
                perc.open(row, col);
            }
            probs[i] = perc.numberOfOpenSites() / (double) (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(probs);
    }

    public double stddev() {
        return StdStats.stddev(probs);
    }

    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(probs.length);
    }

    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(probs.length);
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats
                .confidenceHi() + "]");
    }
}
