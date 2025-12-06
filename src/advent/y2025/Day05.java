package advent.y2025;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day05 {
    private static final List<String>  SAMPLE = Arrays.asList(
            "3-5",
            "10-14",
            "16-20",
            "12-18",
            "",
            "1",
            "5",
            "8",
            "11",
            "17",
            "32");

    private static record Range(BigInteger lo, BigInteger hi) {}

    public static void main(String[] args) {
        List<Range> freshRanges = parseRanges(SAMPLE);
        List<BigInteger> ingredients = parseIngredients(SAMPLE);

        Util.log("part 1 SAMPLE found %d fresh ingredients", countFresh(ingredients, freshRanges));
        Util.log("part 2 SAMPLE found %d fresh ingredient IDs", countFreshIDs(freshRanges));

        Util.log("------");

        List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day05.txt");
        freshRanges = parseRanges(puzzle);
        ingredients = parseIngredients(puzzle);

        Util.log("part 1 puzzle found %d fresh ingredients", countFresh(ingredients, freshRanges));
        Util.log("part 2 puzzle found %d fresh ingredient IDs", countFreshIDs(freshRanges));
    }

    private static BigInteger countFreshIDs(List<Range> freshRanges) {
        Collections.sort(freshRanges, new Comparator<Range>() {
            @Override
            public int compare(Range o1, Range o2) {
                return o1.lo().compareTo(o2.lo());
            }
        });

        List<Range> ranges = buildNonOverlappingRanges(freshRanges);

        BigInteger result = BigInteger.ZERO;
        for (Range range : ranges) {
            result = result.add(range.hi()).subtract(range.lo()).add(BigInteger.ONE);
        }
        return result;
    }

    private static List<Range> buildNonOverlappingRanges(List<Range> freshRanges) {
        List<Range> ranges = new ArrayList<>();         // the non-overlapping set of ranges
        ranges.add(freshRanges.getFirst());

        // range is sorted by lo(), so we only need to adjust ranges.getLast()'s upper bound (maybe) for each new
        // entry from freshRanges
        for (Range range : freshRanges) {
            if (range.lo().compareTo(ranges.getLast().hi()) > 0) {
                ranges.add(range);
                continue;
            }
            // range impinges on ranges.last, so...
            Range oldLast = ranges.removeLast();
            ranges.add(new Range(oldLast.lo(), max(oldLast.hi(), range.hi())));
        }
        return ranges;
    }

    private static BigInteger max(BigInteger i0, BigInteger i1) {
        return i0.compareTo(i1) >= 0 ? i0 : i1;
    }

    private static List<Range> parseRanges(List<String> raw) {
        List<Range> result = new ArrayList<>();
        for (String range : raw) {
            if (range.equals("")) {
                break;
            }
            String[] limits = range.split("-");
            result.add(new Range(new BigInteger(limits[0]), new BigInteger(limits[1])));
        }
        return result;
    }

    private static List<BigInteger> parseIngredients(List<String> raw) {
        boolean doneSkipping = false;
        List<BigInteger> result = new ArrayList<>();
        for (String ingredient : raw) {
            if (! doneSkipping) {
                doneSkipping = ingredient.equals("");
                continue;
            }
            result.add(new BigInteger(ingredient));
        }
        return result;
    }

    private static int countFresh(List<BigInteger> ingredients, List<Range> freshRanges) {
        int freshCount = 0;
        for (BigInteger ingredient : ingredients) {
            for (Range range : freshRanges) {
                if (inRange(range, ingredient)) {
                    ++freshCount;
                    break;
                }
            }
        }
        return freshCount;
    }

    private static boolean inRange(Range range, BigInteger ingredient) {
        if (ingredient.equals(range.lo) || ingredient.equals(range.hi())
                || (ingredient.compareTo(range.lo()) > 0 && ingredient.compareTo(range.hi()) < 0)) {
            return true;
        }
        return false;
    }


}
