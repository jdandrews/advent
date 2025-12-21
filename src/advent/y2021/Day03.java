package advent.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import advent.FileIO;
import advent.Util;

public class Day03 {

    private static final List<String> SAMPLE = Arrays.asList("00100", "11110", "10110", "10111", "10101", "01111",
            "00111", "11100", "10000", "11001", "00010", "01010");

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2021/Day03.txt");
        Util.log("Loaded %d entries from Day03.txt", puzzle.size());

        Util.log("part 1 sample gamma * epsilon = %d", computePowerConsumption(SAMPLE, true));
        Util.log("part 2 sample o2gen * co2scrub = %d", computeLifeSupportRating(SAMPLE, true));

        Util.log("---------");

        Util.log("part 1 puzzle gamma * epsilon = %d", computePowerConsumption(puzzle, false));
        Util.log("part 2 puzzle o2gen * co2scrub = %d", computeLifeSupportRating(puzzle, false));
    }

    private static Object computeLifeSupportRating(List<String> in, boolean logProgress) {
        int o2generator = findRating(in, (v,n) -> v >= n ? '1' : '0', logProgress);
        int co2scrubber = findRating(in, (v,n) -> v < n  ? '1' : '0', logProgress);

        if (logProgress) {
            Util.log("o2gen = %d, co2scrub = %d", o2generator, co2scrubber);
        }

        return o2generator * co2scrubber;
    }

    private static int findRating(List<String> in, BiFunction<Integer, Float, Character> criterion, boolean logProgress) {
        List<String> candidates = new ArrayList<>(in);
        List<String> survivors = new ArrayList<>();

        for (int i = 0; i < in.get(0).length(); ++i) {
            int[] countOnes = countOnes(candidates);

            char keepThisDigit = criterion.apply(countOnes[i], candidates.size()/2f);
            for (String candidate : candidates) {
                if (candidate.charAt(i) == keepThisDigit) {
                    survivors.add(candidate);
                }
            }

            if (survivors.size() == 1) {
                break;
            }

            candidates = survivors;
            survivors = new ArrayList<>();
        }
        if (survivors.size() != 1) {
            throw new IllegalStateException("failed: " + survivors.toString());
        }
        if (logProgress) {
            Util.log("rating %s", survivors.get(0));
        }
        return binaryToInteger(survivors.get(0));
    }

    private static int binaryToInteger(String s) {
        int colValue = 1;
        int result = 0;
        for (int i = s.length() - 1; i >= 0; --i) {
            char c = s.charAt(i);

            result += switch (c) {
            case '1' -> colValue;
            case '0' -> 0;
            default -> throw new UnsupportedOperationException("binary digit " + c + " not supported.");
            };

            colValue *= 2;
        }
        return result;
    }

    private static Object computePowerConsumption(List<String> in, boolean logProgress) {
        int[] countOnes = countOnes(in);

        int gamma = 0;
        int epsilon = 0;
        int colValue = 1;

        for (int i = countOnes.length - 1; i >= 0; --i) {
            gamma += colValue * (countOnes[i] > in.size() / 2 ? 1 : 0);
            epsilon += colValue * (countOnes[i] > in.size() / 2 ? 0 : 1);
            colValue *= 2;
            if (logProgress) {
                Util.log("countOnes[%d] = %d", i, countOnes[i]);
            }
        }

        if (logProgress) {
            Util.log("gamma = %d, epsilon = %d", gamma, epsilon);
        }
        return gamma * epsilon;
    }

    private static int[] countOnes(List<String> in) {
        int[] countOnes = new int[in.get(0).length()];
        for (int i = 0; i < countOnes.length; ++i) {
            countOnes[i] = 0;
        }

        for (String line : in) {
            int i = 0;
            for (char bit : line.toCharArray()) {
                countOnes[i++] += bit == '1' ? 1 : 0;
            }
        }

        return countOnes;
    }

}
