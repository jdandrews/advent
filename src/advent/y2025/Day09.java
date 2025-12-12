package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;
import advent.Util.Point;

public class Day09 {
    private static final List<Point> SAMPLE = Util.getIntegerPoints(Arrays.asList(
            "7,1", "11,1", "11,7", "9,7", "9,5", "2,5", "2,3", "7,3"), ",");

    private static List<Point> puzzle = Util.getIntegerPoints(FileIO.getFileAsList("src/advent/y2025/Day09.txt"), ",");

    private record Segment(Point a, Point b) {
        public boolean isVertical() {
            return a.x() == b.x();
        }
        public boolean isHorizonal() {
            return a.y() == b.y();
        }
        public int minX() {
            return a.x() < b.x() ? a.x() : b.x();
        }
        public int maxX() {
            return a.x() > b.x() ? a.x() : b.x();
        }
        public int minY() {
            return a.y() < b.y() ? a.y() : b.y();
        }
        public int maxY() {
            return a.y() > b.y() ? a.y() : b.y();
        }
    }

    public static void main(String[] args) {
        Util.log("largest SAMPLE rect is %d; expected 50", largestRectangle(SAMPLE));
        Util.log("largest puzzle rect is %d", largestRectangle(puzzle));

        List<Segment> segments = getBoundingSegments(SAMPLE);
        Util.log("segments: %s", segments);
        Util.log("largest SAMPLE rect is %d; expected 24", largestRectangleInside(segments, SAMPLE));
        segments = getBoundingSegments(puzzle);
        Util.log("largest puzzle rect is %d", largestRectangleInside(segments, puzzle));
    }

    private static Object largestRectangleInside(List<Segment> segments, List<Point> points) {
        long max = 0L;
        for (int i = 0; i < points.size()-1; ++i) {
            for (int j = i + 1; j < points.size(); ++j) {
                if (rectangleArea(points.get(i), points.get(j)) == 24) {
                    Util.log("found area 24 rectangle %s, %s", points.get(i), points.get(j));
                }
                if (rectangleCrosses(segments, points.get(i), points.get(j))) {
                    continue;
                }
                long oldMax = max;
                max = Math.max(max,  rectangleArea(points.get(i), points.get(j)));
                if (oldMax < max) {
                    Util.log("new max rect size %d with corners %s, %s", max, points.get(i), points.get(j));
                }

            }
        }

        return max;
    }

    // true if a rectangle contains any segment endpoint, else false
    // not a complete solution, but let's see where it gets us.
    private static boolean rectangleCrosses(List<Segment> segments, Point p1, Point p2) {
        Point lowerLeft = p1.x() < p2.x() ? p1 : p2;
        Point upperRight = p1.x() < p2.x() ? p2 : p1;

        for (Segment s : segments) {
            if (overlaps(s, lowerLeft, upperRight)) {
                return true;
            }
        }
        return false;
    }

    // true if the rectangle contains either segment endpoint.
    // true if the segment runs through the rectangle, too.
    private static boolean overlaps(Segment s, Point lowerLeft, Point upperRight) {
        if (s.minX() > lowerLeft.x()
                && s.minY() > lowerLeft.y()
                && s.minX() < upperRight.x()
                && s.minY() < upperRight.y()) {
            return true;
        }
        else if (s.maxX() > lowerLeft.x()
                && s.maxY() > lowerLeft.y()
                && s.maxX() < upperRight.x()
                && s.maxY() < upperRight.y()) {
            return true;
        }
        else if (s.isHorizonal()
                && s.minX() < lowerLeft.x()
                && s.maxX() > upperRight.x()
                && s.minY() > lowerLeft.y()
                && s.minY() < upperRight.y()) {
            return true;
        }
        else if (s.isVertical()
                && s.minX() > lowerLeft.x()
                && s.minX() < upperRight.x()
                && s.minY() < lowerLeft.y()
                && s.maxY() > upperRight.y()) {
            return true;
        }
        return false;
    }

    private static List<Segment> getBoundingSegments(List<Point> points) {
        List<Segment> segments = new ArrayList<>();
        for(int i = 0; i<points.size(); ++i) {
            segments.add(new Segment(points.get(i), points.get((i+1)%points.size())));
        }

        assert segments.size() == points.size();

        return segments;
    }

    private static long largestRectangle(List<Point> points) {
        long max = 0L;
        for (int i = 0; i < points.size()-1; ++i) {
            for (int j = i; j < points.size(); ++j) {
                max = Math.max(max,  rectangleArea(points.get(i), points.get(j)));
            }
        }
        return max;
    }

    private static long rectangleArea(Point corner1, Point corner2) {
        long dx = corner1.x() - corner2.x()  + 1;
        long dy = corner1.y() - corner2.y()  + 1;

        return Math.abs(dx * dy);
    }
}
