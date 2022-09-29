package advent.y2017;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.Util;

/**
 * The CPU itself is a large, black building surrounded by a bottomless pit. Enormous metal tubes 
 * extend outward from the side of the building at regular intervals and descend down into the void. 
 * There's no way to cross, but you need to get inside.
 * 
 * No way, of course, other than building a bridge out of the magnetic components strewn about nearby.
 * 
 * Each component has two ports, one on each end. The ports come in all different types, and only matching 
 * types can be connected. You take an inventory of the components by their port types (your puzzle input). 
 * Each port is identified by the number of pins it uses; more pins mean a stronger connection for your bridge. 
 * A 3/7 component, for example, has a type-3 port on one side, and a type-7 port on the other.
 * 
 * Your side of the pit is metallic; a perfect surface to connect a magnetic, zero-pin port. Because of this, 
 * the first port you use must be of type 0. It doesn't matter what type of port you end with; your goal is 
 * just to make the bridge as strong as possible.
 * 
 * The strength of a bridge is the sum of the port types in each component. For example, if your bridge is made of 
 * components 0/3, 3/7, and 7/4, your bridge has a strength of 0+3 + 3+7 + 7+4 = 24.
 * 
 * For example, suppose you had the following components:
 * 
 * 0/2
 * 2/2
 * 2/3
 * 3/4
 * 3/5
 * 0/1
 * 10/1
 * 9/10
 * 
 * With them, you could make the following valid bridges:
 * 
 * 0/1
 * 0/1--10/1
 * 0/1--10/1--9/10
 * 0/2
 * 0/2--2/3
 * 0/2--2/3--3/4
 * 0/2--2/3--3/5
 * 0/2--2/2
 * 0/2--2/2--2/3
 * 0/2--2/2--2/3--3/4
 * 0/2--2/2--2/3--3/5
 * 
 * (Note how, as shown by 10/1, order of ports within a component doesn't matter. However, you may only use 
 * each port on a component once.)
 * 
 * Of these bridges, the strongest one is 0/1--10/1--9/10; it has a strength of 0+1 + 1+10 + 10+9 = 31.
 * 
 * What is the strength of the strongest bridge you can make with the components you have available?
 */
public class Day24 {
    private static class Component {
        public Component(int left, int right) {
            if (left <= right) {
                a = left;
                b = right;
            } else {
                a = right;
                b = left;
            }
        }

        public int a;
        public int b;

        @Override
        public String toString() {
            return a + "/" + b;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Component)) {
                return false;
            }

            Component c = (Component) obj;
            return c.a == a && c.b == b;
        }

        @Override
        public int hashCode() {
            return a + 11 * b;
        }
    }

    private static class Bridge {
        public Bridge(List<Component> availableComponents) {
            remainingComponents = new ArrayList<>(availableComponents);
        }

        public Bridge(Bridge b) {
            end = b.end;
            bridge = new ArrayList<>(b.bridge);
            remainingComponents = new ArrayList<>(b.remainingComponents);
        }

        private List<Component> bridge = new ArrayList<>();
        private List<Component> remainingComponents;
        private int end = 0;

        public void add(Component c) {
            if (!remainingComponents.contains(c)) {
                throw new IllegalArgumentException("Component " + c + " is not available.");
            }
            if (end != c.a && end != c.b) {
                throw new IllegalArgumentException(
                        "Component " + c + " does not match end " + end + " in " + toString());
            }
            end = (end == c.a) ? c.b : c.a;
            bridge.add(c);
            remainingComponents.remove(c);
        }

        public int length() {
            return bridge.size();
        }

        public int strength() {
            int result = 0;
            for (Component c : bridge) {
                result += c.a + c.b;
            }
            return result;
        }

        public List<Component> getOptionsForNextComponent() {
            List<Component> result = new ArrayList<>();
            for (Component c : remainingComponents) {
                if (c.a == end || c.b == end) {
                    result.add(c);
                }
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Component c : bridge) {
                sb.append(c.toString());
                sb.append("--");
            }
            if (sb.length()>0) {
                sb.delete(sb.length()-2, sb.length());
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        List<Component> components = parse(INPUT);

        List<Bridge> bridges = new ArrayList<>();
        bridges.add(new Bridge(components));

        List<Bridge> newBridges = new ArrayList<>();
        boolean addedComponents;
        do {
            newBridges.clear();
            addedComponents = false;

            for (Bridge bridge : bridges) {
                List<Component> addMe = bridge.getOptionsForNextComponent();
                if (addMe.size() > 0) {
                    addedComponents = true;
                    for (int i = 1; i < addMe.size(); ++i) {
                        Bridge b2 = new Bridge(bridge);
                        b2.add(addMe.get(i));
                        newBridges.add(b2);
                    }
                    bridge.add(addMe.get(0));
                }
            }

            bridges.addAll(newBridges);
        } while (addedComponents);

        int maxPath = 0;
        int longestStrength = 0;
        int longestLength = 0;

        for (Bridge bridge : bridges) {
            if (bridge.length() > longestLength) {
                longestLength = bridge.length();
                longestStrength = bridge.strength();
            }
            else if (bridge.length() == longestLength) {
                longestStrength = Math.max(longestStrength, bridge.strength());
            }

            maxPath = Math.max(maxPath, bridge.strength());
        }
        Util.log("maxPath = %d", maxPath);
        Util.log("longest is %d with max strength %d", longestLength, longestStrength);
    }

    private static List<Component> parse(String[] stringArray) {
        List<Component> result = new ArrayList<>();
        for (String s : stringArray) {
            String[] parts = s.split("/");
            result.add(new Component(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        return result;
    }

    private static final String[] TEST_INPUT = { "0/2", "2/2", "2/3", "3/4", "3/5", "0/1", "10/1", "9/10" };
    private static final String[] INPUT = { "31/13", "34/4", "49/49", "23/37", "47/45", "32/4", "12/35", "37/30",
            "41/48", "0/47", "32/30", "12/5", "37/31", "7/41", "10/28", "35/4", "28/35", "20/29", "32/20", "31/43",
            "48/14", "10/11", "27/6", "9/24", "8/28", "45/48", "8/1", "16/19", "45/45", "0/4", "29/33", "2/5", "33/9",
            "11/7", "32/10", "44/1", "40/32", "2/45", "16/16", "1/18", "38/36", "34/24", "39/44", "32/37", "26/46",
            "25/33", "9/10", "0/29", "38/8", "33/33", "49/19", "18/20", "49/39", "18/39", "26/13", "19/32" };
}
