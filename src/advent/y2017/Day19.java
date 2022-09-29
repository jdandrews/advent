package advent.y2017;

import java.io.IOException;
import java.util.List;

import advent.Util;

public class Day19 {

    private static class Location {
        int row;
        int col;
        public Location(int rowPosition, int colPosition) {
            row = rowPosition;
            col = colPosition;
        }
        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof Location)) {
                return false;
            }

            Location location = (Location)obj;
            return row == location.row && col == location.col;
        }

        @Override
        public int hashCode() {
            return row + 257 * col;
        }

        public Location plus(Direction direction) {
            return new Location(row + direction.dRow, col + direction.dCol);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", row, col);
        }
    }

    private enum Direction {
        N(-1, 0),
        S( 1, 0),
        E( 0, 1),
        W( 0,-1);

        static {
            N.backtrack = S;
            S.backtrack = N;
            E.backtrack = W;
            W.backtrack = E;
        }

        int dRow;
        int dCol;
        Direction backtrack;

        private Direction(int rowChange, int colChange) {
            dRow = rowChange;
            dCol = colChange;
        }

        @Override
        public String toString() {
            return String.format("%s{%d,%d}", name(), dRow, dCol);
        }
    }

    public static void main(String[] args) {
        char[][] map =  loadSample();
        walkMap(map);

        map = loadMap();
        walkMap(map);
    }

    private static void walkMap(char[][] map) {
        StringBuilder path = new StringBuilder();
        int steps = 0;

        Direction direction = Direction.S;
        Location location = findStart(map);

        for (boolean done = false; !done; ) {
            char here = map[location.row][location.col];
            Location newLocation = location;

            switch(here) {
            case '|':
            case '-':
                newLocation = move(direction, location);
                ++steps;
                break;  // continue

            case '+':
                direction = findNewDirection(map, location, direction);
                if (direction==null) {
                    done = true;
                } else {
                    newLocation = move(direction, location);
                    ++steps;
                }
                break;

            case ' ':
                Util.log("fell off the map at location %s traveling %s", location, direction);
                done = true;
                break;

            default:
                path.append(here);
                newLocation = move(direction, location);
                ++steps;
                break;
            }
            location = newLocation;
        }
        Util.log("path: %s in %d steps", path.toString(), steps);
    }

    private static Direction findNewDirection(char[][] map, Location location, Direction direction) {
        for (Direction newDir : Direction.values()) {
            if (newDir.equals(direction.backtrack)) {
                continue;
            }
            Location next = move(newDir, location);
            if (next.row < map.length && next.col < map[next.row].length && map[next.row][next.col] != ' ' ) {
                return newDir;
            }
        }
        return null;
    }

    private static Location move(Direction direction, Location location) {
        return location.plus(direction);
    }

    private static Location findStart(char[][] map) {
        for (int i=0; i<map[0].length; ++i) {
            if (map[0][i] == '|') {
                return new Location(0, i);
            }
        }
        return null;
    }

    private static char[][] loadSample() {
        String[] map = {
            "        |          ",
            "        |  +--+    ",
            "        A  |  C    ",
            "    F---|----E|--+ ",
            "        |  |  |  D ",
            "        +B-+  +--+ "
            };

        char[][] result = new char[map.length][];
        for (int row=0; row < map.length; ++row) {
            result[row] = map[row].toCharArray();
        }

        return result;
    }

    private static char[][] loadMap() {
        List<String> input;
        try {
            input = Util.readInput("2017", "Day19.txt");
        } catch (IOException e) {
            throw new IllegalArgumentException("can't load data", e);
        }

        char[][] result = new char[input.size()][];
        for (int row=0; row < input.size(); ++row) {
            result[row] = input.get(row).toCharArray();
        }
        return result;
    }

}
