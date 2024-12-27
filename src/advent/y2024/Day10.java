package advent.y2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day10 {
    public static record Cell(int row, int col) {
    }

    public static void main(String[] args) {
        int[][] input = parse(SAMPLE);
        String tag = "Sample ";

        solve("Sample ", parse(SAMPLE));
        solve("Sample 2 ", parse(SAMPLE2));
        solve("Sample 3 ", parse(SAMPLE3));
        solve("input ", parse( FileIO.getFileAsString("src/advent/y2024/Day10.txt")));
    }

    private static int solve(String tag, int[][] input) {
        List<Cell> trailheads = getTrailheads(input);
        int sumOfScores = 0;
        int sumOfRescores = 0;
        for (Cell cell : trailheads) {
            sumOfScores += score(cell, input);
            sumOfRescores += rescore(cell, input);
        }
        Util.log("%s sum of scores = %d", tag, sumOfScores);
        Util.log("%s sum of rescores = %d", tag, sumOfRescores);
        return sumOfRescores;
    }

    private static int score(Cell cell, int[][] map) {
        Set<Cell> here = new HashSet<>();
        here.add(cell);

        Set<Cell> there = new HashSet<>();
        while (!here.isEmpty()) {
            for (Cell c : here) {
                there.addAll(stepUp(c, map));
            }
            here.clear();
            for (Cell c : there) {
                if (map[c.row()][c.col] != 9) {
                    here.add(c);
                }
            }
            there.removeAll(here);
        }
        // Util.log("found %d summits reachable from trailhead %s", there.size(), cell.toString());
        return there.size();
    }

    private static int rescore(Cell trailhead, int[][] map) {
        Set<List<Cell>> trails = new HashSet<>();
        {
            List<Cell> trail = new ArrayList<>();
            trail.add(trailhead);
            trails.add(trail);
        }

        for (int level = map[trailhead.row()][trailhead.col()]; level < 9; ++level) {
            Set<List<Cell>> newTrails = new HashSet<>();
            for (List<Cell> trail : trails) {
                List<Cell> steps = stepUp(trail.getLast(), map);
                for (Cell step : steps) {
                    List<Cell> newTrail = new ArrayList<>(trail);
                    newTrail.add(step);
                    newTrails.add(newTrail);
                }
            }
            trails = new HashSet<>(newTrails);
            newTrails.clear();
        }
        // too high; verify
        /*
        for (List<Cell> trail : trails) {
            int matches = 0;
            for (List<Cell> otherTrail : trails) {
                boolean match = true;
                for (int i = 0; i < 10; ++i) {
                    if (!trail.get(i).equals(otherTrail.get(i))) {
                        match = false;
                        break;
                    }
                }
                if (match)
                    ++matches;
            }
            if (matches != 1) {
                Util.log("bad trail; %d matches: %s", matches, trail);
            }
        }
         */
        // Util.log("found %d trails to summits from trailhead %s", trails.size(), trailhead.toString());
        return trails.size();
    }

    private static List<Cell> stepUp(Cell cell, int[][] map) {
        List<Cell> nextSteps = new ArrayList<>();
        int level = map[cell.row][cell.col] + 1;
        // up
        if (cell.row > 0 && map[cell.row - 1][cell.col] == level) {
            nextSteps.add(new Cell(cell.row() - 1, cell.col()));
        }
        // right
        if (cell.col + 1 < map[cell.row].length && map[cell.row][cell.col + 1] == level) {
            nextSteps.add(new Cell(cell.row(), cell.col() + 1));
        }
        // down
        if (cell.row + 1 < map.length && map[cell.row + 1][cell.col] == level) {
            nextSteps.add(new Cell(cell.row() + 1, cell.col()));
        }
        // left
        if (cell.col > 0 && map[cell.row][cell.col - 1] == level) {
            nextSteps.add(new Cell(cell.row(), cell.col() - 1));
        }
        return nextSteps;
    }

    private static List<Cell> getTrailheads(int[][] map) {
        List<Cell> trailheads = new ArrayList<>();
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[row].length; ++col) {
                if (map[row][col] == 0) {
                    trailheads.add(new Cell(row, col));
                }
            }
        }
        return trailheads;
    }

    private static int[][] parse(String mapAsString) {
        String[] rows = mapAsString.split("\n");
        int[][] map = new int[rows.length][rows[0].length()];
        for (int row = 0; row < rows.length; ++row) {
            for (int col = 0; col < rows[row].length(); ++col) {
                if (rows[row].substring(col, col+1).equals(".")) {
                    map[row][col] = -1;
                } else {
                    map[row][col] = Integer.parseInt(rows[row].substring(col, col + 1));
                }
            }
        }
        return map;
    }

    private static String SAMPLE3 = """
                    ..90..9
                    ...1.98
                    ...2..7
                    6543456
                    765.987
                    876....
                    987....""";

    private static String SAMPLE2 = """
                    012345
                    123456
                    234567
                    345678
                    416789
                    567891
                    """;
    private static String SAMPLE = """
                    89010123
                    78121874
                    87430965
                    96549874
                    45678903
                    32019012
                    01329801
                    10456732""";
}
