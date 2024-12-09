package advent.y2016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import advent.Util;

public class Day14 {
    static final String SAMPLE = "abc";
    static final String SALT = "zpqevtbw";
    static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    static MessageDigest digester;

    private static class Candidate {
        final long index;
        final String salt;

        final String value;
        final char tripledLetter;
        final Set<Character> quintupledLetters = new HashSet<>();

        final String part2value;
        final char part2tripledLetter;
        final Set<Character> part2quintupledLetters = new HashSet<>();

        public Candidate(long index, String salt) {
            this.index = index;
            this.salt = salt;

            value = md5(salt + index);
            tripledLetter = populateQuints();

            part2value = null;
            part2tripledLetter = 'X';
        }

        private char populateQuints() {
            int tripleLetterIndex = Integer.MAX_VALUE;
            char trip = 'X'; // not a hex digit
            for (int i = 0; i < digits.length; ++i) {
                int tlIndex = hasTriple(digits[i], value);
                if (tlIndex >= 0 && tlIndex < tripleLetterIndex) {
                    tripleLetterIndex = tlIndex;
                    trip = digits[i];
                }

                if (hasQuint(digits[i], value))
                    quintupledLetters.add(digits[i]);
            }
            return trip;
        }

        public Candidate(long index, String salt, Object part2) {
            this.index = index;
            this.salt = salt;

            value = md5(salt + index);
            tripledLetter = populateQuints();

            String stretchedMD5 = value;
            for (int i = 0; i < 2016; ++i) {
                stretchedMD5 = md5(stretchedMD5);
            }
            part2value = stretchedMD5;
            part2tripledLetter = populatePart2Quints();
        }

        private char populatePart2Quints() {
            int tripleLetterIndex = Integer.MAX_VALUE;
            char trip = 'X'; // not a hex digit
            for (int i = 0; i < digits.length; ++i) {
                int tlIndex = hasTriple(digits[i], part2value);
                if (tlIndex >= 0 && tlIndex < tripleLetterIndex) {
                    tripleLetterIndex = tlIndex;
                    trip = digits[i];
                }

                if (hasQuint(digits[i], part2value))
                    part2quintupledLetters.add(digits[i]);
            }
            return trip;
        }

        public char tripledLetter() {
            return tripledLetter;
        }

        private static int hasTriple(char c, String v) {
            String triple = new String(new char[3]).replace('\0', c);
            return v.indexOf(triple);
        }

        private static boolean hasQuint(char c, String v) {
            String quint = new String(new char[5]).replace('\0', c);
            return v.contains(quint);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Candidate c) {
                if (part2value == null) {
                    return index == c.index && salt == c.salt;
                }
                return part2value.equals(c.part2value) && salt == c.salt;
            }
            return false;
        }

        @Override
        public int hashCode() {
            if (part2value == null) {
                return (int) index;
            }
            return ("part2" + index).hashCode();
        }

        @Override
        public String toString() {
            return "Candidate(idx=" + index + "; md5=" + (part2value==null ? value : part2value) + ")";
        }

        private static String md5(String string) {
            byte[] md5 = digester.digest(string.getBytes());
            return Util.getHex(md5).toLowerCase();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        digester = MessageDigest.getInstance("MD5");

        List<Candidate> keys = findKeys(SAMPLE);
        Util.log("With salt = %s, found the following keys: %n%s", SAMPLE, keys);
        Util.log("\n%s last key index (of %d) is %d", SAMPLE, keys.size(), keys.get(keys.size() - 1).index);

        keys = findKeys(SALT);
        Util.log("\nWith salt = %s, found the following keys: %n%s", SALT, keys);
        Util.log("\n%s last key index (of %d) is %d", SALT, keys.size(), keys.get(keys.size() - 1).index);

        keys = findPart2Keys(SAMPLE);
        Util.log("\nWith salt = %s, found the following keys: %n%s", SAMPLE, keys);
        Util.log("\n%s last key index (of %d) is %d", SAMPLE, keys.size(), keys.get(keys.size() - 1).index);

        keys = findPart2Keys(SALT);
        Util.log("\nWith salt = %s, found the following keys: %n%s", SALT, keys);
        Util.log("\n%s last key index (of %d) is %d", SALT, keys.size(), keys.get(keys.size() - 1).index);
    }

    private static List<Candidate> findKeys(String salt) {
        int index = 0;

        /** next 1000 candidates */
        Queue<Candidate> candidates = new ArrayDeque<>(1000);
        /** keys identified */
        List<Candidate> keys = new ArrayList<>();

        Candidate candidate = new Candidate(index, salt);
        for (int i = 0; i < 1000; ++i) {
            candidates.add(new Candidate(++index, salt));
        }

        while (keys.size() < 64) {
            for (Candidate c : candidates) {
                if (c.quintupledLetters.contains(candidate.tripledLetter())) {
                    keys.add(candidate);
                }
            }

            candidate = candidates.poll();
            Candidate c = new Candidate(++index, salt);
            candidates.add(c);
        }
        return keys;
    }

    private static List<Candidate> findPart2Keys(String salt) {
        int index = 0;
        Object o = new Object(); // just a constructor flag

        /** next 1000 candidates */
        Queue<Candidate> candidates = new ArrayDeque<>(1000);
        /** keys identified */
        List<Candidate> keys = new ArrayList<>();

        Candidate candidate = new Candidate(index, salt, o);
        for (int i = 0; i < 1000; ++i) {
            candidates.add(new Candidate(++index, salt, o));
        }

        while (keys.size() < 64) {
            for (Candidate c : candidates) {
                if (c.part2quintupledLetters.contains(candidate.part2tripledLetter)) {
                    keys.add(candidate);
                }
            }

            candidate = candidates.poll();
            Candidate c = new Candidate(++index, salt, o);
            candidates.add(c);
        }
        return keys;
    }
}
