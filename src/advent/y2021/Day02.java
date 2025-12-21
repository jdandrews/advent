package advent.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day02 {
    private static final List<String> SAMPLE = Arrays.asList("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2");

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2021/Day02.txt");
        Util.log("Loaded %d entries from Day02.txt", puzzle.size());

        Util.log("part 1 sample depth * distance = %d", solve(SAMPLE, true) );
        Util.log("part 1 puzzle depth * distance = %d", solve(puzzle, false));

        Util.log("---------");

        Util.log("part 2 sample depth * distance = %d", solve2(SAMPLE, true));
        Util.log("part 2 puzzle depth * distance = %d", solve2(puzzle, false));

    }

    private static record Command(String direction, int distance) {}

    private static Object solve(List<String> in, boolean logProgress) {
        List<Command> commands = parse(in);

        int x = 0;
        int depth = 0;

        for (Command command : commands) {
            switch(command.direction()) {
            case "up":
                depth = Math.max(0, depth - command.distance());
                break;
            case "down":
                depth += command.distance();
                break;
            case "forward":
                x += command.distance();
                break;
            default:
                throw new UnsupportedOperationException("Unknown command: " + command.direction());
            }

            if (logProgress) {
                Util.log("%s: depth = %d; x = %d", command, depth, x);
            }
        }
        return x * depth;
    }

    private static Object solve2(List<String> in, boolean logProgress) {
        List<Command> commands = parse(in);

        int aim = 0;
        int x = 0;
        int depth = 0;

        for (Command command : commands) {
            switch(command.direction()) {
            case "up":
                aim -= command.distance();
                break;
            case "down":
                aim += command.distance();
                break;
            case "forward":
                x += command.distance();
                depth += aim * command.distance();
                break;
            default:
                throw new UnsupportedOperationException("Unknown command: " + command.direction());
            }

            if (logProgress) {
                Util.log("%s: depth = %d; x = %d", command, depth, x);
            }
        }
        return x * depth;
    }

    private static List<Command> parse(List<String> in) {
        List<Command> commands = new ArrayList<>();
        for (String line : in) {
            String[] chunks = line.split(" ");
            commands.add(new Command(chunks[0], Integer.parseInt(chunks[1])));
        }
        return commands;
    }

}
