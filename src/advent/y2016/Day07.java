package advent.y2016;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day07 extends LevelBase {
    public static void main(String[] args) throws IOException {
        DATA_FILE = "Day07.txt";
        test();

        List<String> data = readInput();
        int goodAddresses = 0;
        int supportsSsl = 0;
        for (String s : data) {
            if (evaluateAddress(s)) {
                ++goodAddresses;
            }
            if (supportsSsl(s)) {
                ++supportsSsl;
            }
        }
        log("%d ABBA addressses; %d support SSL.", goodAddresses, supportsSsl);
    }

    private static boolean supportsSsl(String address) {
        String supernet = getNonhypernet(address);
        String hypernet = getHypernet(address);

        Set<String> abas = getAbas(supernet);
        for (String aba : abas) {
            if (hypernet.contains(invertAba(aba))) {
                return true;
            }
        }
        return false;
    }

    private static Set<String> getAbas(String string) {
        Set<String> result = new HashSet<>();
        for (int i=0; i<string.length()-2; ++i) {
            if (string.charAt(i)==string.charAt(i+2) && string.charAt(i)!=string.charAt(i+1)) {
                result.add(string.substring(i,i+3));
            }
        }
        return result;
    }

    private static CharSequence invertAba(String aba) {
        return new StringBuilder()
                .append(aba.charAt(1))
                .append(aba.charAt(0))
                .append(aba.charAt(1))
                .toString();
    }

    private static boolean evaluateAddress(String address) {
        String string = getHypernet(address);
        if (hasAbba(string)) {
            return false;
        }

        string = getNonhypernet(address);
        if (hasAbba(string)) {
            return true;
        }
        return false;
    }

    private static String getNonhypernet(String address) {
        boolean in = false;
        StringBuilder result = new StringBuilder();
        for (char c : address.toCharArray()) {
            if (c == '[') {
                in = true;
                continue;
            }
            if (c == ']') {
                in = false;
                result.append(' ');
            }
            if (!in) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String getHypernet(String address) {
        boolean in = false;
        StringBuilder result = new StringBuilder();
        for (char c : address.toCharArray()) {
            if (c == '[') {
                in = true;
                continue;
            }
            if (c == ']') {
                in = false;
                result.append(' ');
            }
            if (in) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static boolean hasAbba(String string) {
        for(int i=0; i<string.length()-3; ++i) {
            String candidate = string.substring(i, i+4);
            if (candidate.substring(0,2).equals(reverse(candidate.substring(2,4)))
                    && ! candidate.substring(0,1).equals(candidate.substring(1,2))) {
                return true;
            }
        }
        return false;
    }

    private static String reverse(String string) {
        StringBuilder result = new StringBuilder();
        for (char c : string.toCharArray()) {
            result.insert(0, c);
        }
        return result.toString();
    }


    private static void test() {
        for (String s : TestData) {
            log("%s: %b %b", s, evaluateAddress(s), supportsSsl(s));
        }
        log("---------- end test ---------");
    }

    private static String[] TestData = {
            "abba[mnop]qrst",    // supports TLS (abba outside square brackets).
            "abcd[bddb]xyyx",   // does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
            "aaaa[qwer]tyui",   // does not support TLS (aaaa is invalid; the interior characters must be different).
            "ioxxoj[asdfgh]zxcvbn",  // supports TLS (oxxo is outside square brackets, even though it's within a larger string).
            "aba[bab]xyz",      // supports SSL (aba outside square brackets with corresponding bab within square brackets).
            "xyx[xyx]xyx",      // does not support SSL (xyx, but no corresponding yxy).
            "aaa[kek]eke",      // supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
            "zazbz[bzb]cdb"     // supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).
    };
}
