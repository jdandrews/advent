package advent.y2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day07 {
    private static class Disk {
        public String name;
        public int weight;
        public int totalWeight;
        public List<String> childNames = new ArrayList<>();
        public List<Disk> children = new ArrayList<>();
        public Disk parent;

        public Disk(String s) {
            String[] chunks = s.split(" ");
            name = chunks[0];
            weight = Integer.parseInt(chunks[1].substring(1, chunks[1].length()-1));
            for (int i=3; i<chunks.length; ++i) {
                String child = chunks[i];
                if (child.endsWith(",")) {
                    child = child.substring(0, child.length()-1);
                }
                childNames.add(child);
            }
        }
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(name);
            result.append(" (");
            result.append(weight);
            result.append(")");
            result.append("[");
            result.append(totalWeight);
            result.append("]");
            if (! childNames.isEmpty()) {
                result.append(" -> ");
                for (String childName : childNames) {
                    result.append(childName);
                    result.append(", ");
                }
                result.delete(result.length()-2, result.length());
            }
            return result.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readInput("2017", "Day07.txt");
        // input = TEST;

        Map<String,Disk> nameToDisk = new HashMap<>();
        for (String diskString : input) {
            Disk disk = new Disk(diskString);
            nameToDisk.put(disk.name, disk);
        }

        for (Disk disk : nameToDisk.values()) {
            for (String name : disk.childNames) {
                disk.children.add(nameToDisk.get(name));
                nameToDisk.get(name).parent = disk;
            }
        }

        Disk root = nameToDisk.values().iterator().next();
        while (root.parent != null) {
            root = root.parent;
        }

        root.totalWeight = getTotalWeight(root);

        Util.log("root disk is %s", root);

        Disk liar = findLiar(root);
        Util.log("liar is %s", liar);

        int refWeight = liar.parent.children.get(0)==liar ? liar.parent.children.get(1).totalWeight : liar.parent.children.get(0).totalWeight;
        Util.log("liar weight should be %s", liar.weight - liar.totalWeight + refWeight);
    }

    private static Disk findLiar(Disk disk) {
        if (disk==null) {
            return null;
        }

        Disk liar = findLiar(disk.children);
        if (liar == null) {
            return disk;
        }
        return findLiar(liar);
    }

    private static Disk findLiar(List<Disk> disks) {
        if (disks.isEmpty() || disks.size() < 3) {
            return null;
        }

        int refWeight;
        if (disks.get(0).totalWeight == disks.get(1).totalWeight) {
            refWeight = disks.get(0).totalWeight;
        } else {
            refWeight = disks.get(2).totalWeight;
        }

        Disk liar = null;
        for (Disk candidate : disks) {
            if (candidate.totalWeight != refWeight) {
                liar = candidate;
                break;
            }
        }

        return liar;
    }

    private static int getTotalWeight(Disk disk) {
        int result = disk.weight;
        for (Disk child : disk.children) {
            child.totalWeight = getTotalWeight(child);
            result += child.totalWeight;
        }
        return result;
    }

    private static final List<String> TEST = Arrays.asList(
            "pbga (66)",
            "xhth (57)",
            "ebii (61)",
            "havc (66)",
            "ktlj (57)",
            "fwft (72) -> ktlj, cntj, xhth",
            "qoyq (66)",
            "padx (45) -> pbga, havc, qoyq",
            "tknk (41) -> ugml, padx, fwft",
            "jptl (61)",
            "ugml (68) -> gyxo, ebii, jptl",
            "gyxo (61)",
            "cntj (57)"
            );
}
