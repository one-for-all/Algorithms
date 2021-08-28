/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> pointsTree = new SET<>();

    private void checkNull(Object thing) {
        if (thing == null)
            throw new IllegalArgumentException();
    }

    public boolean isEmpty() {
        return pointsTree.isEmpty();
    }

    public int size() {
        return pointsTree.size();
    }

    public void insert(Point2D p) {
        checkNull(p);
        pointsTree.add(p);
    }

    public boolean contains(Point2D p) {
        checkNull(p);
        return pointsTree.contains(p);
    }

    public void draw() {
        for (Point2D p : pointsTree)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);

        Queue<Point2D> q = new Queue<>();
        for (Point2D p : pointsTree) {
            if (rect.contains(p))
                q.enqueue(p);
        }

        return q;
    }

    public Point2D nearest(Point2D p) {
        checkNull(p);

        double minDist = -1;
        Point2D nearestPoint = null;
        for (Point2D pCurrent : pointsTree) {
            double dist = p.distanceSquaredTo(pCurrent);
            if (nearestPoint == null || dist < minDist) {
                nearestPoint = pCurrent;
                minDist = dist;
            }
        }
        return nearestPoint;
    }

    private static void testIsEmpty() {
        PointSET ps = new PointSET();
        assert ps.isEmpty();

        ps.insert(new Point2D(0, 0));
        assert !ps.isEmpty();
    }

    private static void testSize() {
        PointSET ps = new PointSET();
        assert ps.size() == 0;

        ps.insert(new Point2D(0, 0));
        assert ps.size() == 1;
    }

    private static void testInsert() {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0, 0));
        ps.insert(new Point2D(0, 0.5));
        assert ps.size() == 2;
        ps.insert(new Point2D(0, 0));
        assert ps.size() == 2;
    }

    private static void testContains() {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0, 0));
        assert ps.contains(new Point2D(0, 0));
        assert !ps.contains(new Point2D(0, 0.5));

        ps.insert(new Point2D(0, 0.5));
        assert ps.contains(new Point2D(0, 0.5));
    }

    private static void testRange() {
        PointSET ps = new PointSET();
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
        PointSET ps = new PointSET();
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
