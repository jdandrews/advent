package advent.y2016;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day24 {
    private static record Location(int row, int col) {
        @Override
        public final boolean equals(Object arg0) {
            if (! (arg0 instanceof Location)) {
                return false;
            }

            Location l = (Location)arg0;
            return l.row == row && l.col == col;
        }
        @Override
        public final int hashCode() {
            return row + 128*col;
        }
    }

    private static record Path(char c1, char c2, List<Location> path) implements Comparable<Path> {
        @Override
        public final String toString() {
            return c1 + "<->" + c2 + ": " + (path.size()-1) + " steps.";
        }
        @Override
        public final boolean equals(Object arg0) {
            if ( ! (arg0 instanceof Path)) {
                return false;
            }
            Path p = (Path)arg0;
            boolean endsMatch = (c1 == p.c1 && c2 == p.c2) || (c1 == p.c2 && c2 == p.c1);

            return endsMatch && (equals(path,p.path) || equals(reverse(path),p.path));
        }

        private boolean equals(List<Location> p1, List<Location> p2) {
            if (p1.size() != p2.size()) {
                return false;
            }
            for (int i = 0; i<p1.size(); ++i) {
                if (! p1.get(i).equals(p2.get(i)))
                    return false;
            }
            return true;
        }

        @Override
        public final int hashCode() {
            int pathHash = 0;
            for (Location l : path) {
                pathHash += l.hashCode();
            }
            return c1 + c2 + pathHash;
        }

        private List<Location> reverse(List<Location> l){
            List<Location> r = new ArrayList<>();
            for (Location location : l) {
                r.add(0, location);
            }
            return r;
        }

        @Override
        public int compareTo(Path o) {
            if (c1 != o.c1) {
                return Character.valueOf(c1).compareTo(Character.valueOf(o.c1));
            }
            if (c2 != o.c2) {
                return Character.valueOf(c2).compareTo(Character.valueOf(o.c2));
            }
            return Integer.valueOf(path.size()).compareTo(Integer.valueOf(o.path.size()));
        }
    }

    public static void main(String[] args) {
        testPathEquality();

        Util.log("\n----\npuzzle:");
        solve(SAMPLE);

        Util.log("\n----\npuzzle:");
        List<String> lines = FileIO.getFileAsList("src/advent/y2016/Day24.txt");
        solve(lines);
    }

    private static void solve(List<String> lines) {
        char[][] grid = parse(lines);

        Map<Character, Location> pointsOfInterest = locatePointsOfInterest(grid);
        Set<Path>paths = findAllPaths(grid, pointsOfInterest);
        //        paths = findShortPaths(paths);

        log(paths);
        printMap(grid, paths);
        /*
        int minimumPath = Integer.MAX_VALUE;
        for (List<Location> path : paths) {
            minimumPath = Math.min(path.size(), minimumPath);
        }
        Util.log("part 1: found minimum path of length %d.", minimumPath);
         */
    }

    private static Set<Path> findShortPaths(Set<Path> paths) {
        Set<Path> result = new HashSet<>();
        List<Path> pathList = new ArrayList<>(paths);
        pathList.sort(null);

        for (int i = 0; i<pathList.size(); ++i) {
            Path shortest = pathList.get(i);
            result.add(shortest);

            int j;
            for (j = i+1; j < pathList.size(); ++j) {
                Path path = pathList.get(j);
                if (path.c1() != shortest.c1() || path.c2() != shortest.c2()) {
                    break;
                }
            }
            i = j-1;
        }
        return result;
    }

    private static void log(Set<Path> paths) {
        List<Path> pathList = new ArrayList<>(paths);
        pathList.sort(null);

        for (Path path : pathList) {
            System.out.println(path);
        }
    }

    private static final String RESET_FG =  "\33[39m";
    // private static final String RESET_ALL = "\33[0m";

    private static final String BRIGHT_RED_FG =     "\033[91m";
    /*
    private static final String BRIGHT_GREEN_FG =   "\033[92m";
    private static final String BRIGHT_YELLOW_FG =  "\033[93m";
    private static final String BRIGHT_BLUE_FG =    "\033[94m";
    private static final String BRIGHT_MAGENTA_FG = "\033[95m";
    private static final String BRIGHT_CYAN_FG =    "\033[96m";
     */
    private static final String[] COLORS = {
            "\033[93m", // bright yellow
            "\033[92m", // bright green
            "\033[96m", // bright cyan
            "\033[95m", // bright magenta
            "\033[91m", // bright red
            "\033[41m", // brick red
            "\033[94m", // bright blue
            "\033[43m", // brown
            "\033[45m", // magenta
            "\033[46m"  // cyan
    };

    private static void printMap(char[][] grid, Set<Path> paths) {
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                if (grid[row][col] == '.' ) {
                    System.out.print(getColor(new Location(row, col), paths) + '*' + RESET_FG);
                } else if (Character.isDigit(grid[row][col])) {
                    System.out.print(BRIGHT_RED_FG + grid[row][col] + RESET_FG);
                } else {
                    System.out.print(grid[row][col]);
                }
            }
            System.out.println("");
        }

    }

    private static String getColor(Location location, Set<Path> paths) {
        char minEnd = 'A';
        for (Path path : paths) {
            if (path.path().contains(location)) {
                minEnd = (char) Math.min(minEnd, path.c1());
                minEnd = (char) Math.min(minEnd, path.c2());
            }
        }
        if ('0' <= minEnd && minEnd <= '9' ) {
            return COLORS[minEnd - 48];
        }
        return "";
    }

    private static Set<Path> findAllPaths(char[][] grid, Map<Character, Location> destinations) {
        Set<Path> results = new HashSet<>();

        for (Map.Entry<Character, Location> destination : destinations.entrySet()) {
            Location robotLocation = destination.getValue();
            Deque<Location> stack = new ArrayDeque<>();

            Map<Location, List<Location>> paths = new HashMap<>();

            stack.push(robotLocation);
            paths.put(robotLocation, List.of(robotLocation));

            while (! stack.isEmpty()) {
                Location loc = stack.pop();
                List<Location> path = paths.get(loc);
                for (Location next : getDoors(grid, loc)) {
                    if (path.contains(next)) {
                        // ignored; it's where we came from.
                    }
                    else if (destinations.values().contains(next)) {
                        //                        path.add(next);
                        results.add(new Path(destination.getKey(), grid[next.row()][next.col()], path));
                    }
                    else {
                        List<Location> updatedPath = new ArrayList<>(path);
                        updatedPath.add(next);
                        paths.put(next, updatedPath);
                        stack.push(next);
                    }
                }
            }
        }

        return results;
    }

    private static List<Location> getDoors(char[][] grid, Location loc) {
        List<Location> result = new ArrayList<>();

        if (loc.row() > 0 && grid[loc.row() - 1][loc.col()] != '#') {
            result.add(new Location(loc.row() - 1, loc.col()));
        }

        if (loc.row() + 1 < grid.length && grid[loc.row()+1][loc.col()] != '#') {
            result.add(new Location(loc.row() + 1, loc.col()));
        }

        if (loc.col() > 0 && grid[loc.row()][loc.col() - 1] != '#') {
            result.add(new Location(loc.row(), loc.col() - 1));
        }

        if (loc.col() + 1 < grid[0].length && grid[loc.row()][loc.col() + 1] != '#') {
            result.add(new Location(loc.row(), loc.col() + 1));
        }

        return result;
    }

    private static Map<Character, Location> locatePointsOfInterest(char[][] grid) {
        Map<Character, Location> result = new HashMap<>();
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                if (Character.isDigit(grid[row][col])) {
                    result.put(grid[row][col], new Location(row, col));
                }
            }
        }
        return result;
    }

    private static char[][] parse(List<String> lines) {
        char[][] result = new char[lines.size()][lines.get(0).length()];
        for (int row = 0; row < result.length; ++row) {
            String s = lines.get(row);
            for (int col = 0; col < s.length(); ++col) {
                result[row][col] = s.charAt(col);
            }
        }
        return result;
    }

    private static final List<String> SAMPLE = List.of(
            "###########",
            "#0.1.....2#",
            "#.#######.#",
            "#4.......3#",
            "###########");

    private static void testPathEquality() {
        Path p1 = new Path('a', 'b', List.of(new Location(0,1), new Location(1, 1), new Location(2, 1), new Location(2,2)));
        Path p2 = new Path('b', 'a', List.of(new Location(2,2), new Location(2, 1), new Location(1, 1), new Location(0,1)));
        Path p3 = new Path('a', 'b', List.of(new Location(0,1), new Location(1, 1), new Location(2, 1), new Location(2,2)));
        Path p4 = new Path('a', 'b', List.of(new Location(1,1), new Location(1, 1), new Location(2, 1), new Location(2,2)));

        System.out.println("p1 ==? p2: " + p1.equals(p2));
        System.out.println("p2 ==? p3: " + p2.equals(p3));
        System.out.println("p1 ==? p3: " + p1.equals(p3));
        System.out.println("p3 ==? p1: " + p3.equals(p1));
        System.out.println("p4 ==? p1: " + p4.equals(p1));

        System.out.println("p1.hashCode = " + p1.hashCode());
        System.out.println("p2.hashCode = " + p2.hashCode());
        System.out.println("p3.hashCode = " + p3.hashCode());
        System.out.println("p4.hashCode = " + p4.hashCode());
    }
}
