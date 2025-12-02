package advent.y2025;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day02 {
    private static final String SAMPLE = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,"
            + "1698522-1698528,446443-446449,38593856-38593862,565653-565659,"
            + "824824821-824824827,2121212118-2121212124";

    private static final String PUZZLE = "4077-5314,527473787-527596071,709-872,2487-3128,6522872-6618473,"
            + "69137-81535,7276-8396,93812865-93928569,283900-352379,72-83,7373727756-7373754121,41389868-41438993,"
            + "5757-6921,85-102,2-16,205918-243465,842786811-842935210,578553879-578609405,9881643-10095708,"
            + "771165-985774,592441-692926,7427694-7538897,977-1245,44435414-44469747,74184149-74342346,"
            + "433590-529427,19061209-19292668,531980-562808,34094-40289,4148369957-4148478173,67705780-67877150,"
            + "20-42,8501-10229,1423280262-1423531012,1926-2452,85940-109708,293-351,53-71";

    private static final boolean PART1 = true;
    private static final boolean PART2 = !PART1;

    public static void main(String[] args) {
        Instant start = Instant.now();
        Util.log("part 1 sample sum of invalid IDs = %d; %s", solve(parse(SAMPLE), PART1), Util.elapsed(start));
        start = Instant.now();
        Util.log("part 1 puzzle sum of invalid IDs = %d; %s", solve(parse(PUZZLE), PART1), Util.elapsed(start));

        Util.log("----------");

        start = Instant.now();
        Util.log("part 2 sample sum of invalid IDs = %d; %s", solve(parse(SAMPLE), PART2), Util.elapsed(start));
        start = Instant.now();
        Util.log("part 2 puzzle sum of invalid IDs = %d; %s", solve(parse(PUZZLE), PART2), Util.elapsed(start));
    }

    private static record Range(String lo, String hi) {
    }

    private static long solve(List<Range> ranges, boolean part1) {
        long sum = 0L;
        for (Range range : ranges) {
            long lo = Long.valueOf(range.lo);
            long hi = Long.valueOf(range.hi);
            for (long v = lo; v <= hi; ++v) {
                if (is_invalid(part1, v)) {
                    sum += v;
                }
            }
        }

        return sum;
    }

    private static final int[] TWO = {2};

    private static boolean is_invalid(boolean part1, long v) {
        String vStr = Long.toString(v);

        int[] lengthDivisors = part1 ? TWO : findLengthDivisors(vStr);

        for (int divisor : lengthDivisors) {
            String segment = vStr.substring(0, vStr.length() / divisor);
            if (repeat(segment, divisor).equals(vStr)) {
                return true;
            }
        }
        return false;
    }

    private static String repeat(String segment, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; ++i) {
            result.append(segment);
        }
        return result.toString();
    }

    private static int[] findLengthDivisors(String s) {
        List<Integer> result = new ArrayList<>();

        for (int i = 2; i <= s.length(); ++i) {
            if (s.length() % i == 0) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    private static List<Range> parse(String in) {
        List<Range> result = new ArrayList<>();
        for (String range : in.split(",")) {
            String[] limits = range.split("-");
            result.add(new Range(limits[0], limits[1]));
        }
        return result;
    }

}
