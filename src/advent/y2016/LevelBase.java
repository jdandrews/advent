package advent.y2016;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class LevelBase {
    protected static String DATA_FOLDER = "src/advent/y2016/";
    protected static String DATA_FILE = "";

    protected static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    protected static List<String> readInput() throws IOException {
        List<String> result = new ArrayList<>();

        File infile = new File(DATA_FOLDER+DATA_FILE);
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
