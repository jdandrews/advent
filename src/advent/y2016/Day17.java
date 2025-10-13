package advent.y2016;

import java.util.ArrayDeque;
import java.util.Deque;
import advent.Util;

public class Day17 {
    public static class Room {
        // 4x4 maze; exit is 3,3
        private static final String doors[][] = {
                { "DR", "DLR", "DLR", "DL" },
                { "UDR", "UDLR", "UDLR", "UDL" },
                { "UDR", "UDLR", "UDLR", "UDL" },
                { "UR", "ULR", "ULR", null }
        };

        final int r;
        final int c;

        public Room(int row, int col) {
            r = row;
            c = col;
        }

        @Override
        public final String toString() {
            return "[" + r + "," + c + "] - " + exits("FFFF");
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Room)) {
                return false;
            }
            Room room = (Room) obj;
            return r == room.r && c == room.c;
        }

        @Override
        public int hashCode() {
            return r + 4 * c;
        }

        public boolean isExit() {
            return r == 3 && c == 3;
        }

        /**
         * Identify open exits to the thie room. Key is a hex string, 4 chars long. 0->A-locked, B->F-open.
         *
         * @param key 4 char hex string
         * @return a list of open exits, UDLR.
         */
        public String exits(String key) {
            if (doors[r][c] == null) {
                return null;
            }
            StringBuilder exits = new StringBuilder();
            // U D L R
            if (doors[r][c].contains("U") && Integer.parseInt(key.substring(0, 1), 16) > 10) {
                exits.append("U");
            }
            if (doors[r][c].contains("D") && Integer.parseInt(key.substring(1, 2), 16) > 10) {
                exits.append("D");
            }
            if (doors[r][c].contains("L") && Integer.parseInt(key.substring(2, 3), 16) > 10) {
                exits.append("L");
            }
            if (doors[r][c].contains("R") && Integer.parseInt(key.substring(3, 4), 16) > 10) {
                exits.append("R");
            }
            return exits.toString();
        }
    }

    private record State(Room room, String path) { }

    private static final String SAMPLE1 = "ihgpwlah";
    private static final String SAMPLE2 = "kglvqrro";
    private static final String SAMPLE3 = "ulqzkmiv";

    private static final String INPUT = "mmsxrhfx";

    public static void main(String[] args) {
        System.out.println("\nSAMPLE1");
        solve(SAMPLE1);

        System.out.println("\nSAMPLE2");
        solve(SAMPLE2);

        System.out.println("\nSAMPLE3");
        solve(SAMPLE3);

        System.out.println("\npart 1:");
        solve(INPUT);

    }

    private static void solve(String root) {
        String minimumLengthResult = null;
        String maximumLengthResult = "";
        Deque<State> stack = new ArrayDeque<>();

        stack.push(new State(new Room(0, 0), ""));
        while (!stack.isEmpty()) {
            State s = stack.pop();
            String key = getKey(root, s.path());
            String doors = s.room().exits(key);
            if (doors.length() == 0) {
                continue;
            }
            for (int i = 0; i < doors.length(); ++i) {
                Room newRoom = exit(s.room(), doors.charAt(i));
                String newPath = s.path() + doors.charAt(i);
                if (newRoom.isExit()) {
                    if (minimumLengthResult == null || newPath.length() < minimumLengthResult.length()) {
                        minimumLengthResult = newPath;
                    }
                    if (newPath.length() == minimumLengthResult.length()) {
                        System.out.println("found short path " + newPath);
                    }

                    if (maximumLengthResult.length() < newPath.length()) {
                        maximumLengthResult = newPath;
                    }
                    if (newPath.length() >= maximumLengthResult.length()) {
                        System.out.println("found long path " + newPath.length() + " chars.");
                    }
                } else {
                    stack.push(new State(newRoom, newPath));
                }
            }
        }

        System.out.println("part 1: first minimum length result is " + minimumLengthResult);
        System.out.println("part 2: first maximum length result is " + maximumLengthResult.length() + " chars.");
        System.out.println("----");
    }

    private static Room exit(Room room, char direction) {
        return switch (direction) {
        case 'U' -> new Room(room.r - 1, room.c);
        case 'D' -> new Room(room.r + 1, room.c);
        case 'L' -> new Room(room.r, room.c - 1);
        case 'R' -> new Room(room.r, room.c + 1);
        default -> throw new UnsupportedOperationException("direction " + direction + " is not supported.");
        };
    }

    public static String getKey(String root, String path) {
        String md5 = Util.md5(root + path);
        return md5.substring(0, 4);
    }

}
