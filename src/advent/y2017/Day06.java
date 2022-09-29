package advent.y2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day06 {
    private static List<Integer> TEST = Arrays.asList(0, 2, 7, 0);
    private static List<Integer> DATA = Arrays.asList(4, 1, 15, 12, 0, 9, 9, 5, 5, 8, 7, 3, 14, 5, 12, 3);
    private static Set<String> configs = new HashSet<>();

    public static void main(String[] args) {
        List<Integer> memory = new ArrayList<>(DATA);

        int count = 0;
        while (! configs.contains(memory.toString())) {
            configs.add(memory.toString());
            count = rebin(memory, count);
        }

        Util.log("found the first cycle at %d", count);

        configs.clear();
        count = 0;
        while (! configs.contains(memory.toString())) {
            configs.add(memory.toString());
            count = rebin(memory, count);
        }

        Util.log("cycle size is %d", count);
    }

    private static int rebin(List<Integer> memory, int count) {
        int fullestBinIndex = fullestBin(memory);
        int fullestBin = memory.get(fullestBinIndex);
        memory.set(fullestBinIndex, 0);
        int index = fullestBinIndex;
        while (fullestBin > 0) {
            index = (index+1)%memory.size();
            memory.set(index, memory.get(index)+1);
            --fullestBin;
        }
        ++count;
        return count;
    }

    private static int fullestBin(List<Integer> memory) {
        int result = -1;
        int contents = 0;
        for (int i=0; i<memory.size(); ++i) {
            if (memory.get(i) > contents) {
                contents = memory.get(i);
                result = i;
            }
        }
        return result;
    }
}
