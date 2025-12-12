package advent.y2025;

import java.util.ArrayList;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day12 {

    public static record Shape(char[][] grid, int coverage) { }

    public static record Tree(int rows, int cols, List<Shape> presents, String name) {
        @Override
        public final String toString() {
            return name;
        }
    }

    private static final String SAMPLE = "src/advent/y2025/Day12sample.txt";
    private static final String PUZZLE = "src/advent/y2025/Day12.txt";

    public static void main(String[] args) {
        List<Shape> shapes = parseShapes(FileIO.getFileAsList(SAMPLE));
        List<Tree> trees = parseTrees(FileIO.getFileAsList(SAMPLE), shapes);

        Util.log("for %d trees,\n\t%d are candidates for successful packing.", trees.size(), countViable(trees));
        Util.log("\t%d are trivally successfully packed.", countExpansive(trees));
        Util.log("\tHand-pack the following trees: %s", listHandPack(trees));

        Util.log("--------");
        shapes = parseShapes(FileIO.getFileAsList(PUZZLE));
        trees = parseTrees(FileIO.getFileAsList(PUZZLE), shapes);

        Util.log("for %d trees, %d are candidates for successful packing.", trees.size(), countViable(trees));
        Util.log("\t%d are trivally successfully packed.", countExpansive(trees));
        Util.log("\tHand-pack the following trees: %s", listHandPack(trees));
    }

    private static List<Tree> listHandPack(List<Tree> trees) {
        List<Tree> result = new ArrayList<>();
        for (Tree tree : trees) {
            if (tree.rows() * tree.cols() / 9 < tree.presents().size()
                    && (tree.rows() * tree.cols() > sumCoverage(tree.presents())) ) {
                result.add(tree);
            }
        }
        return result;
    }

    private static int countExpansive(List<Tree> trees) {
        int n = 0;
        for (Tree tree : trees) {
            if (tree.rows() * tree.cols() / 9 >= tree.presents().size()) {
                ++n;
            }
        }
        return n;
    }

    private static int countViable(List<Tree> trees) {
        int n = 0;
        for (Tree tree : trees) {
            if (tree.rows() * tree.cols() > sumCoverage(tree.presents())) {
                ++n;
            }
        }
        return n;
    }

    private static int sumCoverage(List<Shape> presents) {
        int n = 0;
        for (Shape present : presents) {
            n += present.coverage();
        }
        return n;
    }

    private static List<Tree> parseTrees(List<String> lines, List<Shape> availableShapes) {
        List<Tree> trees = new ArrayList<>();
        for (int i = 30; i < lines.size(); ++i) {
            String[] chunks = lines.get(i).split(" ");

            String[] size = chunks[0].split("x");
            int rows = Integer.parseInt(size[0]);
            int cols = Integer.parseInt(size[1].substring(0, size[1].length()-1));

            List<Shape> shapes = new ArrayList<>();
            for (int j = 1; j < chunks.length; ++j) {
                for (int k = 0; k < Integer.parseInt(chunks[j]); ++k) {
                    shapes.add(availableShapes.get(j-1));
                }
            }
            trees.add(new Tree(rows, cols, shapes, lines.get(i)));
        }
        return trees;
    }

    private static List<Shape> parseShapes(List<String> lines) {
        List<Shape> shapes = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            char[][] grid = new char[3][3];
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    grid[j][k] = lines.get(5 * i + j + 1).charAt(k);
                }
            }
            shapes.add(new Shape(grid, coverage(grid)));
        }
        return shapes;
    }

    private static int coverage(char[][] grid) {
        int n = 0;
        for (int i = 0; i<3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (grid[i][j] == '#' ) {
                    ++n;
                }
            }
        }
        return n;
    }
}
