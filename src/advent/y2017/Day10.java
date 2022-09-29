package advent.y2017;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.Util;
import advent.y2017.datastructures.RingBuffer;

public class Day10 {
    private static final int STRING_SIZE = 256;

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
        List<Integer> input = Arrays.asList(83,0,193,1,254,237,187,40,88,27,2,255,149,29,42,100);
        //  test data: Arrays.asList(3, 4, 1, 5);

        knot(input, 1);
        Util.log("checkproduct: %d\n\n", string.get(0) * string.get(1));

        String[] messages = {"", "AoC 2017", "1,2,3", "1,2,4"};
        for (String message : messages) {
            Util.log("%s", getMessageInts(message));
            byte[] xorb = hash(getMessageInts(message));
            Util.log("hash(\"%s\") = %s\n", message, Util.getHex(xorb).toLowerCase());
        }

        List<Integer> messageInts = getMessageInts("83,0,193,1,254,237,187,40,88,27,2,255,149,29,42,100");
        byte[] xorb = hash(messageInts);

        Util.log("hash = %s", Util.getHex(xorb).toLowerCase());

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
