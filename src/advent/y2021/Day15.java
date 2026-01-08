package advent.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.Util;
import advent.Util.Point;

public class Day15 {
    private static final List<String> SAMPLE = Arrays.asList( "1163751742", "1381373672", "2136511328", "3694931569",
            "7463417111", "1319128137", "1359912421", "3125421639", "1293138521", "2311944581");
    private static final List<String> PUZZLE = Util.readInput("2021", "Day15.txt");

    public static void main(String[] args) {
        int[][] map = parse(SAMPLE);
        Point end = new Point(map.length - 1, map[0].length - 1);
        Util.log("part 1: minimum risk path for SAMPLE is %d",
                new Path(getShortestPathDijkstra(map, new Point(0,0), end), map).getCost());

        map = make5xMap(map);
        end = new Point(map.length - 1, map[0].length - 1);
        Util.log("part 2: minimum risk path for SAMPLE is %d",
                new Path(getShortestPathDijkstra(map, new Point(0,0), end), map).getCost());

        Util.log("-----------");
        map = parse(PUZZLE);
        end = new Point(map.length - 1, map[0].length - 1);
        Util.log("part 1: minimum risk path for PUZZLE is %d",
                new Path(getShortestPathDijkstra(map, new Point(0,0), end), map).getCost());

        map = make5xMap(map);
        end = new Point(map.length - 1, map[0].length - 1);
        Util.log("part 2: minimum risk path for PUZZLE is %d",
                new Path(getShortestPathDijkstra(map, new Point(0,0), end), map).getCost());
    }

    private static int[][] make5xMap(int[][] map) {
        int[][] result = new int[5 * map.length][5 * map[0].length];
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 5; ++j) {
                        int newRisk = (map[row][col] - 1 + i + j) % 9 + 1;
                        result[row + map.length * i][col + map[0].length * j] = newRisk;
                        result[row + map.length * i][col + map[0].length * j] = newRisk;
                    }
                }
            }
        }
        return result;
    }

    // Don't really need this class anymore; it's left over from a previous iteration, and now it just computes
    // the cost of a given path.
    private static class Path extends ArrayList<Point> {
        private static final long serialVersionUID = 1L;
        private long cost = 0L;

        public Path(List<Point> points, int[][] map) {
            super(points);
            for (int i = 1; i<points.size(); ++i) {
                Point p = points.get(i);
                cost += map[p.x()][p.y()];
            }
        }

        // Should override all the default adders, but we're not going to use them.
        @Override
        public boolean add(Point e) {
            throw new UnsupportedOperationException("add should update pointSet and cost, and this doesn't");
        }

        public long getCost() {
            return cost;
        }
    }

    /**
     * Algorithm and notation from pseudocode found in https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     *
     *   1  function Dijkstra(Graph, source):
     *   2
     *   3      for each vertex v in Graph.Vertices:
     *   4          dist[v] ← INFINITY
     *   5          prev[v] ← UNDEFINED
     *   6          add v to Q
     *   7      dist[source] ← 0
     *   8
     *   9      while Q is not empty:
     *  10          u ← vertex in Q with minimum dist[u]
     *  11          Q.remove(u)
     *  12
     *  13          for each arc (u, v) in Q:
     *  14              alt ← dist[u] + Graph.Edges(u, v)
     *  15              if alt < dist[v]:
     *  16                  dist[v] ← alt
     *  17                  prev[v] ← u
     *  18
     *  19      return dist[], prev[]
     *
     *  To find the shortest path between vertices source and target, the search terminates after line 10 if u = target.
     *  The shortest path from source to target can be obtained by reverse iteration:
     *
     *  1  S ← empty sequence
     *  2  u ← target
     *  3  if prev[u] is defined or u = source:          // Proceed if the vertex is reachable
     *  4      while u is defined:                       // Construct the shortest path with a stack S
     *  5          S.push(u)                             // Push the vertex onto the stack
     *  6          u ← prev[u]                           // Traverse from target to source
     *
     * @param map the node-to-node cost grid
     * @param start starting point
     * @param end ending point
     * @return the list of points comprising the shortest path from start to end in map.
     */
    private static List<Point> getShortestPathDijkstra(int[][] map, Point start, Point end) {
        Map<Point, Long> distanceToStart = new HashMap<>();
        Map<Point, Point> previousHops = new HashMap<>();
        Set<Point> q = new HashSet<>();

        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                Point p = new Point(row, col);
                distanceToStart.put(p, Long.MAX_VALUE);
                previousHops.put(p, null);
                q.add(p);
            }
        }
        distanceToStart.put(start, 0L);

        while (! q.isEmpty()) {
            Point u = findMinimumDistance(q, distanceToStart);
            q.remove(u);

            for (Point v : getAdjacentVertices(map, u)) {
                long alt = distanceToStart.get(u) + map[v.x()][v.y()];
                if (alt < distanceToStart.get(v)) {
                    distanceToStart.put(v, alt);
                    previousHops.put(v, u);
                }
            }
        }

        List<Point> s = new ArrayList<>();
        Point u = end;
        if (previousHops.containsKey(u) || start.equals(end)) {
            while (u != null) {
                s.addFirst(u);
                u = previousHops.get(u);
            }
        }
        return s;
    }

    private static List<Point> getAdjacentVertices(int[][] map, Point u) {
        List<Point> result = new ArrayList<>();

        Point p = new Point(u.x() + 1, u.y());
        if (mapContainsPoint(map, p)) {
            result.add(p);
        }

        p = new Point(u.x() - 1, u.y());
        if (mapContainsPoint(map, p)) {
            result.add(p);
        }

        p = new Point(u.x(), u.y() + 1);
        if (mapContainsPoint(map, p)) {
            result.add(p);
        }

        p = new Point(u.x(), u.y() - 1);
        if (mapContainsPoint(map, p)) {
            result.add(p);
        }

        return result;
    }

    private static Point findMinimumDistance(Set<Point> q, Map<Point, Long> distanceToStart) {
        long minimum = Long.MAX_VALUE;
        Point result = null;
        for (Point p : q) {
            long d = distanceToStart.get(p).longValue();
            if (d < minimum) {
                result = p;
                minimum = d;
            }
        }
        return result;
    }

    private static boolean mapContainsPoint(int[][] map, Point n) {
        return n.x() >= 0 && n.x() < map.length && n.y() >= 0 && n.y() < map[0].length;
    }

    private static int[][] parse(List<String> in) {
        int[][] map = new int[in.size()][in.get(0).length()];
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                map[row][col] = in.get(row).charAt(col) - '0';
            }
        }
        return map;
    }
}
