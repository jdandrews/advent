package advent.y2024;

import java.util.ArrayList;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day15 {
    private static record Robot(int row, int col) {}
    private static record Box(int row, int col) {}

    public static void main(String[] args) {
        Util.log("---- EXAMPLE 0 ----");
        char[][] map = getMap(EXAMPLE_0);
        String moves = getMoves(EXAMPLE_0);
        map = execute(moves, map, true);
        int score = score(map);
        Util.log(map);
        Util.log("final GPS sum is %d", score);

        Util.log("---- EXAMPLE 1 ----");
        map = getMap(EXAMPLE_1);
        moves = getMoves(EXAMPLE_1);
        map = execute(moves, map, false);
        score = score(map);
        Util.log(map);
        Util.log("final GPS sum is %d", score);

        Util.log("---- Part 1 ----");
        map = getMap(FileIO.getFileAsString("src/advent/y2024/Day15.txt"));
        moves = getMoves(FileIO.getFileAsString("src/advent/y2024/Day15.txt"));
        map = execute(moves, map, false);
        score = score(map);
        Util.log("final GPS sum is %d", score);

        Util.log("---- EXAMPLE 2 ----");
        map = getMap(EXAMPLE_2);
        map = widen(map);
        moves = getMoves(EXAMPLE_2);
        map = execute(moves, map, true);
        score = score(map);
        Util.log("final GPS sum is %d", score);

    }

    private static char[][] widen(char[][] map){
        char[][] widerMap = new char[map.length][2 * map[0].length];
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                switch(map[row][col]) {
                case '.':
                    widerMap[row][2 * col] = '.';
                    widerMap[row][2 * col + 1] = '.';
                    break;
                case '#':
                    widerMap[row][2 * col] = '#';
                    widerMap[row][2 * col + 1] = '#';
                    break;
                case 'O':
                    widerMap[row][2 * col] = '[';
                    widerMap[row][2 * col + 1] = ']';
                    break;
                case '@':
                    widerMap[row][2 * col] = '@';
                    widerMap[row][2 * col + 1] = '.';
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported map char: " + map[row][col]);
                }
            }
        }
        return widerMap;
    }

    private static int score(char[][] map) {
        int score = 0;
        for (int r = 0; r < map.length; ++r) {
            for (int c = 0; c < map[0].length; ++c) {
                if (map[r][c] == 'O')
                    score += 100 * r + c;
            }
        }
        return score;
    }

    private static char[][] execute(String moves, char[][] map, boolean showMoves) {
        for (int i=0; i<moves.length(); ++i) {
            switch (moves.charAt(i)) {
            case '^':
                map = move(map, -1, 0);
                break;
            case '>':
                map = move(map, 0, 1);
                break;
            case 'v':
                map = move(map, 1, 0);
                break;
            case '<':
                map = move(map, 0, -1);
                break;
            default:
                throw new UnsupportedOperationException("No move defined for " + moves.charAt(i));
            }
            if (showMoves) {
                Util.log("\nMove: %c:", moves.charAt(i));
                Util.log(map);
            }
        }
        return map;
    }

    private static char[][] move(char[][] map, int dRow, int dCol) {
        Robot robot = locateRobot(map);
        List<Box> boxes = new ArrayList<>();

        boolean canMove = true;
        boolean moveDefined = false;

        int row = robot.row() + dRow;
        int col = robot.col() + dCol;
        // in wide maps, the pile of boxes can be arbitrarily wide moving up or down. Track and check the path width.
        int minCol = col;
        int maxCol = col;

        while (! moveDefined) {
            if (map[row][col] == 'O') {
                boxes.add(new Box(row, col));

            } else if (map[row][col] == '.') {
                moveDefined = true;

            } else if (map[row][col] == '#') {
                canMove = false;
                moveDefined = true;

            } else if (map[row][col] == '[') {
                boxes.add(new Box(row, col));
                throw new UnsupportedOperationException("not coded yet");

            } else if (map[row][col] == ']') {
                boxes.add(new Box(row, col-1));
                throw new UnsupportedOperationException("not coded yet");

            } else {
                throw new IllegalStateException("Unhandled map character: " + map[row][col]);
            }
            row = row + dRow;
            col = col + dCol;
        }

        if (canMove) {
            map[robot.row()][robot.col()] = '.';
            map[robot.row() + dRow][robot.col() + dCol] = '@';
            for (Box box : boxes) {
                map[box.row() + dRow][box.col() + dCol] = 'O';
            }
        }
        return map;
    }

    private static Robot locateRobot(char[][] map) {
        for (int r = 0; r < map.length; ++r) {
            for (int c = 0; c < map[0].length; ++c){
                if (map[r][c] == '@') {
                    return new Robot(r,c);
                }
            }
        }
        throw new UnsupportedOperationException("No robot found!");
    }

    private static String getMoves(String s) {
        List<String> lines = Util.getLines(s);
        boolean afterMap = false;
        StringBuilder moves = new StringBuilder();
        for (int i = 0; i<lines.size(); ++i) {
            if (!afterMap) {
                if (lines.get(i).trim().length() == 0) {
                    afterMap = true;
                }
                continue;
            }
            moves.append(lines.get(i).trim());
        }
        return moves.toString();
    }

    private static char[][] getMap(String s) {
        char[][] map = null;
        List<String> lines = Util.getLines(s);
        for (int i = 0; i<lines.size(); ++i) {
            if (lines.get(i).trim().length() == 0) {
                map = new char[i][lines.get(0).length()];
                break;
            }
        }
        if (map == null) {
            throw new IllegalStateException("no map found.");
        }
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < lines.get(0).length(); ++col) {
                map[row][col] = lines.get(row).charAt(col);
            }
        }
        return map;
    }

    private static final String EXAMPLE_2 = """
                    #######
                    #...#.#
                    #.....#
                    #..OO@#
                    #..O..#
                    #.....#
                    #######

                    <vv<<^^<<^^
                    """;

    private static final String EXAMPLE_1 = """
                    ##########
                    #..O..O.O#
                    #......O.#
                    #.OO..O.O#
                    #..O@..O.#
                    #O#..O...#
                    #O..O..O.#
                    #.OO.O.OO#
                    #....O...#
                    ##########

                    <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                    vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                    ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                    <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                    ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                    ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                    >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                    <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                    ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                    v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                    """;
    private static final String EXAMPLE_0 = """
                    ########
                    #..O.O.#
                    ##@.O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########

                    <^^>>>vv<v>>v<<
                    """;
}
