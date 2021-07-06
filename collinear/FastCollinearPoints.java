/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private final Queue<LineSegment> lineSegments = new Queue<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null)
                throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (p.compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        Point[] aux = points.clone();
        for (Point p : aux) {
            // First sort by compareTo
            Arrays.sort(points);
            // Then sort by slope order to p
            Arrays.sort(points, p.slopeOrder());

            int i = 0;
            while (i < points.length) {
                Point q = points[i];
                if (p.compareTo(q) >= 0) {
                    // Check maximal line segment
                    i++;
                    continue;
                }
                double slopeToQ = p.slopeTo(q);
                if (i > 0 && p.slopeTo(points[i - 1]) == slopeToQ) {
                    // Check not in the middle of line segment
                    i++;
                    continue;
                }

                // Check 4 or more points
                int j = i + 2;
                while (j < points.length && p.slopeTo(points[j]) == slopeToQ)
                    j++;
                if (j - i > 2) {
                    // Found proper line segment!
                    lineSegments.enqueue(new LineSegment(p, points[j - 1]));
                    i = j;
                }
                else
                    i++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
