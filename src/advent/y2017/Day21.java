package advent.y2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.Util;
import advent.y2017.day21.Picture;
import advent.y2017.day21.Rule;

public class Day21 {

    public static void main(String[] args) {
        List<Rule> rules = loadSampleRules();
        Picture picture = new Picture(".#./..#/###");
        for (int i=0; i<2; ++i) {
            picture = process(picture, rules);
        }
        Util.log("found %d pixels on", countPixels(picture));

        rules = loadRules();
        picture = new Picture(".#./..#/###");
        for (int i=0; i<5; ++i) {
            picture = process(picture, rules);
        }
        Util.log("found %d pixels on after 5 iterations", countPixels(picture));

        picture = new Picture(".#./..#/###");
        for (int i=0; i<18; ++i) {
            picture = process(picture, rules);
        }
        Util.log("found %d pixels on after 18 iterations", countPixels(picture));
    }

    private static Object countPixels(Picture picture) {
        int pixels = 0;
        for (char c : picture.text.toCharArray()) {
            if (c == '#') {
                ++pixels;
            }
        }
        return pixels;
    }

    private static Picture process(Picture picture, List<Rule> rules) {
        Picture[][] subPictures = picture.subdivide();
        // note that this is always square anyway, but what the heck.
        Picture[][] result = new Picture[subPictures.length][subPictures[0].length];
        for (int i=0; i<subPictures.length; ++i) {
            for (int j=0; j<subPictures[0].length; ++j) {
                Rule rule = findRule(subPictures[i][j], rules);
                result[i][j] = new Picture(rule.output);
            }
        }
        return new Picture(result);
    }

    private static Rule findRule(Picture picture, List<Rule> rules) {
        for (Rule rule : rules) {
            if (rule.matches(picture)) {
                return rule;
            }
        }
        throw new UnsupportedOperationException("no rule matches picture "+picture);
    }

    private static List<Rule> loadSampleRules() {
        return Arrays.asList(
                new Rule("../.# => ##./#../..."),
                new Rule(".#./..#/### => #..#/..../..../#..#"));
    }

    private static List<Rule> loadRules() {
        List<String> input = Util.readInput("2017", "Day21.txt");

        List<Rule> result = new ArrayList<>();
        for (String s : input) {
            if (s.trim().length() > 0) {
                result.add(new Rule(s));
            }
        }
        return result;
    }

}
