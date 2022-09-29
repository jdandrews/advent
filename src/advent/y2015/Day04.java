package advent.y2015;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import advent.Util;

public class Day04 {
    private static class HashResult {
        public byte[] md5;
        public int idx;
        public HashResult(int s, byte[] hash) {
            idx = s;
            md5 = hash;
        }
    }

    private static String seed = "ckczppom";
    private static String sample1 = "abcdef";
    private static String sample2 = "pqrstuv";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        HashResult r = nextInterestingHash(0, sample1);
        Util.log("MD5(%s %d) = %s", sample1, r.idx, Util.getHex(r.md5));

        r = nextInterestingHash(0, sample2);
        Util.log("MD5(%s %d) = %s", sample2, r.idx, Util.getHex(r.md5));

        r = nextInterestingHash(0, seed);
        Util.log("MD5(%s %d) = %s", seed, r.idx, Util.getHex(r.md5));

        do {
            r = nextInterestingHash(r.idx+1, seed);
        } while ( ! Util.getHex(r.md5).startsWith("000000"));
        Util.log("MD5(%s %d) = %s", seed, r.idx, Util.getHex(r.md5));
    }

    private static HashResult nextInterestingHash(int start, String seed) throws NoSuchAlgorithmException {
        byte[] md5;
        MessageDigest digester = MessageDigest.getInstance("MD5");
        for (int i = start; i < Integer.MAX_VALUE; ++i) {
            String seedString = seed + i;
            md5 = digester.digest(seedString.getBytes());
            // if first 5 digits in hex are 0
            if (md5[0] == 0 && md5[1] == 0 && md5[2] <= 15 && md5[2] >= 0) {
                return new HashResult(i, md5);
            }
        }
        throw new IllegalStateException("unable to find interesting hash");
    }

}
