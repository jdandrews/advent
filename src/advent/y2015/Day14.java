package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day14 {

    private static class Reindeer {
        public String name;
        public int speed;
        public int flyTime;
        public int restTime;
        public boolean resting;
        public int remainingRestTime;
        public int remainingFlyTime;
        public int distance=0;
        public int points = 0;
    }
    public static void main(String[] args) throws IOException {
        List<String> lines = Util.readInput("2015", "Day14.txt");
        List<Reindeer> reindeer = new ArrayList<>();
        for (String s : lines) {
            reindeer.add(makeReineer(s));
        }

        for (int i=0; i<2503; ++i) {
            tick(reindeer);
            score(reindeer);
        }

        int maxDistance = 0;
        int maxPoints = 0;
        String distanceLeader = "duh";
        String pointsLeader = "duh";
        for (Reindeer r : reindeer) {
            if (maxDistance < r.distance) {
                distanceLeader = r.name;
                maxDistance = r.distance;
            }
            if (maxPoints < r.points) {
                pointsLeader = r.name;
                maxPoints = r.points;
            }
            Util.log("%s: %d km; %d pts", r.name, r.distance, r.points);
        }
        Util.log("\n%s flew furthest: %d", distanceLeader, maxDistance);
        Util.log("\n%s most points:   %d", pointsLeader, maxPoints);
    }

    private static void score(List<Reindeer> reindeer) {
        Reindeer leader = null;
        for (Reindeer r : reindeer) {
            if (leader==null || r.distance > leader.distance) {
                leader = r;
            }
        }

        ++leader.points;
        for (Reindeer r : reindeer) {
            if (r.distance==leader.distance && r != leader) {
                ++r.points;
            }
        }
    }

    private static void tick(List<Reindeer> reindeer) {
        for (Reindeer r : reindeer) {
            if (! r.resting) {
                r.distance += r.speed;
                --r.remainingFlyTime;
                if (r.remainingFlyTime==0) {
                    r.remainingFlyTime=r.flyTime;
                    r.resting = true;
                }
            } else {
                --r.remainingRestTime;
                if (r.remainingRestTime==0) {
                    r.remainingRestTime = r.restTime;
                    r.resting = false;
                }
            }
        }
    }
    private static Reindeer makeReineer(String s) {
        String[] sa = s.split(" ");
        Reindeer r = new Reindeer();
        r.name = sa[0];
        r.speed = Integer.parseInt(sa[3]);
        r.flyTime = Integer.parseInt(sa[6]);
        r.restTime = Integer.parseInt(sa[13]);
        r.resting = false;
        r.remainingFlyTime = r.flyTime;
        r.remainingRestTime = r.restTime;
        return r;
    }

}
