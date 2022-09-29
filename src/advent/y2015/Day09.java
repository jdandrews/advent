package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day09 {
    private enum City {
        FAERUN("Faerun", 0), 
        TRISTRAM("Tristram", 1), 
        TAMBI("Tambi", 2), 
        NORRATH("Norrath", 3), 
        SNOWDIN("Snowdin", 4),
        STRAYLIGHT("Straylight", 5), 
        ALPHACENTAURI("AlphaCentauri", 6), 
        ARBRE("Arbre", 7);

        public String name;
        public int index;

        private City(String n, int i) {
            this.name = n;
            this.index = i;
        }

        public static City byName(String string) {
            for (City city : values()) {
                if (city.name.equals(string)) {
                    return city;
                }
            }
            return null;
        }
    };

    private static int[][] graph = new int[8][];
    static {
        for (int i = 0; i < 8; ++i) {
            graph[i] = new int[8];
        }
    }

    public static void main(String[] args) throws IOException {
        buildGraph(Util.readInput("2015", "Day09.txt"));

        int shortestDistance = Integer.MAX_VALUE;
        int longestDistance = 0;
        int[] orderedCityArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
        permute(orderedCityArray, 0, 7);

        Util.log("found %d permutations", permutations.size());

        for (int[] permutation : permutations) {
            int total = 0;
            for (int i=1; i<permutation.length; ++i) {
                total += graph[permutation[i-1]][permutation[i]];
            }

            shortestDistance = Math.min(shortestDistance, total);
            longestDistance = Math.max(longestDistance, total);
        }

        System.out.println(shortestDistance);
        System.out.println(longestDistance);
    }
/*
    private static Object toString(int[] permutation) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<permutation.length; ++i){
            result.append(permutation[i]);
            result.append(' ');
        }
        return result.toString();
    }
*/
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
        for (int x=0; x<a.length; ++x) {
            b[x] = a[x];
        }
        b[i] = a[j];
        b[j] = a[i];
        return b;
    }

    private static void buildGraph(List<String> lines) {
        for (String line : lines) {
            String[] data = line.split(" ");
            City city1 = City.byName(data[0]);
            City city2 = City.byName(data[2]);
            int distance = Integer.parseInt(data[4]);

            graph[city1.index][city2.index] = distance;
            graph[city2.index][city1.index] = distance;
        }
    }

}
