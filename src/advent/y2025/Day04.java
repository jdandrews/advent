package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day04 {
    private static record Location(int row, int col) {}

    private static class Map {
        private final List<String> map;
        private final int width;

        public Map(List<String> mapContent) {
            this.map = mapContent;
            width = map.get(0).length();
        }

        public char charAt(Location loc) {
            return map.get(loc.row()).charAt(loc.col());
        }

        public void setChar(Location loc, char value) {
            String row = map.get(loc.row());
            row = row.substring(0, loc.col()) + value + row.substring(loc.col()+1);
            map.set(loc.row(), row);
        }

        public int rows() {
            return map.size();
        }

        public int cols() {
            return width;
        }
    }

    private static final Map SAMPLE = new Map(Arrays.asList(
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."));

    public static void main(String[] args) {
        final Map puzzle = new Map(FileIO.getFileAsList("src/advent/y2025/Day04.txt"));

        Util.log("part 1 SAMPLE has %d moveable rolls.", countMoveableRolls(SAMPLE));
        Util.log("part 1 puzzle has %d moveable rolls.", countMoveableRolls(puzzle));

        Util.log("------------");

        Util.log("part 2 SAMPLE removed %d rolls.", removeAllMovableRolls(SAMPLE));
        Util.log("part 2 puzzle removed %d rolls.", removeAllMovableRolls(puzzle));

    }

    /**
     * Recursively identify and remove movable rolls, until only immovable rolls are left.
     *
     * @param map
     * @return the number of rows removed
     */
    private static int removeAllMovableRolls(Map map) {
        int removed = 0;
        int removedThisRound = 0;
        do {
            List<Location> removeMe = locateMoveableRolls(map);
            removedThisRound = removeMe.size();
            removed += removedThisRound;
            removeRolls(map, removeMe);
        } while (removedThisRound > 0);

        return removed;
    }

    private static void removeRolls(Map map, List<Location> moveable) {
        for (Location loc : moveable) {
            map.setChar(loc, '.');
        }
    }

    private static int countMoveableRolls(Map map) {
        return locateMoveableRolls(map).size();
    }

    private static List<Location> locateMoveableRolls(Map map) {
        List<Location> moveable = new ArrayList<>();
        for (int row = 0; row < map.rows(); ++row) {
            for (int col = 0; col < map.cols(); ++col) {
                Location loc = new Location(row, col);
                if (isMovable(map, loc)) {
                    moveable.add(loc);
                }
            }
        }
        return moveable;
    }

    private static boolean isMovable(Map map, Location loc) {
        if (map.charAt(loc) != '@'){
            return false;
        }

        int neighbors = 0;
        for (int r = Math.max(0, loc.row() - 1); r <= Math.min(map.rows() - 1, loc.row() + 1); ++r) {
            for (int c = Math.max(0, loc.col() - 1); c <= Math.min(map.cols() - 1,  loc.col() + 1); ++c) {
                if ((c != loc.col() || r != loc.row()) && map.charAt(new Location(r, c)) == '@') {
                    ++neighbors;
                }
            }
        }
        return neighbors < 4;
    }
}