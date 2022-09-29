package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

/*
 * TODO: this works with integer math masked on output (only), but fails with short math. Why?
 */
public class Day07 {
    private enum Operator {
        AND, OR, LSHIFT, RSHIFT, NOT, ASSIGNMENT;
    }

    private static class Instruction {
        boolean resolved = false;

        String in1;
        int v1;
        String in2;
        int v2;

        Operator op;

        String out;
        int value;
        
        @Override
        public String toString() {
            return out + " = " + (value & 0xffff);
        }
    }
    
    private static Map<String, Instruction> circuit = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<Instruction> gates = parse(sampleData);
        buildCircuit(gates);
        run();

        System.out.println(circuit);

        System.out.println("----");
        circuit.clear();

        gates = parse(Util.readInput("2015", "Day07.txt"));
        buildCircuit(gates);
        System.out.println("---- part 1 ----");
        run();
        
        int a = circuit.get("a").value;
        circuit.clear();
        gates = parse(Util.readInput("2015", "Day07.txt"));
        buildCircuit(gates);
        Instruction i = new Instruction();
        i.in1 = Integer.toString(a);
        i.op = Operator.ASSIGNMENT;
        i.out = "b";
        circuit.put("b", i);
        System.out.println("---- part 2 ----");
        run();
    }

    private static void buildCircuit(List<Instruction> gates) {
        for (Instruction gate : gates) {
            if ( circuit.containsKey(gate.out)) {
                throw new UnsupportedOperationException("double assigment of "+gate.out);
            }
            circuit.put(gate.out, gate);
        }
    }

    private static void run() {
        boolean completed = true;
        List<Instruction> resolved = new ArrayList<>();

        do {
            completed = true;
            for (Instruction gate : circuit.values()) {
                int startValue=0;
                boolean wasResolved = gate.resolved;
                if (wasResolved) {
                    startValue = gate.value;
                }
                try {
                    switch(gate.op) {
                    case AND:
                        gate.v1 = getValue(gate.in1);
                        gate.v2 = getValue(gate.in2);
                        gate.value = (short) (gate.v1 & gate.v2);
                        gate.resolved = true;
                        break;
                    case ASSIGNMENT:
                        gate.v1 = getValue(gate.in1);
                        gate.value = gate.v1;
                        gate.resolved = true;
                        break;
                    case LSHIFT:
                        gate.v1 = getValue(gate.in1);
                        gate.v2 = getValue(gate.in2);
                        gate.value = (short) (gate.v1 << gate.v2);
                        gate.resolved = true;
                        break;
                    case NOT:
                        gate.v1 = getValue(gate.in1);
                        gate.value = (short) ~(gate.v1);
                        gate.resolved = true;
                        break;
                    case OR:
                        gate.v1 = getValue(gate.in1);
                        gate.v2 = getValue(gate.in2);
                        gate.value = (short) (gate.v1 | gate.v2);
                        gate.resolved = true;
                        break;
                    case RSHIFT:
                        gate.v1 = getValue(gate.in1);
                        gate.v2 = getValue(gate.in2);
                        gate.value = (short) (gate.v1 >>> gate.v2);
                        gate.resolved = true;
                        break;
                    default:
                        throw new UnsupportedOperationException("not implemented: "+gate.op);
                    }
                } catch (@SuppressWarnings("unused") IllegalStateException e) {
                    // catch it on the next pass
                    completed = false;
                }
                if (wasResolved && gate.value != startValue) {
                    System.out.println("re-resolved gate "+gate);
                } else if (! wasResolved && gate.resolved) {
                    resolved.add(gate);
                }
            }
        } while ( ! completed );
        System.out.println("---> "+circuit.get("a"));
    }

    @SuppressWarnings("unused")
    private static int getValue(String vString) {
        int v;
        try {
            v = Integer.parseInt(vString);
        } catch (NumberFormatException e) {
            Instruction instruction = circuit.get(vString);
            if (! instruction.resolved) {
                throw new IllegalStateException("no value for vString "+vString);
            }
            v = instruction.value;
        }

        return v;
    }

    private static List<Instruction> parse(List<String> data) {
        List<Instruction> result = new ArrayList<>();
        for (String datum : data) {
            Instruction instruction = new Instruction();
            String[] parts = datum.split(" -> ");
            if (datum.startsWith("NOT")) {
                // all the "NOT"s are simple assignments
                instruction.out = parts[1];
                instruction.op = Operator.NOT;
                instruction.in1 = parts[0].split(" ")[1];
            } else {
                String[] nibbles = parts[0].split(" ");
                if (nibbles.length==1) {
                    instruction.out = parts[1];
                    instruction.op = Operator.ASSIGNMENT;
                    instruction.in1 = parts[0];
                } else if (nibbles.length==2) {
                    throw new UnsupportedOperationException("can't handle "+parts[0]);
                } else {    // nibbles.length==3
                    instruction.out = parts[1];
                    instruction.in1 = nibbles[0];
                    instruction.op = Operator.valueOf(nibbles[1]);
                    instruction.in2 = nibbles[2];
                }
            }
            result.add(instruction);
        }
        return result;
    }

    private static List<String> sampleData = new ArrayList<>();
    static {
        sampleData.add("123 -> x");
        sampleData.add("456 -> y");
        sampleData.add("x AND y -> d");
        sampleData.add("x OR y -> e");
        sampleData.add("x LSHIFT 2 -> f");
        sampleData.add("y RSHIFT 2 -> g");
        sampleData.add("NOT x -> h");
        sampleData.add("NOT y -> i");
    }
}
