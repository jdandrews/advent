package advent.y2024;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import advent.Util;

public class Day11 {
    private static final long[] SAMPLE = { 125, 17 };
    private static final long[] INPUT = { 5, 127, 680267, 39260, 0, 26, 3553, 5851995 };

    private enum PRINT {
        YES, NO;
    }

    public static void main(String[] args) {
        List<Long> result;

        result = evolve(asLongList(SAMPLE), 6, PRINT.YES);
        result = evolve(asLongList(SAMPLE), 25, PRINT.NO);
        Util.log("SAMPLE result after 25 iterations - %d stones.", result.size());

        result = evolve(asLongList(INPUT), 25, PRINT.NO);
        Util.log("Part 1 input result after 25 iterations- %d stones.", result.size());

        long resultSize = evolve2(asLongList(INPUT), 25, PRINT.NO);
        Util.log("Part 2 input result after 25 iterations- %d stones.", resultSize);

        resultSize = evolve2(asLongList(INPUT), 75, PRINT.NO);
        Util.log("Part 2 input result after 75 iterations- %d stones.", resultSize);
    }

    private static List<Long> asLongList(long[] data) {
        List<Long> longList = new ArrayList<>(data.length);
        for (long d : data) {
            longList.add(Long.valueOf(d));
        }
        return longList;
    }

    private static class SubList {
        public List<Long> list;
        public int iterations;
        public SubList(List<Long> list, int iterations) {
            this.list = list;
            this.iterations = iterations;
        }
        @Override
        public String toString() {
            return "Sub:{iterations: " + iterations + "; list of " + list.size() + "; {"
                    + list.get(0) + ", " + list.get(1) + ", " + list.get(2) + ", ... " + list.getLast() + "}}";
        }
    }

    private static long evolve2(List<Long> start, int iterations, PRINT yes) {
        long result = 0;
        Stack<SubList> lists = new Stack<>();
        List<Long> currentList = new ArrayList<>(start);

        lists.push(new SubList(currentList, iterations));

        long resultCounter = 0;
        Instant procStart = Instant.now();
        Instant loopStart = Instant.now();
        while (! lists.isEmpty()) {
            SubList workingSubList = lists.pop();
            List<Long> workingList = workingSubList.list;
            int n0 = workingList.size();
            int n = Math.min(5, n0);
            if (n0 > n) {
                lists.push(new SubList(new ArrayList<>(workingList.subList(n, n0)), workingSubList.iterations));
            }

            workingList = new ArrayList<>(workingList.subList(0, n));
            int workingIterations = Math.min(workingSubList.iterations, 5);

            workingList = evolve(workingList, workingIterations, yes);

            if (workingIterations == workingSubList.iterations) {
                result += workingList.size();

                if (++resultCounter % 100_000_000 == 0) {
                    Instant now = Instant.now();
                    Util.log("| elapsed: %s, last 100M iterations: %s",
                            Duration.between(procStart, now),
                            Duration.between(loopStart, now));
                    loopStart = now;

                } else if (resultCounter % 10000000 == 0) {
                    System.out.print("|");

                } else if (resultCounter % 1000000 == 0) {
                    System.out.print(".");
                }
            } else {
                lists.push(new SubList(workingList, workingSubList.iterations - workingIterations));
            }
        }
        Util.log(" Total duration: %s", Duration.between(procStart, Instant.now()));

        return result;
    }

    private static List<Long> evolve(List<Long> start, int iterations, PRINT yes) {
        List<Long> result = new ArrayList<>(start);
        List<Long> newResult = new ArrayList<>();
        for (int i = 0; i < iterations; ++i) {
            if (PRINT.YES == yes) {
                Util.log("iteration %d: %s", i, result);
            }
            for (Long value : result) {
                if (value == 0) {
                    newResult.add(1L);
                } else if (Long.toString(value).length() % 2 == 0) {
                    String v = Long.toString(value);
                    Long v1 = Long.valueOf(v.substring(0, v.length() / 2));
                    newResult.add(v1);
                    Long v2 = Long.valueOf(v.substring(v.length() / 2));
                    newResult.add(v2);
                } else {
                    newResult.add(value * 2024);
                }
            }
            result = newResult;
            newResult = new ArrayList<>();
        }
        if (PRINT.YES == yes) {
            Util.log("iteration %d: %s", iterations, result);
        }
        return result;
    }

}
