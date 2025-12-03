package advent.y2025;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day03 {
    private static final List<String> SAMPLE = Arrays.asList(
            "987654321111111",
            "811111111111119",
            "234234234234278",
            "818181911112111"
            );

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day03.txt");

        Util.log("part 1 SAMPLE sum of joltages = %s", solve(SAMPLE, 2));
        Util.log("part 1 puzzle sum of joltages = %s", solve(puzzle, 2));
        Util.log("-----------");
        Util.log("part 2 SAMPLE sum of joltages = %s", solve(SAMPLE, 12));
        Util.log("part 2 puzzle sum of joltages = %s", solve(puzzle, 12));
    }

    private static String solve(List<String> batteryBanks, int cells) {
        BigInteger totalJoltage = BigInteger.ZERO;
        for (String bank : batteryBanks) {
            totalJoltage = totalJoltage.add(maxJoltage(bank, cells));
        }
        return totalJoltage.toString();
    }

    private static BigInteger maxJoltage(String bank, int cells) {
        StringBuilder result = new StringBuilder();
        int startIndex = 0;

        for (int cell = cells - 1; cell >= 0; --cell) {
            char digit = '0' - 1;
            for (int i = startIndex; i < bank.length() - cell; ++i) {
                if (bank.charAt(i) > digit) {
                    startIndex = i;
                    digit = bank.charAt(i);
                }
                if (digit == '9') {
                    break;
                }
            }
            result.append(digit);
            ++startIndex;
        }

        return new BigInteger(result.toString());
    }
}
