package advent.y2016;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day03 {
    private static final String DATA_FILE = "src/advent/y2016/Day03.txt";

    public static void main(String[] args) throws IOException {
        List<String> inputLines = readInput();
        int nHorizontal = 0;
        int nVertical = 0;
        int row = 0;
        int[][] verticalValues = new int[3][];
        for (String line : inputLines) {
            if (line.trim().length()==0) {
                continue;
            }
            String[] valueStrings = parseLine(line);
            int[] values = valueOf(valueStrings);

            // vertical - part 2 of the puzzle
            verticalValues[row] = Arrays.copyOf(values, 3);
            row = (row+1)%3;
            if (row==0) {
                int[] candidate = new int[3];
                for (int col=0; col<3; ++col) {
                    candidate[0] = verticalValues[0][col];
                    candidate[1] = verticalValues[1][col];
                    candidate[2] = verticalValues[2][col];
                    candidate = sort(candidate);
                    if (candidate[0]+candidate[1] > candidate[2]) {
                        ++nVertical;
                    }
                }
            }

            // horizontal - part 1 of the puzzle
            values = sort(values);
            if (values[0]+values[1] > values[2]) {
                ++nHorizontal;
            }


        }
        System.out.println("horizontal (part 1) triangles: "+nHorizontal);
        System.out.println("vertical (part 2) triangles: "+nVertical);
    }

    private static int[] sort(int[] values) {
        Arrays.sort(values);
        return values;
    }

    private static int[] valueOf(String[] valueStrings) {
        int[] result = new int[valueStrings.length];
        for (int i=0; i<valueStrings.length; ++i) {
            result[i]=Integer.parseInt(valueStrings[i].trim());
        }
        return result;
    }

    private static String[] parseLine(String line) {
        String[] rawResult = line.trim().split(" ");
        String[] result = new String[3];
        int row=0;
        for (int i=0; i<rawResult.length; ++i) {
            String s = rawResult[i].trim();
            if (s.length() > 0) {
                result[row++] = s;
            }
        }
        return result;
    }

    private static List<String> readInput() throws IOException {
        List<String> result = new ArrayList<>();

        File infile = new File(DATA_FILE);
        System.out.println("reading "+infile.getAbsolutePath());

        try(LineNumberReader in = new LineNumberReader(new FileReader(infile))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                result.add(inputLine);
            }
        }

        return result;
    }
}
