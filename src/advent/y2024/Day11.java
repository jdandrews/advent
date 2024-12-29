package advent.y2024;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day11 {
    private static final long[] SAMPLE = { 125, 17 };
    private static final long[] INPUT = { 5, 127, 680267, 39260, 0, 26, 3553, 5851995 };
    private static final DecimalFormat FMT = new DecimalFormat("#,###");

    private enum PRINT {
        YES, NO;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        List<Long> result;

        result = evolve(asLongList(SAMPLE), 6, PRINT.YES);
        result = evolve(asLongList(SAMPLE), 25, PRINT.NO);
        Util.log("SAMPLE result after 25 iterations - %d stones.", result.size());
        Util.log("----------");

        result = evolve(asLongList(INPUT), 25, PRINT.NO);
        Util.log("Part 1 input result after 25 iterations- %d stones.", result.size());
        Util.log("----------");

        BigDecimal resultSize = evolve2(asLongList(INPUT), 25, PRINT.NO);
        Util.log("Part 2 input result after 25 iterations- %s stones.", FMT.format(resultSize));
        Util.log("----------");

        for (int blinks = 35; blinks <= 75; blinks += 5) {
            resultSize = evolve2(asLongList(INPUT), blinks, PRINT.NO);
            Util.log("Part 2 input result after %d iterations - %s stones.", blinks, FMT.format(resultSize));
            Util.log("----------");
        }

        Util.log("\nFull run total duration : %s", Duration.between(start, Instant.now()).truncatedTo(ChronoUnit.SECONDS));
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
        public SubList(List<Long> listOfStones, int iterationsRemaining) {
            this.list = listOfStones;
            this.iterations = iterationsRemaining;
        }
        @Override
        public String toString() {
            return "Sub:{iterations: " + iterations + "; list of " + list.size() + "; {"
                    + list.get(0) + ", " + list.get(1) + ", " + list.get(2) + ", ... " + list.getLast() + "}}";
        }
    }

    private static Map<List<Long>, List<Long>> cache = new HashMap<>();
    private static long cacheQueries = 0L;
    private static long cacheMisses = 0L;

    private static final int WORKING_LIST_SIZE = 25;

    private static BigDecimal evolve2(List<Long> start, int iterations, PRINT yes) {
        BigDecimal result = BigDecimal.ZERO;
        Deque<SubList> stack = new ArrayDeque<>();

        for (Long l : start) {
            List<Long> list = new ArrayList<>();
            list.add(l);
            stack.add(new SubList(list, iterations));
        }

        long resultCounter = 0;
        Instant procStart = Instant.now();
        Instant loopStart = Instant.now();
        while (! stack.isEmpty()) {
            SubList workingSubList = stack.pop();
            List<Long> workingList = workingSubList.list;

            // split the working sublist into left and right portions usinng "WORKING_LIST_SIZE" for the size
            // of the left portion.
            int n0 = workingList.size();
            int n = Math.min(WORKING_LIST_SIZE, n0);

            // if we didn't get the whole list in the left-hand portion, save the rest to the stack
            // the next iteration. The stack depth will max out at "iterations" / "workingIterations".
            if (n0 > n) {
                stack.push(new SubList(new ArrayList<>(workingList.subList(n, n0)), workingSubList.iterations));
            }

            workingList = new ArrayList<>(workingList.subList(0, n));

            // each time through the loop, we do this many blinks on the working list.
            int workingIterations = Math.min(workingSubList.iterations, 5);

            if ( ! cache.containsKey(workingList)) {
                cache.put(workingList, evolve(workingList, workingIterations, yes));
                ++cacheMisses;
            }
            workingList = cache.get(workingList);
            ++cacheQueries;

            if (workingIterations != workingSubList.iterations) {
                stack.push(new SubList(workingList, workingSubList.iterations - workingIterations));

            } else {
                // just status reporting.
                result = result.add(BigDecimal.valueOf(workingList.size()));

                if (++resultCounter % 1_000_000_000 == 0) {
                    Instant now = Instant.now();
                    Util.log("| total: %s, last 1B: %s; cache miss: %.2f%% on %d entries.",
                            Duration.between(procStart, now).truncatedTo(ChronoUnit.SECONDS),
                            Duration.between(loopStart, now).truncatedTo(ChronoUnit.MILLIS),
                            100. * cacheMisses / cacheQueries, cache.size());
                    loopStart = now;

                } else if (resultCounter % 100_000_000 == 0) {
                    System.out.print("|");

                } else if (resultCounter % 10_000_000 == 0) {
                    System.out.print(".");
                }
            }
        }
        Util.log("\n Total duration: %s; cache miss: %.4f%% on %d entries.",
                Duration.between(procStart, Instant.now()).truncatedTo(ChronoUnit.MILLIS),
                100. * cacheMisses / cacheQueries, cache.size());

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

                } else if (hasEvenDigitCount(value)) {
                    String v = Long.toString(value);
                    Long v1 = Long.valueOf(v.substring(0, v.length() / 2));
                    newResult.add(v1);
                    Long v2 = Long.valueOf(v.substring(v.length() / 2));
                    newResult.add(v2);

                } else {
                    newResult.add(value * 2024L);
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

    private static boolean hasEvenDigitCount(Long value) {
        if (value < 10L) {
            return false;
        }
        if (value < 100L) {
            return true;
        }
        if (value < 1000L) {    // 14.323586S - start thru part 2 with 40 blinks
            return false;
        }
        if (value < 10000L) {   // 13.862484S
            return true;
        }
        if (value < 100000L) {  // 12.813865S
            return false;
        }
        if (value < 1000000L) { // 12.57413S
            return true;
        }
        // these seem to return roughly equivalent times once you filter out the smaller numbers.
        // Strings are slightly faster when short numbers are included.
        return (Math.floor(Math.log10(value)) + 1) % 2 == 0;
        // return (Long.toString(value).length() % 2 == 0);
    }

}
