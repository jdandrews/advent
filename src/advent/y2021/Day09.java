package advent.y2021;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day09 {
    private static final String BRIGHT_GREEN_FG =   "\033[92m";
    private static final String RESET_FG =  "\33[39m";

    private static record Point(int row, int col, int z) {}

    public static void main(String[] args) throws IOException {
        int[][] map = parse(SAMPLE);
        Util.log("map is %d x %d", map.length, map[0].length);
        List<Point> lowPoints = findLowPoints(map);
        Util.log("sample sum of low point z = %d", score(lowPoints));
        print(map, lowPoints);
        List<Integer> poolSizes = new ArrayList<>();
        for (Point p : lowPoints) {
            poolSizes.add(findPool(map, p).size());
        }
        poolSizes.sort(null);
        int n = poolSizes.size() - 1;
        Util.log("part 2 SAMPLE product of 3 largest pool sizes: %s", poolSizes.get(n) * poolSizes.get(n-1) * poolSizes.get(n-2));

        List<String> puzzle = Util.readInput("2021", "Day09.txt");
        map = parse(puzzle);
        Util.log("map is %d x %d", map.length, map[0].length);
        lowPoints = findLowPoints(map);
        Util.log("puzzle sum of low point z = %d", score(lowPoints));

        poolSizes.clear();
        for (Point p : lowPoints) {
            poolSizes.add(findPool(map, p).size());
        }
        poolSizes.sort(null);
        n = poolSizes.size() - 1;
        Util.log("part 2 puzzle product of 3 largest pool sizes: %s", poolSizes.get(n) * poolSizes.get(n-1) * poolSizes.get(n-2));

    }

    private static void print(int[][] grid, List<Point> lowPoints) {
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                System.out.print(grid[row][col] == 0 ? "." : grid[row][col] == 9 ? BRIGHT_GREEN_FG + "#" + RESET_FG: grid[row][col]);
            }
            System.out.println();
        }
    }

    private static int score(List<Point> lowPoints) {
        return lowPoints.stream().mapToInt(v -> v.z() + 1).sum();
    }

    private static Set<Point> findPool(int[][] map, Point lowPoint){
        Set<Point> result = new HashSet<>();
        Deque<Point> stack = new ArrayDeque<>();
        stack.push(lowPoint);
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            if (result.contains(p)) {
                continue;
            }
            result.add(p);
            // N
            if (p.row() > 0 && map[p.row() - 1][p.col()] < 9) {
                stack.push(new Point(p.row() - 1, p.col(), map[p.row() - 1][p.col()]));
            }
            // S
            if (p.row() < map.length - 1 && map[p.row() + 1][p.col()] < 9) {
                stack.push(new Point(p.row() + 1, p.col(), map[p.row() + 1][p.col()]));
            }
            // W
            if (p.col() > 0 && map[p.row()][p.col() - 1] < 9) {
                stack.push(new Point(p.row(), p.col() - 1, map[p.row][p.col() - 1]));
            }
            // E
            if (p.col() < map[0].length-1 && map[p.row()][p.col() + 1] < 9) {
                stack.push(new Point(p.row(), p.col() + 1, map[p.row][p.col() + 1]));
            }
        }
        return result;
    }

    private static List<Point> findLowPoints(int[][] map) {
        List<Point> result = new ArrayList<>();
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                int e = (col == map[0].length - 1 ? Integer.MAX_VALUE : map[row][col+1]);
                int w = (col == 0 ? Integer.MAX_VALUE : map[row][col-1]);
                int n = (row == 0 ? Integer.MAX_VALUE : map[row-1][col]);
                int s = (row == map.length - 1 ? Integer.MAX_VALUE : map[row+1][col]);

                if (map[row][col] < n && map[row][col] < s && map[row][col] < e && map[row][col] < w) {
                    result.add(new Point(row, col, map[row][col]));
                }
            }
        }
        return result;
    }

    private static int[][] parse(List<String> in) {
        int[][] result = new int[in.size()][in.get(0).length()];
        for (int row = 0; row < in.size(); ++row) {
            String r = in.get(row);
            for (int col = 0; col < in.get(0).length(); ++col) {
                result[row][col] = r.charAt(col) - '0';
            }
        }
        return result;
    }

    public static List<String> SAMPLE = Arrays.asList(
            "2199943210",
            "3987894921",
            "9856789892",
            "8767896789",
            "9899965678");
}
