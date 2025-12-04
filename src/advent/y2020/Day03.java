package advent.y2020;

import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day03 {

    private static final List<String> SAMPLE = Arrays.asList(
            "..##.......",
            "#...#...#..",
            ".#....#..#.",
            "..#.#...#.#",
            ".#...##..#.",
            "..#.##.....",
            ".#.#.#....#",
            ".#........#",
            "#.##...#...",
            "#...##....#",
            ".#..#...#.#");

    private static record Slope(int right, int down) {}

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2020/Day03.txt");

        Util.log("part 1 SAMPLE counted %d trees.", countTrees(SAMPLE, 0, 0, 3, 1));
        Util.log("part 1 puzzle counted %d trees.", countTrees(puzzle, 0, 0, 3, 1));

        Util.log("---------------");

        final List<Slope> slopes = Arrays.asList(
                new Slope(1,1),
                new Slope(3,1),
                new Slope(5,1),
                new Slope(7,1),
                new Slope(1,2)
                );

        Util.log("part 2 SAMPLE product of all slopes is %d.", multiplyTrees(SAMPLE, 0, 0, slopes));
        Util.log("part 2 puzzle counted of all slopes is %d.", multiplyTrees(puzzle, 0, 0, slopes));
    }

    private static long multiplyTrees(List<String> map, int startRow, int startCol, List<Slope> slopes) {
        long product = 1L;
        for (Slope slope : slopes) {
            product *= countTrees(map, startRow, startCol, slope.right(), slope.down());
        }
        return product;
    }

    private static int countTrees(List<String> map, int startRow, int startCol, int right, int down) {
        int trees = 0;

        int col = startCol;
        for (int row = startRow; row < map.size(); row += down) {
            String mapRow = map.get(row);
            col = col % mapRow.length();

            if (mapRow.charAt(col) == '#') {
                ++trees;
            }
            col += right;
        }

        return trees;
    }

}
