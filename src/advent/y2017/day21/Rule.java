package advent.y2017.day21;

public class Rule {
    public String text;
    public String input;
    public String output;

    public Rule(String ruleText) {
        text = ruleText;
        String[] s = text.split(" => ");
        input = s[0];
        output = s[1];
    }

    public boolean matches(Picture picture) {
        char[][] pictureChars = extractChars(picture);

        if (checkRotations(pictureChars)) {
            return true;
        }

        return (checkRotations(flip(extractChars(picture))));
    }

    private char[][] flip(char[][] picture) {
        char[][] result = new char[picture.length][picture[0].length];
        for (int row=0; row<picture.length; ++row) {
            for (int col=0; col < picture[0].length; ++col) {
                result[picture.length-1-row][col] = picture[row][col];
            }
        }
        return result;
    }

    private boolean checkRotations(char[][] pictureChars) {
        if (input.equals(new Picture(pictureChars).text)) {
            return true;
        }

        char[][] rotation = pictureChars;
        for (int i=1; i<4; ++i) {
            rotation = rotate(rotation);
            if (input.equals(new Picture(rotation).text)){
                return true;
            }
        }

        return false;
    }

    private static class Coord {
        public int row;
        public int col;
        public Coord(int r, int c) {
            row = r;
            col = c;
        }
    }

    private static final Coord[][] TWO_BY_TWO_MAP = {
            {new Coord(0, 1), new Coord(1, 1)},
            {new Coord(0, 0), new Coord(1, 0)}
    };

    private static final Coord[][] THREE_BY_THREE_MAP = {
            {new Coord(0, 2), new Coord(1, 2), new Coord(2, 2)},
            {new Coord(0, 1), new Coord(1, 1), new Coord(2, 1)},
            {new Coord(0, 0), new Coord(1, 0), new Coord(2, 0)}
    };

    private char[][] rotate(char[][] source) {
        char[][] result = new char[source[0].length][source.length];
        Coord[][] mapping;
        if (source.length==2) {
            mapping = TWO_BY_TWO_MAP;
        }
        else if (source.length==3) {
            mapping = THREE_BY_THREE_MAP;
        }
        else {
            throw new UnsupportedOperationException(
                    "only 2x2 or 3x3 are mapped; can't map "+source.length+"x"+source.length);
        }

        for (int i=0; i<source.length; ++i) {
            for (int j=0; j<source.length; ++j) {
                result[mapping[i][j].row][mapping[i][j].col] = source[i][j];
            }
        }

        return result;
    }

    private char[][] extractChars(Picture picture) {
        String[] rows = picture.text.split("/");
        char[][] pictureChars = new char[rows.length][];
        for (int row = 0; row<rows.length; ++row) {
            pictureChars[row] = rows[row].toCharArray();
        }
        return pictureChars;
    }
}
