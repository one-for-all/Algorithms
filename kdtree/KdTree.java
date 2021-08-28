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
        private final boolean isVertical;
        private Node left, right;

        public Node(Point2D p, boolean isVertical) {
            this.p = p;
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
        root = insert(root, p, true);
    }

    private Node insert(Node x, Point2D p, boolean isVertical) {
        if (x == null) {
            n++;
            return new Node(p, isVertical);
        }

        if (x.p.equals(p)) return x;

        int cmp;
        if (x.isVertical)
            cmp = Double.compare(p.x(), x.p.x());
        else
            cmp = Double.compare(p.y(), x.p.y());

        if (cmp <= 0) x.left = insert(x.left, p, !x.isVertical);
        else x.right = insert(x.right, p, !x.isVertical);

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
        if (x.isVertical)
            cmp = Double.compare(p.x(), x.p.x());
        else
            cmp = Double.compare(p.y(), x.p.y());
        if (cmp <= 0) return contains(x.left, p);
        else return contains(x.right, p);
    }

    public void draw() {
        Queue<RectHV> verticalLines = new Queue<>();
        Queue<RectHV> horizontalLines = new Queue<>();
        draw(root, verticalLines, horizontalLines);
    }

    private void draw(Node x, Queue<RectHV> verticalLines, Queue<RectHV> horizontalLines) {
        if (x == null) return;

        // Draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        // Draw line
        StdDraw.setPenRadius(0.005);
        if (x.isVertical) {
            double yBottom = 0;
            double yTop = 1;

            for (RectHV line : horizontalLines) {
                if (line.xmin() <= x.p.x() && line.xmax() >= x.p.x()) {
                    double yLine = line.ymin();
                    if (yLine <= x.p.y() && yLine > yBottom)
                        yBottom = yLine;
                    else if (yLine >= x.p.y() && yLine < yTop)
                        yTop = yLine;
                }
            }

            StdDraw.setPenColor(StdDraw.RED);

            double pX = x.p.x();
            StdDraw.line(pX, yBottom, pX, yTop);
            verticalLines.enqueue(new RectHV(pX, yBottom, pX, yTop));
        }
        else {
            double xLeft = 0;
            double xRight = 1;

            for (RectHV line : verticalLines) {
                if (line.ymin() <= x.p.y() && line.ymax() >= x.p.y()) {
                    double xLine = line.xmin();
                    if (xLine <= x.p.x() && xLine > xLeft)
                        xLeft = xLine;
                    else if (xLine >= x.p.x() && xLine < xRight)
                        xRight = xLine;
                }
            }

            StdDraw.setPenColor(StdDraw.BLUE);

            double pY = x.p.y();
            StdDraw.line(xLeft, pY, xRight, pY);
            horizontalLines.enqueue(new RectHV(xLeft, pY, xRight, pY));
        }

        draw(x.left, verticalLines, horizontalLines);
        draw(x.right, verticalLines, horizontalLines);
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

        if (pNearest[0] == null || x.p.distanceTo(p) < pNearest[0].distanceTo(p))
            pNearest[0] = x.p;

        double dist2Line = x.isVertical ? x.p.x() - p.x() : x.p.y() - p.y();

        if (dist2Line >= 0) {
            nearest(x.left, p, pNearest);
            if (dist2Line < pNearest[0].distanceTo(p))
                nearest(x.right, p, pNearest);
        }
        else {
            nearest(x.right, p, pNearest);
            if (-dist2Line < pNearest[0].distanceTo(p))
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
