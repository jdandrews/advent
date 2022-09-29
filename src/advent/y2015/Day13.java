package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day13 {
    private enum Person {
        ALICE("Alice", 0), 
        BOB("Bob", 1), 
        CAROL("Carol", 2), 
        DAVID("David", 3), 
        ERIC("Eric", 4), 
        FRANK("Frank", 5),
        GEORGE("George", 6), 
        MALLORY("Mallory", 7), 
        JERRY("Jerry", 8);

        public String name;
        public int index;

        private Person(String n, int i) {
            this.name = n;
            this.index = i;
        }

        public static Person byName(String string) {
            for (Person person : values()) {
                if (person.name.equals(string)) {
                    return person;
                }
            }
            return null;
        }
    }

    private static int[][] graph;

    public static void main(String[] args) throws IOException {
        buildGraph(SAMPLE);
        findMaxHappiness();

        buildGraph(Util.readInput("2015", "Day13.txt"));
        findMaxHappiness();

        List<String> lines = Util.readInput("2015", "Day13.txt");
        lines.add("Jerry would gain 0 happiness units by sitting next to Alice.");
        lines.add("Alice would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to Bob.");
        lines.add("Bob would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to Carol.");
        lines.add("Carol would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to David.");
        lines.add("David would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to Eric.");
        lines.add("Eric would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to Frank.");
        lines.add("Frank would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to George.");
        lines.add("George would gain 0 happiness units by sitting next to Jerry.");
        lines.add("Jerry would gain 0 happiness units by sitting next to Mallory.");
        lines.add("Mallory would gain 0 happiness units by sitting next to Jerry.");

        buildGraph(lines);
        findMaxHappiness();
    }

    private static void findMaxHappiness() {
        int mostHappiness = 0;

        int[] orderedPeopleArray = new int[graph.length];
        for (int i=0; i<graph.length; ++i){ orderedPeopleArray[i] = i;}

        permutations = new ArrayList<>();
        permute(orderedPeopleArray, 0, orderedPeopleArray.length-1);

        Util.log("found %d permutations", permutations.size());
        Util.log("graph = \n%s", toString(graph));

        for (int[] permutation : permutations) {
            int total = 0;
            for (int i = 1; i < permutation.length; ++i) {
                total += graph[permutation[i - 1]][permutation[i]];
                total += graph[permutation[i]][permutation[i - 1]];
            }
            total += graph[permutation[0]][permutation[permutation.length - 1]];
            total += graph[permutation[permutation.length - 1]][permutation[0]];

            mostHappiness = Math.max(mostHappiness, total);
            if (mostHappiness==total) {
                Util.log("best so far at %d: %s", total, toString(permutation));
            }
        }

        System.out.println(mostHappiness);
    }

    private static String toString(int[][] array) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<array.length; ++i) {
            result.append(toString(array[i]));
            result.append("\n");
        }
        return result.toString();
    }

    private static String toString(int[] array) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<array.length; ++i){
            result.append(String.format("%4d",array[i]));
            result.append(' ');
        }
        return result.toString();
    }

    private static List<int[]> permutations = new ArrayList<>();

    private static void permute(int[] array, int l, int r) {
        if (l == r)
            permutations.add(array);
        else {
            for (int i = l; i <= r; i++) {
                array = swap(array, l, i);
                permute(array, l + 1, r);
                array = swap(array, l, i);
            }
        }
    }

    private static int[] swap(int[] a, int i, int j) {
        int[] b = new int[a.length];
        for (int x = 0; x < a.length; ++x) {
            b[x] = a[x];
        }
        b[i] = a[j];
        b[j] = a[i];
        return b;
    }

    private static void buildGraph(List<String> lines) {
        Set<String> names = new HashSet<>();
        for (String line : lines) {
            String[] data = line.split(" ");
            names.add(data[0]);
            names.add(data[10].substring(0, data[10].length() - 1));
        }

        Util.log("building graph for %d names.", names.size());

        graph = new int[names.size()][];
        for (int i=0; i<names.size(); ++i) {
            graph[i] = new int[names.size()];
        }

        for (String line : lines) {
            String[] data = line.split(" ");
            Person person1 = Person.byName(data[0]);
            Person person2 = Person.byName(data[10].substring(0, data[10].length() - 1));
            int distance = Integer.parseInt(data[3]);
            if ("lose".equals(data[2])) {
                distance = -distance;
            }

            graph[person1.index][person2.index] = distance;
        }
    }

    private static List<String> SAMPLE = new ArrayList<String>()
    { 
        private static final long serialVersionUID = 1L;
        {
            add("Alice would gain 54 happiness units by sitting next to Bob.");
            add("Alice would lose 79 happiness units by sitting next to Carol."); 
            add("Alice would lose 2 happiness units by sitting next to David.");
            add("Bob would gain 83 happiness units by sitting next to Alice.");
            add("Bob would lose 7 happiness units by sitting next to Carol.");
            add("Bob would lose 63 happiness units by sitting next to David.");
            add("Carol would lose 62 happiness units by sitting next to Alice.");
            add("Carol would gain 60 happiness units by sitting next to Bob.");
            add("Carol would gain 55 happiness units by sitting next to David.");
            add("David would gain 46 happiness units by sitting next to Alice.");
            add("David would lose 7 happiness units by sitting next to Bob.");
            add("David would gain 41 happiness units by sitting next to Carol.");
    } };
}
