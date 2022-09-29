package advent.y2016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import advent.Util;

public class Day14FromSolutionsB {

    private static String ID = "zpqevtbw";	// "ngcjuoqr";
    private static Map<String, String> map;
    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startPart1() {
        start(1);
    }

    public static void startPart2() {
        start(2017);
    }

    private static void start(int amount) {
        map = new HashMap<>();
        int count = 0;
        int iteration = 0;
        while (count != 64) {
            String hexEncoded = repeatedHash(ID + iteration, amount);
            for (int i = 0; i < hexEncoded.length() - 2; i++) {
                if (isTriple(hexEncoded, i)) {
                    char c = hexEncoded.charAt(i);
                    for (int j = 1; j <= 1000; j++) {
                        hexEncoded = repeatedHash(ID + (iteration + j), amount);
                        if (hexEncoded.contains(repeatedChar(c, 5))) {
                            count++;
                            break;
                        }
                    }
                    break;
                }
            }
            iteration++;
        }
        System.out.println(iteration - 1);
    }

    private static boolean isTriple(String s, int index) {
        return s.substring(index, index + 3).equals(repeatedChar(s.charAt(index), 3));
    }

    private static String repeatedChar(char c, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static String repeatedHash(String s, int amount) {
        if (map.containsKey(s)) {
            return map.get(s);
        }
        String hash = s;
        for (int i = 0; i < amount; i++) {
            byte[] idBytes = hash.getBytes();
            byte[] encodedBytes = digest.digest(idBytes);
            hash = Util.getHex(encodedBytes);
        }
        map.put(s, hash);
        return hash;
    }
}