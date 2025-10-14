package advent.y2016;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import advent.FileIO;

public class Day20 {
    private static final List<String[]> SAMPLE = new ArrayList<>();
    static {
        String[][] a = {{"5", "8"}, {"0","2"}, {"4","7"}};
        SAMPLE.add(a[0]);
        SAMPLE.add(a[1]);
        SAMPLE.add(a[2]);

    };
    public static void main(String[] args) {
        System.out.println("Sample:");
        solve(SAMPLE, 9L);

        System.out.println("\npart 1:");
        final List<String[]> DATA = FileIO.getFileLinesSplit("./src/advent/y2016/Day20.txt", "-");
        solve(DATA, 4294967295L);
    }

    private static void solve(List<String[]> rules, long maxAddress) {
        List<Long[]> loRules = new ArrayList<>(rules.size());

        for (String[] rule : rules) {
            Long[] loRule = new Long[2];
            loRule[0] = Long.parseLong(rule[0]);
            loRule[1] = Long.parseLong(rule[1]);

            loRules.add(loRule);
        }

        loRules.sort(new Comparator<Long[]>() {
            @Override
            public int compare(Long[] o1, Long[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });

        List<Long[]> blocked = new ArrayList<>();

        long lo = loRules.get(0)[0];
        long hi = loRules.get(0)[1];

        for (Long[] rule : loRules) {
            if (rule[0] == hi+1) {
                hi = rule[1];
            }
            else if (rule[0] > hi) { // start of a new block
                Long[] block = {lo, hi};
                blocked.add(block);

                lo = rule[0];
                hi = rule[1];
            }
            else {
                hi = Math.max(hi, rule[1]);
            }
        }
        Long[] block = {lo, hi};
        blocked.add(block);

        System.out.println("part 1:\nlowest = " + (blocked.get(0)[1] + 1));
        long nOpenAddresses = blocked.get(0)[0];

        for (int n = 1; n < blocked.size(); ++n) {
            nOpenAddresses += blocked.get(n)[0] - blocked.get(n-1)[1] - 1;
        }
        nOpenAddresses += maxAddress - blocked.get(blocked.size()-1)[1];

        System.out.println("part 2:\nopenAddresses = " + nOpenAddresses);
    }

}
