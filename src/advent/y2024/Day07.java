package advent.y2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day07 {
    private static class Equation {
        long value;
        long[] operands;

        public Equation(String s) {
            String[] blocks = s.split(" ");
            value = Long.valueOf(blocks[0].substring(0, blocks[0].length() - 1));
            operands = new long[blocks.length - 1];
            for (int i = 1; i < blocks.length; ++i)
                operands[i - 1] = Long.valueOf(blocks[i]);
        }

        public int nOperators() {
            return operands.length - 1;
        }

        public boolean resolves(String[] operators) {
            if (operators.length != nOperators()) {
                throw new UnsupportedOperationException(
                        String.format("Requires %d operators; found %d", nOperators(), operators.length));
            }
            long result = operands[0];
            for (int i = 0; i < operators.length; ++i) {
                switch (operators[i]) {
                case "+":
                    result += operands[i + 1];
                    break;
                case "*":
                    result *= operands[i + 1];
                    break;
                case "||":
                    result = Long.valueOf(String.format("%d%d", result, operands[i + 1]));
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported operator: " + operators[i]);
                }
            }
            return result == value;
        }
    }

    public static void main(String[] args) {
        Util.log("Part 1 Sample results = %d", sumPossibleTestValues(loadEquations(Arrays.asList(SAMPLE)), PART_1_OPERATORS));
        Util.log("Part 1 results = %d",
                sumPossibleTestValues(loadEquations(FileIO.getFileAsList("src/advent/y2024/Day07.txt")), PART_1_OPERATORS));
        Util.log("Part 2 Sample results = %d", sumPossibleTestValues(loadEquations(Arrays.asList(SAMPLE)), PART_2_OPERATORS));
        Util.log("Part 2 results = %d",
                sumPossibleTestValues(loadEquations(FileIO.getFileAsList("src/advent/y2024/Day07.txt")), PART_2_OPERATORS));
    }

    private static final String[] PART_1_OPERATORS = { "+", "*" };
    private static final String[] PART_2_OPERATORS = { "+", "*", "||" };

    private static long sumPossibleTestValues(List<Equation> equations, String[] allOperators) {
        long sum = 0;

        for (Equation e : equations) {
            long maxPermutations = (long)Math.pow(allOperators.length, e.nOperators()) - 1;
            String format = "%" + (Long.toString(maxPermutations, allOperators.length).length()) + "s";
            String[] operators = new String[e.nOperators()];

            // 1 << n -> 2**n
            for (int p = 0; p <= maxPermutations; ++p) {
                // generate 0-padded base 2 or 3 permuation string
                String permutation = String.format(format, Long.toString(p, allOperators.length)).replace(" ", "0");
                for (int n = 0; n < permutation.length(); ++n) {
                    operators[n] = allOperators[Integer.valueOf(permutation.substring(n, n + 1))];
                }

                if (e.resolves(operators)) {
                    sum += e.value;
                    break;
                }
            }
        }
        return sum;
    }

    private static List<Equation> loadEquations(List<String> rawData) {
        List<Equation> equations = new ArrayList<>();
        for (String e : rawData) {
            equations.add(new Equation(e));
        }
        return equations;
    }

    private static final String[] SAMPLE = { "190: 10 19", "3267: 81 40 27", "83: 17 5", "156: 15 6", "7290: 6 8 6 15",
            "161011: 16 10 13", "192: 17 8 14", "21037: 9 7 18 13", "292: 11 6 16 20" };
}
