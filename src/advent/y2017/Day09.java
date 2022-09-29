package advent.y2017;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import advent.Util;

public class Day09 {
    static int garbage = 0;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[] tests = {"{}",                     // score of 1
                "{{{}}}",                           // score of 1 + 2 + 3 = 6.
                "{{},{}}",                          // score of 1 + 2 + 2 = 5.
                "{{{},{},{{}}}}",                   // score of 1 + 2 + 3 + 3 + 3 + 4 = 16.
                "{<a>,<a>,<a>,<a>}",                // score of 1.
                "{{<ab>},{<ab>},{<ab>},{<ab>}}",    // score of 1 + 2 + 2 + 2 + 2 = 9.
                "{{<!!>},{<!!>},{<!!>},{<!!>}}",    // score of 1 + 2 + 2 + 2 + 2 = 9.
                "{{<a!>},{<a!>},{<a!>},{<ab>}}",    // score of 1 + 2 = 3.
        };

        int score;
        for (String test : tests) {
            try (Reader in = new StringReader(test) ) {
                garbage = 0;
                score = process(in);
            }
            Util.log("%s score is %d; found %d garbage characters", test, score, garbage);
        }

        try (Reader in = new FileReader("src/advent/y2017/Day09.txt") ) {
            garbage = 0;
            score = process(in);
        }
        Util.log("Full data score is %d; found %d garbage characters", score, garbage);
    }

    private static int process(Reader in) throws IOException {
        return process(in, 0);
    }

    private static int process(Reader in, int level) throws IOException {
        int score = 0;
        for (int ch = in.read(); ch >= 0; ch = in.read()) {
            switch(ch) {
            case '{':
                score += process(in, level+1);
                break;

            case '}':
                return score+level;                 // <-- recursive exit point

            case '<':
                skipGarbage(in);
                break;

            case '!':
                in.read();                      // skip 1 character
                break;

            default:
                // just continue
            }
        }
        return score;
    }

    private static void skipGarbage(Reader in) throws IOException {
        for (int ch = in.read(); ch >= 0; ch = in.read()) {
            switch(ch) {
            case '>':
                return;

            case '!':
                in.read();                      // skip 1 character
                break;

            default:
                ++garbage;
            }
        }
    }

}
