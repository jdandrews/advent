package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day06 {

    private static final List<String> SAMPLE = Arrays.asList(
            "123 328  51 64 ",
            " 45 64  387 23 ",
            "  6 98  215 314",
            "*   +   *   +  ");

    private static class Problem {
        List<Integer> values = new ArrayList<>();
        String operator;

        public void addValue(String value) {
            values.add(Integer.parseInt(value));
        }

        public void addOperator(char op) {
            operator = "" + op;
        }

        public void addOperator(String op) {
            operator = op;
        }

        public long evaluate() {
            long result = values.get(0);
            for (int i=1; i<values.size(); ++i) {
                switch(operator) {
                case "+":
                    result += values.get(i);
                    break;
                case "*":
                    result *= values.get(i);
                    break;
                default:
                    throw new UnsupportedOperationException("unsupported operator: " +  operator);
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return "Problem{" + operator + " " + values + "}";
        }
    }

    public static void main(String[] args) {
        List<Problem> homework = parse(SAMPLE);
        Util.log("part 1 SAMPLE sum of problem results = %d", evaluate(homework));

        homework = parsePart2(SAMPLE);
        Util.log("part 2 SAMPLE sum of problem results = %d", evaluate(homework));

        Util.log("--------");

        List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day06.txt");

        homework = parse(puzzle);
        Util.log("part 1 puzzle sum of problem results = %d", evaluate(homework));

        homework = parsePart2(puzzle);
        Util.log("part 2 puzzle sum of problem results = %d", evaluate(homework));
    }


    private static long evaluate(List<Problem> homework) {
        long sum = 0;
        for (Problem problem : homework) {
            sum += problem.evaluate();
        }
        return sum;
    }

    private static List<Problem> parsePart2(List<String> worksheet) {
        List<Problem> problems = new ArrayList<>();

        String ops = worksheet.getLast();
        int operatorColumn = 0;

        while (operatorColumn < ops.length()) {
            Problem problem = new Problem();
            problem.addOperator(ops.charAt(operatorColumn));
            int loCol = operatorColumn;

            ++operatorColumn;
            while (operatorColumn < ops.length() && ' ' == ops.charAt(operatorColumn)) {
                ++operatorColumn;
            }

            int hiCol = operatorColumn - 1;
            if (operatorColumn == ops.length()) {
                ++hiCol;
            }

            // now we have a bounding rectangle for the current problem; fill it out.
            for (int c = loCol; c < hiCol; ++c) {
                String valueString = "";
                for (int r = 0; r < worksheet.size() - 1; ++r) {
                    char digit = worksheet.get(r).charAt(c);
                    if (digit != ' '){
                        valueString = valueString + digit;
                    }
                }
                problem.addValue(valueString);
            }

            problems.add(problem);
        }

        return problems;
    }

    private static List<Problem> parse(List<String> worksheet) {
        List<Problem> problems = getBlankProblems(worksheet.get(0));
        for (int row = 0; row < worksheet.size() - 1; ++row) {
            String line = worksheet.get(row);
            List<String> values = parseLine(line);
            for (int i = 0; i<problems.size(); ++i) {
                problems.get(i).addValue(values.get(i));
            }
        }
        int idx = 0;
        for (String op : worksheet.getLast().split(" ")) {
            if (! "".equals(op)) {
                problems.get(idx++).addOperator(op);
            }
        }
        return problems;
    }

    private static List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        for (String value : line.split(" ")) {
            if (! "".equals(value)) {
                values.add(value);
            }
        }
        return values;
    }

    private static List<Problem> getBlankProblems(String unparsedLine) {
        List<Problem> problems =  new ArrayList<>();
        for (String v : parseLine(unparsedLine)) {
            problems.add(new Problem());
        }
        return problems;
    }
}
