package advent.y2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day08 {
    private static Map<String, Integer> registers = new HashMap<>();
    private static List<Command> commands = new ArrayList<>();

    private enum Operation { INC, DEC; }

    private static class Command {
        public String register;
        public Operation operation;
        public int amount;
        public Condition condition;

        public Command(String line) {
            String[] tokens = line.split(" ");
            register = tokens[0];
            operation = Operation.valueOf(tokens[1].toUpperCase());
            amount = Integer.parseInt(tokens[2]);
            // tokens[3] = "if"
            condition = new Condition(tokens[4], tokens[5], tokens[6]);
        }

        public int execute() {
            int value = registers.get(register);
            if (condition.check()) {
                switch (operation) {
                case DEC:
                    value -= amount;
                    break;
                case INC:
                    value += amount;
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported operation "+operation);
                }
            }
            return value;
        }
    }

    private static class Condition {
        public String register;
        public String comparison;
        public int value;

        public Condition(String registerName, String comparisonOperator, String valueString) {
            this.register = registerName;
            this.comparison = comparisonOperator;
            this.value = Integer.parseInt(valueString);
        }

        public boolean check() {
            int registerValue = registers.get(register);
            switch (comparison) {
            case ">":
                return registerValue > value;
            case "<":
                return registerValue < value;
            case "==":
                return registerValue == value;
            case "!=":
                return registerValue != value;
            case "<=":
                return registerValue <= value;
            case ">=":
                return registerValue >= value;
            default:
                throw new UnsupportedOperationException("unsupported operation: "+comparison);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        loadCommands();

        int maxValue = Integer.MIN_VALUE;
        for (Command command : commands) {
            int result = command.execute();
            maxValue = Integer.max(result, maxValue);
            registers.put(command.register, result);
        }

        Util.log("max register value ever is %d", maxValue);

        maxValue = Integer.MIN_VALUE;
        for (int value : registers.values()) {
            maxValue = Integer.max(maxValue, value);
        }

        Util.log("max register value at the end is %d", maxValue);
    }

    private static void loadCommands() throws IOException {
        List<String> lines = Util.readInput("2017", "Day08.txt");
        for (String line : lines) {
            Command command = new Command(line);
            commands.add(command);
            registers.put(command.register, 0);
        }
    }

}
