package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.FileIO;
import advent.Util;

public class Day07 {

    private static final List<String> SAMPLE = new ArrayList<>(Arrays.asList(
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."));

    public static void main(String[] args) {
        Util.log("part 1 SAMPLE found %d splits; expected 21.", countSplits(SAMPLE));
        Util.log("part 2 SAMPLE found %d paths; expected 40.", countPaths(SAMPLE, true));

        Util.log("---------");

        List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day07.txt");
        Util.log("part 1 puzzle found %d splits.", countSplits(puzzle));
        Util.log("part 2 puzzle found %d paths.", countPaths(puzzle, false));
    }

    private record Loc(int row, int col) {}

    private static int countSplits(List<String> map) {
        locToVisit.clear();
        int splits = 0;

        for (int i = 0; i < map.size() - 1; ++i) {
            splits += updateMap(i, map);
        }

        return splits;
    }

    /**
     * Counts paths through the manifold.
     * A path is an ordered list of columns--the columns through which the path runs.
     *
     * Brute force: To count distinct paths,
     * we only need consider even rows, as they are the only ones containing possible alternate paths.
     * Straighforward depth-first search. Dupe removal is handled by the set data structure.
     *
     * This fails on OOM for the supplied puzzle; I didn't work out how far it got, but "not far enough" on 10G of heap.-
     *
     * Second attempt:
     * For any given node, there are n paths through that node, which we know starts at the top with a single
     * path and associated node. The final code keeps track of the number of paths through each node descendinng
     * through the manifold. The final count is the sum at the end. We only keep one row's-worth of counts,
     * because we don't need a row once we're done with it.
     *
     * @param map a map
     * @return number of paths
     */
    private static long countPaths(List<String> map, boolean logProgress) {
        String row =  map.get(0);
        long[] pathCount = new long[row.length()];
        pathCount[row.indexOf("S")] = 1;

        for(int nRow = 1; nRow < map.size(); ++nRow) {
            row = map.get(nRow);

            for (int nCol = 0; nCol < row.length(); nCol++) {
                if (row.charAt(nCol) == '^' && pathCount[nCol] > 0) {
                    long sourceCount = pathCount[nCol];
                    pathCount[nCol] = 0;
                    if (nCol > 0) {
                        pathCount[nCol - 1] += sourceCount;
                    }
                    if (nCol + 1 < pathCount.length) {
                        pathCount[nCol + 1] += sourceCount;
                    }
                }
            }
            if (logProgress) {
                Util.log(pathCount);
            }
        }

        return Arrays.stream(pathCount).sum();

        /*
         *  brute force: depth-first search of all paths. Works for sample... but OOM for larger trees.
         *
        Set<List<Integer>> paths = new HashSet<>();
        Deque<ArrayList<Integer>> stack = new ArrayDeque<>();

        ArrayList<Integer> path = new ArrayList<>();

        int start = map.get(0).indexOf('S');
        path.add(start);
        stack.push(path);

        while (! stack.isEmpty()) {
            path = stack.pop();
            int nRow = 2 * path.size();
            if (nRow >= map.size()) {
                paths.add(path);
                continue;
            }

            String row = map.get(nRow);
            int nCol = path.getLast();
            if (row.charAt(nCol) == '^') {
                ArrayList<Integer> left = new ArrayList<>(path);
                left.add(nCol - 1);
                stack.push(left);

                ArrayList<Integer> right = new ArrayList<>(path);
                right.add(nCol + 1);
                stack.push(right);
            }
            else {
                path.add(nCol);
                stack.push(path);
            }
        }

        return paths.size();
         */
    }

    private static void print(List<String> map) {
        for (int row = 0; row < map.size(); ++row) {
            for (int col = 0; col < map.get(row).length(); ++col) {
                Long count = locToVisit.get(new Loc(row, col));
                if (count == null) {
                    System.out.print(map.get(row).charAt(col) + " ");
                } else {
                    System.out.print(count.longValue() + " ");
                }
            }
            System.out.println("");
        }
        // TODO Auto-generated method stub

    }

    private static Map<Loc, Long> locToVisit = new HashMap<>();

    private static int updateMap(int nRow, List<String> map) {
        String thisRow = map.get(nRow);
        String nextRow = map.get(nRow + 1);

        int splits = 0;

        for (int nCol = 0; nCol < thisRow.length(); ++nCol) {
            switch(thisRow.charAt(nCol)) {
            case '.':
            case '^':
                break;
            case 'S':
                nextRow = setBeam(nextRow, nCol);
                recordVisit(nRow + 1, nCol, map);
                break;
            case '|':
                if (nextRow.charAt(nCol) == '.') {
                    nextRow = setBeam(nextRow, nCol);
                    recordVisit(nRow + 1, nCol, map);
                }
                else if (nextRow.charAt(nCol) == '^') {
                    ++splits;

                    nextRow = setBeam(nextRow, nCol - 1);
                    nextRow = setBeam(nextRow, nCol + 1);
                    recordVisit(nRow + 1, nCol - 1, map);
                    recordVisit(nRow + 1, nCol + 1, map);
                }
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unknown char a (" + nRow + "," + nCol + "; " + thisRow.charAt(nCol));
            }
        }

        map.set(nRow, thisRow);
        map.set(nRow + 1, nextRow);

        return splits;
    }

    private static void recordVisit(int nRow, int nCol, List<String> map) {
        Loc loc = new Loc(nRow, nCol);

        if (! locToVisit.containsKey(loc)) {
            locToVisit.put(loc, 0L);
        }
        locToVisit.put(loc, locToVisit.get(loc) + 1);
    }

    private static String setBeam(String nextRow, int nCol) {
        nextRow = nextRow.substring(0, nCol) + "|" + nextRow.substring(nCol + 1);
        return nextRow;
    }
}
