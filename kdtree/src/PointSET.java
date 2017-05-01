import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * Created by olehgol on 30.04.17.
 */
public class PointSET {
    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    } // construct an empty set of points

    public boolean isEmpty() {
        return points.isEmpty();
    } // is the points empty?

    public int size() {
        return points.size();
    } // number of points in the points

    public void insert(Point2D p) {
        throwOnNullInput(p);
        points.add(p);
    } // add the point to the points (if it is not already in the points)

    public boolean contains(Point2D p) {
        throwOnNullInput(p);
        return points.contains(p);
    } // does the points contain point point?

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : points) {
           p.draw();
        }
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        throwOnNullInput(rect);
        ArrayList<Point2D> result = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    } // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        throwOnNullInput(p);
        if (size() == 0) {
            return null;
        }
        Point2D resultP = points.min();
        double minDistance = resultP.distanceTo(p);
        double distance;
        for (Point2D point : points) {
            distance = point.distanceTo(p);
            if (point.distanceTo(p) < minDistance) {
                resultP = p;
                minDistance = distance;
            }
        }
        return resultP;
    } // a nearest neighbor in the points to point point; null if the points is empty

    private void throwOnNullInput(Object input) {
        if (input == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(0.1, 0.1));
        set.insert(new Point2D(0.2, 0.2));
        set.insert(new Point2D(0.6, 0.6));
        set.draw();
    } // unit testing of the methods (optional)
}
