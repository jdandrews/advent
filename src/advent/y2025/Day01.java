package advent.y2025;

import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day01 {
    private static final List<String> SAMPLE = Arrays.asList("L68","L30","R48","L5","R60","L55","L1","L99","R14","L82");

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day01.txt");
        Util.log("Loaded %d entries from Day01.txt", puzzle.size());

        Util.log("part 1 sample password = %d", solve(50, SAMPLE, true));
        Util.log("part 1 puzzle password = %d", solve(50, puzzle, false));

        Util.log("---------");

        Util.log("part 2 sample password = %d", solve2(50, SAMPLE, true));
        Util.log("part 2 puzzle password = %d", solve2(50, puzzle, false));
    }

    private static Object solve(int initial, List<String> moves, boolean log_progress) {
        int dial = initial;
        int password = 0;
        for (String move : moves) {
            int sign = parseDirection(move);
            int count = parseTickCount(move);

            dial = (dial + 100 + sign * count) % 100;
            if (log_progress) {
                Util.log("dial is at %d", dial);
            }
            if (dial == 0) {
                ++password;
            }
        }
        return password;
    }


    private static Object solve2(int initial, List<String> moves, boolean log_progress) {
        int dial = initial;
        int password = 0;
        for (String move : moves) {
            int sign = parseDirection(move);
            int ticks = parseTickCount(move);

            password += ticks / 100;
            ticks = ticks % 100;

            if (dial != 0 && (dial + sign * ticks >= 100 || dial + sign * ticks <= 0)) {
                ++password;
                if (log_progress) {
                    Util.log("passed zero: ");
                }
            }

            dial = (dial + 100 + sign * ticks) % 100;

            if (log_progress) {
                Util.log("dial is at %d", dial);
            }
        }
        return password;
    }

    private static int parseTickCount(String move) {
        int ticks = Integer.valueOf(move.substring(1));
        return ticks;
    }

    private static int parseDirection(String move) {
        int sign = switch(move.substring(0,1)) {
        case "L" -> -1;
        case "R" -> 1;
        default -> throw new UnsupportedOperationException("?? " + move.substring(0,1));
        };
        return sign;
    }
}
