import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        checkInput(points);

        Arrays.sort(points);

        Point[] pointsForSort = new Point[points.length];
        System.arraycopy(points, 0, pointsForSort, 0, points.length);

        for (Point p : points) {
            System.arraycopy(points, 0, pointsForSort, 0, points.length);
            Arrays.sort(pointsForSort, p.slopeOrder());
            for (int i = 1; i < points.length; i++) {
                if (p.compareTo(pointsForSort[i]) > 0) {
                    continue;
                }
                double basicSlope = p.slopeTo(pointsForSort[i++]);
                int counter = 2; // p itself + point from basic slope
                int lastId = 0;
                for (; i < points.length; i++) {
                    double s = p.slopeTo(pointsForSort[i]);
                    if (Double.compare(s, basicSlope) == 0) {
                        if (p.compareTo(pointsForSort[i]) > 0) {
                            counter = 0;
                            break;
                        }
                        counter++;
                        lastId = i;
                    } else {
                        break;
                    }
                }
                i--;
                if (counter > 3) { // we need at least 4 points for the task
                    segments.add(new LineSegment(p, pointsForSort[lastId]));
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    private void checkInput(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException();
            }
        }

        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("collinear/input9.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
