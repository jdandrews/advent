package advent.y2016;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day06 extends LevelBase {
    public static void main(String[] args) throws IOException {
        DATA_FILE = "Day06.txt";
        test();

        List<String> data = readInput();
        log("part 1/part 2: "+decode(data));
    }

    private static void test() {
        log("part 1/part 2: "+decode(Arrays.asList(TestData)));
        log("---------- end test ---------");
    }

    private static String decode(List<String> data) {
        List<Map<Character, Integer>> characterCount = new ArrayList<>();
        for (String row : data) {
            int nColumn = 0;
            for (Character c : row.toCharArray()) {
                while (nColumn >= characterCount.size()) {
                    characterCount.add(new HashMap<Character, Integer>());
                }
                if ( ! characterCount.get(nColumn).containsKey(c)) {
                    characterCount.get(nColumn).put(c,0);
                }
                characterCount.get(nColumn).put(c, characterCount.get(nColumn).get(c).intValue()+1);
                ++nColumn;
            }
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<characterCount.size(); ++i) {
            result.append(mostCommonCharacter(characterCount.get(i)));
        }
        result.append("/");
        for (int i=0; i<characterCount.size(); ++i) {
            result.append(leastCommonCharacter(characterCount.get(i)));
        }
        return result.toString();
    }

    private static char mostCommonCharacter(Map<Character, Integer> map) {
        Map<Integer, Character> resultMap = new HashMap<>();
        int max = 0;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            int count = entry.getValue().intValue();
            if (count > max) {
                max = count;
            }
            resultMap.put(entry.getValue(), entry.getKey());
        }
        return resultMap.get(max);
    }

    private static char leastCommonCharacter(Map<Character, Integer> map) {
        Map<Integer, Character> resultMap = new HashMap<>();
        int min = Integer.MAX_VALUE;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            int count = entry.getValue().intValue();
            if (count < min) {
                min = count;
            }
            resultMap.put(entry.getValue(), entry.getKey());
        }
        return resultMap.get(min);
    }

    private static String[] TestData = {
            "eedadn",
            "drvtee",
            "eandsr",
            "raavrd",
            "atevrs",
            "tsrnev",
            "sdttsa",
            "rasrtv",
            "nssdts",
            "ntnada",
            "svetve",
            "tesnvt",
            "vntsnd",
            "vrdear",
            "dvrsen",
            "enarar"
    };
}
