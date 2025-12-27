package advent.y2021;

import advent.Util;

public class Day06 {

    private static final String SAMPLE = "3,4,3,1,2";
    private static final String PUZZLE = "3,5,4,1,2,1,5,5,1,1,1,1,4,1,4,5,4,5,1,3,1,1,1,4,1,1,3,1,1,5,3,1,1,3,1,3,1,1,"
            + "1,4,1,2,5,3,1,4,2,3,1,1,2,1,1,1,4,1,1,1,1,2,1,1,1,3,1,1,4,1,4,1,5,1,4,2,1,1,5,4,4,4,1,4,1,1,1,1,3,1,5,"
            + "1,4,5,3,1,4,1,5,2,2,5,1,3,2,2,5,4,2,3,4,1,2,1,1,2,1,1,5,4,1,1,1,1,3,1,5,4,1,5,1,1,4,3,4,3,1,5,1,1,2,1,"
            + "1,5,3,1,1,1,1,1,5,1,1,1,1,1,1,1,2,2,5,5,1,2,1,2,1,1,5,1,3,1,5,2,1,4,1,5,3,1,1,1,2,1,3,1,4,4,1,1,5,1,1,"
            + "4,1,4,2,3,5,2,5,1,3,1,2,1,4,1,1,1,1,2,1,4,1,3,4,1,1,1,1,1,1,1,2,1,5,1,1,1,1,2,3,1,1,2,3,1,1,3,1,1,3,1,"
            + "3,1,3,3,1,1,2,1,3,2,3,1,1,3,5,1,1,5,5,1,2,1,2,2,1,1,1,5,3,1,1,3,5,1,3,1,5,3,4,2,3,2,1,3,1,1,3,4,2,1,1,"
            + "3,1,1,1,1,1,1";

    public static void main(String[] args) {
        Util.log("part 1 sample day 18 fish count = %d", countFish(SAMPLE, 18, true));
        Util.log("part 1 sample day 80 fish count = %d", countFish(SAMPLE, 80, false));
        Util.log("part 1 puzzle day 80 fish count = %d", countFish(PUZZLE, 80, false));
        Util.log("---------");
        Util.log("part 2 sample day 256 fish count = %d", countFish(SAMPLE, 256, false));
        Util.log("part 2 puzzle day 256 fish count = %d", countFish(PUZZLE, 256, false));

    }

    private static Object countFish(String in, int days, boolean logProgress) {
        long[] daysToSpawn = new long[9];
        for (int i = 0; i<daysToSpawn.length; ++i) {
            daysToSpawn[i] = 0;
        }

        // count fish at each day count
        for (String s : in.split(",")) {
            ++daysToSpawn[s.charAt(0) - '0'];
        }
        if (logProgress) {
            Util.log(" S: %s", Util.toString(daysToSpawn));
        }

        for (int day = 0; day<days; ++day) {
            long spawning = daysToSpawn[0];
            for (int i = 1; i<daysToSpawn.length; ++i) {
                daysToSpawn[i-1] = daysToSpawn[i];
            }
            daysToSpawn[8] = spawning;
            daysToSpawn[6] += spawning;
            if (logProgress && day < 20) {
                Util.log("%2d: %s -> %d", day, Util.toString(daysToSpawn), sum(daysToSpawn));
            }
        }

        long count = sum(daysToSpawn);
        return count;
    }

    private static long sum(long[] daysToSpawn) {
        long count = 0;
        for (int i = 0; i<daysToSpawn.length; ++i) {
            count += daysToSpawn[i];
        }
        return count;
    }

}
