package advent.y2021;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day10 {

    public static void main(String[] args) throws IOException {
        List<Status> lineStatus = computeStatus(SAMPLE);
        Util.log("SAMPLE found %d lines scoring an error scoe of %d and a completion score of %d",
                lineStatus.size(), errorScore(lineStatus), completionScore(lineStatus));

        Util.log("------------");
        lineStatus = computeStatus(Util.readInput("2021", "Day10.txt"));
        Util.log("puzzle found %d lines scoring an error scoe of %d and a completion score of %d",
                lineStatus.size(), errorScore(lineStatus), completionScore(lineStatus));
    }

    private static long completionScore(List<Status> statuses) {
        List<Long> scores = new ArrayList<>();
        for (Status s : statuses) {
            if (s.error() == '~') {
                long score = 0;
                for (char c : s.completion()) {
                    score *= 5;
                    score += switch(c){
                    case '(' -> 1;
                    case '[' -> 2;
                    case '{' -> 3;
                    case '<' -> 4;
                    default -> throw new UnsupportedOperationException("Unsupported closing char: " + s.error());
                    };
                }
                scores.add(score);
            }
        }
        scores.sort(null);
        System.out.println(scores);
        return scores.get(scores.size()/2);
    }

    private static Object errorScore(List<Status> statuses) {
        int score = 0;
        for (Status s : statuses) {
            if (s.error() != '~') {
                score += switch(s.error()) {
                case ')' -> 3;
                case ']' -> 57;
                case '}' -> 1197;
                case '>' -> 25137;
                default -> throw new UnsupportedOperationException("Unsupported closing char: " + s.error());
                };
            }
        }
        return score;
    }

    private static Map<Character, Character> delimiterPairs = new HashMap<>();
    static {
        delimiterPairs.put('<', '>');
        delimiterPairs.put('(', ')');
        delimiterPairs.put('{', '}');
        delimiterPairs.put('[', ']');
    }

    private static record Status(char error, List<Character> completion ) { }

    private static List<Status> computeStatus(List<String> in) {
        List<Status> result = new ArrayList<>();
        for (String line : in) {
            Deque<Character> stack = new ArrayDeque<>();
            boolean foundError = false;
            for (char c : line.toCharArray()) {
                if (delimiterPairs.keySet().contains(c)) {
                    stack.push(c);
                } else {
                    char expected = delimiterPairs.get(stack.pop());
                    if (expected != c) {
                        result.add(new Status(c, new ArrayList<>()));
                        foundError = true;
                        break;
                    }
                }
            }
            if (!foundError) {
                result.add(new Status('~', new ArrayList<>(stack)));
            }
        }
        return result;
    }

    private static final List<String> SAMPLE = Arrays.asList(
            "[({(<(())[]>[[{[]{<()<>>",
            "[(()[<>])]({[<{<<[]>>(",
            "{([(<{}[<>[]}>{[]{[(<()>",
            "(((({<>}<{<{<>}{[]{[]{}",
            "[[<[([]))<([[{}[[()]]]",
            "[{[{({}]{}}([{[{{{}}([]",
            "{<[[]]>}<{[{[{[]{()[[[]",
            "[<(<(<(<{}))><([]([]()",
            "<{([([[(<>()){}]>(<<{{",
            "<{([{{}}[<[[[<>{}]]]>[]]");
}
