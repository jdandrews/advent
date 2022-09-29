package advent.y2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day16 {

    private static class Instruction {
        private enum Action {
            SPIN,
            EXCHANGE,
            PARTNER;
        }
        private Action action;
        int n1;
        int n2;
        char p1;
        char p2;

        public Instruction(String instruction) {
            if (instruction.charAt(0)=='s') {
                action = Action.SPIN;
                n1 = Integer.parseInt(instruction.substring(1));
            } else if (instruction.charAt(0)=='x') {
                action = Action.EXCHANGE;
                int n = instruction.indexOf('/');
                n1 = Integer.parseInt(instruction.substring(1, n));
                n2 = Integer.parseInt(instruction.substring(n+1));
            } else if (instruction.charAt(0)=='p') {
                action = Action.PARTNER;
                int n = instruction.indexOf('/');
                p1 = instruction.substring(1, n).charAt(0);
                p2 = instruction.substring(n+1).charAt(0);
            } else {
                throw new IllegalArgumentException("unable to parse "+instruction);
            }
        }

        public void perform(char[] data) {
            switch(action) {
            case EXCHANGE:
                swap(data, n1, n2);
                break;

            case PARTNER:
                n1 = -1;
                n2 = -1;
                for (int i=0; (n1 < 0 || n2 < 0) && i<data.length; ++i) {
                    char c = data[i];
                    if (n1 < 0 && c == p1) {
                        n1 = i;
                    }
                    if (n2 < 0 && c == p2) {
                        n2 = i;
                    }
                }

                swap(data, n1, n2);
                break;

            case SPIN:
                emitData("spin: %s by "+n1, data);
                char[] holding = Arrays.copyOfRange(data, data.length-n1, data.length);
                emitData("holding: %s", holding);
                for (int i=data.length-n1-1; i>=0; --i) {
                    data[i+n1] = data[i];
                }
                for (int i=0; i<holding.length; ++i) {
                    data[i] = holding[i];
                }
                emitData("spun: %s", data);
                break;

            default:
                throw new UnsupportedOperationException("unable to perform "+action);
            }
        }

        private void emitData(String format, char[] data) {
            /*
            Util.log(format, showLine(data));
            */
        }

        private void swap(char[] data, int position1, int position2) {
            char c = data[position1];
            data[position1] = data[position2];
            data[position2] = c;
        }
    }

    public static void main(String[] args) throws IOException {
        // sample data
        char[] samplePrograms = {'a', 'b', 'c', 'd', 'e'};
        String[] instructionStrings = {"s1", "x3/4", "pe/b"};
        List<Instruction> sampleInstructions = parseInstructions(instructionStrings);
        char[] result = dance(samplePrograms, sampleInstructions);
        Util.log("sample result: %s", showLine(result));

        result = dance(result, sampleInstructions);
        Util.log("sample transform result: %s", showLine(result));

        // real data
        Map<String, char[]> precalculatedResults = new HashMap<>();

        char[] programs = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p'};
        List<Instruction> instructions = parseInstructions(loadInstructions());

        result = dance(programs, instructions);
        Util.log("after 1  dance: %s", showLine(result));
        precalculatedResults.put(new String(programs), Arrays.copyOf(result, result.length));

        programs = result;
        for (int i=1; i<1000000000; ++i) {
            String programString = new String(programs);
            if (precalculatedResults.containsKey(programString)) {
                programs = Arrays.copyOf(precalculatedResults.get(programString), programs.length);
                continue;
            }

            result = dance(programs, instructions);
            precalculatedResults.put(programString, Arrays.copyOf(result, result.length));
            programs = result;

            if (i%100000 == 0) {
                System.out.print(".");
            }
            if (i%10000000 == 0) {
                System.out.println(" "+i);
            }
        }
        Util.log("\nafter 1B dance: %s", showLine(programs));
    }

    private static List<Instruction> parseInstructions(String[] instructions) {
        List<Instruction> result = new ArrayList<>();
        for (String instruction : instructions) {
            Instruction i = parsedInstructions.get(instruction);
            if (i==null) {
                i = new Instruction(instruction);
                parsedInstructions.put(instruction, i);
            }
            result.add(i);
        }
        return result;
    }

    private static String showLine(char[] chars) {
        return new String(chars);
    }

    private static Map<String, Instruction> parsedInstructions = new HashMap<>();

    private static char[] dance(char[] programs, List<Instruction> instructions) {
        char[] result = Arrays.copyOf(programs, programs.length);
        for (Instruction instruction : instructions) {
            instruction.perform(result);
            // Util.log("%s", showLine(result));
        }
        return result;
    }

    private static String[] loadInstructions() throws IOException {
        List<String> contents = Util.readInput("2017", "Day16.txt");
        return contents.get(0).split(",");
    }
}
