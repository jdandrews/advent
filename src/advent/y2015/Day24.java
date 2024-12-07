package advent.y2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day24 {
    public static void main(String[] args) {
        log("--------- SAMPLE ---------");
        sortPackages(SAMPLE);

        log("--------- part 1 ---------");
        sortPackages(PART1);

        log("--------- SAMPLE 2 ---------");
        sortPackagesInto4(SAMPLE);

        log("--------- part 2 ---------");
        sortPackagesInto4(PART1);
    }

    private record Load(int[] g1, int[] g2, int[] g3) {
        public long entanglement() {
            long n = 1;
            for (int m : g1) {
                n *= m;
            }
            return n;
        }
    }

    private record LoadOf4(int[] g1, int[] g2, int[] g3, int[] g4) {
        public long entanglement() {
            long n = 1;
            for (int m : g1) {
                n *= m;
            }
            return n;
        }
    }

    // Smallest possible package is 6 for part 1; 2 for the sample
    private static void sortPackages(int[] packages) {
        int totalWeight = 0;
        for (int i = 0; i < packages.length; ++i) {
            totalWeight += packages[i];
        }

        if (totalWeight % 3 != 0) {
            throw new IllegalStateException("total weight is " + totalWeight + "; not divisible by 3");
        }
        int targetWeight = totalWeight / 3;
        log("Target weight = " + targetWeight);
        log("Total package count = " + packages.length);

        log("groups of 2:");
        List<int[]> groupings = findGroupings(packages, 2, targetWeight);
        log(groupings);
        if (groupings.size() > 0) {
            log(findBestLoad(packages, targetWeight, groupings));
            return;
        }

        log("groups of 6:");
        groupings = findGroupings(packages, 6, targetWeight);
        log(groupings);
        if (groupings.size() > 0) {
            log(findBestLoad(packages, targetWeight, groupings));
            return;
        }

        log("failed.");
        return;
    }

    // Smallest possible package is 5 for part 1; 2 for the sample
    private static void sortPackagesInto4(int[] packages) {
        int totalWeight = 0;
        for (int i = 0; i < packages.length; ++i) {
            totalWeight += packages[i];
        }

        if (totalWeight % 4 != 0) {
            throw new IllegalStateException("total weight is " + totalWeight + "; not divisible by 4");
        }
        int targetWeight = totalWeight / 4;
        log("Target weight = " + targetWeight);
        log("Total package count = " + packages.length);

        log("groups of 2:");
        List<int[]> groupings = findGroupings(packages, 2, targetWeight);
        log(groupings);
        if (groupings.size() > 0) {
            log(findBestLoadOf4(packages, targetWeight, groupings));
            return;
        }

        log("groups of 4:");
        groupings = findGroupings(packages, 4, targetWeight);
        log(groupings);
        if (groupings.size() > 0) {
            log(findBestLoad(packages, targetWeight, groupings));
            return;
        }

        /*
         * log("groups of 5:"); groupings = findGroupings(packages, 6, targetWeight);
         * log(groupings); /* if (groupings.size() > 0) { log(findBestLoad(packages,
         * targetWeight, groupings)); } return;
         */

        log("failed.");
        return;
    }

    private static Load findBestLoad(int[] packages, int targetWeight, List<int[]> groupings) {
        List<Load> loads = findLoads(groupings, packages, targetWeight);
        if (loads.isEmpty()) {
            throw new RuntimeException("groups of " + groupings.get(0).length + " fails");
        }
        return findMinimumEntanglement(loads);
    }

    private static LoadOf4 findBestLoadOf4(int[] packages, int targetWeight, List<int[]> groupings) {
        List<LoadOf4> loads = findLoadsOf4(groupings, packages, targetWeight);
        if (loads.isEmpty()) {
            throw new RuntimeException("groups of " + groupings.get(0).length + " fails");
        }
        return findMinimumEntanglementOf4(loads);
    }

    private static void log(Load load) {
        StringBuilder result = new StringBuilder("Load:{g1:");
        result.append(Arrays.toString(load.g1)).append(", g2: ").append(Arrays.toString(load.g2)).append(", g3: ")
        .append(Arrays.toString(load.g3)).append("}");
        log(result.toString());
        log("entanglement = " + load.entanglement());
    }

    private static void log(LoadOf4 load) {
        StringBuilder result = new StringBuilder("Load:{g1:");
        result.append(Arrays.toString(load.g1)).append(", g2: ").append(Arrays.toString(load.g2)).append(", g3: ")
        .append(Arrays.toString(load.g3)).append(", g4: ").append(Arrays.toString(load.g4)).append("}");
        log(result.toString());
        log("entanglement = " + load.entanglement());
    }

    private static Load findMinimumEntanglement(List<Load> loads) {
        long min = Long.MAX_VALUE;
        Load result = null;
        for (Load load : loads) {
            if (load.entanglement() < min) {
                result = load;
                min = load.entanglement();
            }
        }
        return result;
    }

    private static LoadOf4 findMinimumEntanglementOf4(List<LoadOf4> loads) {
        long min = Long.MAX_VALUE;
        LoadOf4 result = null;
        for (LoadOf4 load : loads) {
            if (load.entanglement() < min) {
                result = load;
                min = load.entanglement();
            }
        }
        return result;
    }

    private static List<Load> findLoads(List<int[]> group1list, int[] allPackages, int targetWeight) {
        List<Load> result = new ArrayList<>();
        for (int[] group1 : group1list) {
            int[] packages = getNonMembers(allPackages, group1);

            for (int size = group1.length; size < packages.length / 2; ++size) {
                List<int[]> group2List = findGroupings(packages, size, targetWeight);
                for (int[] group2 : group2List) {
                    int[] group3 = getNonMembers(packages, group2);
                    result.add(new Load(group1, group2, group3));
                }
            }
        }
        return result;
    }

    private static List<LoadOf4> findLoadsOf4(List<int[]> group1list, int[] allPackages, int targetWeight) {
        List<LoadOf4> result = new ArrayList<>();
        for (int[] group1 : group1list) {
            int[] packages1 = getNonMembers(allPackages, group1);

            for (int size = group1.length; size < packages1.length / 2; ++size) {
                List<int[]> group2List = findGroupings(packages1, size, targetWeight);
                for (int[] group2 : group2List) {
                    int[] packages2 = getNonMembers(packages1, group2);

                    for (int size2 = group1.length; size2 < packages2.length / 2; ++size2) {
                        List<int[]> group3List = findGroupings(packages2, size2, targetWeight);
                        for (int[] group3 : group3List) {
                            int[] group4 = getNonMembers(packages2, group3);
                            result.add(new LoadOf4(group1, group2, group3, group4));
                        }
                    }
                }
            }
        }
        return result;
    }

    private static int[] getNonMembers(int[] allPackages, int[] group1) {
        Arrays.sort(group1);
        int[] packages = new int[allPackages.length - group1.length];
        int i = 0;
        for (int j = 0; j < allPackages.length; ++j) {
            if (Arrays.binarySearch(group1, allPackages[j]) < 0) {
                packages[i++] = allPackages[j];
            }
        }
        return packages;
    }

    private static void log(List<int[]> groupings) {
        List<String> result = new ArrayList<>();
        for (int[] group : groupings) {
            result.add(Arrays.toString(group));
        }
        log(result.toString());
    }

    // see
    // https://stackoverflow.com/questions/29910312/algorithm-to-get-all-the-combinations-of-size-n-from-an-array-java
    private static List<int[]> findGroupings(int[] packages, int count, int weight) {
        List<int[]> result = new ArrayList<>();

        for (int[] candidate : findKSizedGroupings(packages, count)) {
            if (getWeight(candidate) == weight) {
                result.add(candidate);
            }
        }
        return result;
    }

    private static List<int[]> findKSizedGroupings(int[] input, int k) {
        List<int[]> subsets = new ArrayList<>();
        int[] s = new int[k]; // here we'll keep indices pointing to elements in input array

        if (k > input.length)
            return subsets;

        // first index sequence: 0, 1, 2, ...
        for (int i = 0; i < k; i++)
            s[i] = i;

        subsets.add(getSubset(input, s));

        for (;;) {
            int i;
            // find position of item that can be incremented
            for (i = k - 1; i >= 0 && s[i] == input.length - k + i; i--)
                ;

            if (i < 0) {
                break;
            }
            s[i]++; // increment this item
            for (++i; i < k; i++) { // fill up remaining items
                s[i] = s[i - 1] + 1;
            }
            subsets.add(getSubset(input, s));
        }
        return subsets;
    }

    private static int getWeight(int[] candidate) {
        int weight = 0;
        for (int w : candidate)
            weight += w;
        return weight;
    }

    // generate actual subset by index sequence
    private static int[] getSubset(int[] input, int[] subset) {
        int[] result = new int[subset.length];
        for (int i = 0; i < subset.length; i++)
            result[i] = input[subset[i]];
        return result;
    }

    private static void log(String s) {
        System.out.println(s);
    }

    static final int[] SAMPLE = { 1, 2, 3, 4, 5, 7, 8, 9, 10, 11 };
    static final int[] PART1 = { 1, 2, 3, 5, 7, 13, 17, 19, 23, 29, 31, 37, 41, 43, 53, 59, 61, 67, 71, 73, 79, 83, 89,
            97, 101, 103, 107, 109, 113 };
}
