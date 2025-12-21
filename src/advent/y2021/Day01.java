package advent.y2021;

import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day01 {
    private static final List<String> SAMPLE = Arrays.asList("199", "200", "208", "210", "200", "207", "240", "269", "260", "263");

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2021/Day01.txt");
        Util.log("Loaded %d entries from Day01.txt", puzzle.size());

        Util.log("part 1 sample increses = %d", solve(SAMPLE, 1, true));
        Util.log("part 1 puzzle increses = %d", solve(puzzle, 1, false));

        Util.log("---------");

        Util.log("part 2 sample increases = %d", solve(SAMPLE, 3, true));
        Util.log("part 2 puzzle increases = %d", solve(puzzle, 3, false));
    }

    private static Object solve(List<String> in, int window, boolean logStatus) {
        int[] depths = new int[in.size()];
        int i = 0;
        for (String line : in) {
            depths[i++] = Integer.parseInt(line);
        }

        int[] windowedDepths = new int[depths.length - window + 1];
        for (i = 0; i < windowedDepths.length; ++i) {
            int sum = 0;
            for (int j = 0; j < window; ++j) {
                sum += depths[i + j];
            }
            windowedDepths[i] = sum;
            if (logStatus) {
                Util.log("windowedDepths[%d] = %d", i, sum);
            }
        }

        int previous = Integer.MAX_VALUE;

        int increases = 0;
        for (i = 0; i < windowedDepths.length; ++i){
            int current = windowedDepths[i];
            if (current > previous) {
                ++increases;
            }
            previous = current;
        }
        return increases;
    }

}
