/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private final Queue<LineSegment> lineSegments = new Queue<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        int numPoints = points.length;

        for (int i = 0; i < numPoints; i++) {
            Point p = points[i];
            if (p == null)
                throw new IllegalArgumentException();

            // Check repeated points
            for (int j = i + 1; j < numPoints; j++) {
                if (p.compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        // Find all 4-point line segments
        for (int i = 0; i < numPoints - 3; i++) {
            Point p = points[i];
            for (int j = i + 1; j < numPoints - 2; j++) {
                Point q = points[j];
                double slopeToQ = p.slopeTo(q);

                for (int k = j + 1; k < numPoints - 1; k++) {
                    Point r = points[k];
                    double slopeToR = p.slopeTo(r);

                    // Stop early if p, q, r are not collinear
                    if (slopeToQ != slopeToR)
                        continue;

                    for (int m = k + 1; m < numPoints; m++) {
                        Point t = points[m];
                        double slopeToT = p.slopeTo(t);
                        if (slopeToQ == slopeToT) {
                            Point[] linePoints = { p, q, r, t };
                            sort(linePoints);
                            lineSegments.enqueue(new LineSegment(linePoints[0], linePoints[3]));
                        }
                    }
                }
            }
        }
    }

    private void sort(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            for (int j = i; j > 0 && points[j].compareTo(points[j - 1]) < 0; j--) {
                Point tmp = points[j];
                points[j] = points[j - 1];
                points[j - 1] = tmp;
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment seg : lineSegments) {
            segs[i] = seg;
            i++;
        }
        return segs;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
