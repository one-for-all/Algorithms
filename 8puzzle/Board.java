/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final int n;
    private final int[][] tiles;
    private int blankI;
    private int blankJ;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                this.tiles[i][j] = val;
                if (val == 0) {
                    this.blankI = i;
                    this.blankJ = j;
                }
            }
        }
    }

    // The goal value at position (i, j)
    private int goalVal(int i, int j) {
        if (i == n - 1 && j == n - 1)
            return 0;
        return i * n + j + 1;
    }

    private class Position {
        public final int i;
        public final int j;

        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    // The goal position (i, j) of the value
    // (inverse function of goalNumber)
    private Position goalPosition(int val) {
        if (val == 0)
            return new Position(n - 1, n - 1);
        int i = (val - 1) / n;
        int j = (val - 1) % n;
        return new Position(i, j);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int[] arr : tiles) {
            for (int item : arr) {
                s.append(String.format("%2d ", item));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }


    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val != 0 && val != goalVal(i, j))
                    distance++;
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val != 0) {
                    Position goalPos = goalPosition(val);
                    distance += Math.abs(i - goalPos.i) + Math.abs(j - goalPos.j);
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != goalVal(i, j))
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        if (that.n != this.n) return false;

        return Arrays.deepEquals(that.tiles, this.tiles);
    }

    // return the board that would result from moving the tile
    // at position (i, j)
    private Board movefrom(int i, int j) {
        int[][] newTiles = new int[n][n];
        for (int k = 0; k < n; k++)
            for (int m = 0; m < n; m++)
                newTiles[k][m] = tiles[k][m];

        newTiles[i][j] = 0;
        newTiles[blankI][blankJ] = tiles[i][j];
        return new Board(newTiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neis = new Queue<>();
        if (blankI > 0)
            neis.enqueue(movefrom(blankI - 1, blankJ));
        if (blankI < n - 1)
            neis.enqueue(movefrom(blankI + 1, blankJ));
        if (blankJ > 0)
            neis.enqueue(movefrom(blankI, blankJ - 1));
        if (blankJ < n - 1)
            neis.enqueue(movefrom(blankI, blankJ + 1));
        return neis;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = new int[n][n];
        int swapVal = 0;
        int swapVal2 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                twinTiles[i][j] = val;
                if (val == 0)
                    continue;

                if (swapVal == 0) {
                    swapVal = val;
                    if (swapVal < n * n - 1)
                        swapVal2 = swapVal + 1;
                    else
                        swapVal2 = swapVal - 1;
                    twinTiles[i][j] = swapVal2;
                }
                else if (val == swapVal2) {
                    twinTiles[i][j] = swapVal;
                }
            }
        }
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // Create an initial board
        StdOut.println("Initial board:");
        int[][] tiles = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tiles[i][j] = i * 3 + j;
        Board b = new Board(tiles);
        StdOut.println(b.toString());

        // Test dimension()
        assert b.dimension() == 3;

        // Test hamming distance
        assert b.hamming() == 8;

        // Test manhattan distance
        assert b.manhattan() == 12;

        // Test isGoal()
        assert !b.isGoal();

        // Test equals()
        assert b.equals(b);

        // Test twin()
        Board twin = b.twin();
        StdOut.println("Twin board:");
        StdOut.println(twin.toString());
        assert !b.equals(twin);

        // Test neighbors()
        Iterable<Board> neis = b.neighbors();
        StdOut.println("Neighbor boards:");
        for (Board nei : neis)
            StdOut.println(nei.toString());
    }
}
