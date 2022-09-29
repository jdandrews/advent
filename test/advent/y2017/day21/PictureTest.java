package advent.y2017.day21;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PictureTest {

    @Test
    public void testSubdivide() {
        Picture source = new Picture("#./.#");
        Picture[][] result = source.subdivide();
        assertNotNull(result);
        assertEquals(result.length, 1);

        source = new Picture("#..#/#.../..#./#..#");
        result = source.subdivide();
        assertNotNull(result);
        assertEquals(result.length, 2);
        assertEquals(result[0].length, 2);
        /*
         * #.|.#
         * #.|..
         * --+--
         * ..|#.
         * #.|.#
         */
        assertEquals(result[0][0].text, "#./#.");
        assertEquals(result[0][1].text, ".#/..");
        assertEquals(result[1][0].text, "../#.");
        assertEquals(result[1][1].text, "#./.#");
    }

    @Test
    public void testConstructFromArray() {
        Picture[][] fragments = new Picture[2][2];
        fragments[0][0] = new Picture("#.#/#../###");
        fragments[0][1] = new Picture(".##/.../#.#");
        fragments[1][0] = new Picture(".#./#.#/.#.");
        fragments[1][1] = new Picture("#../.#./...");

        Picture result = new Picture(fragments);
        assertEquals(result.text, "#.#.##/#...../####.#/.#.#../#.#.#./.#....");
    }
}
