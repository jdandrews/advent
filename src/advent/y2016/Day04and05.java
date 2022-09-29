package advent.y2016;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day04and05 {
    private static final String DATA_FILE = "src/advent/y2016/Day04.txt";
    private static class HashResult {
        public byte[] md5;
        public int seed;
        public HashResult(int s, byte[] hash) {
            seed = s;
            md5 = hash;
        }
    }
    private static class Combination {
        private char[] combo = {0,0,0,0,0,0,0,0};
        public String put(int position, char c) {
            if (position >= 0 && position < combo.length && combo[position]==0) {
                combo[position] = c;
            }
            return toString();
        }
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int i=0; i<combo.length; ++i) {
                result.append(combo[i]==0 ? '_' : combo[i]);
            }
            return result.toString();
        }
        public boolean isComplete() {
            for (int i=0; i<combo.length; ++i) {
                if (combo[i]==0) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        test();

        List<String> inputLines = readInput();

        int sectorIdSum = 0;
        for (String line : inputLines) {
            String[] segments = parseLine(line);
            String checksum = checksum(segments[0]);
            if (checksum.equals(segments[2])) {
                // real room
                int sectorId = Integer.parseInt(segments[1]);
                sectorIdSum += sectorId;

                String room = decrypt(segments[0], sectorId);
                if (room.contains("north")) {
                    log("%s --> %s", line, room);
                }
            }
        }
        log("sector sum: %s", sectorIdSum);

        // find door combo, starting with results from the previous stuff
        String doorId = "ffykfhsq";
        Combination combo1 = new Combination();
        Combination combo2 = new Combination();
        int nDigit = 0;
        int seed = 0;
        while ((!combo1.isComplete() || !combo2.isComplete()) && seed < Integer.MAX_VALUE) {
            HashResult h = nextInterestingHash(doorId, seed);
            log("%02x %02x %02x %02x %s",h.md5[0], h.md5[1], h.md5[2], h.md5[3], seed);

            if (!combo1.isComplete()) {
                combo1.put(nDigit++, String.format("%02x", h.md5[2]).charAt(1));
            }
            if (!combo2.isComplete()) {
                combo2.put(h.md5[2], String.format("%02x", h.md5[3]).charAt(0));
            }
            seed = h.seed+1;
        }
        log("door combination 1: %s", combo1.toString());
        log("door combination 2: %s", combo2.toString());
    }

    private static HashResult nextInterestingHash(String doorId, int seed) throws NoSuchAlgorithmException {
        byte[] md5;
        MessageDigest digester = MessageDigest.getInstance("MD5");
        for (int i = seed; i < Integer.MAX_VALUE; ++i) {
            String seedString = doorId + i;
            md5 = digester.digest(seedString.getBytes());
            if (md5[0] == 0 && md5[1] == 0 && md5[2] >= 0 && md5[2] < 16) {
                return new HashResult(i,md5);
            }
        }
        throw new IllegalStateException("unable to find interesting hash");
    }

    private static String checksum(String s) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : s.toCharArray()) {
            if (! Character.isLowerCase(c)) {
                continue;
            }
            if (!charCount.containsKey(c)) {
                charCount.put(c, 0);
            }

            charCount.put(c, charCount.get(c).intValue()+1);
        }

        List<Character> letters = new ArrayList<>(charCount.keySet());
        Collections.sort(letters, new Comparator<Character>() {

            @Override
            public int compare(Character o1, Character o2) {
                int nO1 = charCount.get(o1.charValue());
                int nO2 = charCount.get(o2.charValue());

                return nO1==nO2 ? o1.compareTo(o2) : nO1 > nO2 ? -1 : 1;
            }
        });

        StringBuilder result = new StringBuilder();
        for (int i=0; i<5; ++i) {
            result.append(letters.get(i));
        }

        return result.toString();
    }

    private static String[] parseLine(String line) {
        int checksumStart = line.indexOf('[');
        int checksumEnd = line.indexOf(']');
        int sectorIdStart = line.lastIndexOf('-', checksumStart);
        if (checksumStart<0 || checksumEnd<0 || sectorIdStart<0) {
            throw new IllegalArgumentException("supplied line is not in the expected format: "+line);
        }
        String[] result = new String[3];

        result[0] = line.substring(0, sectorIdStart);
        result[1] = line.substring(sectorIdStart+1, checksumStart);
        result[2] = line.substring(checksumStart+1, checksumEnd);

        return result;
    }

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private static void test() {
        log("--- tests ---");
        String[] s = {"aaaaa-bbb-z-y-x-123[abxyz]", "a-b-c-d-e-f-g-h-987[abcde]", "not-a-real-room-404[oarel]", "totally-real-room"};
        for (int i=0; i<s.length; ++i) {
            log("checksum %s = %s", s[i], checksum(s[i]));
        }

        // qzmt-zixmtkozy-ivhz-343 is very encrypted name.
        log("%s", decrypt("qzmt-zixmtkozy-ivhz",343));

        log("----end tests----\n");
    }

    private static String decrypt(String s, int n) {
        int rot = n%26;
        StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray()) {
            c += rot;
            if (c>'z') {
                c -= 26;
            }
            if (! Character.isLowerCase(c)) {
                c = ' ';
            }

            result.append(c);
        }
        return result.toString();
    }

    private static List<String> readInput() throws IOException {
        List<String> result = new ArrayList<>();

        File infile = new File(DATA_FILE);
        System.out.println("reading "+infile.getAbsolutePath());

        try(LineNumberReader in = new LineNumberReader(new FileReader(infile))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                result.add(inputLine);
            }
        }

        return result;
    }
}
