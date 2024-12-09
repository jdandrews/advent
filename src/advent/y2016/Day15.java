package advent.y2016;

import java.util.ArrayList;
import java.util.List;

// input
// Disc #1 has 17 positions; at time=0, it is at position 1.
// Disc #2 has 7 positions; at time=0, it is at position 0.
// Disc #3 has 19 positions; at time=0, it is at position 2.
// Disc #4 has 5 positions; at time=0, it is at position 0.
// Disc #5 has 3 positions; at time=0, it is at position 0.
// Disc #6 has 13 positions; at time=0, it is at position 5.

public class Day15 {
    public static class Disk {
        private final int positions;
        private int position;

        public Disk(int totalPositions, int startingPosition) {
            positions = totalPositions;
            position = startingPosition;
        }

        public int getPosition() {
            return position;
        }

        public int tick() {
            position = (position + 1) % positions;
            return position;
        }

        @Override
        public String toString() {
            return "Disk(" + positions + " : " + position + ")";
        }
    }

    public static void main(String[] args) {
        List<Disk> sample = new ArrayList<>();
        Disk s1 = new Disk(5, 4);
        Disk s2 = new Disk(2, 1);

        List<Disk> samples = new ArrayList<>();
        samples.add(s1);
        samples.add(s2);
        long time = 0;
        while (disksAreNotSequenced(samples)) {
            for (Disk d : samples) {
                d.tick();
            }
            ++time;
        }
        System.out.println(samples);
        System.out.println("Disks align at " + time + "; drop at " + (time - 1));

        System.out.println("--- part 1 ------");
        Disk d1 = new Disk(17, 1);
        Disk d2 = new Disk(7, 0);
        Disk d3 = new Disk(19, 2);
        Disk d4 = new Disk(5, 0);
        Disk d5 = new Disk(3, 0);
        Disk d6 = new Disk(13, 5);

        List<Disk> disks = new ArrayList<>();
        disks.add(d1);
        disks.add(d2);
        disks.add(d3);
        disks.add(d4);
        disks.add(d5);
        disks.add(d6);

        time = 0;
        while (disksAreNotSequenced(disks)) {
            for (Disk d : disks) {
                d.tick();
            }
            ++time;
        }
        System.out.println(disks);
        System.out.println("Disks will align at " + time + "; drop at " + (time - 1));

        System.out.println("---- part 2 -----");
        d1 = new Disk(17, 1);
        d2 = new Disk(7, 0);
        d3 = new Disk(19, 2);
        d4 = new Disk(5, 0);
        d5 = new Disk(3, 0);
        d6 = new Disk(13, 5);
        Disk d7 = new Disk(11, 0);

        disks = new ArrayList<>();
        disks.add(d1);
        disks.add(d2);
        disks.add(d3);
        disks.add(d4);
        disks.add(d5);
        disks.add(d6);
        disks.add(d7);

        time = 0;
        while (disksAreNotSequenced(disks)) {
            for (Disk d : disks) {
                d.tick();
            }
            ++time;
        }
        System.out.println(disks);
        System.out.println("Disks will align at " + time + "; drop at " + (time - 1));
    }

    private static boolean disksAreNotSequenced(List<Disk> disks) {
        int offset = 0;
        for (Disk d : disks) {
            if (0 != ((d.position + offset++) % d.positions))
                return true;
        }
        return false;
    }
}
