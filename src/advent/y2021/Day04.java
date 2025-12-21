package advent.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day04 {
    private static final String BRIGHT_GREEN_FG =   "\033[92m";
    private static final String RESET_FG =  "\33[39m";

    public static void main(String[] args) {
        List<String> sample = FileIO.getFileAsList("src/advent/y2021/Day04sample.txt");
        Util.log("Loaded %d entries from Day04sample.txt", sample.size());

        List<String> puzzle = FileIO.getFileAsList("src/advent/y2021/Day04.txt");
        Util.log("Loaded %d entries from Day04.txt", puzzle.size());

        Util.log("part 1 sample winning score = %d", computeWinningBingoScore(sample, true));
        Util.log("part 2 sample losing score = %d", computeLosingBingoScore(sample, true));

        Util.log("---------");
        Util.log("part 1 puzzle winning score = %d", computeWinningBingoScore(puzzle, true));
        Util.log("part 2 puzzle losing score = %d", computeLosingBingoScore(puzzle, true));


    }

    private static class Board {
        private final int[][] squares;
        private boolean[][] draws;
        private int lastDraw = -1;

        public Board(int[][] squares) {
            this.squares = squares;
            this.draws = new boolean[5][5];
            for (int row = 0; row<squares.length; ++row) {
                for (int col = 0; col < squares[0].length; ++col) {
                    draws[row][col] = false;
                }
            }
        }

        public boolean applyDraw(int draw) {
            lastDraw = draw;
            for (int row = 0; row<squares.length; ++row) {
                for (int col = 0; col < squares[0].length; ++col) {
                    if (draw == squares[row][col]) {
                        draws[row][col] = true;
                    }
                }
            }
            return winner();
        }

        public boolean winner() {
            for (int row = 0; row<draws.length; ++row) {
                boolean winner = true;
                for (int col = 0; col < draws[0].length; ++col) {
                    if (! draws[row][col]) {
                        winner = false;
                        break;
                    }
                }
                if (winner) {
                    return winner;
                }
            }

            for (int col = 0; col < draws[0].length; ++col) {
                boolean winner = true;
                for (int row = 0; row<draws.length; ++row) {
                    if (! draws[row][col]) {
                        winner = false;
                        break;
                    }
                }
                if (winner) {
                    return winner;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int row = 0; row < squares.length; ++row) {
                for (int col = 0; col < squares[0].length; ++col) {
                    boolean hit = draws[row][col];
                    result
                    .append(hit ? BRIGHT_GREEN_FG : "")
                    .append(String.format("%3d ", squares[row][col]))
                    .append(hit ? RESET_FG : "");
                }
                result.append("\n");
            }
            return result.toString();
        }

        public int score() {
            int unmarkedSum = 0;

            for (int row = 0; row < squares.length; ++row) {
                for (int col = 0; col < squares[0].length; ++col) {
                    if (! draws[row][col]) {
                        unmarkedSum += squares[row][col];
                    }
                }
            }
            return unmarkedSum * lastDraw;
        }
    }

    private static int computeWinningBingoScore(List<String> input, boolean b) {
        List<Integer> draws = parseDraws(input);
        List<Board> boards = parseBoards(input);
        for (int draw : draws) {
            for (Board board : boards) {
                boolean winner = board.applyDraw(draw);
                if (winner) {
                    Util.log("we have a winnah!\n%s", board);
                    return board.score();
                }
            }
        }
        return -1;
    }

    private static int computeLosingBingoScore(List<String> input, boolean b) {
        List<Integer> draws = parseDraws(input);
        List<Board> boards = parseBoards(input);
        List<Board> winners = new ArrayList<>();
        for (int draw : draws) {
            for (Board board : boards) {
                boolean winner = board.applyDraw(draw);
                if (winner) {
                    if (boards.size() == 1) {
                        Util.log("we have a loser!\n%s", board);
                        return board.score();
                    }
                    winners.add(board);
                }
            }
            boards.removeAll(winners);
        }
        return -1;
    }

    private static List<Board> parseBoards(List<String> input) {
        List<Board> result = new ArrayList<>();
        for (int i = 2; i<input.size(); i += 6) {
            int[][] squares =  new int[5][];
            for (int j = 0; j < 5; ++j) {
                squares[j] =
                        Arrays.stream(input.get(i+j).split(" "))
                        .filter(v -> v.length() > 0)
                        .map(v -> Integer.parseInt(v)).mapToInt(v -> v)
                        .toArray();
            }
            result.add(new Board(squares));
        }
        return result;
    }

    private static List<Integer> parseDraws(List<String> input) {
        String[] chunks = input.get(0).split(",");
        return Arrays.stream(chunks)
                .map(v -> Integer.parseInt(v))
                .toList();
    }

}
