import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * Created by olehgol on 30.04.17.
 */
public class KdTree {
    private Node root;
    private int n;

    public KdTree() {
    } // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    } // is the set empty?

    public int size() {
        return n;
    } // number of points in the set

    public void insert(Point2D p) {
        throwOnNullInput(p);
        if (isEmpty()) {
            root = new Node(p, Orientation.VERTICAL);
            root.rect = new RectHV(0, 0, 1, 1);
        } else {
            root = insert(root, Orientation.VERTICAL, p);
        }
        n++;
    } // add the point to the set (if it is not already in the set)


    private Node insert(Node n, Orientation orientation, Point2D p) {
        if (n == null) {
            return new Node(p, orientation);
        }

        if (n.point.equals(p)) {
            return n;
        }
        int cmp = compare(p, n.point, orientation);
        if (cmp < 0) {
            n.left = insert(n.left, orientation.next(), p);
            if (n.left.rect == null) {
                if (orientation == Orientation.VERTICAL) {
                    n.left.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.point.x(), n.rect.ymax());
                } else {
                    n.left.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.point.y());
                }
            }
        } else {
            n.right = insert(n.right, orientation.next(), p);
            if (n.right.rect == null) {
                if (orientation == Orientation.VERTICAL) {
                    n.right.rect = new RectHV(n.point.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
                } else {
                    n.right.rect = new RectHV(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.rect.ymax());
                }
            }
        }

        return n;
    }


    public boolean contains(Point2D p) {
        return contains(root, p);

    } // does the set contain point point?

    private boolean contains(Node n, Point2D p) {
        if (n == null) {
            return false;
        }
        if (n.point.equals(p)) {
            return true;
        }
        int cmp = compare(p, n.point, n.orientation);
        if (cmp < 0) {
            return contains(n.left, p);
        } else {
            return contains(n.right, p);
        }
    }

    public void draw() {
        draw(root, Orientation.VERTICAL);
    } // draw all points to standard draw

    private void draw(Node n, Orientation orientation) {
        if (n == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();
        StdDraw.setPenRadius(0.001);
        if (orientation == Orientation.HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
        } else {
            StdDraw.setPenColor(StdDraw.RED);
        }
        n.rect.draw();
        orientation = orientation.next();
        draw(n.left, orientation);
        draw(n.right, orientation);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> points = new ArrayList<>();
        range(points, root, rect);
        if (points.size() == 0) {
            return null;
        }
        return points;
    } // all points that are inside the rectangle

    private void range(ArrayList<Point2D> points, Node n, RectHV rect) {
        if (n == null) {
            return;
        }
        if (!rect.intersects(n.rect)) {
            return;
        }
        if (rect.contains(n.point)) {
            points.add(n.point);
        }
        range(points, n.left, rect);
        range(points, n.right, rect);
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        return nearest(root, p, root.point, Double.MAX_VALUE, Orientation.VERTICAL);
    } // a nearest neighbor in the set to point point; null if the set is empty

    private Point2D nearest(Node n, Point2D p, Point2D nearest, double nearestDistance, Orientation orientation) {
        if (n == null) {
            return nearest;
        }

        Point2D closest = nearest;
        double closestDistance = nearestDistance;
        double distance = n.point.distanceSquaredTo(p);
        if (distance < nearestDistance) {
            closest = n.point;
            closestDistance = distance;
        }

        Node first, second;
        if (orientation == Orientation.VERTICAL) {
            if (p.x() < n.point.x()) {
                first = n.left;
                second = n.right;
            } else {
                first = n.right;
                second = n.left;
            }
        } else {
            if (p.y() < n.point.y()) {
                first = n.left;
                second = n.right;
            } else {
                first = n.right;
                second = n.left;
            }
        }

        Orientation nextOrientation = orientation.next();
        if (first != null && first.rect.distanceSquaredTo(p) < closestDistance) {
            closest = nearest(first, p, closest, closestDistance, nextOrientation);
            closestDistance = closest.distanceSquaredTo(p);
        }
        if (second != null
                && second.rect.distanceSquaredTo(p) < closestDistance) {
            closest = nearest(second, p, closest, closestDistance, nextOrientation);
        }

        return closest;
    }

    private int compare(Point2D p, Point2D q, Orientation orientation) {
        if (orientation == Orientation.VERTICAL) {
            return Double.compare(p.x(), q.x());
        } else {
            return Double.compare(p.y(), q.y());
        }
    }

    private class Node {
        Point2D point;
        RectHV rect;
        Node left;
        Node right;
        Orientation orientation;

        Node(Point2D point, Orientation orientation) {
            this.point = point;
            this.orientation = orientation;
        }
    }

    private enum Orientation {
        HORIZONTAL,
        VERTICAL;

        public Orientation next() {
            if (this.equals(Orientation.HORIZONTAL)) {
                return Orientation.VERTICAL;
            }
            return Orientation.HORIZONTAL;
        }
    }

    private void throwOnNullInput(Object input) {
        if (input == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.7, 0.2));
        t.insert(new Point2D(0.5, 0.4));
        t.insert(new Point2D(0.2, 0.3));
        t.insert(new Point2D(0.4, 0.7));
        t.insert(new Point2D(0.9, 0.6));
        t.draw();
        boolean contains = t.contains(new Point2D(0.4, 0.7));
        contains = t.contains(new Point2D(0.1, 0.1));
    } // unit testing of the methods (optional)
}
