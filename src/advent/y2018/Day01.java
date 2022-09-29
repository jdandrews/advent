package advent.y2018;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day01 {

    public static void main(String[] args) {
        List<String> stringData = FileIO.getFileAsList("src/advent/y2018/Day01.txt");
        int[] data = parse(stringData);
        
        Set<Integer> seen = new HashSet<>();
        seen.add(0);
        int sum = 0;
        for (boolean done = false; !done; ) {
            for (int i : data) {
                sum += i;
                if (! seen.add(sum)) {
                    Util.log("saw %d twice", sum);
                    done = true;
                }
            }
            Util.log("sum = %d", sum);
        }
        Util.log("seen.size() = %d", seen.size());
    }

    private static int[] parse(List<String> data) {
        int[] result = new int[data.size()];
        for (int i=0; i<data.size(); ++i) {
            result[i] = Integer.valueOf(data.get(i));
        }
        return result;
    }
}
