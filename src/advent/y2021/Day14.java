package advent.y2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

/**
 * solution by https://www.reddit.com/user/Natrium_Benzoat/; annotated and a slightly restructured by me.
 */
public class Day14 {
    private static List<String> SAMPLE = Arrays.asList("NNCB", "",
            "CH -> B", "HH -> N", "CB -> H", "NH -> C", "HB -> C", "HC -> B", "HN -> C", "NN -> C",
            "BH -> H", "NC -> B", "NB -> B", "BN -> B", "BB -> N", "BC -> B", "CC -> N", "CN -> C");

    private static List<String> PUZZLE = Arrays.asList("OKSBBKHFBPVNOBKHBPCO", "",
            "CB -> P", "VH -> S", "CF -> P", "OV -> B", "CH -> N", "PB -> F", "KF -> O", "BC -> K", "FB -> F", "SN -> F",
            "FV -> B", "PN -> K", "SF -> V", "FN -> F", "SS -> K", "VP -> F", "VB -> B", "OS -> N", "HP -> O", "NF -> S",
            "SK -> H", "OO -> S", "PF -> C", "CC -> P", "BP -> F", "OB -> C", "CS -> N", "BV -> F", "VV -> B", "HO -> F",
            "KN -> P", "VC -> K", "KK -> N", "BO -> V", "NH -> O", "HC -> S", "SB -> F", "NN -> V", "OF -> V", "FK -> S",
            "OP -> S", "NS -> C", "HV -> O", "PC -> C", "FO -> H", "OH -> F", "BF -> S", "SO -> O", "HB -> P", "NK -> H",
            "NV -> C", "NB -> B", "FF -> B", "BH -> C", "SV -> B", "BK -> K", "NO -> C", "VN -> P", "FC -> B", "PH -> V",
            "HH -> C", "VO -> O", "SP -> P", "VK -> N", "CP -> H", "SC -> C", "KV -> H", "CO -> C", "OK -> V", "ON -> C",
            "KS -> S", "NP -> O", "CK -> C", "BS -> F", "VS -> B", "KH -> O", "KC -> C", "KB -> N", "OC -> F", "PP -> S",
            "HK -> H", "BN -> S", "KO -> K", "NC -> B", "PK -> K", "CV -> H", "PO -> O", "BB -> C", "HS -> F", "SH -> K",
            "CN -> S", "HN -> S", "KP -> O", "FP -> H", "HF -> F", "PS -> B", "FH -> K", "PV -> O", "FS -> N", "VF -> V");

    // map of character to replacement, e.g. "CB -> P" results in CB:[CP,PB].
    private static Map<String, String[]> rules = new HashMap<>();
    // first character of the seed, which is the first char of any resulting polymer string
    private static Character first;

    public static void main(String[] args) {
        Map<String, Long> templateMap = parse(SAMPLE);
        for (int i = 0; i < 10; i++) {
            templateMap = transform(templateMap);
        }
        print(countChars(templateMap));

        templateMap = parse(SAMPLE);
        for (int i = 0; i < 40; i++) {
            templateMap = transform(templateMap);
        }
        print(countChars(templateMap));

        Util.log("------------");

        templateMap = parse(PUZZLE);
        for (int i = 0; i < 10; i++) {
            templateMap = transform(templateMap);
        }
        print(countChars(templateMap));

        templateMap = parse(PUZZLE);
        for (int i = 0; i < 40; i++) {
            templateMap = transform(templateMap);
        }
        print(countChars(templateMap));
    }

    private static void print(Map<Character, Long> templateMap) {
        char maxChar = 0;
        char minChar = 0;
        Long max = Long.MIN_VALUE;
        Long min = Long.MAX_VALUE;

        for (Character c : templateMap.keySet()) {
            if (templateMap.get(c) > max) {
                max = templateMap.get(c);
                maxChar = c;
            } else if (templateMap.get(c) < min) {
                min = templateMap.get(c);
                minChar = c;
            }
        }

        Util.log("Most common: %c - %d times; least common : %c - %d times; ∆ is %d", maxChar, max, minChar, min, max - min);
    }

    private static Map<String, Long> parse(List<String> in) {
        String polymer = in.get(0);
        first = polymer.charAt(0);
        for (int i = 2; i < in.size(); ++i) {
            createInsertions(in.get(i).split(" -> "));
        }
        return createStartingMap(polymer);
    }

    private static void createInsertions(String[] line) {
        String pattern = line[0];
        String plus = line[1];
        String[] replace = {pattern.charAt(0) + plus, plus  + pattern.charAt(1)};
        rules.put(pattern, replace);
    }

    private static Map<String, Long> createStartingMap(String line) {
        Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < line.length() - 1; i++) {
            String s = line.substring(i, i + 2);
            Long n = map.getOrDefault(s, 0L);
            map.put(s, n + 1);
        }
        return map;
    }

    private static Map<String, Long> transform(Map<String, Long> record) {
        Map<String, Long> newRecord = new HashMap<>();

        for (String key : record.keySet()) {
            Long countKey = record.get(key);

            if (rules.containsKey(key)) {
                String insert1 = rules.get(key)[0];
                String insert2 = rules.get(key)[1];
                Long count1 = newRecord.getOrDefault(insert1, 0L);
                Long count2 = newRecord.getOrDefault(insert2, 0L);
                newRecord.put(insert1, count1 + countKey);
                newRecord.put(insert2, count2 + countKey);
            } else {
                newRecord.put(key, countKey);
            }
        }
        return newRecord;
    }

    private static Map<Character, Long> countChars(Map<String, Long> templateMap) {
        Map<Character, Long> charMap = new HashMap<>();
        Long firstN = charMap.getOrDefault(first, 0L);
        charMap.put(first, firstN + 1);
        // This is non-obvious: the count of any given character is the number of times that character
        // appears as the 2nd character of a 2-character pair. E.g. if BB appears 3x and NB appears 2x, B
        // appears 5 times total. The reason for this is the first character of a given pair is the 2nd
        // character of an equal number of different pairs - 1. E.g. in this actual example,
        // templateMap = {BB=812, ... BC=120, BH=81, ... BN=735, NB=796, ... HB=26, ..., CB=115}
        // note that 812 + 120 + 81 + 735 = 812 + 796 + 26 + 115 + 1,
        for (String key : templateMap.keySet()) {
            char c = key.charAt(1);
            Long n = templateMap.get(key);

            Long charN = charMap.getOrDefault(c, 0L);
            charMap.put(c, charN + n);
        }
        return charMap;
    }

}