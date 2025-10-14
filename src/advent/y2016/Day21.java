package advent.y2016;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day21 {
    private static final List<String> SAMPLE = List.of(
            "swap position 4 with position 0",
            "swap letter d with letter b",
            "reverse positions 0 through 4",
            "rotate left 1 step",
            "move position 1 to position 4",
            "move position 3 to position 0",
            "rotate based on position of letter b",
            "rotate based on position of letter d"
            );

    private static List<String> DATA;
    static {
        try {
            DATA = Util.readInput("2016", "Day21.txt");
        } catch (IOException e) {
            e.printStackTrace();
            DATA = new ArrayList<>();
        }
    }

    enum OP_CODE { SWAP_BY_INDEX, SWAP_BY_LETTER, ROTATE_BY_N, ROTATE_BY_LETTER, REVERSE, MOVE }
    private static record Op(OP_CODE opcode, int idx1, int idx2, char ch1, char ch2) {
        @Override
        public final String toString() {
            return "[" + opcode + ": " + idx1 + ", "+ idx2 + ", "+ ch1 + ", "+ ch2 + "]";
        }
    }

    public static void rotTest() {
        rotTest('a', "abcdefgh");
        rotTest('b', "abcdefgh");
        rotTest('c', "abcdefgh");
        rotTest('d', "abcdefgh");
        rotTest('e', "abcdefgh");
        rotTest('f', "abcdefgh");
        rotTest('g', "abcdefgh");
        rotTest('h', "abcdefgh");

        /*
        rotTest('a', "abcde");
         */
    }

    private static void rotTest(char letter, String pass) {
        String result = rotateByLetter(letter ,pass);
        int idx = result.indexOf(letter);
        Util.log("rotate %s: %s, idx = %d", letter, result, idx);

        result = unrotateByLetter(letter, result);
        idx = result.indexOf(letter);
        Util.log("unrotate %s: %s, idx = %d", letter, result, idx);
    }

    public static void main(String[] args) throws IOException {
        // rotTest();

        Util.log("part 1 SAMPLE scrambled: %s", solve(SAMPLE, "abcde"));
        Util.log("\npart 1 DATA scrambled:   %s", solve(DATA, "abcdefgh"));

        Util.log("\npart 2 unscrambled:   %s", unscramble(DATA, "fbgdceah"));
    }

    private static String solve(List<String> algorithmDescription, String password) {
        List<Op> algorithm = parse(algorithmDescription);
        String scrambled = password;
        for (Op operation: algorithm) {
            scrambled = switch (operation.opcode()) {
            case SWAP_BY_INDEX -> swapByIndex(operation.idx1(), operation.idx2(), scrambled);
            case SWAP_BY_LETTER -> swapByLetter(operation.ch1(), operation.ch2(), scrambled);
            case ROTATE_BY_N -> rotateByN(operation.idx1(), operation.idx2(), scrambled);
            case ROTATE_BY_LETTER -> rotateByLetter(operation.ch1(), scrambled);
            case REVERSE -> reverse(operation.idx1(), operation.idx2(), scrambled);
            case MOVE -> move(operation.idx1(), operation.idx2(), scrambled);
            default -> throw new UnsupportedOperationException("not implemented yet: " + operation.opcode());
            };
            Util.log("after %s, scrambled is %s", operation, scrambled);
        }
        return scrambled;
    }

    private static String unscramble(List<String> algorithmDescription, String scrambled) {
        Util.log("\n----\nscrambled text is %s", scrambled);
        List<String> unscramble = new ArrayList<>();
        for (String step : algorithmDescription) {
            unscramble.add(0, step);
        }

        List<Op> algorithm = parse(unscramble);
        String password = scrambled;
        for (Op operation: algorithm) {
            password = switch (operation.opcode()) {
            case SWAP_BY_INDEX -> swapByIndex(operation.idx1(), operation.idx2(), password);
            case SWAP_BY_LETTER -> swapByLetter(operation.ch1(), operation.ch2(), password);
            case ROTATE_BY_N -> rotateByN( - operation.idx1(), operation.idx2(), password);
            case ROTATE_BY_LETTER -> unrotateByLetter(operation.ch1(), password);
            case REVERSE -> reverse(operation.idx1(), operation.idx2(), password);
            case MOVE -> move(operation.idx2(), operation.idx1(), password);    // ...unmove
            default -> throw new UnsupportedOperationException("not implemented yet: " + operation.opcode());
            };
            Util.log("after %s, password is %s", operation, password);
        }
        return password;
    }

    private static String move(int idx1, int idx2, String in) {
        StringBuilder out = new StringBuilder(in);
        char ch = in.charAt(idx1);
        out.deleteCharAt(idx1);
        out.insert(idx2, ch);
        return out.toString();
    }

    private static String reverse(int idx1, int idx2, String in) {
        return in.substring(0, idx1) + reverse(in.substring(idx1, idx2+1)) + in.substring(idx2+1);
    }

    private static String reverse(String s) {
        StringBuilder out = new StringBuilder();
        for (char c : s.toCharArray()) {
            out.insert(0, c);
        }
        return out.toString();
    }

    private static String rotateByLetter(char ch1, String in) {
        int idx = in.indexOf(ch1) + 1;
        if (idx > 4) ++idx;

        // rotate right
        return rotateByN(+1, idx, in);
    }

    private static String unrotateByLetter(char ch1, String in) {
        if (in.length() != 8) {
            throw new UnsupportedOperationException("not implemented yet: in.length() != 8");
        }

        int [] findIdx = {1, 3, 5, 7, 2, 4, 6, 0};
        int n = in.indexOf(ch1);
        int idx = 0;
        for (int i = 0; i < 8; ++i) {
            if (n == findIdx[i]) {
                idx = i + 1;
                if (idx > 4) ++idx;
                break;
            }
        }
        // rotate left
        return rotateByN(-1, idx, in);
    }

    private static String rotateByN(int idx1, int idx2, String in) {
        int n = idx2 % in.length();
        if (idx1 > 0) { // rotate right
            return in.substring(in.length() - n) + in.substring(0, in.length() - n);
        }
        else if (idx1 < 0) {  // rotate left
            return in.substring(n) + in.substring(0, n);
        }
        else {
            throw new UnsupportedOperationException("can't rotate direction 0");
        }
    }

    private static String swapByIndex(int idx1, int idx2, String in) {
        StringBuilder out = new StringBuilder(in);
        char ch = out.charAt(idx1);
        out.setCharAt(idx1, out.charAt(idx2));
        out.setCharAt(idx2, ch);
        return out.toString();
    }

    private static String swapByLetter(char ch1, char ch2, String in) {
        StringBuilder out = new StringBuilder(in);

        int idx1 = in.indexOf(ch1);
        int idx2 = in.indexOf(ch2);

        char ch = out.charAt(idx1);
        out.setCharAt(idx1, out.charAt(idx2));
        out.setCharAt(idx2, ch);
        return out.toString();
    }

    private static List<Op> parse(List<String> algorithmDescription) {
        List<Op> result = new ArrayList<>();

        for (String op : algorithmDescription) {
            String[] words = op.split(" ");
            if (op.startsWith("swap position")) {
                result.add(new Op(OP_CODE.SWAP_BY_INDEX, Integer.parseInt(words[2]), Integer.parseInt(words[5]), '.', '.'));
            }
            else if (op.startsWith("swap letter")) {
                result.add(new Op(OP_CODE.SWAP_BY_LETTER, 0, 0, words[2].charAt(0), words[5].charAt(0)));
            }
            else if (op.startsWith("rotate left")) {
                result.add(new Op(OP_CODE.ROTATE_BY_N, -1, Integer.parseInt(words[2]), '.', '.'));
            }
            else if (op.startsWith("rotate right")) {
                result.add(new Op(OP_CODE.ROTATE_BY_N, +1, Integer.parseInt(words[2]), '.', '.'));
            }
            else if (op.startsWith("rotate based")) {
                result.add(new Op(OP_CODE.ROTATE_BY_LETTER, +1, 0, words[6].charAt(0), '.'));
            }
            else if (op.startsWith("reverse")) {
                result.add(new Op(OP_CODE.REVERSE, Integer.parseInt(words[2]), Integer.parseInt(words[4]), '.', '.'));
            }
            else if (op.startsWith("move")) {
                result.add(new Op(OP_CODE.MOVE, Integer.parseInt(words[2]), Integer.parseInt(words[5]), '.', '.'));
            }
        }
        return result;
    }
}
