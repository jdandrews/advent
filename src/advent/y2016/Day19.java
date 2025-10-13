package advent.y2016;

import java.util.ArrayList;
import java.util.List;

public class Day19 {

    public static void main(String[] args) {
        System.out.println("\npart 1");
        solve1(5, true);
        solve1(3014387, false);

        System.out.println("\npart 2");
        solve2(5, true);
        solve2(3014387, false);
    }

    private static void solve1(int size, boolean printExchanges) {
        int[] circleOfElves = new int[size];
        for (int i = 0; i < circleOfElves.length; ++i) {
            circleOfElves[i] = 1;
        }

        int elvesRemaining = circleOfElves.length;
        int index = 0;

        while (elvesRemaining > 1) {
            while (circleOfElves[index] == 0) {
                index = (++index) % circleOfElves.length;
            }

            int target = (index + 1) % circleOfElves.length;
            while (circleOfElves[target] == 0) {
                target = (target + 1) % circleOfElves.length;
            }

            circleOfElves[index] += circleOfElves[target];
            circleOfElves[target] = 0;

            --elvesRemaining;
            if (printExchanges) {
                System.out.println("elf " + (index + 1) + " took elf " + (target + 1) + "'s presents. "
                        + elvesRemaining + " elves remain.");
            }
            index = (++index) % circleOfElves.length;
        }
        System.out.println("Elf " + index + " got all the presents.");
    }

    private static record Elf(int id, int presents) {}

    // TODO: we should be able to execute this without actually managing the array.
    // We just want to keep track of the elves indices after the delete; why is this
    // slower than #1 above?
    private static void solve2(int size, boolean printExchanges) {
        List<Elf> elves = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            elves.add(new Elf(i+1, 1));
        }

        int index = 0;

        while (elves.size() > 1) {
            int target = (index + elves.size()/2) % elves.size();

            if (printExchanges) {
                Elf currentElf = elves.get(index);
                Elf targetElf = elves.get(target);

                System.out.println("elf " + currentElf.id() + " at " + index
                        + " took elf " + targetElf.id() + "'s presents (at " + target + "); "
                        + elves.size() + " elves remain.");
            }

            elves.remove(target);

            int n = elves.size();
            if (n % 10000 == 0) {
                System.out.print(".");
                if (n % 500000 == 0) {
                    System.out.println(" " + n + " remaining.");
                }
            }

            if (index < target) {
                index = (++index) % elves.size();
            } else {
                index = index % elves.size();
            }
        }
        System.out.println("Elf " + elves.get(index).id() + " got all the presents.");
    }
}
