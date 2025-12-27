package advent.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

        public Digit(int value, Set<Character> segments) {
            this.value = value;
            this.segments = new HashSet<>(segments);
        }

        public boolean equals(Set<Character> candidate) {
            return this.segments.equals(candidate);
        }

        public int getValue() {
            return this.value;
        }
    }

    private static class Decoder {
        private Set<Digit> digits = new HashSet<>();
        public int decode(Set<Character> segments) {
            for (Digit digit : digits) {
                if (digit.equals(segments)) {
                    return digit.getValue();
                }
            }
            // throw new IllegalStateException("Unknown digit: " + segments);
            return -1;
        }
        public void addDigit(Digit digit) {
            this.digits.add(digit);
        }
    }

    public static void main(String[] args) {
        Util.log("%s", decode(SAMPLE1));
        int count = 0;
        for (String s : SAMPLE2) {
            Util.log("%s", decode(s));
            count += decode(s).size();
        }
        Util.log("part 1 SAMPLE1 found %d simple digits", count);

        count = 0;
        for (String s : FileIO.getFileAsList("src/advent/y2021/Day08.txt")) {
            count += decode(s).size();
        }
        Util.log("part 1 puzzle: found %d simple digits", count);
    }

    private static Set<Character> toSet(String s){
        return new HashSet<>(s.chars().mapToObj(c -> (char) c).toList());
    }

    private static List<Integer> decode(String raw) {
        String[] chunks = raw.split(" ");

        Decoder d = new Decoder();

        d.addDigit(new Digit(1, toSet(find(chunks,2).get(0))));
        d.addDigit(new Digit(7, toSet(find(chunks,3).get(0))));
        d.addDigit(new Digit(4, toSet(find(chunks,4).get(0))));
        d.addDigit(new Digit(8, toSet(find(chunks,7).get(0))));

        List<Integer> data = new ArrayList<>();
        for (int i = 11; i < chunks.length; ++i) {
            int v = d.decode(toSet(chunks[i]));
            if (v > 0) {
                data.add(v);
            }
        }
        return data;
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

    private final static String SAMPLE1 = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf";
    private final static List<String> SAMPLE2 = Arrays.asList(
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
