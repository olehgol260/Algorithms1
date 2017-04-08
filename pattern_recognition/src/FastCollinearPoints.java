import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();
    private ArrayList<LineVisited> lineVisiteds = new ArrayList<>();
    private final int MINIMUM_COLLINEAR_POINTS_COUNT = 4;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        checkInput(points);

        Arrays.sort(points);

        Point[] pointsForSort = new Point[points.length];
        System.arraycopy(points, 0, pointsForSort, 0, points.length);

        Point from, to;
        for (Point p : points) {
            Arrays.sort(pointsForSort, p.slopeOrder());
            // the first entry of pointsForSort is p itself. So we start iterating from 1.
            for (int i = 1; i < points.length; i++) {
                ArrayList<Point> collinear = new ArrayList<>();
                collinear.add(p);
                collinear.add(pointsForSort[i]);

                double basicSlope = p.slopeTo(pointsForSort[i++]);
                for (; i < points.length; i++) {
                    double s = p.slopeTo(pointsForSort[i]);
                    if (Double.compare(s, basicSlope) == 0) {
                        collinear.add(pointsForSort[i]);
                    } else {
                        break;
                    }
                }
                // the previous for loop will end iterating when there is an end of the points array OR
                // if pointsForSort[i] has another slope as the currently processing order of points.
                // If we meet a new slope, which differs from those in basicSlope, it can also be the beginning
                // of a new series of collinear points.
                // As the loop where i is initialized will make an increment when finish this iteration,
                // thus we can miss the beginning of a new order of collinear points.
                i--;

                if (collinear.size() >= MINIMUM_COLLINEAR_POINTS_COUNT) {
                    Collections.sort(collinear);
                    from = collinear.get(0);
                    to = collinear.get(collinear.size() - 1);
                    if (!exists(from, to)) {
                        lineVisiteds.add(new LineVisited(from, to));
                        segments.add(new LineSegment(from, to));
                    }
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

    // It's a workaround to reach from and to of a line segment and not to break the API.
    // This is used when check if such a line was already saved.
    private class LineVisited {
        Point from;
        Point to;

        LineVisited(Point from, Point to) {
            this.from = from;
            this.to = to;
        }
    }

    private boolean exists(Point from, Point to) {
        for (LineVisited lineVisited : lineVisiteds) {
            if (lineVisited.from.equals(from) && lineVisited.to.equals(to)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("collinear/input8.txt");
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
