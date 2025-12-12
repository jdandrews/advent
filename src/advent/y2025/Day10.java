package advent.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import advent.FileIO;
import advent.Util;

public class Day10 {

    private static final List<String> SAMPLE = Arrays.asList(
            "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
            "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
            "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}");

    private static final List<String> puzzle = FileIO.getFileAsList("src/advent/y2025/Day10.txt");

    public static void main(String[] args) {
        List<Integer> presses = solve(SAMPLE);
        Util.log("part 1 SAMPLE presses %s\n\tsums to %d total to align the lights.", presses,
                presses.stream().mapToInt(Integer::intValue).sum());

        Util.log("---------");

        presses = solve(puzzle);
        Util.log("part 1 puzzle presses %s\n\tsums to %d total to align the lights.", presses,
                presses.stream().mapToInt(Integer::intValue).sum());
    }

    private record JButtons(int[][] A) {}
    private record Machine(int nLights, int lights, List<Integer> buttons, List<Integer> joltages, JButtons jb) {}

    private static List<Integer> solve(List<String> lines) {
        List<Machine> machines = parse(lines);
        List<Integer> result = new ArrayList<>();
        for (Machine machine : machines) {
            try {
                result.add(minimumButtonPresses(machine));
            } catch (UnsupportedOperationException e) {
                Util.log("failed at %d of %d", result.size() + 1, machines.size());
                throw e;
            }
        }
        return result;
    }

    /*
     * Knowing that each press toggles the lights, we know that the required presses
     * (a) only involve 1 press of any given button, and (b) we can just XOR those presses
     * to match the bit pattern of the lights. The hard work here is in generating all
     * the various combinations, which I did recursively (and I hate it; I'll fix it later).
     *
     * ...but I don't hate it as much as the initial version, where I just copy/pasted loops
     * for 2, 3, 4, and 5 combinations.
     */
    private static Integer minimumButtonPresses(Machine machine) {
        for (int i = 1; i<= machine.buttons.size(); ++i) {
            List<List<Integer>> buttonLists = generateCombinations(i, machine.buttons());
            for (List<Integer> buttonList : buttonLists) {
                if (buttonList.stream().map(v -> v.intValue()).reduce(0, (a, b) -> a ^ b) == machine.lights) {
                    return i;
                }
            }
        }

        throw new UnsupportedOperationException("Not Found");
    }


    private static List<List<Integer>> generateCombinations(int n, List<Integer> buttons) {
        List<List<Integer>> combinations = new ArrayList<>();
        if (n < 0 || n > buttons.size()) {
            return combinations; // Invalid input
        }
        generateCombinationsRecursive(buttons, n, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private static void generateCombinationsRecursive(
            List<Integer> buttons, int n, int startIndex,
            ArrayList<Integer> currentCombination,
            List<List<Integer>> allCombinations) {

        if (currentCombination.size() == n) {
            allCombinations.add(new ArrayList<>(currentCombination)); // Found a combination
            return;
        }

        if (startIndex >= buttons.size()) {
            return; // No more elements
        }

        // include the current element
        currentCombination.add(buttons.get(startIndex));
        generateCombinationsRecursive(buttons, n, startIndex + 1, currentCombination, allCombinations);
        currentCombination.remove(currentCombination.size() - 1); // Backtrack

        // Exclude the current element
        generateCombinationsRecursive(buttons, n, startIndex + 1, currentCombination, allCombinations);
    }

    // treating buttons and lights as bit patterns (see above for why)
    //
    private static List<Machine> parse(List<String> lines) {
        List<Machine> results = new ArrayList<>();
        for (String line : lines) {
            String[] chunks = line.split(" ");

            int nLights = chunks[0].length() - 2;
            int lights = Integer.parseInt(
                    chunks[0].substring(1, chunks[0].length() - 1).replaceAll("\\.", "0").replaceAll("#", "1"),
                    2);

            // the subtlety here is that a button that lights light 0 of a 4-light bank has a
            // bitmask value of 8, and the button that lights light 3 has a bitmask value of 1.
            // so we have to remap the "light number" provided to a bit location by subtracting
            // it from the total number of lights, and then one more to account for 0-based numbering.
            List<Integer> buttons = new ArrayList<>();
            for (int i = 1; i < chunks.length - 1; ++i) {
                String lightString = chunks[i].substring(1, chunks[i].length() - 1);
                int button = Arrays.asList(lightString.split(",")).stream()
                        .map(v -> Integer.parseInt(v))
                        .map(v -> (int)Math.pow(2, nLights - v - 1))
                        .reduce(0, (a, b) -> a | b);
                buttons.add(button);
            }

            String joltageString = chunks[chunks.length - 1].substring(1, chunks[chunks.length - 1].length() - 1);
            List<Integer> joltages = Arrays.asList(joltageString.split(",")).stream()
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            int[][] a = new int[buttons.size()][joltages.size()];
            for (int i = 0; i<buttons.size(); ++i) {
                String[] bitStrings = chunks[i + 1].substring(1, chunks[i+1].length() - 1).split(",");
                List<Integer> bits = Arrays.asList(bitStrings).stream()
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .collect(Collectors.toList());

                for (int j = 0; j<joltages.size(); ++j) {
                    a[i][j] = (bits.contains(j)) ? 1 : 0;
                }
            }

            results.add(new Machine(nLights, lights, buttons, joltages, new JButtons(a)));
        }

        return results;
    }
}
