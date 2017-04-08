import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException();
            }
        }

        for (int i = 0; i < points.length - 1; i++){
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Point pp;
        for (int p = 0; p < points.length - 3; p++) {
            for (int q = p+1; q < points.length - 2; q++) {
                for (int r = q+1; r < points.length -1; r++) {
                    for (int s = r+1; s < points.length; s++) {
                        pp = points[p];
                        if (pp.slopeTo(points[q]) == 0 && pp.slopeTo(points[r]) == 0 && pp.slopeTo(points[s]) == 0) {
                            segments.add(new LineSegment(pp, points[s]));
                        }
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

    public static void main(String[] args){
        In in = new In("collinear/input6.txt");
        ArrayList<Point> points = new ArrayList<>();
        int n = in.readInt();
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();
            points.add(new Point(x, y));
        }
        BruteCollinearPoints bcp = new BruteCollinearPoints(points.toArray(new Point[0]));
        System.out.println(bcp.numberOfSegments());
    }
}
