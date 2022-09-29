package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day16 {
    private static class Sue {
        public Sue(String c, String t1, String n1, String t2, String n2, String t3, String n3) {
            this.n= Integer.parseInt(c);
            stuff.put(t1, Integer.parseInt(n1));
            stuff.put(t2, Integer.parseInt(n2));
            stuff.put(t3, Integer.parseInt(n3));
        }
        int n;
        Map<String, Integer> stuff = new HashMap<>();
        @Override
        public String toString() {
            return "Sue "+n+ " "+stuff.toString();
        }
    }

    private static class Thing {
        public Thing(String n, int c) {
            name = n;
            count = c;
        }
        public String name;
        public int count;
    }

    public static void main(String[] args) throws IOException {
        List<String> input = Util.readInput("2015", "Day16.txt");
        List<Sue> data = parse(input);
        
        List<Thing> clues = new ArrayList<Thing>(){{
            add(new Thing("children", 3));
            add(new Thing("cats", 7));
            add(new Thing("samoyeds", 2));
            add(new Thing("pomeranians", 3));
            add(new Thing("akitas", 0));
            add(new Thing("vizslas", 0));
            add(new Thing("goldfish", 5));
            add(new Thing("trees", 3));
            add(new Thing("cars", 2));
            add(new Thing("perfumes", 1));
        }};

        for (Thing thing : clues) {
            List<Sue> removeMe = new ArrayList<>();
            for (Sue sue : data) {
                if (sue.stuff.containsKey(thing.name) && sue.stuff.get(thing.name)!=thing.count) {
                    removeMe.add(sue);
                }
            }
            data.removeAll(removeMe);
        }

        System.out.println("part 1: "+data);

        data = parse(input);
        System.out.println("part 2: "+screen(clues, data));
    }

    private static List<Sue> screen(List<Thing> clues, List<Sue> data) {
        for (Thing thing : clues) {
            List<Sue> removeMe = new ArrayList<>();
            for (Sue sue : data) {
                if (thing.name.equals("cats") || thing.name.equals("trees")) {
                    if (sue.stuff.containsKey(thing.name) && sue.stuff.get(thing.name)<thing.count) {
                        removeMe.add(sue);
                    }
                }
                else if (thing.name.equals("pomeranians") || thing.name.equals("goldfish")) {
                    if (sue.stuff.containsKey(thing.name) && sue.stuff.get(thing.name)>thing.count) {
                        removeMe.add(sue);
                    }
                }
                else {
                    if (sue.stuff.containsKey(thing.name) && sue.stuff.get(thing.name)!=thing.count) {
                        removeMe.add(sue);
                    }
                }
            }
            data.removeAll(removeMe);
        }
        return data;
    }

    private static List<Sue> parse(List<String> input) {
        List<Sue> result = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(" |,|:");
            if (parts.length!=14) throw new UnsupportedOperationException();

            Sue sue = new Sue(parts[1], parts[3], parts[5], parts[7], parts[9], parts[11], parts[13]);
            result.add(sue);
        }
        return result;
    }
}
