package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day04 {

    private static final List<String> SAMPLE = Arrays.asList(
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@.");

    public static void main(String[] args) {
        final List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day04.txt");

        Util.log("part 1 SAMPLE has %d moveable rolls.", countMoveableRolls(SAMPLE));
        Util.log("part 1 puzzle has %d moveable rolls.", countMoveableRolls(puzzle));

        Util.log("------------");

        Util.log("part 2 SAMPLE removed %d rolls.", countRemovedRolls(SAMPLE));
        Util.log("part 2 puzzle removed %d rolls.", countRemovedRolls(puzzle));

    }

    private static Object countRemovedRolls(List<String> map) {
        int removed = 0;
        int removedThisRound = 0;
        do {
            List<Location> removeMe = nameMoveableRolls(map);
            removedThisRound = removeMe.size();
            removed += removedThisRound;
            removeMoveableRolls(map, removeMe);
        } while (removedThisRound > 0);

        return removed;
    }

    private static void removeMoveableRolls(List<String> map, List<Location> moveable) {
        for (Location loc : moveable) {
            String row = map.get(loc.row());
            row = row.substring(0, loc.col()) + '.' + row.substring(loc.col()+1);
            map.set(loc.row(), row);
        }
    }

    private static record Location(int row, int col) {}

    private static int countMoveableRolls(List<String> map) {
        return nameMoveableRolls(map).size();
    }

    private static List<Location> nameMoveableRolls(List<String> map) {
        List<Location> moveable = new ArrayList<>();
        for (int row = 0; row < map.size(); ++row) {
            for (int col = 0; col < map.get(row).length(); ++col) {
                if (isMovable(map, row, col)) {
                    moveable.add(new Location(row,col));
                }
            }
        }
        return moveable;
    }

    private static boolean isMovable(List<String> map, int row, int col) {
        if (map.get(row).charAt(col) != '@'){
            return false;
        }

        int neighbors = 0;
        for (int r = Math.max(0, row-1); r <= Math.min(map.size() - 1, row+1); ++r) {
            for (int c = Math.max(0,  col-1); c <= Math.min(map.get(r).length() - 1,  col + 1); ++c) {
                if ((c != col || r != row) && map.get(r).charAt(c) == '@') {
                    ++neighbors;
                }
            }
        }
        return neighbors < 4;
    }

}