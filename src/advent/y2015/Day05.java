package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day05 {
    public static void main(String[] args) throws IOException {
        List<String> data = Util.readInput("2015", "Day05.txt");
        String[] samples = {
                "ugknbfddgicrmopn", 
                "aaa", 
                "jchzalrnumimnmhp", 
                "haegwjzuvuyypxyu", 
                "dvszwmarrgswjxmb",
                "qjhvhtzxzqqjkmpb",
                "xxyxx",
                "uurcxstgmygtbstg",
                "ieodomkazucvgmuy"};

        for (int i=0; i<samples.length; ++i) {
            Util.log("%s: %s, %s", samples[i], check1(samples[i]), check2(samples[i]));
        }

        int nice = 0;
        for (int i=0; i<data.size(); ++i) {
            if (check1(data.get(i))) ++nice;
        }
        Util.log("found %s nice strings using the first check", nice);

        nice = 0;
        for (int i=0; i<data.size(); ++i) {
            if (check2(data.get(i))) ++nice;
        }
        Util.log("found %s nice strings using the second check", nice);
    }

    private static boolean check1(String s) {
        char[] chars = s.toCharArray();

        if (! contains3vowels(chars)) {
            return false;
        }
        
        if (! charDoubled(chars)) {
            return false;
        }
        
        return doesNotContainBadDoubles(s);
    }

    private static boolean check2(String s) {
        char[] chars = s.toCharArray();

        if (! contains2pair(s)) {
            return false;
        }
        
        return containsAba(chars);
    }

    private static boolean containsAba(char[] chars) {
        char c0 = chars[0];
        char c1 = chars[1];
        int aba = 0;
        for (int i=2; i<chars.length; ++i) {
            if (chars[i]==c0) {
                ++aba;
            }
            c0 = c1;
            c1 = chars[i];
        }
        return aba >= 1;
    }

    private static boolean contains2pair(String s) {
        for (int i=0; i<s.length()-2; ++i) {
            if (s.substring(i+2, s.length()).contains(s.substring(i,i+2)))
                return true;
        }
        return false;
    }

    private static boolean doesNotContainBadDoubles(String s) {
        String[] badDoubles = {"ab", "cd", "pq", "xy"};
        for (String bad : badDoubles) {
            if (s.contains(bad)) {
                return false;
            }
        }
        return true;
    }

    private static boolean charDoubled(char[] chars) {
        char c0 = chars[0];
        for (int i=1; i<chars.length; ++i) {
            if (chars[i]==c0) {
                return true;
            }
            c0 = chars[i];
        }
        return false;
    }

    private static boolean contains3vowels(char[] s) {
        List<Character> vowels = vowelList();

        int vowelCount=0;
        for (Character c : s) {
            if (vowels.contains(c)) {
                ++vowelCount;
            }
        }
        
        return vowelCount >= 3;
    }

    private static List<Character> vowelList() {
        Character[] vowelArray = {'a', 'e', 'i', 'o', 'u'};
        List<Character> vowels = new ArrayList<>();
        for (Character c : vowelArray) {
            vowels.add(c);
        }
        return vowels;
    }
}
