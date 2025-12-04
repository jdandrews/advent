package advent.y2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day02 {
    private static final List<String> SAMPLE = Arrays.asList(
            "1-3 a: abcde",
            "1-3 b: cdefg",
            "2-9 c: ccccccccc");

    private static record Entry(int lo, int hi, char letter, String password) {}

    public static void main(String[] args) {
        List<Entry> sample = parse(SAMPLE);
        List<Entry> puzzle = parse(FileIO.getFileAsList("src/advent/y2020/Day02.txt"));

        Util.log("part 1 sample found %d valid passwords.", countValid(sample));
        Util.log("part 1 puzzle found %d valid passwords.", countValid(puzzle));

        Util.log("----------------");

        Util.log("part 2 sample found %d valid passwords.", countValid2(sample));
        Util.log("part 2 puzzle found %d valid passwords.", countValid2(puzzle));
    }

    private static Object countValid(List<Entry> entries) {
        int nValid = 0;
        for (Entry entry : entries) {
            int nChar = 0;
            for (char c : entry.password.toCharArray()) {
                if (c == entry.letter) {
                    ++nChar;
                }
            }
            if (nChar >= entry.lo && nChar <= entry.hi) {
                ++nValid;
            }
        }
        return nValid;
    }

    private static Object countValid2(List<Entry> entries) {
        int nValid = 0;
        for (Entry entry : entries) {
            boolean p1 = entry.password.charAt(entry.lo - 1) == entry.letter();
            boolean p2 = entry.password.charAt(entry.hi - 1) == entry.letter();

            if (p1 ^ p2) {
                ++nValid;
            }
        }
        return nValid;
    }

    private static List<Entry> parse(List<String> raw) {
        List<Entry> result = new ArrayList<>();
        for (String item : raw) {
            String[] parts = item.split(" ");
            String[] limits = parts[0].split("-");

            result.add(new Entry(Integer.valueOf(limits[0]), Integer.valueOf(limits[1]), parts[1].charAt(0), parts[2]));
        }
        return result;
    }

}
