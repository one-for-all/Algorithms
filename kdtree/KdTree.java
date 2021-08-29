/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private class Node {
        private final Point2D p;
        private final RectHV rect;
        private final boolean isVertical;
        private Node left, right;

        public Node(Point2D p, RectHV rect, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.isVertical = isVertical;
        }
    }

    private Node root;
    private int n = 0;

    private void checkNull(Object thing) {
        if (thing == null)
            throw new IllegalArgumentException();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        checkNull(p);
        root = insert(root, p, null);
    }

    private Node insert(Node x, Point2D p, Node xParent) {
        if (x == null) {
            n++;
            if (xParent == null)
                return new Node(p, new RectHV(0, 0, 1, 1), true);
            else {
                RectHV rectParent = xParent.rect;
                double xmin = rectParent.xmin(), ymin = rectParent.ymin(), xmax = rectParent.xmax(),
                        ymax = rectParent.ymax();

                // Create the proper rectangle
                if (xParent.isVertical && p.x() <= xParent.p.x())
                    xmax = xParent.p.x();
                else if (xParent.isVertical)
                    xmin = xParent.p.x();
                else if (p.y() <= xParent.p.y())
                    ymax = xParent.p.y();
                else
                    ymin = xParent.p.y();

                return new Node(p, new RectHV(xmin, ymin, xmax, ymax), !xParent.isVertical);
            }
        }

        if (x.p.equals(p)) return x;

        int cmp;
        if (x.isVertical) cmp = Double.compare(p.x(), x.p.x());
        else cmp = Double.compare(p.y(), x.p.y());

        if (cmp <= 0) x.left = insert(x.left, p, x);
        else x.right = insert(x.right, p, x);

        return x;
    }

    public boolean contains(Point2D p) {
        checkNull(p);
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;

        if (x.p.equals(p)) return true;

        int cmp;
        if (x.isVertical) cmp = Double.compare(p.x(), x.p.x());
        else cmp = Double.compare(p.y(), x.p.y());

        if (cmp <= 0) return contains(x.left, p);
        else return contains(x.right, p);
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;

        // Draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        // Draw line
        StdDraw.setPenRadius(0.005);
        if (x.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            double pX = x.p.x();
            StdDraw.line(pX, x.rect.ymin(), pX, x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            double pY = x.p.y();
            StdDraw.line(x.rect.xmin(), pY, x.rect.xmax(), pY);
        }

        draw(x.left);
        draw(x.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        Queue<Point2D> q = new Queue<>();
        range(root, rect, q);
        return q;
    }

    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) return;

        if (rect.contains(x.p))
            q.enqueue(x.p);

        if (x.isVertical) {
            if (rect.xmin() <= x.p.x())
                range(x.left, rect, q);
            if (rect.xmax() >= x.p.x())
                range(x.right, rect, q);
        }
        else {
            if (rect.ymin() <= x.p.y())
                range(x.left, rect, q);
            if (rect.ymax() >= x.p.y())
                range(x.right, rect, q);
        }
    }

    public Point2D nearest(Point2D p) {
        checkNull(p);
        Point2D[] pNearest = new Point2D[1];
        nearest(root, p, pNearest);
        return pNearest[0];
    }

    private void nearest(Node x, Point2D p, Point2D[] pNearest) {
        if (x == null) return;

        if (pNearest[0] == null || x.p.distanceSquaredTo(p) < pNearest[0].distanceSquaredTo(p))
            pNearest[0] = x.p;

        double xmin = x.rect.xmin(), ymin = x.rect.ymin(), xmax = x.rect.xmax(),
                ymax = x.rect.ymax();
        RectHV rectLeft;
        RectHV rectRight;
        boolean cmp;
        if (x.isVertical) {
            rectLeft = new RectHV(xmin, ymin, x.p.x(), ymax);
            rectRight = new RectHV(x.p.x(), ymin, xmax, ymax);
            cmp = x.p.x() >= p.x();
        }
        else {
            rectLeft = new RectHV(xmin, ymin, xmax, x.p.y());
            rectRight = new RectHV(xmin, x.p.y(), xmax, ymax);
            cmp = x.p.y() >= p.y();
        }

        // Heuristic for looking left subtree or right subtree first
        if (cmp) {
            if (rectLeft.contains(p) || rectLeft.distanceSquaredTo(p) < pNearest[0]
                    .distanceSquaredTo(p))
                nearest(x.left, p, pNearest);
            if (rectRight.distanceSquaredTo(p) < pNearest[0].distanceSquaredTo(p))
                nearest(x.right, p, pNearest);
        }
        else {
            if (rectRight.contains(p) || rectRight.distanceSquaredTo(p) < pNearest[0]
                    .distanceSquaredTo(p))
                nearest(x.right, p, pNearest);
            if (rectLeft.distanceSquaredTo(p) < pNearest[0].distanceSquaredTo(p))
                nearest(x.left, p, pNearest);
        }
    }

    private static void testIsEmpty() {
        KdTree ps = new KdTree();
        assert ps.isEmpty();

        ps.insert(new Point2D(0, 0));
        assert !ps.isEmpty();
    }

    private static void testSize() {
        KdTree ps = new KdTree();
        assert ps.size() == 0;

        ps.insert(new Point2D(0, 0));
        assert ps.size() == 1;
    }

    private static void testInsert() {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0, 0));
        ps.insert(new Point2D(0, 0.5));
        assert ps.size() == 2;
        ps.insert(new Point2D(0, 0));
        assert ps.size() == 2;
    }

    private static void testContains() {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0, 0));
        assert ps.contains(new Point2D(0, 0));
        assert !ps.contains(new Point2D(0, 0.5));

        ps.insert(new Point2D(0, 0.5));
        assert ps.contains(new Point2D(0, 0.5));
    }

    private static void testRange() {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.5, 0.5));
        ps.insert(new Point2D(0, 0));

        RectHV rect = new RectHV(0.4, 0.4, 0.6, 0.6);
        Iterable<Point2D> intersects = ps.range(rect);
        int count = 0;
        for (Point2D p : intersects) {
            count++;
            assert p.equals(new Point2D(0.5, 0.5));
        }
        assert count == 1;
    }

    private static void testNearest() {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.5, 0.5));
        ps.insert(new Point2D(0, 0));

        Point2D pTest = new Point2D(0.4, 0.4);
        assert ps.nearest(pTest).equals(new Point2D(0.5, 0.5));
    }

    public static void main(String[] args) {
        testIsEmpty();
        testSize();
        testInsert();
        testContains();
        testRange();
        testNearest();
    }
}
