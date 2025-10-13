package advent.y2016;

import java.util.HashSet;
import java.util.Set;

public class Day18 {
    private static final String EXAMPLE1 = "..^^.";
    private static final String EXAMPLE2 = ".^^.^.^^^^";
    private static final String INPUT =
            ".^.^..^......^^^^^...^^^...^...^....^^.^...^.^^^^....^...^^.^^^...^^^^.^^.^.^^..^.^^^..^^^^^^.^^^..^";

    private static final boolean PRINT_FLOOR = true;

    public static void main(String[] args) {
        System.out.println("EX 1: ");
        solve(EXAMPLE1, 3, PRINT_FLOOR);

        System.out.println("\nEX 2: ");
        solve(EXAMPLE2, 10, PRINT_FLOOR);

        System.out.println("\npart 1: ");
        solve(INPUT, 40, ! PRINT_FLOOR);

        System.out.println("\npart 2: ");
        solve(INPUT, 400000, ! PRINT_FLOOR);

    }

    public static void solve(final String row, int nRows, boolean printFloor) {
        String r = row;
        if (printFloor) {
            System.out.println(r);
        }
        int nsafe = countSafe(r);
        for (int i = 0; i < nRows - 1; ++i) {
            r = nextRow(r);
            if (printFloor) {
                System.out.println(r);
            }
            nsafe += countSafe(r);
        }
        System.out.println("found " + nsafe + " safe tiles.");
    }

    private static int countSafe(String row) {
        int result = 0;
        for (int i = 0; i<row.length(); ++i) {
            if (row.charAt(i) == '.') {
                ++result;
            }
        }
        return result;
    }

    private static final Set<String> trapCells = new HashSet<>();
    static {
        trapCells.add("^^.");
        trapCells.add(".^^");
        trapCells.add("^..");
        trapCells.add("..^");
    }

    public static String nextRow(String row) {
        String r = "." + row + ".";
        StringBuilder newRow = new StringBuilder();
        for (int i = 1; i <= row.length(); ++i) {
            if (trapCells.contains(r.substring(i-1,i+2))) {
                newRow.append("^");
            }
            else {
                newRow.append(".");
            }
        }
        return newRow.toString();
    }
}
