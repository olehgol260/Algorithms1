import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }

        MinPQ<SearchNode> nodesInitial = new MinPQ<>();
        nodesInitial.insert(new SearchNode(initial));

        MinPQ<SearchNode> nodesTwin = new MinPQ<>();
        nodesTwin.insert(new SearchNode(initial.twin()));

        SearchNode nodeInitial;
        SearchNode nodeTwin;
        while (true) {
            nodeInitial = nodesInitial.delMin();
            if (nodeInitial.board.isGoal()) {
                solution = nodeInitial;
                break;
            }

            nodeTwin = nodesTwin.delMin();
            if (nodeTwin.board.isGoal()) {
                break;
            }

            makeStep(nodeInitial, nodesInitial);
            makeStep(nodeTwin, nodesTwin);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solution.moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? buildSolution(solution) : null;
    }

    private void makeStep(SearchNode currentNode, MinPQ<SearchNode> pq) {
        Iterable<Board> neighbors = currentNode.board.neighbors();
        for (Board neighbor : neighbors) {
            if (currentNode.previous == null || !neighbor.equals(currentNode.previous.board)) {
                pq.insert(new SearchNode(currentNode, neighbor));
            }
        }
    }

    private static Stack<Board> buildSolution(SearchNode node) {
        Stack<Board> solutionStack = new Stack<>();

        while (node != null) {
            solutionStack.push(node.board);
            node = node.previous;
        }

        return solutionStack;
    }

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int moves;
        SearchNode previous;

        SearchNode(Board board) {
            this.board = board;
        }

        SearchNode(SearchNode previous, Board board) {
            this.board = board;
            this.moves = previous.moves + 1;
            this.previous = previous;
        }

        @Override
        public int compareTo(SearchNode searchNode) {
            return (this.board.manhattan() + this.moves) - (searchNode.board.manhattan() + searchNode.moves);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
    }// solve a slider puzzle (given below)
}
