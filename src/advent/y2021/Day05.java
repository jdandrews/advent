package advent.y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import advent.FileIO;
import advent.Util;

public class Day05 {

    public record Position(int x, int y) {}
    public record Segment(int x0, int y0, int x1, int y1) {

        public boolean isVertical() {
            return y0 == y1;
        }

        public boolean isHorizontal() {
            return x0 == x1;
        }

        /**
         * Generates a list of points approximating a straight line between two given endpoints
         * using the Bresenham line algorithm.
         *
         * @param x1 The x-coordinate of the start point.
         * @param y1 The y-coordinate of the start point.
         * @param x2 The x-coordinate of the end point.
         * @param y2 The y-coordinate of the end point.
         * @return A List of Point objects on the line.
         */
        public List<Position> getLine(){
            List<Position> points = new ArrayList<>();

            int xa = x0;
            int ya = y0;
            int xb = x1;
            int yb = y1;

            int dx = Math.abs(xb - xa);
            int dy = Math.abs(yb - ya);
            int sx = (xa < xb) ? 1 : -1; // Direction to move in x
            int sy = (ya < yb) ? 1 : -1; // Direction to move in y
            int err = dx - dy; // Initial decision parameter/error term

            while (true) {
                points.add(new Position(xa, ya)); // Plot the current point

                if (xa == xb && ya == yb) {
                    break;
                }

                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    xa += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    ya += sy;
                }
            }
            return points;
        }
    }

    public static void main(String[] args) {
        List<String> sample = FileIO.getFileAsList("src/advent/y2021/Day05sample.txt");
        Util.log("Loaded %d entries from Day04sample.txt", sample.size());

        List<String> puzzle = FileIO.getFileAsList("src/advent/y2021/Day05.txt");
        Util.log("Loaded %d entries from Day05.txt", puzzle.size());

        Util.log("part 1 sample overlaps = %d", computeOverlaps(sample, false, true));
        Util.log("---------");
        Util.log("part 2 sample overlaps = %d", computeOverlaps(sample, true, true));

        Util.log("~~~~~~~~~~");

        Util.log("part 1 puzzle overlaps = %d", computeOverlaps(puzzle, false, false));
        Util.log("part 2 puzzle overlaps = %d", computeOverlaps(puzzle, true, false));
    }

    private static int computeOverlaps(List<String> input, boolean includeDiagonals, boolean logProgress) {
        List<Segment> segments = parse(input);
        int[][] map = zero(
                segments.stream().mapToInt(v -> v.x0() > v.x1() ? v.x0() : v.x1()).max().orElseThrow(),
                segments.stream().mapToInt(v -> v.y0() > v.y1() ? v.y0() : v.y1()).max().orElseThrow());

        for (Segment segment : segments) {
            if (includeDiagonals || segment.isHorizontal() || segment.isVertical()) {
                for (Position p : segment.getLine()) {
                    ++map[p.x()][p.y()];
                }
            }
        }

        if (logProgress) {
            log(map);
        }

        return count(map, v -> v > 1);
    }

    private static int count(int[][] map, Function<Integer, Boolean> f) {
        int count = 0;
        for (int i = 0; i<map.length; ++i) {
            for (int j = 0; j<map[0].length; ++j) {
                if (f.apply(map[i][j])){
                    ++count;
                }
            }
        }
        return count;
    }

    private static final String BRIGHT_GREEN_FG =   "\033[92m";
    private static final String RESET_FG =  "\33[39m";
    public static void log(int[][] map) {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                boolean hit = map[row][col] > 1;
                result
                .append(hit ? BRIGHT_GREEN_FG : "")
                .append(String.format("%3d ", map[row][col]))
                .append(hit ? RESET_FG : "");
            }
            result.append("\n");
        }
        System.out.println(result.toString());
    }

    private static int[][] zero(int maxX, int maxY) {
        int[][] map = new int[maxX+1][maxY+1];
        for (int i = 0; i<map.length; ++i) {
            for (int j = 0; j<map[0].length; ++j) {
                map[i][j] = 0;
            }
        }
        return map;
    }

    private static List<Segment> parse(List<String> input) {
        List<Segment> result = new ArrayList<>();
        // 72,504 -> 422,154
        for (String s : input) {
            String[] chunks = s.split(" ");

            String[] s0 = chunks[0].split(",");
            String[] s1 = chunks[2].split(",");

            result.add(new Segment(
                    Integer.parseInt(s0[0]), Integer.parseInt(s0[1]), Integer.parseInt(s1[0]), Integer.parseInt(s1[1])));

        }
        return result;
    }
}

