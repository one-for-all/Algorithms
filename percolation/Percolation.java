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
    private final WeightedQuickUnionUF fullUF;
    private final WeightedQuickUnionUF percolatesUF;
    private final int size;
    private final int top;
    private final int bottom;
    private int count = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        // Keep track of whether each site is open
        sites = new boolean[n][n];
        size = n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sites[i][j] = false;
            }
        }

        // A union-find structure for checking site fullness
        // with one virtual site connecting the top row
        fullUF = new WeightedQuickUnionUF(n * n + 1);
        top = n * n; // ID of the top site
        for (int i = 1; i <= n; i++)
            fullUF.union(top, toID(1, i));

        // A union-find structure for checking percolation
        // with one top virtual site, and one bottom virtual site
        percolatesUF = new WeightedQuickUnionUF(n * n + 2);
        bottom = n * n + 1; // ID of the bottom site
        for (int i = 1; i <= n; i++) {
            percolatesUF.union(top, toID(1, i));
            percolatesUF.union(bottom, toID(size, i));
        }
    }

    private int toID(int row, int col) {
        // Convert (row, col) to a unique integer representing the ID
        // of the site
        assert row > 0 && row <= size && col > 0 && col <= size;
        return (row - 1) * size + (col - 1);
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
        return sites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        return (isOpen(row, col) && fullUF.find(toID(row, col)) == fullUF.find(top));
    }

    public void open(int row, int col) {
        if (isOpen(row, col))
            return;

        sites[row - 1][col - 1] = true;
        count++;
        int cur = toID(row, col);
        // Connect site above, below, left and right
        if (row > 1 && isOpen(row - 1, col)) {
            fullUF.union(cur, toID(row - 1, col));
            percolatesUF.union(cur, toID(row - 1, col));
        }
        if (row < size && isOpen(row + 1, col)) {
            fullUF.union(cur, toID(row + 1, col));
            percolatesUF.union(cur, toID(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            fullUF.union(cur, toID(row, col - 1));
            percolatesUF.union(cur, toID(row, col - 1));
        }
        if (col < size && isOpen(row, col + 1)) {
            fullUF.union(cur, toID(row, col + 1));
            percolatesUF.union(cur, toID(row, col + 1));
        }

    }

    public int numberOfOpenSites() {
        return count;
    }

    public boolean percolates() {
        if (size == 1) {
            return sites[0][0];
        }
        return percolatesUF.find(top) == percolatesUF.find(bottom);
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
