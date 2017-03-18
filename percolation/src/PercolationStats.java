import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] fractionsOfOpenSites;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        StdRandom.setSeed(System.currentTimeMillis());
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        fractionsOfOpenSites = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }

            fractionsOfOpenSites[i] = ((double) percolation.numberOfOpenSites()) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractionsOfOpenSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractionsOfOpenSites);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt((double) (fractionsOfOpenSites.length)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt((double) (fractionsOfOpenSites.length)));
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        int trials = StdIn.readInt();
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.printf("mean\t= %f\n", percolationStats.mean());
        StdOut.printf("stddev\t= %f\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval\t= [%f, %f]\n", percolationStats.confidenceLo(),
                percolationStats.confidenceHi());
    }
}
