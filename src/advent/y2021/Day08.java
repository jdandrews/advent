package advent.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.FileIO;
import advent.Util;

/**
 * Scrambled 7-segment display
 *
 * 2 segs -- 1: c, f
 * 3 segs -- 7: a, c, f
 * 4 segs -- 4: b, c, d, f
 * 5 segs -- 2, 3, 5: missing bf, be, ce, respectively
 * 6 segs -- 0, 6, 9: missing d, c, e respectively
 * 7 segs -- 8: a->f:
 */
public class Day08 {
    private static class Digit {
        Set<Character> segments;
        int value;

        public Digit(int digitValue, Set<Character> displaySegments) {
            this.value = digitValue;
            this.segments = new HashSet<>(displaySegments);
        }

        public boolean equals(Set<Character> candidate) {
            return this.segments.equals(candidate);
        }

        public int getValue() {
            return this.value;
        }

        public Set<Character> getSegments() {
            return new HashSet<>(segments);
        }
        @Override
        public String toString() {
            return value + ":" + segments;
        }
    }

    private static class Decoder {
        private Map<Integer, Digit> digits = new HashMap<>();

        public int decode(Set<Character> segments) {
            for (Digit digit : digits.values()) {
                if (digit.equals(segments)) {
                    return digit.getValue();
                }
            }
            throw new IllegalStateException("Unknown digit: " + segments);
        }

        public void addDigit(Digit digit) {
            this.digits.put(digit.getValue(), digit);
        }

        public Digit get(int i) {
            return digits.get(i);
        }
        @Override
        public String toString() {
            return digits.toString();
        }
    }

    public static void main(String[] args) {
        int count = 0;
        int sum = 0;
        for (String s : SAMPLE) {
            Util.log("%s", decode(s));
            int value = 0;
            for (int v : decode(s)) {
                value = value * 10 + v;
                if (v == 1 || v == 4 || v == 7 || v == 8) {
                    ++count;
                }
            }
            sum += value;
        }
        Util.log("part 1 SAMPLE2 found %d simple digits", count);
        Util.log("part 2 SAMPLE2 found sum of digits is %d", sum);

        Util.log("---------");

        count = 0;
        sum = 0;
        for (String s : FileIO.getFileAsList("src/advent/y2021/Day08.txt")) {
            int value = 0;
            for (int v : decode(s)) {
                value = value * 10 + v;
                if (v == 1 || v == 4 || v == 7 || v == 8) {
                    ++count;
                }
            }
            sum += value;
        }
        Util.log("part 1 puzzle: found %d simple digits", count);
        Util.log("part 2 puzzle: found sum of digits is %d", sum);
    }

    private static Set<Character> toSet(String s){
        return new HashSet<>(s.chars().mapToObj(c -> (char) c).toList());
    }

    private static List<Integer> decode(String raw) {
        String[] chunks = raw.split(" ");

        Decoder d = deduceDecoder(chunks);

        // weird remnant: returns the part 1 results
        List<Integer> data = new ArrayList<>();
        for (int i = 11; i < chunks.length; ++i) {
            data.add(d.decode(toSet(chunks[i])));
        }
        return data;
    }

    private static Decoder deduceDecoder(String[] chunks) {
        Decoder d = new Decoder();

        d.addDigit(new Digit(1, toSet(find(chunks,2).get(0))));
        d.addDigit(new Digit(7, toSet(find(chunks,3).get(0))));
        d.addDigit(new Digit(4, toSet(find(chunks,4).get(0))));
        d.addDigit(new Digit(8, toSet(find(chunks,7).get(0))));

        Digit four = d.get(4);
        Digit one = d.get(1);

        // segmentMap indexes:
        //
        //  000
        // 1   2
        //  333
        // 4   5
        //  666
        //
        char[] segmentMap = new char[7];

        List<String> sixes = find(chunks, 6);
        for (String six : sixes) {
            Set<Character> sixSet = toSet(six);
            if (sixSet.containsAll(four.getSegments())){
                d.addDigit(new Digit(9, sixSet));
                segmentMap[4] = getUnusedSegment(sixSet);
                sixes.remove(six);
                break;
            }
        }
        if (sixes.size() == 3) {
            throw new IllegalStateException("didn't find 9");
        }

        for (String six : sixes) {
            Set<Character> sixSet = toSet(six);
            if (sixSet.containsAll(one.getSegments())){
                d.addDigit(new Digit(0, sixSet));
                segmentMap[3] = getUnusedSegment(sixSet);
                sixes.remove(six);
                break;
            }
        }
        if (sixes.size() == 2) {
            throw new IllegalStateException("didn't find 0");
        }

        Set<Character> sixSet = toSet(sixes.get(0));
        d.addDigit(new Digit(6, sixSet));
        segmentMap[2] = getUnusedSegment(sixSet);

        Set<Character> sevenSegs = d.get(7).getSegments();
        sevenSegs.removeAll(one.getSegments());
        segmentMap[0] = sevenSegs.iterator().next();

        Set<Character> oneSegs = one.getSegments();
        oneSegs.remove(segmentMap[2]);
        segmentMap[5] = oneSegs.iterator().next();

        Set<Character> fourSegs = four.getSegments();
        fourSegs.remove(segmentMap[2]);
        fourSegs.remove(segmentMap[3]);
        fourSegs.remove(segmentMap[5]);
        assert fourSegs.size() == 1;
        segmentMap[1] = fourSegs.iterator().next();

        Set<Character> eight = toSet("abcdefg");
        for (char c : segmentMap) {
            eight.remove(c);
        }
        assert eight.size() == 1;
        segmentMap[6] = eight.iterator().next();

        d.addDigit(new Digit(2, new HashSet<>(Arrays.asList(segmentMap[0], segmentMap[2], segmentMap[3], segmentMap[4], segmentMap[6]))));
        d.addDigit(new Digit(3, new HashSet<>(Arrays.asList(segmentMap[0], segmentMap[2], segmentMap[3], segmentMap[5], segmentMap[6]))));
        d.addDigit(new Digit(5, new HashSet<>(Arrays.asList(segmentMap[0], segmentMap[1], segmentMap[3], segmentMap[5], segmentMap[6]))));

        return d;
    }

    private static char getUnusedSegment(Set<Character> set) {
        Set<Character> eight = toSet("abcdefg");
        eight.removeAll(set);
        assert eight.size() == 1;
        return eight.iterator().next();
    }

    private static List<String> find(String[] chunks, int n) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i<10; ++i) {
            if (chunks[i].length() == n) {
                result.add(sorted(chunks[i]));
            }
        }
        return result;
    }

    private static String sorted(String string) {
        char[] letters = string.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    private final static List<String> SAMPLE = Arrays.asList(
            "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
            "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
            "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
            "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
            "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
            "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
            "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
            "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
            "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
            "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce");
}
