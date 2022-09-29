package advent.y2017;

import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day17 {
    public static void main(String[] args) {
        List<Integer> ring = new ArrayList<>();
        int step = 314;
        int position = 0;

        // part 1
        ring.add(0);
        for (int i=0; i<2017; ++i) {
            position = (position + step) % ring.size();
            ring.add(position+1, i+1);
            ++position;
        }
        Util.log("part 1 next entry: %d", ring.get(position+1));

        // part 2
        ring = new ArrayList<>();
        ring.add(0);
        int ringSize = ring.size();
        int after0 = 0;
        for (int i=0; i<50000000; ++i) {
            position = (position + step) % ringSize;   // i+1 takes longer than ring.size() for ArrayList (!)
            // ring.add(position+1, i+1);
            if (position==0) {
                after0 = i+1;
                Util.log("after zero: %d", after0);
            }
            ++ringSize;
            ++position;
        }
        Util.log("after zero at 50M: %d", after0);
    }
}
