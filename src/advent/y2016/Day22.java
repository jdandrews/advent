package advent.y2016;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day22 {
    /*
     * header for Day22.txt
     *
     * root@ebhq-gridcenter# df -h
     * Filesystem               Size Used Avail Use%
     */
    private static record NodeStorage(int x, int y, int used, int avail) {
        @Override
        public final String toString() {
            return "[/dev/grid/node-x" + x + "-y" + y + "\tS: " + total() + "T\tU: " + used + "T\tA: " + avail + "T]";
        }

        public final String toStr() {
            return String.format("%3d U:%3d A:%3d",total(), used(), avail());
        }

        public int total() {
            return used + avail;
        }
    }

    private static record Pair(NodeStorage a, NodeStorage b) {
        @Override
        public final String toString() {
            return "(" + a.x + "," + a.y + ") -> (" + b.x + "," + b.y + ")";
        }
    }

    public static void main(String[] args) {
        List<NodeStorage> sample = parse(SAMPLE);
        List<NodeStorage> data = parse();

        solve1(sample);
        solve1(data);

        Util.log("");
        solve2(sample);
        Util.log("\n\n");
        solve2(data);
    }

    /**
     * By inspection, we have to empty node 0,0 to get node 34,0's data into it.
     *
     * By printing out the grid, we see a "bar" along row 11 that can't be moved, and one node with zero
     * content. Counting manually, we see 35 steps to move the "hole" at 27-14 to 34,0, then moving the
     * hole to the left of the data we want and moving it one block to the left takes 5 steps per move,
     * 33 moves, for a total of 35 + 5 * 33 = 200.
     *
     * @param data
     */
    private static void solve2(List<NodeStorage> data) {
        int x = -1;
        int y = -1;
        for (NodeStorage node : data) {
            x = Math.max(node.x(), x);
            y = Math.max(node.y(), y);
        }
        NodeStorage[][] grid = new NodeStorage[x+1][y+1];
        for (NodeStorage node : data) {
            grid[node.x()][node.y()] = node;
        }

        System.out.println("\n\nprinting total/used for each node\n");
        for (int i = 0; i<=y; ++i) {
            System.out.printf("%3d/%3d", grid[i][0].total(), grid[i][0].used());
            for (int j = 1; j<=x; ++j) {
                System.out.printf(" -- %3d/%3d", grid[j][i].total(), grid[j][i].used());
            }
            if (i!=y) {
                System.out.printf("\n%4s%10s%10s\n", "|", "|", "|");
            }
        }
    }

    private static void solve1(List<NodeStorage> data) {
        int nPairs = 0;
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < data.size(); ++j) {
                if (i == j) {
                    continue;
                }

                NodeStorage A = data.get(i);
                NodeStorage B = data.get(j);

                if (A.used() > 0 && A.used() <= B.avail()) {
                    ++ nPairs;
                }
            }
        }
        Util.log("part 1: found %d viable pairs", nPairs);
    }

    private static List<NodeStorage> parse() {
        List<String> raw;
        try {
            raw = Util.readInput("2016", "Day22.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parse(raw);
    }

    private static List<NodeStorage> parse(List<String> raw) {
        List<NodeStorage> result = new ArrayList<>();
        for (String s : raw) {
            String[] cols = s.split("[xyT-]");
            int x = Integer.parseInt(cols[2].trim());
            int y = Integer.parseInt(cols[4].split(" ")[0].trim());
            int used = Integer.parseInt(cols[5].trim());
            int avail = Integer.parseInt(cols[6].trim());

            result.add(new NodeStorage(x, y, used, avail));
        }
        return result;
    }

    private static final List<String> SAMPLE = List.of(
            "/dev/grid/node-x0-y0   10T    8T     2T   80%",
            "/dev/grid/node-x0-y1   11T    6T     5T   54%",
            "/dev/grid/node-x0-y2   32T   28T     4T   87%",
            "/dev/grid/node-x1-y0    9T    7T     2T   77%",
            "/dev/grid/node-x1-y1    8T    0T     8T    0%",
            "/dev/grid/node-x1-y2   11T    7T     4T   63%",
            "/dev/grid/node-x2-y0   10T    6T     4T   60%",
            "/dev/grid/node-x2-y1    9T    8T     1T   88%",
            "/dev/grid/node-x2-y2    9T    6T     3T   66%");
}
