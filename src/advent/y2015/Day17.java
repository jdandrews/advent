package advent.y2015;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.math3.util.CombinatoricsUtils;

import advent.Util;

public class Day17 {
    private static int[] containers = {11, 30, 47, 31, 32, 36, 3, 1, 5, 3, 32, 36, 15, 11, 46, 26, 28, 1, 19, 3};

    public static void main(String[] args) {
        Arrays.sort(containers);

        int sum = Arrays.stream(containers).sum();
        int count = (int) Arrays.stream(containers).count();
        Util.log("sum=%d n=%d", sum, count);
        int nFills = 0;
        boolean reported = false;
        int nMin = 0;
        for (int k=3; k<count-3; ++k) {
            Iterator<int[]> cIterator = CombinatoricsUtils.combinationsIterator(count, k);
            while(cIterator.hasNext()) {
                int[] c = cIterator.next();
                long s = Arrays.stream(c).mapToLong(i -> containers[i]).sum();
                if (s==150) {
                    nFills++;
                    ++nMin;
                }
            }
            if (! reported && nMin > 0) {
                Util.log("min=%d, nMin=%d", k, nMin);
                reported = true;
            }
        }
        Util.log("found %d", nFills);
    }
}
