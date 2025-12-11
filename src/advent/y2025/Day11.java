package advent.y2025;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day11 {
    private static final List<String> SAMPLE = Arrays.asList(
            "aaa: you hhh",
            "you: bbb ccc",
            "bbb: ddd eee",
            "ccc: ddd eee fff",
            "ddd: ggg",
            "eee: out",
            "fff: out",
            "ggg: out",
            "hhh: ccc fff iii",
            "iii: out");

    private static final List<String> SAMPLE2 = Arrays.asList(
            "svr: aaa bbb",
            "aaa: fft",
            "fft: ccc",
            "bbb: tty",
            "tty: ccc",
            "ccc: ddd eee",
            "ddd: hub",
            "hub: fff",
            "eee: dac",
            "dac: fff",
            "fff: ggg hhh",
            "ggg: out",
            "hhh: out");

    private static final List<String> PUZZLE = FileIO.getFileAsList("src/advent/y2025/Day11.txt");

    private static final List<String> INCLUDES = Arrays.asList("fft", "dac");
    private static final List<String> NO_INCLUDES = new ArrayList<>();

    private static class PathFinder implements Runnable {
        private String start;
        private String end;
        private boolean runForward;

        public PathFinder(String start, String end, boolean runForward) {
            this.start = start;
            this.end = end;
            this.runForward = runForward;
        }

        @Override
        public void run() {
            long n;
            if (runForward) {
                n = countPaths(PUZZLE, start, end);
            }
            else {
                n = countReversePaths(PUZZLE, start, end);
            }
            Util.log("found %d paths from '%s' to '%s' %s", n, start, end, (runForward ? "" : "(backwards)"));
        }

    }

    public static void main(String[] args) {
        Util.log("part 1 SAMPLE found %d paths from 'you' to 'out'", countPaths(SAMPLE, "you", "out", NO_INCLUDES) );
        Util.log("part 1 puzzle found %d paths from 'you' to 'out'", countPaths(PUZZLE, "you", "out", NO_INCLUDES) );
        Util.log("----");
        // naive solution runs for the sample
        Util.log("part 2 SAMPLE found %d paths from 'svr' to 'out'", countPaths(SAMPLE2, "svr", "out", INCLUDES) );

        // runs forever:
        //Util.log("part 2 puzzle found %d paths from 'svr' to 'out'", countPaths(PUZZLE,  "svr", "out", INCLUDES) );

        // 0 paths from dac to fft, so the only path is svr->fft->dac->out
        Util.log("part 2 puzzle found %d paths from 'dac' to 'fft' (just confirming)", countPaths(PUZZLE, "dac", "fft"));


        Util.log("");

        // this runs fast
        long n0 = countPaths(PUZZLE, "dac", "out");
        Util.log("part 2 puzzle found %d paths from 'dac' to 'out'", n0);

        Set<Thread> threads = new HashSet<>();
        threads.add(Thread.startVirtualThread(new PathFinder("svr", "fft", true)));
        threads.add(Thread.startVirtualThread(new PathFinder("svr", "fft", false)));
        threads.add(Thread.startVirtualThread(new PathFinder("fft", "dac", true)));
        threads.add(Thread.startVirtualThread(new PathFinder("fft", "dac", false)));

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private static int countPaths(List<String> in, String start, String end) {
        return countPaths(in, start, end, NO_INCLUDES);
    }

    private static record Device(String name, List<String> outputs) {}

    private static int countReversePaths(List<String> in, String start, String end) {
        Map<String, Device> nameToDevice = parse(in);
        Map<String, Set<Device>> outletNameToDevice = mapOutletNamesToDevices(nameToDevice);

        Deque<String> stack = new ArrayDeque<>();

        stack.push(end);

        int counter = 0;
        while (! stack.isEmpty()) {
            String path = stack.pop();
            String pathEnd = path.substring(path.length() - 3);
            if (start.equals(pathEnd)) {
                ++counter;
            } else if ("svr".equals(pathEnd)) {
                // no path back from here; skipping.
            }
            else {
                for (Device input : outletNameToDevice.get(pathEnd)) {
                    if (path.contains(input.name)) {
                        continue;   // loop detected
                    }
                    stack.push(path + " " + input.name);
                }
            }
        }
        return counter;
    }

    private static Map<String, Set<Device>> mapOutletNamesToDevices(Map<String, Device> nameToDevice) {
        Map<String, Set<Device>> result = new HashMap<>();
        for (Device device : nameToDevice.values()) {
            for (String output : device.outputs()) {
                if (! result.containsKey(output)) {
                    result.put(output, new HashSet<>());
                }
                result.get(output).add(device);
            }
        }
        return result;
    }

    private static int countPaths(List<String> in, String start, String end, List<String> includes) {
        Map<String, Device> nameToDevice = parse(in);

        Deque<String> stack = new ArrayDeque<>();

        stack.push(start);

        int counter = 0;
        while (! stack.isEmpty()) {
            String path = stack.pop();
            String pathEnd = path.substring(path.length() - 3);
            if (end.equals(pathEnd)) {
                boolean validPath = true;
                for (String include : includes) {
                    if (!path.contains(include)) {
                        validPath = false;
                    }
                }
                if (validPath) {
                    ++counter;
                }
            } else if ("out".equals(pathEnd)) {
                // no path to the end on this branch
            }
            else {
                Device d = nameToDevice.get(pathEnd);
                if (d == null) {
                    Util.log("no path to %s; path = %s", pathEnd, path);
                }
                for (String output : d.outputs()) {
                    if (path.contains(output)) {
                        continue;   // loop detected
                    }
                    stack.push(path + " " + output);
                }
            }
        }
        return counter;
    }

    private static Map<String,Device> parse(List<String> in) {
        Map<String, Device> devices = new HashMap<>();

        for (String line : in) {
            String[] chunks = line.split(" ");
            String name = chunks[0].substring(0, 3);
            List<String> out = new ArrayList<>();
            for (int i = 1; i < chunks.length; ++i) {
                out.add(chunks[i]);
            }
            devices.put(name, new Device(name, out));
        }

        return devices;
    }
}
