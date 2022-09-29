package advent.y2017;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;
import advent.y2017.datastructures.Islands;
import advent.y2017.datastructures.RingBuffer;

public class Day14 {
    private static final int STRING_SIZE = 256;
    private static final int BIT_ARRAY_SIZE = 128;

    private static List<Integer> string = new RingBuffer<>(STRING_SIZE);
    static {
        initBuffer();
    }

    private static void initBuffer() {
        for (int i=0; i<STRING_SIZE; ++i) {
            string.add(i,i);
        }
    }

    public static void main(String[] args) {
        /* flqrgnkx
         *
         * ##.#.#..-->      : 1101 0100 = d4...
         * .#.#.#.#         : 0101 0101 = 55...
         * ....#.#.         : 0000 1010 = 0a...
         * #.#.##.#         : 1010 1101 = ad...
         * .##.#...         : 0110 1000 = 68...
         * ##..#..#         : 1100 1001 = c9...
         * .#...#..         : 0100 0100 = 44...
         * ##.#.##.-->      : 1101 0110 = d6...
         * |      |
         * V      V
         */
        Map<Character, Integer> bitsSetPerNybble = mapNybblesToBitsSet();

        String messageBase =  "nbysizxe-";  // mine: "nbysizxe-"; sample: "flqrgnkx-";
        int blocksInUse = 0;
        int M[][]= new int[BIT_ARRAY_SIZE][BIT_ARRAY_SIZE];

        for (int i=0; i<BIT_ARRAY_SIZE; ++i) {
            String message = messageBase + i;
            byte[] xorb = hash(getMessageInts(message));
            String hash = Util.getHex(xorb).toLowerCase();
            // Util.log("hash(\"%s\") = %s", message, hash);

            for (int j=0; j<hash.length(); ++j) {
                char nybble = hash.charAt(j);
                blocksInUse += bitsSetPerNybble.get(nybble);
                for (int k=0; k<4; ++k) {
                    M[i][4*j+k] = getBit(nybble, k);
                }
            }
        }

        Islands islands = new Islands(BIT_ARRAY_SIZE,BIT_ARRAY_SIZE);
        int nRegions = islands.countIslands(M);

        Util.log("blocks used: %d; regions: %d", blocksInUse, nRegions);
   }

    private static int getBit(char nybble, int bitsFromTheLeft) {
        return ( Arrays.binarySearch(nybbles, nybble) >> (3 - bitsFromTheLeft )) & 0b0001;
    }

    private static char[] nybbles = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static Map<Character, Integer> mapNybblesToBitsSet() {
        Map<Character, Integer> bitsSetPerByte = new HashMap<>();

        for (byte n = 0; n<nybbles.length; ++n) {
            int nBits = 0;
            byte b = n;
            while (b>0) {
                nBits += b & 0b0001;
                b = (byte) (b>>1);
            }
            bitsSetPerByte.put(nybbles[n], nBits);
        }
        return bitsSetPerByte;
    }

    private static List<Integer> getMessageInts(String message) {
        List<Integer> termination = Arrays.asList(17, 31, 73, 47, 23);      // DC1, US, 'I', '/', ETB
        byte[] messageBytes = message.getBytes(Charset.forName("US-ASCII"));
        List<Integer> messageInts = new ArrayList<>(messageBytes.length+termination.size());
        for (byte b : messageBytes) {
            messageInts.add((int)b);
        }
        messageInts.addAll(termination);
        return messageInts;
    }

    private static byte[] hash(List<Integer> messageInts) {
        knot(messageInts, 64);

        byte[] xorb = new byte[STRING_SIZE/16];
        for (int i=0; i<STRING_SIZE/16; ++i) {
            xorb[i] = string.get(i*16).byteValue();
            for (int j=1; j<16; ++j) {
                xorb[i] = (byte) (xorb[i] ^ string.get(i*16 + j).byteValue());
            }
        }
        return xorb;
    }

    private static void knot(List<Integer> input, int cycles) {
        initBuffer();
        int skip = 0;
        int pc = 0;
        for (int cycle = 0; cycle < cycles; ++cycle) {
            for (int i : input) {
                reverse(string, pc, i);
                pc += i + skip++;
            }
        }
    }

    private static void reverse(List<Integer> s, int start, int length) {
        for (int i=0; i < length/2; ++i) {
            int n0 = start+i;
            int n1 = start+length-i-1;
            int swap = s.get(n0);
            s.set(n0, s.get(n1));
            s.set(n1, swap);
        }
    }
}
