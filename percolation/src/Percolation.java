import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int upVirtualSite;
    private int downVirtualSite;
    private boolean[] opened;
    private int n;
    private int nOpened = 0;
    private WeightedQuickUnionUF weightedQuickUnionUF;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        this.n = n;
        upVirtualSite = 0;
        downVirtualSite = n * n + 1;

        opened = new boolean[n * n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                opened[siteId(i, j)] = false;
            }
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        int currentSiteId = siteId(row, col);

        throwIfOutOfRange(row, col);
        if (row == 1) {
            weightedQuickUnionUF.union(upVirtualSite, currentSiteId);
        }
        if (row == n) {
            weightedQuickUnionUF.union(downVirtualSite, currentSiteId);
        }

        int[][] neighbors = new int[][]{{row - 1, col}, {row, col - 1}, {row + 1, col}, {row, col + 1}};
        for (int[] neighbor : neighbors) {
            int neighborRow = neighbor[0];
            int neighborCol = neighbor[1];

            if (isInRange(neighborRow, neighborCol) && isOpen(neighborRow, neighborCol)) {
                weightedQuickUnionUF.union(currentSiteId, siteId(neighborRow, neighborCol));
            }
        }

        opened[siteId(row, col)] = true;
        nOpened++;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        throwIfOutOfRange(row, col);
        return opened[siteId(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        throwIfOutOfRange(row, col);
        return weightedQuickUnionUF.connected(upVirtualSite, siteId(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return nOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.connected(upVirtualSite, downVirtualSite);
    }

    private void throwIfOutOfRange(int row, int col) {
        if (!isInRange(row, col)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean isInRange(int row, int col) {
        return row > 0 && row <= n && col > 0 && col <= n;
    }

    private int siteId(int row, int col) {
        return n * (row - 1) + col;
    }

    //    private void connectWithNeighbors()
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(1, 1);
        if (percolation.isOpen(1, 1)) {
            System.out.println("OPENED");
        }
        percolation.open(2, 1);
        percolation.open(2, 2);
        percolation.open(3, 2);
        if (percolation.numberOfOpenSites() == 4) {
            System.out.println("numberOfOpenSites is correct");
        }
        if (percolation.isFull(3, 2)) {
            System.out.println("FULL");
        }
        if (percolation.percolates()) {
            System.out.println("PERCOLATES");
        }
    }   // test client (optional)
}
