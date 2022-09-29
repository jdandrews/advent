package advent.y2017.day21;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

import advent.y2017.day21.Picture;
import advent.y2017.day21.Rule;

public class RuleTest {

    @Test
    public void testMatches() {
        /*
         * rot
         *  0   flip  flip/rot 180
         * .#.   .#.   #..     ###
         * ..#   #..   #.#     ..#
         * ###   ###   ##.     .#.
         */
        String[] rotationCases = {".#./..#/###", "#../#.#/##.", "###/#../.#.", ".##/#.#/..#"};
        String[] flipCases =     {".#./#../###", "##./#.#/#..", "###/..#/.#.", "..#/#.#/.##"};

        /**
         * .#.     #..#
         * ..#  => ....
         * ###     ....
         *         #..#
         */
        Rule rule = new Rule(".#./..#/### => #..#/..../..../#..#");
        assertTrue(rule.matches(new Picture("###/..#/.#.")));

        for (int i=0; i<rotationCases.length; ++i) {
            assertTrue(rule.matches(new Picture(rotationCases[i])), rotationCases[i]);
        }

        for (int i=0; i<flipCases.length; ++i) {
            assertTrue(rule.matches(new Picture(flipCases[i])), flipCases[i]);
        }
    }
}
