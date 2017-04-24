import java.util.ArrayList;

public final class Board {
    private final int[][] blocks; // the size must be such that math.sqrt return integer value

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
                if (blocks[row][col] == 0) {
                    continue;
                }
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
                if (blocks[row][col] == 0) {
                    continue;
                }
                correctPos = correctPosition(blocks[row][col]);
                sum += Math.abs(row - correctPos.row) + Math.abs(col - correctPos.col);
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
        Position firstNonZero = null;
        Position secondNonZero;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] != 0) {
                    if (firstNonZero == null) {
                        firstNonZero = new Position(row, col);
                    } else {
                        secondNonZero = new Position(row, col);
                        int[][] newBlocks = copy(blocks);
                        exchange(newBlocks, firstNonZero, secondNonZero);
                        return new Board(newBlocks);
                    }
                }
            }
        }
        return null;
    }                    // a board that is obtained by exchanging any pair of blocks

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (getClass() != y.getClass()) {
            return false;
        }
        Board yBoard = (Board) y;
        if (yBoard.dimension() != this.dimension()) {
            return false;
        }
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] != yBoard.blocks[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Position zeroPosition = zeroPosition();

        ArrayList<Board> neighbors = new ArrayList<>();

        Position[] newZeroPositions = {
                new Position(zeroPosition.row - 1, zeroPosition.col),
                new Position(zeroPosition.row, zeroPosition.col - 1),
                new Position(zeroPosition.row + 1, zeroPosition.col),
                new Position(zeroPosition.row, zeroPosition.col + 1),
        };

        for (Position newZeroPosition : newZeroPositions) {
            if (!newZeroPosition.inBound(dimension())) {
                continue;
            }

            int[][] newBlocks = copy(blocks);
            exchange(newBlocks, zeroPosition, newZeroPosition);
            neighbors.add(new Board(newBlocks));
        }

        return neighbors;
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

    private Position zeroPosition() {
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] == 0) {
                    return new Position(row, col);
                }
            }
        }
        return new Position(-1, -1);
    }

    private static void exchange(int[][] blocks, Position pos1, Position pos2) {
        int v = blocks[pos1.row][pos1.col];
        blocks[pos1.row][pos1.col] = blocks[pos2.row][pos2.col];
        blocks[pos2.row][pos2.col] = v;
    }

    private static int[][] copy(int[][] src) {
        int length = src.length;
        int[][] res = new int[length][src[0].length];
        for (int row = 0; row < length; row++) {
            System.arraycopy(src[row], 0, res[row], 0, src[row].length);
        }
        return res;
    }

    private class Position {
        int row;
        int col;

        Position(int r, int c) {
            row = r;
            col = c;
        }

        public boolean inBound(int dimension) {
            return (row >= 0 && row < dimension && col >= 0 && col < dimension);
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

        int[][] board1 = {
                {8, 1, 3},
                {4, 2, 0},
                {7, 6, 5}
        };
        Board b1 = new Board(board1);
        Iterable<Board> neighbors = b1.neighbors();
        int a = 1;

        int[][] board3 = {
                {1, 0, 3},
                {4, 2, 5},
                {7, 8, 6}
        };
        Board b3 = new Board(board3);

        int abcd = b3.manhattan();
        int d = 0;
    } // unit tests (not graded)
}
