package advent.y2016;

import advent.Util;

public class Day17 {
    public static record Room(String doors) {
        private static String openKey = "bcdef";
        public String exits(String exitKey) {
            StringBuilder exits = new StringBuilder();
            // U D L R
            if (doors.contains("U") && openKey.contains(exitKey.substring(0,1))) {
                exits.append("U");
            }
            if (doors.contains("D") && openKey.contains(exitKey.substring(1,2))) {
                exits.append("D");
            }
            if (doors.contains("L") && openKey.contains(exitKey.substring(2,3))) {
                exits.append("L");
            }
            if (doors.contains("R") && openKey.contains(exitKey.substring(3,4))) {
                exits.append("R");
            }
            return exits.toString();
        }
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static String getKey(String root, String path) {
        String md5 = Util.md5(root + path);
        return md5.substring(0, 4);
    }

}
