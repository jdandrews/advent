package advent.y2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day11 {

    public static void main(String[] args) {
        char[][] testPasswords = {
                {'h', 'i', 'j', 'k', 'l', 'm', 'm', 'n'},
                {'a', 'b', 'b', 'c', 'e', 'f', 'f', 'g'},
                {'a', 'b', 'b', 'c', 'e', 'g', 'j', 'k'}
        };
        Util.log("password %s : %s", toString(testPasswords[0]), isValid(testPasswords[0]));
        Util.log("password %s : %s", toString(testPasswords[1]), isValid(testPasswords[1]));
        Util.log("password %s : %s", toString(testPasswords[2]), isValid(testPasswords[2]));

        char[] testPassword = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        String testPwd = toString(testPassword);
        while (! isValid(testPassword)) {
            testPassword=increment(testPassword);
        }
        Util.log("first valid test password after %s is %s", testPwd, toString(testPassword));

        char[] testPassword2 = {'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n'};
        String testPwd2 = toString(testPassword2);
        while (! isValid(testPassword2)) {
            testPassword2=increment(testPassword2);
        }
        Util.log("first valid test password after %s is %s", testPwd2, toString(testPassword2));
        
        // part 1
        char[] password = {'h', 'e', 'p', 'x', 'c', 'r', 'r', 'q'};
        String pwd = toString(password);
        while (! isValid(password)) {
            password=increment(password);
        }
        Util.log("part 1: first valid password after %s is %s", pwd, toString(password));
        // part 2
        password=increment(password);
        while (! isValid(password)) {
            password=increment(password);
        }
        Util.log("part 2: next password is %s", toString(password));
    }

    private static char[] increment(char[] chars) {
        char[] result = Arrays.copyOf(chars, chars.length);
        // pre-empt long fruitless searches (this makes the answer too high. why?)
        /*
        for (int i=0; i<result.length; ++i) {
            if (result[i]=='i') {
                result[i]='j';
                break;
            } else if (result[i]=='o') {
                result[i]='p';
                break;
            } else if (result[i]=='l') {
                result[i]='m';
                break;
            }
        }
        */

        result[result.length-1]++;

        if (result[chars.length-1] > 'z') {
            if (result.length==1) {
                result[0] = 'a';
                return result;
            }
            char[] prefix = increment(Arrays.copyOf(result, result.length-1));
            for (int i=0; i<prefix.length; ++i) {
                result[i] = prefix[i];
            }
            result[prefix.length] = 'a';
        }

        return result;
    }

    private static String toString(char[] password) {
        StringBuilder result = new StringBuilder();
        for (char c : password) {
            result.append(c);
        }

        return result.toString();
    }

    private static final List<String> THREE_LETTER_SEQUENCES = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("abc"); add("bcd"); add("cde"); add("def"); add("efg"); add("fgh"); add("ghi"); add("hij");
            add("ijk"); add("jkl"); add("klm"); add("lmn"); add("mno"); add("nop"); add("opq"); add("pqr");
            add("qrs"); add("rst"); add("stu"); add("tuv"); add("uvw"); add("vwx"); add("wxy"); add("xyz");
        }
    };

    private static final List<String> TWO_LETTER_DUPLICATES = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("aa"); add("bb"); add("cc"); add("dd"); add("ee"); add("ff"); add("gg"); add("HH");
            add("ii"); add("jj"); add("kk"); add("ll"); add("mm"); add("nn"); add("oo"); add("pp");
            add("qq"); add("rr"); add("ss"); add("tt"); add("uu"); add("vv"); add("ww"); add("xx"); add("yy"); add("zz");
        }
    };

    private static boolean isValid(char[] password) {
        String pwd = String.valueOf(password);
        boolean hasTls = false;
        for (String tls : THREE_LETTER_SEQUENCES) {
            if (pwd.contains(tls)) {
                hasTls=true;
                break;
            }
        }
        if (! hasTls) {
            return false;
        }

        Set<String> tlds = new HashSet<>();
        for (String tld : TWO_LETTER_DUPLICATES) {
            if (pwd.contains(tld)) {
                tlds.add(tld);
            }
            if (tlds.size()>=2) {
                break;
            }
        }
        if (tlds.size() < 2) {
            return false;
        }

        if (pwd.contains("i") || pwd.contains("o") || pwd.contains("l")) {
            return false;
        }
        return true;
    }
}
