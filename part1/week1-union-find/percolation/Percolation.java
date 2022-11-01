/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private byte[] sites;
    private final WeightedQuickUnionUF uf;
    private final int size;
    private int count = 0;
    private boolean percolates = false;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        // Use the first 3 bits of the byte to keep track of
        // whether open, whether connected to top, whether connected to bottom.
        // However, the later 2 info are only correct when the sites are roots.
        sites = new byte[n * n];
        size = n;

        // A union-find structure for connecting sites
        uf = new WeightedQuickUnionUF(n * n);
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
        return (sites[toID(row, col)] >> 7 & 1) == 1;
    }

    public boolean isFull(int row, int col) {
        return isOpen(row, col) && (sites[uf.find(toID(row, col))] >> 6 & 1) == 1;
    }

    public void open(int row, int col) {
        if (isOpen(row, col))
            return;

        int curId = toID(row, col);

        // Set top and bottom connections for top and bottom rows.
        if (row == 1) {
            sites[curId] |= 0x40;
        }
        if (row == size) {
            sites[curId] |= 0x20;
        }

        sites[curId] |= 0x80;
        count++;
        byte curRoot = sites[uf.find(curId)];
        // Connect site above, below, left and right
        if (row > 1 && isOpen(row - 1, col)) {
            curRoot = unionAndUpdateCurRoot(row - 1, col, curId, curRoot);
        }
        if (row < size && isOpen(row + 1, col)) {
            curRoot = unionAndUpdateCurRoot(row + 1, col, curId, curRoot);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            curRoot = unionAndUpdateCurRoot(row, col - 1, curId, curRoot);
        }
        if (col < size && isOpen(row, col + 1)) {
            curRoot = unionAndUpdateCurRoot(row, col + 1, curId, curRoot);
        }

        if ((curRoot >> 5 & 7) == 7) {
            percolates = true;
        }
    }

    private byte unionAndUpdateCurRoot(int neiRow, int neiCol, int curId, byte curRoot) {
        int neiId = toID(neiRow, neiCol);
        byte neiRoot = sites[uf.find(neiId)];

        uf.union(curId, neiId);

        int curRootId = uf.find(curId);
        sites[curRootId] |= curRoot | neiRoot;
        return sites[curRootId];
    }

    public int numberOfOpenSites() {
        return count;
    }

    public boolean percolates() {
        return percolates;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(StdIn.readString());
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int row = Integer.parseInt(StdIn.readString());
            int col = Integer.parseInt(StdIn.readString());
            percolation.open(row, col);
        }
        boolean percolates = percolation.percolates();
        StdOut.println("Percolates: " + percolates);
    }
}
