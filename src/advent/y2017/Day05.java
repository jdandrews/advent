package advent.y2017;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import advent.Util;

public class Day05 {
    public static void main(String[] args) throws IOException {
        List<String> rawData = Util.readInput("2017", "Day05.txt");
        List<Integer> data = rawData.stream().map(Integer::parseInt).collect(Collectors.toList());

        // part 1
        int step = 0;
        for (int pc = 0; pc >= 0 && pc < data.size(); ) {
            int n = data.get(pc);
            data.set(pc, data.get(pc)+1);
            pc += n;
            ++step;
        }

        Util.log("part 1 stepped out of bounds at step %d", step);

        // part 2
        data = rawData.stream().map(Integer::parseInt).collect(Collectors.toList());
        step = 0;
        for (int pc = 0; pc >= 0 && pc < data.size(); ) {
            int n = data.get(pc);
            if (n >=3) {
                data.set(pc, data.get(pc)-1);
            }
            else {
                data.set(pc, data.get(pc)+1);
            }
            pc += n;
            ++step;
        }

        Util.log("part 2 stepped out of bounds at step %d", step);
        Util.log("result is %s", data);
    }
}
