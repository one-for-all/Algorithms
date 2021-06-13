/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] sites;
    private final WeightedQuickUnionUF uf;
    private final int size;
    private final int top;
    private final int bottom;
    private int count = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        sites = new boolean[n][n];
        size = n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sites[i][j] = false;
            }
        }

        // Two extra virtual sites, one connecting to
        // all sites on the top row, and one connecting to
        // all sites on the bottom row.
        uf = new WeightedQuickUnionUF(n * n + 2);
        top = n * n;
        bottom = n * n + 1;
        for (int i = 0; i < n; i++) {
            uf.union(top, i);
        }
        for (int i = n * (n - 1); i < n * n; i++) {
            uf.union(bottom, i);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
        return sites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        return (isOpen(row, col) && uf.find(toID(row, col)) == uf.find(top));
    }

    private int toID(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        sites[row - 1][col - 1] = true;
        count++;
        int cur = toID(row, col);
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(cur, toID(row - 1, col));
        }
        if (row < size && isOpen(row + 1, col)) {
            uf.union(cur, toID(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(cur, toID(row, col - 1));
        }
        if (col < size && isOpen(row, col + 1)) {
            uf.union(cur, toID(row, col + 1));
        }
    }

    public int numberOfOpenSites() {
        return count;
    }

    public boolean percolates() {
        if (size == 1) {
            return numberOfOpenSites() == 1;
        }
        return uf.find(top) == uf.find(bottom);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(StdIn.readString());
        Percolation perc = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int row = Integer.parseInt(StdIn.readString());
            int col = Integer.parseInt(StdIn.readString());
            perc.open(row, col);
        }
        boolean percolates = perc.percolates();
        StdOut.println("Percolates: " + percolates);
    }
}
