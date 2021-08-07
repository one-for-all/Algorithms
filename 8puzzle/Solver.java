/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private class SearchNode {
        private final Board board;
        private final int numMoves;
        private final SearchNode prevNode;
        private int hamming = -1;
        private int manhattan = -1;

        public SearchNode(Board cur, int numMoves, SearchNode prevNode) {
            this.board = cur;
            this.numMoves = numMoves;
            this.prevNode = prevNode;
        }

        public int hammingPriority() {
            if (hamming == -1)
                hamming = numMoves + this.board.hamming();
            return hamming;
        }

        public int manhattanPriority() {
            if (manhattan == -1)
                manhattan = numMoves + this.board.manhattan();
            return manhattan;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public Iterable<SearchNode> children() {
            Queue<SearchNode> queue = new Queue<>();
            for (Board nei : board.neighbors())
                if (prevNode == null || !nei.equals(prevNode.board))
                    queue.enqueue(new SearchNode(nei, numMoves + 1, this));
            return queue;
        }

        public int numberOfMoves() {
            return numMoves;
        }

        public Iterable<Board> solution() {
            Stack<Board> stack = new Stack<>();
            SearchNode node = this;
            while (node != null) {
                stack.push(node.board);
                node = node.prevNode;
            }
            return stack;
        }
    }

    private class ByHammingPriority implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            if (a == null || b == null)
                throw new NullPointerException();
            return Integer.compare(a.hammingPriority(), b.hammingPriority());
        }
    }

    private class ByManhattanPriority implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            if (a == null || b == null)
                throw new NullPointerException();
            return Integer.compare(a.manhattanPriority(), b.manhattanPriority());
        }
    }

    private SearchNode solutionNode = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        // A* search from the initial board & twin board
        MinPQ<SearchNode> pq = new MinPQ<>(new ByManhattanPriority());
        pq.insert(new SearchNode(initial, 0, null));

        MinPQ<SearchNode> twinPQ = new MinPQ<>(new ByManhattanPriority());
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));
        while (true) {
            // Search target game tree
            SearchNode node = pq.delMin();
            if (node.isGoal()) {
                this.solutionNode = node;
                break;
            }
            for (SearchNode child : node.children())
                pq.insert(child);

            // Search twin game tree
            SearchNode twinNode = twinPQ.delMin();
            if (twinNode.isGoal()) {
                break; // No solution for original board
            }
            for (SearchNode child : twinNode.children())
                twinPQ.insert(child);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutionNode != null ? solutionNode.numberOfMoves() : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionNode != null ? solutionNode.solution() : null;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
