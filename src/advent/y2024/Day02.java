package advent.y2024;

import java.util.List;

import advent.FileIO;

public class Day02 {

    public static void main(String[] args) {
        List<String[]> in = FileIO.getFileLinesSplit("src/advent/y2024/Day02.txt", " ");

        int safeCount = 0;
        for (String[] values : in) {
            if (-1 == isSafe(values)) {
                ++safeCount;
            }
        }

        System.out.println(safeCount + " are safe.");

        safeCount = 0;
        int newSafe = 0;
        for (String[] values : in) {
            if (-1 == isSafe(values)) {
                ++safeCount;
            } else {
                // this brute-forces removing one value at a time; a single "success" stops the loop
                for (int i = 0; i < values.length; ++i) {
                    String[] newValues = new String[values.length - 1];
                    int n = 0;
                    for (int j = 0; j < i; ++j) {
                        newValues[n++] = values[j];
                    }
                    for (int j = i + 1; j < values.length; ++j) {
                        newValues[n++] = values[j];
                    }
                    if (-1 == isSafe(newValues)) {
                        ++newSafe;
                        ++safeCount;
                        break;
                    }
                }
            }
        }
        System.out.println(safeCount + " are safe if the ProblemDampener is engaged (" + newSafe + " new)");
    }

    private static int isSafe(String[] values) {
        int safe = -1;
        boolean descending = false;

        for (int i = 1; i < values.length; ++i) {
            int delta = Integer.valueOf(values[i]) - Integer.valueOf(values[i - 1]);
            if (i == 1) {
                descending = (delta < 0);
            }
            if (Math.abs(delta) > 3 || (delta == 0) || (descending && delta > 0) || ((!descending) && delta < 0)) {
                safe = i;
                break;
            }
        }
        return safe;
    }

}
