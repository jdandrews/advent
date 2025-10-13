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
        solve(SAMPLE);

        System.out.println("\npart 1:");
        final List<String[]> DATA = FileIO.getFileLinesSplit("./src/advent/y2016/Day20.txt", "-");
        solve(DATA);
    }

    // TODO: this algorithm produces a result that is too low.
    private static void solve(List<String[]> rules) {
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

        long lowest;
        if (loRules.get(0)[0] == 0L) {
            lowest = loRules.get(0)[1] + 1;
        } else {
            lowest = loRules.get(0)[0] - 1;
        }

        for (Long[] rule : loRules) {
            if (rule[0] > lowest) {
                break;
            }
            if (rule[0] <= lowest) {
                if (rule[1] > lowest) {
                    lowest = Math.max(lowest, rule[1] + 1);
                }
            }
        }
        System.out.println("lowest = " + lowest);
    }

}
