package advent.y2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;

public class Day04 {
    private record Point(int row, int col) {
    }

    public static void main(String[] args) {
        System.out.println("SAMPLE: found " + find(Arrays.asList(SAMPLE)) + " XMASs.");
        System.out.println("part 1: found " + find(FileIO.getFileAsList("src/advent/y2024/Day04.txt")) + " XMASs.");
        System.out.println("SAMPLE2: found " + findXs(Arrays.asList(SAMPLE)) + " XMASs.");
        System.out.println("part 2: found " + findXs(FileIO.getFileAsList("src/advent/y2024/Day04.txt")) + " XMASs.");
    }

    private static int findXs(List<String> list) {
        int count = 0;
        for (int row = 1; row < list.size() - 1; ++row) {
            for (int col = 1; col < list.get(0).length() - 1; ++col) {
                Point origin = new Point(row, col);

                if (letterAt(origin, list) != 'A')
                    continue;

                List<Point> mList = findAdjacent('M', origin, list);
                // remove points that fall on the same row or column as the 'A', so we only get "X"'s.
                List<Point> removeMe = new ArrayList<>();
                for (Point p : mList) {
                    if (p.row == row || p.col == col)
                        removeMe.add(p);
                }

                mList.removeAll(removeMe);
                // if we only have 1 or 0 then we can't have an X
                // if we have more than 2, then both ends of one of the diagonals must be an "M"
                if (mList.size() != 2)
                    continue;

                // at this point, we have 2 legs of an X started, and need to check the other end
                Point x0 = extendSegment(mList.get(0), origin);
                Point x1 = extendSegment(mList.get(1), origin);
                if (letterAt(x0, list) == 'S' && letterAt(x1, list) == 'S') {
                    ++count;
                }
            }
        }
        return count;
    }

    private static int find(List<String> list) {
        int count = 0;
        for (int row = 0; row < list.size(); ++row) {
            for (int col = 0; col < list.get(row).length(); ++col) {
                if (list.get(row).charAt(col) != 'X')
                    continue;

                for (Point p : findAdjacent('M', new Point(row, col), list)) {
                    Point origin = new Point(row, col);
                    Point nextLocation = extendSegment(origin, p);
                    Point lastLocation = extendSegment(p, nextLocation);
                    if (letterAt(nextLocation, list) == 'A' && letterAt(lastLocation, list) == 'S') {
                        ++count;
                    }
                }
            }
        }
        return count;
    }

    private static Point extendSegment(Point p1, Point p2) {
        int drow = p2.row - p1.row;
        int dcol = p2.col - p1.col;
        return new Point(p2.row + drow, p2.col + dcol);
    }

    private static List<Point> findAdjacent(char findMe, Point origin, List<String> list) {
        List<Point> result = new ArrayList<>();
        for (int r = origin.row - 1; r <= origin.row + 1; ++r) {
            for (int c = origin.col - 1; c <= origin.col + 1; ++c) {
                Point p = new Point(r, c);
                if ((c != origin.col || r != origin.row) && letterAt(p, list) == findMe) {
                    result.add(p);
                }
            }
        }

        return result;
    }

    private static char letterAt(Point p, List<String> list) {
        if (p.row < 0 || p.col < 0 || p.row >= list.size() || p.col >= list.get(0).length()) {
            return 0;
        }
        return list.get(p.row).charAt(p.col);
    }

    private static String[] SAMPLE = {
            "MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX"
    };
}
