package advent.y2016;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author /u/Philboyd_Studge on 12/13/2016.
 * https://gist.github.com/anonymous/cda6487a70ac7591c1a7d548d2fede8e
 */
public class Day14FromSolutions {

    static Pattern p = Pattern.compile("([a-f\\d])\\1\\1");
    static int[] lookup = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
    static MessageDigest md;

    static String has3(String s) {
        Matcher m = p.matcher(s);
        if (m.find()) return m.group(1);
        return "";
    }

    static byte[] hashBytes(byte[] in) {
        byte[] digest = new byte[in.length * 2];
        for (int i = 0, j = 0; i < in.length; i++, j += 2) {
            digest[j] = (byte) lookup[(in[i] >> 4) & 0xf];
            digest[j + 1] = (byte) lookup[in[i] & 0xf];
        }
        md.update(digest);
        return md.digest();
    }

    static String make5(String c) {
        return c +  c + c + c + c;
    }
    static boolean has5(String s, String t) {
        return t.contains(s);
    }

    static String bytesToHex(byte[] in) {
        StringBuilder sb = new StringBuilder();
        for(byte each : in) {
            sb.append((char) lookup[(each >> 4) & 0xf]).append((char) lookup[each & 0xf]);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        md = MessageDigest.getInstance("MD5");

        boolean part2 = false;
        String salt = "zpqevtbw";	// "jlmsuwbz"; //"jlmsuwbz";

        List<Integer> keys = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>();

        int index = 0;

        long time = System.currentTimeMillis();
        while (keys.size() < 64) {
            String temp = salt + index;
            String hex;
            if (map.containsKey(index)) {
                hex = map.get(index);
            } else {
                byte[] hash = md.digest(temp.getBytes());
                if (part2) {
                    for (int i = 0; i < 2016; i++) {
                        hash = hashBytes(hash);
                    }
                }
                hex = bytesToHex(hash);
                map.putIfAbsent(index, hex);
            }

            String c = has3(hex);

            if (!c.equals("")) {
                byte[] hash2;
                String hex2;
                String five = make5(c);
                for (int i = index + 1; i < index + 1000; i++) {
                    if (map.containsKey(i)) {
                        hex2 = map.get(i);
                    } else {
                        temp = salt + i;
                        hash2 = md.digest(temp.getBytes());
                        if (part2) {
                            for (int j = 0; j < 2016; j++) {
                                hash2 = hashBytes(hash2);
                            }
                        }
                        hex2 = bytesToHex(hash2);
                        map.putIfAbsent(i, hex2);
                    }
                    if (has5(five, hex2)) {
                        keys.add(index);
                        break;
                    }

                }
            }
            index++;
        }
        System.out.println("Time : " + (System.currentTimeMillis() - time));
        System.out.println(keys.get(keys.size() - 1));
    }
}
