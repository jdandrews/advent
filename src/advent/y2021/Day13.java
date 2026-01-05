package advent.y2021;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.Grid;
import advent.Util;
import advent.Util.Point;

public class Day13 {
    private static List<String> SAMPLE = Arrays.asList("6,10", "0,14", "9,10", "0,3", "10,4", "4,11", "6,0", "6,12",
            "4,1", "0,13", "10,12", "3,4", "3,0", "8,4", "1,10", "2,14", "8,10", "9,0",
            "", "fold along y=7", "fold along x=5");

    private static List<String> PUZZLE;
    static {
        try {
            PUZZLE = Util.readInput("2021", "Day13.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(10);
        }
    }

    private static record Fold(char axis, int value) {}

    public static void main(String[] args) {
        int[][] paper = parsePaper(SAMPLE);
        List<Fold> folds = parseFolds(SAMPLE);
        paper = fold(paper, folds.get(0));
        Util.log("SAMPLE folded 1 displays %d points; expected 17", countPoints(paper));
        Util.logT(paper);
        Util.log("");
        for (int i = 1; i<folds.size(); ++i) {
            paper = fold(paper, folds.get(i));
        }
        Util.logT(paper);

        Util.log("---------------");
        paper = parsePaper(PUZZLE);
        folds = parseFolds(PUZZLE);
        paper = fold(paper, folds.get(0));
        Util.log("PUZZLE folded 1 displays %d points", countPoints(paper));
        for (int i = 1; i<folds.size(); ++i) {
            paper = fold(paper, folds.get(i));
        }
        Util.logT(paper);
    }


    private static int countPoints(int[][] paper) {
        int count = 0;
        for (int x = 0; x < paper.length; ++x) {
            for (int y = 0; y < paper[0].length; ++y) {
                count += paper[x][y];
            }
        }
        return count;
    }


    private static int[][] fold(int[][] paper, Fold fold) {
        int[][] result;
        if (fold.axis() == 'x') {
            if (fold.value() > paper.length / 2) {
                throw new UnsupportedOperationException("can't fold " + fold.value() + " on width = " + paper[0].length);
            }
            result = new int[fold.value()][paper[0].length];

            for (int x = 0; x < fold.value(); ++x) {
                for (int y = 0; y < paper[0].length - 1; ++y) {
                    result[x][y] = paper[x][y];
                }
            }
            int newx = fold.value() - 1;
            for (int x = fold.value() + 1; x < paper.length; ++x) {
                for (int y = 0; y < paper[0].length; ++y) {
                    result[newx][y] = paper[x][y] + result[newx][y] > 0 ? 1 : 0;
                }
                --newx;
            }
        } else if (fold.axis() == 'y') {
            if (fold.value() > paper[0].length / 2) {
                throw new UnsupportedOperationException("can't fold " + fold.value() + " on length = " + paper[0].length);
            }
            result = new int[paper.length][fold.value()];
            for (int x = 0; x < paper.length; ++x) {
                for (int y = 0; y < paper[0].length / 2; ++y) {
                    result[x][y] = paper[x][y];
                }
            }
            for (int x = 0; x < paper.length; ++x) {
                int newy = fold.value() - 1;
                for (int y = fold.value() + 1; y < paper[0].length; ++y) {
                    result[x][newy] = paper[x][y] + result[x][newy] > 0 ? 1 : 0;
                    --newy;
                }
            }

        } else {
            throw new UnsupportedOperationException("Can't fold on axis " + fold.axis());
        }
        return result;
    }


    private static List<Fold> parseFolds(List<String> in) {
        int blankLineNumber = 0;
        while (blankLineNumber<in.size()) {
            if (in.get(blankLineNumber).trim().length() == 0) {
                break;
            }
            ++blankLineNumber;
        }

        List<Fold> result = new ArrayList<>();
        for (int i = blankLineNumber + 1; i < in.size(); ++i) {
            String[] chunks = in.get(i).substring("fold along ".length()).split("=");
            result.add(new Fold(chunks[0].charAt(0), Integer.parseInt(chunks[1])));
        }

        return result;
    }


    private static int[][] parsePaper(List<String> in) {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        List<Point> points = new ArrayList<>();
        for (int i = 0; in.get(i).trim().length() > 0; ++i) {
            Point p = Util.getIntegerPoint(in.get(i), ",");
            maxX = maxX >= p.x() ? maxX : p.x();
            maxY = maxY >= p.y() ? maxY : p.y();
            points.add(p);
        }

        int[][] result = new int[maxX + 1][maxY + 1];
        Grid.set(result, v -> 0);
        for (Point p : points) {
            result[p.x()][p.y()] = 1;
        }
        return result;
    }

}
