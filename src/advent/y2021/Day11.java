package advent.y2021;

import advent.Util;

public class Day11 {
    public static void main(String[] args) {
        Util.log("part 1 sample flashed %d times in 100 cycles", flashes(parse(SAMPLE), 100));
        Util.log("part 1 puzzle flashed %d times in 100 cycles", flashes(parse(PUZZLE), 100));
        Util.log("----------");
        Util.log("part 2 sample");
        flashes(parse(SAMPLE), 200);
        Util.log("part 2 puzzle");
        flashes(parse(PUZZLE), 400);
    }


    private static int[][] parse(String[] in) {
        int[][] map = new int[in.length][in[0].length()];
        for (int row = 0; row < in.length; ++row) {
            for (int col = 0; col < in[0].length(); ++col) {
                map[row][col] = in[row].charAt(col) - '0';
            }
        }
        return map;
    }


    private static long flashes(int[][] map, int cycles) {
        long flashes = 0;
        for (int i = 0; i < cycles; ++i) {
            // bump values
            for (int row = 0; row < map.length; ++row) {
                for (int col = 0; col < map[0].length; ++col) {
                    ++map[row][col];
                }
            }
            boolean flash = false;
            do {
                flash = false;
                // flash
                for (int row = 0; row < map.length; ++row) {
                    for (int col = 0; col < map[0].length; ++col) {
                        if (map[row][col] > 9) {
                            incrementAdjacent(row, col, map);
                            map[row][col] = Integer.MIN_VALUE;
                            flash = true;
                        }
                    }
                }
            } while (flash);

            // reset energy
            boolean allFlashed = true;
            for (int row = 0; row < map.length; ++row) {
                for (int col = 0; col < map[0].length; ++col) {
                    if (map[row][col] < 0) {
                        map[row][col] = 0;
                        ++flashes;
                    } else {
                        allFlashed = false;
                    }
                }
            }
            if (allFlashed) {
                Util.log("All flashed at %d", i+1);
            }
        }
        return flashes;
    }


    private static void incrementAdjacent(int row, int col, int[][] map) {
        for (int r = row - 1; r <= row + 1; ++r) {
            for (int c = col - 1; c <= col + 1; ++c) {
                if ((r != row || c != col) && r >= 0 && r < map.length &&  c >= 0 && c < map[0].length) {
                    ++map[r][c];
                }
            }
        }
    }


    private static String[] PUZZLE = {
            "7147713556",
            "6167733555",
            "5183482118",
            "3885424521",
            "7533644611",
            "3877764863",
            "7636874333",
            "8687188533",
            "7467115265",
            "1626573134"
    };
    private static String[] SAMPLE = {
            "5483143223",
            "2745854711",
            "5264556173",
            "6141336146",
            "6357385478",
            "4167524645",
            "2176841721",
            "6882881134",
            "4846848554",
            "5283751526"
    };
}
