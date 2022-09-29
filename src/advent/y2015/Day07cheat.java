package advent.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.IntBinaryOperator;

public class Day07cheat {
    static HashMap<String, IntBinaryOperator> ops;
    static HashMap<String, Node> vars;

    public static void main(String[] args) throws FileNotFoundException {

        ops = new HashMap<>();
        ops.put("AND", (a, b) -> a & b);
        ops.put("OR", (a, b) -> a | b);
        ops.put("LSHIFT", (a, b) -> a << b);
        ops.put("RSHIFT", (a, b) -> a >> b);

        // part 1
        loadVars();
        int a = vars.get("a").getValue();
        System.out.println("part 1: a = "+a);

        // part 2
        loadVars();
        vars.get("b").setValue(new Literal(a));

        System.out.println("part 2: a = "+vars.get("a").getValue());
    }

    private static void loadVars() throws FileNotFoundException {
        vars = new HashMap<>();

        try (Scanner s = new Scanner(new File("src/advent/y2015/Day7.txt"))) {

            while (s.hasNextLine()) {
                String line = s.nextLine();
                // System.out.println(line);
                String[] split = line.split(" ");
    
                Node var = new Node();
    
                if (split.length == 3) {
                    var.setValue(getValue(split[0]));
                    vars.put(split[2], var);
                } else if (split[0].equals("NOT")) {
                    var.setValue(new Negation(getValue(split[1])));
                    vars.put(split[3], var);
                } else {
                    var.setValue(new Operator(split[1], getValue(split[0]), getValue(split[2])));
                    vars.put(split[4], var);
                }
    
            }

        } 
    }

    static Element getValue(String s) {
        if (s.matches("\\d+")) {
            return new Literal(Integer.parseInt(s));
        }
        return new LazyNode(s);
    }

    interface Element {
        int getValue();
    }

    static class Node implements Element {
        Element value;
        Integer cached = null;

        public void setValue(Element newValue) {
            this.value = newValue;
        }

        @Override
        public int getValue() {
            if (cached == null)
                cached = value.getValue() & 0xffff;
            return cached;
        }
    }

    static class LazyNode implements Element {
        String name;

        public LazyNode(String nodeName) {
            this.name = nodeName;
        }

        @Override
        public int getValue() {
            return vars.get(name).getValue();
        }
    }

    static class Literal implements Element {
        int value;

        public Literal(int literalValue) {
            this.value = literalValue;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    static class Negation implements Element {
        Element orig;

        public Negation(Element valueToNegate) {
            this.orig = valueToNegate;
        }

        @Override
        public int getValue() {
            return ~orig.getValue();
        }
    }

    static class Operator implements Element {
        IntBinaryOperator op;
        String title;
        Element left, right;

        public Operator(String title, Element left, Element right) {
            this.title = title;
            op = ops.get(title);
            this.left = left;
            this.right = right;
        }

        @Override
        public int getValue() {
            return op.applyAsInt(left.getValue(), right.getValue());
        }
    }
}