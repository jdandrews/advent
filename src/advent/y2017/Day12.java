package advent.y2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;
import advent.y2017.datastructures.graph.DepthFirstSearch;
import advent.y2017.datastructures.graph.Graph;

public class Day12 {
    static Graph g = new Graph(2000);
    /**
     * each entry in this list is a set of nodes.  Each set contains one group in the graph. Each group should
     * only occur once (if my code is correct).
     */
    static List<Set<Integer>> groups = new ArrayList<>();

    private static class Pipes {
        public int program;
        public List<Integer> connections = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        List<Pipes> pipesList = getPipes(Util.readInput("2017", "Day12.txt"));
        buildPipesGraph(pipesList);

        Util.log("found %d paths leading to 0", countPaths(0));

        buildGroupList();
        Util.log("found %d groups", groups.size());
    }

    private static void buildGroupList() {
        Set<Integer> sieve = new HashSet<>();
        for (int i=0; i<g.V(); ++i) {
            sieve.add(i);
        }

        while (sieve.size() > 0) {
            int hook = sieve.iterator().next();
            Set<Integer> currentGroup = new HashSet<>();
            DepthFirstSearch dfs = new DepthFirstSearch(g, hook);
            for (int value : sieve) {
                if (dfs.marked(value)) {
                    currentGroup.add(value);
                }
            }
            sieve.removeAll(currentGroup);
            groups.add(currentGroup);
        }
    }

    private static int countPaths(int fromTarget) {
        DepthFirstSearch dfs = new DepthFirstSearch(g, fromTarget);

        return dfs.count();
    }

    private static void buildPipesGraph(List<Pipes> pipesList) {
        for (Pipes pipes : pipesList) {
            for (int target : pipes.connections) {
                g.addEdge(pipes.program, target);
            }
        }
    }

    private static List<Pipes> getPipes(List<String> inputStrings) {
        List<Pipes> result = new ArrayList<>();

        for (String pipeList : inputStrings) {
            String[] pipeItems = pipeList.split(" ");
            Pipes pipes = new Pipes();
            pipes.program = Integer.parseInt(pipeItems[0]);
            for (int i=2; i<pipeItems.length; ++i) {
                String target = pipeItems[i];
                if (target.endsWith(","))
                    target = target.substring(0,  target.length()-1);
                pipes.connections.add(Integer.parseInt(target));
            }
            result.add(pipes);
        }

        return result;
    }
}
