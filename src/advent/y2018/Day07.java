package advent.y2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import advent.FileIO;
import advent.Util;

public class Day07 {
    private static class Entry {
        public char identity;
        public char predecessor;

        public Entry(String id, String pre) {
            identity = id.charAt(0);
            predecessor = pre.charAt(0);
        }
        @Override
        public String toString() {
            return predecessor + " -> " + identity;
        }
    }

    public static void main(String[] args) {
        List<String> sample = Arrays.asList(SAMPLE);
        List<String> data = FileIO.getFileAsList("src/advent/y2018/Day07.txt");

        Util.log("sample order is %s", executionOrder(parse(sample)));
        Util.log("data order is %s", executionOrder(parse(data)));
    }

    private static String executionOrder(List<Entry> entries) {
        StringBuilder result = new StringBuilder();

        Set<Character> unusedNodes = new HashSet<>();
        for (Entry e : entries) {
            unusedNodes.add(e.identity);
            unusedNodes.add(e.predecessor);
        }

        Set<Character> usedNodes = new HashSet<>();
        while (unusedNodes.size()>0) {
            Set<Character> availableNodes = new HashSet<>(unusedNodes);
            for (Entry e : entries) {
                if ( ! usedNodes.contains(e.predecessor)) {
                    availableNodes.remove(e.identity);
                }
            }
            availableNodes.removeAll(usedNodes);
            List<Character> sortMe = new ArrayList<>(availableNodes);
            Collections.sort(sortMe);

            char next = sortMe.get(0);

            result.append(next);
            usedNodes.add(next);
            unusedNodes.remove(next);
        }

        return result.toString();
    }

    private static final int A = (new Character('A')).charValue();

    private static class Worker {
        public int timeDone;
        public void start(Character c, int now) {
            timeDone = 60 + c.charValue() - A + 1;
        }
    }

    private static int executionTime(List<Entry> entries) {
        Stack<Worker> availableWorkers = new Stack<>();
        Stack<Worker> busyWorkers = new Stack<>();
        for (int i=0; i<4; ++i) {
            availableWorkers.push(new Worker());
        }

        while (availableWorkers.size() > 0) {

        }

        int result = 0;

        Set<Character> unusedNodes = new HashSet<>();
        for (Entry e : entries) {
            unusedNodes.add(e.identity);
            unusedNodes.add(e.predecessor);
        }

        Set<Character> usedNodes = new HashSet<>();
        while (unusedNodes.size()>0) {
            Set<Character> availableNodes = new HashSet<>(unusedNodes);
            for (Entry e : entries) {
                if ( ! usedNodes.contains(e.predecessor)) {
                    availableNodes.remove(e.identity);
                }
            }
            availableNodes.removeAll(usedNodes);
            List<Character> sortMe = new ArrayList<>(availableNodes);
            Collections.sort(sortMe);

            char next = sortMe.get(0);

            // converting from strings to sum here
//------->  result.append(next);
            usedNodes.add(next);
            unusedNodes.remove(next);
        }

        return 0; // result.toString();
    }

    private static List<Entry> parse(List<String> strings) {
        List<Entry> result = new ArrayList<>();
        for (String s : strings) {
            String[] parts = s.split(" ");
            result.add(new Entry(parts[7], parts[1]));
        }
        return result;
    }

    private static final String[] SAMPLE = {
            "Step C must be finished before step A can begin.",
            "Step C must be finished before step F can begin.",
            "Step A must be finished before step B can begin.",
            "Step A must be finished before step D can begin.",
            "Step B must be finished before step E can begin.",
            "Step D must be finished before step E can begin.",
            "Step F must be finished before step E can begin."
    };
}
