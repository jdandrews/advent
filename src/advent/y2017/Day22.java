package advent.y2017;

import java.util.HashMap;
import java.util.Map;

import advent.Util;

public class Day22 {

    private enum Turn { RIGHT, LEFT; }
    private enum Direction {
        N(-1, 0), S(1, 0), E(0, 1), W(0, -1);

        private int dRow;
        private int dCol;

        private Direction(int dR, int dC) {
            dRow = dR;
            dCol = dC;
        }

        public Direction turn(Turn t) {
            switch(this) {
            case N:
                if (t == Turn.RIGHT) {
                    return Direction.E;
                } else if (t == Turn.LEFT) {
                    return Direction.W;
                } else {
                    throw new UnsupportedOperationException("can't figure out how to turn "+t);
                }

            case E:
                if (t == Turn.RIGHT) {
                    return Direction.S;
                } else if (t == Turn.LEFT) {
                    return Direction.N;
                } else {
                    throw new UnsupportedOperationException("can't figure out how to turn "+t);
                }

            case S:
                if (t == Turn.RIGHT) {
                    return Direction.W;
                } else if (t == Turn.LEFT) {
                    return Direction.E;
                } else {
                    throw new UnsupportedOperationException("can't figure out how to turn "+t);
                }

            case W:
                if (t == Turn.RIGHT) {
                    return Direction.N;
                } else if (t == Turn.LEFT) {
                    return Direction.S;
                } else {
                    throw new UnsupportedOperationException("can't figure out how to turn "+t);
                }

            default:
                throw new UnsupportedOperationException("Unsupported direction: "+this);
            }
        }
    }

    private static class Point {
        int col;
        int row;

        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof Point)) {
                return false;
            }

            Point p = (Point)obj;
            return p.col == col && p.row == row;
        }

        @Override
        public int hashCode() {
            return col + 21*row;
        }

        public Point(int r, int c) {
            row = r;
            col = c;
        }

        public Point move(Direction direction) {
            return new Point(row + direction.dRow, col + direction.dCol);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", row, col);
        }
    }

    public enum InfectionState {
        CLEAN,
        WEAKENED,
        INFECTED,
        FLAGGED;
    }

    private static class Grid {
        Map<Point, InfectionState> infections = new HashMap<>();

        public InfectionState get(Point p) {
            if (infections.containsKey(p)) {
                return infections.get(p);
            }
            return InfectionState.CLEAN;
        }

        public void infect(Point point) {
            infections.put(point, InfectionState.INFECTED);
        }

        public void flag(Point point) {
            infections.put(point, InfectionState.FLAGGED);
        }

        public void weaken(Point point) {
            infections.put(point, InfectionState.WEAKENED);
        }

        public void clean(Point point) {
            infections.remove(point);
        }
    }

    public static void main(String[] args) {
        Grid grid = loadGrid(sampleGrid);
        Direction direction = Direction.N;
        Point position = new Point(1,1);
        int newInfections = runCleanInfected(grid, direction, position);
        Util.log("%d new infections with sample data", newInfections);

        grid = loadGrid(puzzleGrid);
        direction = Direction.N;
        position = new Point(12,12);
        newInfections = runCleanInfected(grid, direction, position);
        Util.log("%d new infections with puzzle data", newInfections);

        grid = loadGrid(sampleGrid);
        direction = Direction.N;
        position = new Point(1,1);
        newInfections = run4state(grid, direction, position, 100);
        grid = loadGrid(sampleGrid);
        Util.log("new infections with sample data: %d with 100, %d with 10000000", newInfections, run4state(grid, direction, position, 10000000));

        grid = loadGrid(puzzleGrid);
        direction = Direction.N;
        position = new Point(12,12);
        newInfections = run4state(grid, direction, position, 10000000);
        Util.log("new infections with puzzle data: %d with 10000000", newInfections);
    }

    private static int runCleanInfected(Grid grid, Direction direction, Point position) {
        int newInfections = 0;
        for (int i=0; i<10000; ++i) {
            // step 1
            if (InfectionState.INFECTED == grid.get(position)) {
                direction = direction.turn(Turn.RIGHT);
            } else {
                direction = direction.turn(Turn.LEFT);
            }
            // step 2
            if (InfectionState.INFECTED == grid.get(position)) {
                grid.clean(position);
            } else {
                grid.infect(position);
                ++newInfections;
            }
            // step 3
            position = position.move(direction);
        }
        return newInfections;
    }

    private static int run4state(Grid grid, Direction direction, Point position, int iterations) {
        int newInfections = 0;
        for (int i=0; i<iterations; ++i) {
            switch(grid.get(position)) {
            case CLEAN:
                direction = direction.turn(Turn.LEFT);
                grid.weaken(position);
                break;

            case WEAKENED:
                grid.infect(position);
                ++newInfections;
                break;

            case INFECTED:
                direction = direction.turn(Turn.RIGHT);
                grid.flag(position);
                break;

            case FLAGGED:
                direction = direction.turn(Turn.RIGHT).turn(Turn.RIGHT);     // turn 180
                grid.clean(position);
                break;

            default:
                throw new UnsupportedOperationException("unknown state: "+grid.get(position));
            }

            // step 3
            position = position.move(direction);
        }
        return newInfections;
    }

    private static Grid loadGrid(String[] grid) {
        Grid result = new Grid();

        for (int row = grid.length-1; row >= 0; --row) {
            for (int col = 0; col < grid[row].length(); ++col) {
                if (grid[row].charAt(col)=='#') {
                    result.infect(new Point(row, col));
                }
            }
        }

        return result;
    }

    private static final String[] sampleGrid = {
            "..#",
            "#..",
            "..."
    };

    private static final String[] puzzleGrid = {
            //123456789-123456789-1234
            ".#...#.#.##..##....##.#.#",    // 24
            "###.###..##...##.##....##",    // 23
            "....#.###..#...#####..#.#",
            ".##.######..###.##..#...#",
            "#..#..#..##..###...#..###",
            "..####...#.##.#.#.##.####",
            "#......#..####..###..###.",
            "#####.##.#.#.##.###.#.#.#",
            ".#.###....###....##....##",
            ".......########.#.#...#..",
            "...###.####.##..###.##..#",
            "#.#.###.####.###.###.###.",
            ".######...###.....#......",    // 12
            "....##.###..#.#.###...##.",
            "#.###..###.#.#.##.#.##.##",
            "#.#.#..###...###.###.....",
            "##..##.##...##.##..##.#.#",
            ".....##......##..#.##...#",
            "..##.#.###.#...#####.#.##",
            "....##..#.#.#.#..###.#..#",
            "###..##.##....##.#....##.",
            "#..####...####.#.##..#.##",
            "####.###...####..##.#.#.#",
            "#.#.#.###.....###.##.###.",
            ".#...##.#.##..###.#.###.."     // 0
    };
}
