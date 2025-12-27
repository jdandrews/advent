package advent.y2021;

import java.util.Arrays;
import java.util.function.Function;

import advent.FileIO;
import advent.Util;

public class Day07 {

    private static final String[] SAMPLE = "16,1,2,0,4,2,7,1,2,14".split(",");
    private static final String[] PUZZLE = FileIO.getFileAsString("src/advent/y2021/Day07.txt").split(",");
    public static void main(String[] args) {
        Util.log("part 1 sample day minimum fuel = %d", getMinimumTotalFuel(SAMPLE, true, v -> v));
        Util.log("part 1 puzzle day minimum fuel = %d", getMinimumTotalFuel(PUZZLE, false, v -> v));
        Util.log("----------");
        Util.log("part 2 sample day minimum fuel = %d", getMinimumTotalFuel(SAMPLE, true, v -> sumOfOneToN(v)));
        Util.log("part 2 puzzle day minimum fuel = %d", getMinimumTotalFuel(PUZZLE, false, v -> sumOfOneToN(v)));
    }

    private static Object getMinimumTotalFuel(String[] in, boolean logProgress, Function<Integer, Integer> fuelForDX) {
        int[] start = Arrays.stream(in).mapToInt(Integer::parseInt).toArray();

        int totalFuel = Integer.MAX_VALUE;
        int maxX = Arrays.stream(start).max().orElseThrow();
        int minX = Arrays.stream(start).min().orElseThrow();
        int minPosition = 0;

        for (int x = minX; x <= maxX; ++x) {
            int f = totalFuel(start, x, fuelForDX);
            if (f < totalFuel) {
                totalFuel = f;
                minPosition = x;
            } else if (f == totalFuel) {
                Util.log("dupe max = %d at %d", f, x);
            }
            if (logProgress) {
                Util.log("p = %d; fuel = %d;", x, f);
            }
        }

        Util.log("p = %d; fuel = %d;", minPosition, totalFuel);
        return totalFuel;
    }
    private static int totalFuel(int[] start, int position, Function<Integer, Integer> fuelForDX) {
        return Arrays.stream(start)
                .map(v -> v - position > 0 ? v - position : position - v)
                .map(v -> fuelForDX.apply(v))
                .sum();
    }

    private static int sumOfOneToN(int i) {
        return (i * i + i) / 2;
    }
}
