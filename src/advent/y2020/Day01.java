package advent.y2020;

import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day01 {
    private static final List<String> SAMPLE = Arrays.asList("1721", "979", "366", "299", "675", "1456");

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2020/Day01.txt");

        Util.log("part 1 SAMPLE result = %d", solve(SAMPLE));
        Util.log("part 1 puzzle result = %d", solve(puzzle));

        Util.log("------------");

        Util.log("part 2 SAMPLE result = %d", solve2(SAMPLE));
        Util.log("part 2 puzzle result = %d", solve2(puzzle));
    }

    private static Object solve2(List<String> puzzle) {
        for (int i = 0; i < puzzle.size() - 2; ++i) {
            long n = Long.valueOf(puzzle.get(i));
            for (int j = i + 1; j < puzzle.size() - 1; ++j) {
                long m = Long.valueOf(puzzle.get(j));
                for (int k = j + 1; k < puzzle.size(); ++k) {
                    long o = Long.valueOf(puzzle.get(k));
                    if (o + n + m == 2020L) {
                        return o * m * n;
                    }
                }
            }
        }
        return -1;
    }

    private static long solve(List<String> puzzle) {
        for (int i = 0; i < puzzle.size() - 1; ++i) {
            long n = Long.valueOf(puzzle.get(i));
            for (int j = i + 1; j < puzzle.size(); ++j) {
                long m = Long.valueOf(puzzle.get(j));
                if (n + m == 2020L) {
                    return m * n;
                }
            }
        }
        return -1;
    }
}
