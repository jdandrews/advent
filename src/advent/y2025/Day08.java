package advent.y2025;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day08 {
    private static final List<String> SAMPLE = Arrays.asList(
            "162,817,812",
            "57,618,57",
            "906,360,560",
            "592,479,940",
            "352,342,300",
            "466,668,158",
            "542,29,236",
            "431,825,988",
            "739,650,466",
            "52,470,668",
            "216,146,977",
            "819,987,18",
            "117,168,530",
            "805,96,715",
            "346,949,466",
            "970,615,88",
            "941,993,340",
            "862,61,35",
            "984,92,344",
            "425,690,689");

    public static record Box(int x, int y, int z) {}
    public static record Pair(Box a, Box b, long distance) { }

    public static void main(String[] args) throws IOException {
        List<Pair> sortedPairs = buildPairList(SAMPLE);
        List<Set<Box>> circuits = buildCircuits(sortedPairs, 10);
        Util.log("part 1 SAMPLE found %d is the product of the largest 3 circuits", multiplyLargestCircuitSizes(circuits));

        Pair finalPair = buildCircuitsUntilDone(sortedPairs, SAMPLE.size());
        Util.log("part 2 SAMPLE found %d = x's from %s and %s.", finalPair.a().x() * finalPair.b().x(), finalPair.a(), finalPair.b());

        Util.log("---------");

        List<String> lines = FileIO.getFileAsList("src/advent/y2025/Day08.txt");
        sortedPairs = buildPairList(lines);
        circuits = buildCircuits(sortedPairs, 1000);
        Util.log("part 1 puzzle found %d is the product of the largest 3 circuits", multiplyLargestCircuitSizes(circuits));

        finalPair = buildCircuitsUntilDone(sortedPairs, lines.size());
        Util.log("part 2 puzzle found %d = x's from %s and %s.", 1L * finalPair.a().x() * finalPair.b().x(), finalPair.a(), finalPair.b());
    }

    private static Pair buildCircuitsUntilDone(List<Pair> sortedPairs, int boxCount) {
        Pair pair = null;
        List<Set<Box>> circuits = new ArrayList<>();
        int i = 0;
        do {
            pair = sortedPairs.get(i++);

            circuits = linkPair(circuits, pair);
        } while (circuits.size() != 1 || circuits.get(0).size() != boxCount);

        Util.log("final link at i = %d", i);

        return pair;
    }

    private static List<Set<Box>> buildCircuits(List<Pair> sortedPairs, int linkCount) {
        List<Set<Box>> circuits = new ArrayList<>();
        for (int i = 0; i<linkCount; ++i) {
            Pair pair = sortedPairs.get(i);

            circuits = linkPair(circuits, pair);
        }
        return circuits;
    }

    private static List<Set<Box>> linkPair(List<Set<Box>> circuits, Pair pair) {
        int aIndex = findInCircuits(circuits, pair.a());
        int bIndex = findInCircuits(circuits, pair.b());

        if (aIndex < 0 && bIndex < 0) {
            Set<Box> circuit = new HashSet<>();
            circuit.add(pair.a());
            circuit.add(pair.b());

            circuits.add(circuit);
        }
        else if (aIndex >= 0 && bIndex >= 0 && aIndex != bIndex) {
            circuits.get(aIndex).addAll(circuits.get(bIndex));

            // we should see the largest as 52 (/43/38). I have no idea why this is doing this.
            if (circuits.get(aIndex).size() > 60) {
                throw new IllegalStateException("we have now " + circuits.size() + " circuits, the largest of which is " + circuits.get(aIndex).size());
            }

            Set<Box> removed = circuits.remove(bIndex);

            if (removed == null || removed.size() == 0){
                throw new IllegalStateException("Unable to remove circuit B");
            }
        }
        else if (bIndex < 0) {
            circuits.get(aIndex).add(pair.b());
        }
        else if (aIndex < 0) {
            circuits.get(bIndex).add(pair.a());
        }
        else {
            // no-op--both boxes are already in the same circuit
            assert aIndex >= 0 && aIndex == bIndex;
        }
        //        Util.log("linked %s to %s; there are %d circuits", finalPair.a(), finalPair.b(), circuits.size());
        return circuits;
    }

    private static List<Pair> buildPairList(List<String> boxes){
        List<Pair> result = new ArrayList<>();
        Set<Box> boxSet = new HashSet<>();
        for (int i = 0; i<boxes.size() - 1; ++i) {
            for (int j = i+1; j<boxes.size(); ++j) {
                int[] coords = Util.getIntegers(boxes.get(i), ",");
                Box a = new Box(coords[0], coords[1], coords[2]);

                coords = Util.getIntegers(boxes.get(j), ",");
                Box b = new Box(coords[0], coords[1], coords[2]);

                result.add(new Pair(a, b, distance(a,b)));
                boxSet.add(a);
                boxSet.add(b);
            }
            assert boxSet.size() == boxes.size();
        }

        result.sort(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return Long.valueOf(o1.distance()).compareTo(o2.distance());
            }
        });

        return result;
    }

    private static long distance(Box a, Box b) {
        int dx = a.x() - b.x();
        int dy = a.y() - b.y();
        int dz = a.z() - b.z();

        return dx * dx + dy * dy + dz * dz;
    }

    private static long multiplyLargestCircuitSizes(List<Set<Box>> circuits) {
        circuits.sort(new Comparator<Set<Box>>() {
            @Override
            public int compare(Set<Box> o1, Set<Box> o2) {
                return Integer.valueOf(o1.size()).compareTo(o2.size());
            }
        });

        Collections.reverse(circuits);

        long product = 1;
        List<Integer> circuitSizes = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            circuitSizes.add(circuits.get(i).size());
            product *= circuits.get(i).size();
        }
        Util.log("Three largest circuits have sizes: %s", circuitSizes);
        return product;
    }

    private static int findInCircuits(List<Set<Box>> circuits, Box box) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < circuits.size(); ++i) {
            if (circuits.get(i).contains(box)) {
                results.add(i);
            }
        }
        if (results.size() > 1) {
            throw new IllegalStateException("more than one circuit contains this box: " + box);
        }
        else if (results.size() == 1) {
            return results.get(0);
        }
        return -1;
    }
}
