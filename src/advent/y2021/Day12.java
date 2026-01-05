package advent.y2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day12 {

    private static class Node {
        private final String name;
        private Set<Node> exits = new HashSet<>();

        public Node(String nodeName) {
            this.name = nodeName;
        }

        public void addExit(Node to) {
            exits.add(to);
        }

        @Override
        public boolean equals(Object obj) {
            if (! this.getClass().isAssignableFrom(obj.getClass())){
                return false;
            }
            Node n = (Node)obj;
            return name.equals(n.name) && exits.equals(n.exits);
        }

        @Override
        public int hashCode() {
            StringBuilder exitNames = new StringBuilder();
            for (Node exit : exits) {
                exitNames.append(exit.name).append(" ");
            }
            return name.hashCode() + 13 * exitNames.hashCode();
        }

        public Object getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public Set<Node> getExits() {
            return new HashSet<>(exits);
        }

        public boolean canRevisit() {
            return Character.isUpperCase(name.charAt(0));
        }
    }

    private static class Path extends ArrayList<Node> {
        private Node extraVisit = null;

        public Path() {
            super();
        }

        public Path(Path path) {
            super(path);
            extraVisit = path.extraVisit;
        }

        public Node getExtraVisit() {
            return extraVisit;
        }

        public void setExtraVisit(Node n) {
            if (extraVisit != null) {
                throw new UnsupportedOperationException(
                        "can't reset extra visit; already contains " + extraVisit.toString());
            }
            extraVisit = n;
        }

        private static final long serialVersionUID = 1L;
        @Override
        public String toString() {
            return super.toString() + (extraVisit == null ? "" : "; extra="+extraVisit);
        }
    }

    private static List<String> SAMPLE = Arrays.asList("fs-end", "he-DX", "fs-he", "start-DX", "pj-DX", "end-zg", "zg-sl",
            "zg-pj", "pj-he", "RW-he", "fs-DX", "pj-RW", "zg-RW", "start-pj", "he-WI", "zg-he", "pj-fs", "start-RW");
    private static List<String> PUZZLE = Arrays.asList("zs-WO", "zs-QJ", "WO-zt", "zs-DP", "WO-end", "gv-zt", "iu-SK",
            "HW-zs", "iu-WO", "gv-WO", "gv-start", "gv-DP", "start-WO", "HW-zt", "iu-HW", "gv-HW", "zs-SK", "HW-end",
            "zs-end", "DP-by", "DP-iu", "zt-start");

    public static void main(String[] args) {
        Node start = parse(SAMPLE);
        Util.log("found %d paths through SAMPLE; expected 226", findPaths(start).size());
        Util.log("found %d extra paths through SAMPLE; expected 3509", findExtraPaths(start).size());

        Util.log("-----------");

        start = parse(PUZZLE);
        Util.log("found %d paths through PUZZLE", findPaths(start).size());
        Util.log("found %d extra paths through PUZZLE", findExtraPaths(start).size());
    }

    private static Set<Path> findPaths(Node start) {
        Set<Path> result = new HashSet<>();
        Deque<Path> stack = new ArrayDeque<>();

        Path path = new Path();
        path.add(start);
        stack.push(path);

        while (!stack.isEmpty()) {
            path = stack.pop();
            Node last = path.getLast();
            if (last.getName().equals("end")) {
                result.add(path);
                continue;
            }
            for (Node child : last.getExits()) {
                if (child.canRevisit() || ! path.contains(child)){
                    Path newPath = new Path(path);
                    newPath.add(child);
                    stack.push(newPath);
                }
            }
        }
        return result;
    }

    private static Set<Path> findExtraPaths(Node start) {
        Set<Path> result = new HashSet<>();
        Deque<Path> stack = new ArrayDeque<>();

        Path path = new Path();
        path.add(start);
        stack.push(path);

        while (!stack.isEmpty()) {
            path = stack.pop();
            Node last = path.getLast();
            if (last.getName().equals("end")) {
                result.add(path);
                continue;
            }
            for (Node child : last.getExits()) {
                if (child.canRevisit() || ! path.contains(child) ||
                        (! "start".equals(child.getName()) && ! "end".equals(child.getName()) && path.getExtraVisit() == null)){
                    Path newPath = new Path(path);
                    newPath.add(child);
                    if (! child.canRevisit() && path.contains(child)) {
                        newPath.setExtraVisit(child);
                    }
                    stack.push(newPath);
                }
            }
        }
        return result;
    }

    /**
     * Parses the input; returns the start node of a graph containing all the supplied nodes.
     * @param in
     * @return
     */
    private static Node parse(List<String> in) {
        Set<Node> nodes = new HashSet<>();
        Node start = null;
        for (String s : in) {
            String[] chunks = s.split("-");
            Node n1 = findOrAdd(nodes, chunks[0]);
            Node n2 = findOrAdd(nodes, chunks[1]);
            n1.addExit(n2);
            n2.addExit(n1);
            if (start == null && n1.getName().equals("start")) {
                start = n1;
            }
            if (start == null && n2.getName().equals("start")) {
                start = n2;
            }
        }
        return start;
    }

    private static Node findOrAdd(Set<Node> nodes, String s) {
        Node n = null;
        for (Node node : nodes) {
            if (node.getName().equals(s)) {
                n = node;
                break;
            }
        }
        if (n == null) {
            n = new Node(s);
            nodes.add(n);
        }
        return n;
    }

}
