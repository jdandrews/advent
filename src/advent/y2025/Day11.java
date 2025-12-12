package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
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
            long n = 0;
            if (runForward) {
                n = countPaths(PUZZLE, start, end);
            }
            else {
                //                n = countReversePaths(PUZZLE, start, end);
            }
            Util.log("found %d paths from '%s' to '%s' %s", n, start, end, (runForward ? "" : "(backwards)"));
        }

    }

    public static void main(String[] args) {
        Util.log("part 1 SAMPLE found %d paths from 'you' to 'out'", countPaths(SAMPLE, "you", "out") );
        Util.log("part 1 puzzle found %d paths from 'you' to 'out'", countPaths(PUZZLE, "you", "out") );
        Util.log("----");
        Util.log("part 2 SAMPLE found %d paths from 'svr' to 'out'", countPaths(SAMPLE2, "svr", "out", true) );

        long n0 = countPaths(SAMPLE2, "svr", "fft");
        long n1 = countPaths(SAMPLE2, "fft", "dac");
        long n2 = countPaths(SAMPLE2, "dac", "out");
        long r1 = n0 * n1 * n2;

        n0 = countPaths(SAMPLE2, "svr", "dac");
        n1 = countPaths(SAMPLE2, "dac", "fft");
        n2 = countPaths(SAMPLE2, "fft", "out");
        long r2 = n0 * n1 * n2;

        long r3 = r1 + r2;
        Util.log("part 2 SAMPLE found %d paths from 'svr' to 'out' including fft and dar", r3);

        n0 = countPaths(PUZZLE, "svr", "fft");
        n1 = countPaths(PUZZLE, "fft", "dac");
        n2 = countPaths(PUZZLE, "dac", "out");
        r1 = n0 * n1 * n2;

        n0 = countPaths(PUZZLE, "svr", "dac");
        n1 = countPaths(PUZZLE, "dac", "fft");
        n2 = countPaths(PUZZLE, "fft", "out");
        r2 = n0 * n1 * n2;

        r3 = r1 + r2;

        Util.log("part 2 puzzle found %d paths from 'svr' to 'out' including fft and dar", r3);
    }

    private static record Device(String name, List<String> outputs) {}

    private static Map<String, Set<String>> mapOutletNamesToDevices(Map<String, Device> nameToDevice) {
        Map<String, Set<String>> result = new HashMap<>();
        for (Device device : nameToDevice.values()) {
            for (String output : device.outputs()) {
                if (! result.containsKey(output)) {
                    result.put(output, new HashSet<>());
                }
                result.get(output).add(device.name());
            }
        }
        return result;
    }

    private static long countPaths(List<String> in, String start, String end) {
        return countPaths(in, start, end, false);
    }

    private static long countPaths(List<String> in, String start, String end, boolean includeDacAndFft) {
        Map<String, Device> nameToDevice = parse(in);
        Map<String, Set<String>> nameToInbounds = mapOutletNamesToDevices(nameToDevice);
        Map<String, Long> resultCache = new HashMap<>();

        return countPaths(start, end, nameToDevice, resultCache);
    }

    private static long countPaths(String start, String end, Map<String, Device> nameToDevice, Map<String, Long> resultCache) {
        if ("out".equals(start) && !end.contentEquals("out")) {
            // no path from out to anything; don't add anything to the recursion resut
            return 0;
        }

        if (resultCache.containsKey(start)) {
            return resultCache.get(start);
        }

        // we need 1 here, not zero, because this really is just the last step in the recursion.
        if (start.equals(end)) {
            return 1;
        }

        long counter = 0;
        for (String output : nameToDevice.get(start).outputs()) {
            counter += countPaths(output, end, nameToDevice, resultCache);
        }
        resultCache.put(start, counter);

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
