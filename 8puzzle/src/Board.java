/**
 * Created by olehgol on 16.04.17.
 */
public class Board {
    private int[][] blocks; // the size must be such that math.sqrt return integer value

    public Board(int[][] blocks) {
        this.blocks = new int[blocks.length][];
        for (int i = 0; i < blocks.length; i++) {
            this.blocks[i] = new int[blocks.length];
            for (int j = 0; j < blocks[i].length; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }       // construct a board from an n-by-n array of blocks

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return blocks.length;
    }            // board dimension n

    public int hamming() {
        int sum = 0;
        Position correctPos;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                correctPos = correctPosition(blocks[row][col]);
                if (row != correctPos.row || col != correctPos.col) {
                    sum++;
                }
            }
        }
        return sum;
    }               // number of blocks out of place

    public int manhattan() {
        int sum = 0;
        Position correctPos;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                correctPos = correctPosition(blocks[row][col]);
                sum += (row - correctPos.row) + (col - correctPos.col);
            }
        }
        return sum;
    }                // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        Position correctPos;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                correctPos = correctPosition(blocks[row][col]);
                if (row != correctPos.row || col != correctPos.col) {
                    return false;
                }
            }
        }
        return true;
    }               // is this board the goal board?

    public Board twin() {
        return null;
    }                    // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y) {
        if (getClass() != y.getClass()) {
            return false;
        }
        Board yBoard = (Board) y;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] != yBoard.blocks[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }       // does this board equal y?

    public Iterable<Board> neighbors() {
        return null;
    }   // all neighboring boards

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(dimension());
        res.append("\n");
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (col != 0) {
                    res.append(" ");
                }
                res.append(blocks[row][col]);
            }
            res.append("\n");
        }
        return res.toString();
    }

    private Position correctPosition(int currentValue) {
        if (currentValue == 0) {
            return new Position(dimension() - 1, dimension() - 1);
        }
        return new Position((currentValue - 1) / dimension(), (currentValue - 1) % dimension());
    }

    private class Position {
        int row;
        int col;

        Position(int r, int c) {
            row = r;
            col = c;
        }
    }

    public static void main(String[] args) {
        int[][] board = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board b = new Board(board);
        if (b.isGoal()) {
            System.out.println("GOAL");
        }
        System.out.println(b);

        int[][] board2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 8}
        };
        Board b2 = new Board(board2);
        System.out.println(b.equals(b2));
    } // unit tests (not graded)
}
