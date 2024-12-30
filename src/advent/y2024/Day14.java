package advent.y2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day14 {

    private record Position(int row, int col) {}
    private record Velocity(int dr, int dc) {}

    private static class Robot {
        Velocity velocity;
        Position position;

        public Robot(Position p, Velocity v) {
            position = p;
            velocity = v;
        }

        public void move(int seconds, int gridRows, int gridCols) {
            int row = position.row() + velocity.dr() * seconds;
            int col = position.col() + velocity.dc() * seconds;

            if (row < 0) {
                row = gridRows + (row % gridRows);
            }
            if (col < 0) {
                col = gridCols + (col % gridCols);
            }
            position = new Position(row % gridRows, col % gridCols);
        }
    }

    public static void main(String[] args) throws IOException {
        List<Robot> robots = parse(Util.getLines(SAMPLE));
        int[][] robotPositions = move(robots, 100, 7, 11);
        Util.log(robotPositions);
        Util.log("SAMPLE safety factor: %d", score(robotPositions));

        /*
        Util.log("----------");
        robots = parse(Util.getLines(SAMPLE));
        robotPositions = move(robots, 0, 7, 11);
        Util.log(robotPositions);
        for (int i = 0; i<10; ++i) {
            try { Thread.sleep(250); } catch (InterruptedException e) {}
            robotPositions = move(robots, 1, 7, 11);
            System.out.print("\033[A\033[A\033[A\033[A\033[A\033[A\033[A");
            Util.log(robotPositions);
        }
         */

        Util.log("----------");
        robots = parse(FileIO.getFileAsList("src/advent/y2024/Day14.txt"));
        robotPositions = move(robots, 100, 103, 101);
        Util.log("Part 1 safety factor: %d", score(robotPositions));

        Util.log("----------");
        robots = parse(FileIO.getFileAsList("src/advent/y2024/Day14.txt"));
        boolean done = false;
        for (int i = 1; !done; ++i) {
            robotPositions = move(robots, 1, 103, 101);
            int n = 0;
            for (int r = 0; !done && r < robotPositions.length; ++r) {
                for (int c = 0; c < robotPositions[0].length; ++c) {
                    if (robotPositions[r][c] > 0) {
                        for (n = 1; n+c < robotPositions[0].length; ++n) {
                            if (robotPositions[r][n + c] == 0) break;
                        }
                        if (n > 10) {
                            done = true;
                            Util.log("---------- %d --------", i);
                            break;
                        }
                    }
                }
            }
        }
        Util.log(robotPositions);
    }

    private static int score(int[][] robotPositions) {
        int rows = robotPositions.length;
        int cols = robotPositions[0].length;
        int middleRow = rows / 2;
        int middleCol = cols / 2;

        int[] quads = new int[4];

        for (int r = 0; r<rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                if (r < middleRow && c < middleCol) {
                    quads[0] += robotPositions[r][c];
                }
                if (r < middleRow && c > middleCol) {
                    quads[1] += robotPositions[r][c];
                }
                if (r > middleRow && c < middleCol) {
                    quads[2] += robotPositions[r][c];
                }
                if (r > middleRow && c > middleCol) {
                    quads[3] += robotPositions[r][c];
                }
            }
        }

        return quads[0] * quads[1] * quads[2] * quads[3];
    }

    private static int[][] move(List<Robot> robots, int seconds, int gridRows, int gridCols) {
        int[][] map = new int[gridRows][gridCols];
        for (Robot robot : robots) {
            robot.move(seconds, gridRows, gridCols);
            ++map[robot.position.row()][robot.position.col()];
        }
        return map;
    }

    private static List<Robot> parse(List<String> lines) {
        List<Robot> robots = new ArrayList<>();
        for (String line : lines) {
            String[] s = line.split("[=, ]");
            robots.add(new Robot(
                    new Position(Integer.valueOf(s[2]), Integer.valueOf(s[1])),
                    new Velocity(Integer.valueOf(s[5]), Integer.valueOf(s[4]))
                    ));
        }
        return robots;
    }

    // note: input is in (x,y) which is "col, row" format. Code uses row, col format.
    private static final String SAMPLE = """
                    p=0,4 v=3,-3
                    p=6,3 v=-1,-3
                    p=10,3 v=-1,2
                    p=2,0 v=2,-1
                    p=0,0 v=1,3
                    p=3,0 v=-2,-2
                    p=7,6 v=-1,-3
                    p=3,0 v=-1,-2
                    p=9,3 v=2,3
                    p=7,3 v=-1,2
                    p=2,4 v=2,-3
                    p=9,5 v=-3,-3
                    """;
}
