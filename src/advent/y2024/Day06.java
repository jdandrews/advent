package advent.y2024;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day06 {

    public static void main(String[] args) {
        Util.log("sample solution: %d", solve(Arrays.asList(SAMPLE)));
        Util.log("part 1 solution: %d", solve(FileIO.getFileAsList("src/advent/y2024/Day06.txt")));
        Util.log("part 2 solution: %d", solvePart2(Arrays.asList(SAMPLE)));
        Util.log("part 2 solution: %d", solvePart2(FileIO.getFileAsList("src/advent/y2024/Day06.txt")));
    }

    private static class Guard {
        private int row;
        private int col;
        private char facing = '^';

        public Guard(int r, int c) {
            row = r;
            col = c;
        }

        public Guard(Guard g) {
            this(g.row, g.col);
            this.facing = g.facing;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Guard g) {
                return row == g.row && col == g.col && facing == g.facing;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return row + 1000 * col + 1000000 * facing;
        }

        public boolean isIn(int[][] room) {
            return row >= 0 && row < room.length && col >= 0 && col < room[0].length;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public void step(int[][] room) {
            switch (facing) {
            case '^':
                if (row > 0 && room[row - 1][col] < 0) {
                    facing = '>';
                    ++col;
                } else {
                    --row;
                }
                break;
            case '>':
                if (col + 1 < room[0].length && room[row][col + 1] < 0) {
                    facing = 'v';
                    ++row;
                } else {
                    ++col;
                }
                break;
            case '<':
                if (col > 0 && room[row][col - 1] < 0) {
                    facing = '^';
                    --row;
                } else {
                    --col;
                }
                break;
            case 'v':
                if (row + 1 < room.length && room[row + 1][col] < 0) {
                    facing = '<';
                    --col;
                } else {
                    ++row;
                }
                break;
            default:
                throw new UnsupportedOperationException("direction unknown: " + facing);
            }
        }
    }

    private static class Log extends HashSet<Guard> {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean add(Guard e) {
            Guard g = new Guard(e);
            return super.add(g);
        }
    }

    private static int solve(List<String> input) {
        int[][] room = buildRoom(input);
        Guard guard = findGuard(input);
        return walk(guard, room);
    }

    private static int solvePart2(List<String> input) {
        int loopCount = 0;
        for (int oRow = 0; oRow < input.size(); ++oRow) {
            for (int oCol = 0; oCol < input.get(0).length(); ++oCol) {
                Guard guard = findGuard(input);
                int[][] room = buildRoom(input);
                if ((oRow == guard.getRow() && oCol == guard.getCol()) || room[oRow][oCol] < 0)
                    continue;

                room[oRow][oCol] = -1; // add an obstacle

                if (Integer.MAX_VALUE == walk(guard, room))
                    ++loopCount;
            }
            showProgress(oRow);
        }
        System.out.println("");
        return loopCount;
    }

    private static void showProgress(int oRow) {
        if (oRow % 10 == 9) {
            System.out.print("|");
            if ((oRow + 1) % 50 == 0) {
                System.out.println("");
            }
        } else {
            System.out.print(".");
        }
    }

    private static int walk(Guard guard, int[][] room) {
        return walk(guard, room, null);
    }

    private static int walk(Guard guard, int[][] room, Log optionalLog) {
        Log log = optionalLog == null ? new Log() : optionalLog;

        while (guard.isIn(room)) {
            ++room[guard.getRow()][guard.getCol()];
            if (log.contains(guard)) {
                return Integer.MAX_VALUE; // "infinity", indicating a loop
            }
            log.add(guard);
            guard.step(room);
        }
        return countVisited(room);
    }

    private static int countVisited(int[][] room) {
        int n = 0;
        for (int row = 0; row < room.length; ++row) {
            for (int col = 0; col < room[0].length; ++col) {
                if (room[row][col] > 0) {
                    ++n;
                }
            }
        }
        return n;
    }

    private static Guard findGuard(List<String> input) {
        Guard guard = null;
        for (int row = 0; row < input.size(); ++row) {
            int col = input.get(row).indexOf("^");
            if (col >= 0) {
                guard = new Guard(row, col);
                break;
            }
        }
        return guard;
    }

    private static int[][] buildRoom(List<String> input) {
        int[][] room = new int[input.size()][input.get(0).length()];
        for (int row = 0; row < input.size(); ++row) {
            String rowText = input.get(row);
            for (int col = 0; col < rowText.length(); ++col) {
                switch (rowText.charAt(col)) {
                case '#':
                    room[row][col] = -1;
                    break;
                default:
                    room[row][col] = 0;
                }
            }
        }

        return room;
    }

    private static final String[] SAMPLE = { "....#.....", ".........#", "..........", "..#.......", ".......#..",
            "..........", ".#..^.....", "........#.", "#.........", "......#..." };
}
