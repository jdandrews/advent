package advent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    private Util() {
        // don't instantiate this
    }

    public record Point(int x, int y) {}

    public static String DATA_FOLDER = "src/advent/y";
    static MessageDigest digester = null;

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void log(char[][] array) {
        for (int row = 0; row < array.length; ++row) {
            for (int col = 0; col < array[row].length; ++col) {
                System.out.print(array[row][col] + " ");
            }
            System.out.println("");
        }
    }

    public static List<String> readInput(String year, String dataFile) throws IOException {
        List<String> result = new ArrayList<>();

        File infile = new File(DATA_FOLDER+year+"/"+dataFile);
        System.out.println("reading "+infile.getAbsolutePath());

        try(LineNumberReader in = new LineNumberReader(new FileReader(infile))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                result.add(inputLine);
            }
        }

        return result;
    }

    private static final String    HEXES    = "0123456789ABCDEF";

    public static String getHex(byte[] raw) {
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static String md5(String string) {
        if (digester == null) {
            try {
                digester = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);          // not going to happen.
            }
        }

        byte[] md5 = digester.digest(string.getBytes());
        return Util.getHex(md5).toLowerCase();
    }

    /**
     * Break up a string delimited with newlines.
     * @param s the string in question
     * @return a List<String>, one entry for each line in the input.
     */
    public static List<String> getLines(String s) {
        String[] strings = s.trim().split("\n");
        return Arrays.asList(strings);
    }

    public static void log(int[][] grid) {
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                System.out.print(grid[row][col] == 0 ? "." : grid[row][col]);
            }
            System.out.println();
        }
    }

    private static final String BRIGHT_GREEN_FG =   "\033[92m";
    private static final String RESET_FG =  "\33[39m";

    public static void logT(int[][] grid) {
        for (int col = 0; col < grid[0].length; ++col) {
            for (int row = 0; row < grid.length; ++row) {
                System.out.print(grid[row][col] == 0 ? ". " : BRIGHT_GREEN_FG + grid[row][col] + " " + RESET_FG);
            }
            System.out.println();
        }
    }

    public static void log(long[] vector) {
        System.out.println(toString(vector));
    }

    public static String toString(long[] vector) {
        StringBuilder result = new StringBuilder();
        for (int col = 0; col < vector.length; ++col) {
            result.append(String.format("%3s", vector[col] == 0 ? "." : Long.toString(vector[col])));
        }
        return result.toString();
    }

    public static String elapsed(Instant start) {
        Duration result = Duration.between(start, Instant.now());
        return result.getSeconds() + "." + result.getNano()/1000000L + " s";
    }

    public static int[] getIntegers(String s, String separator) {
        String[] parts = s.split(separator);
        int[] result = new int[parts.length];
        for (int i = 0; i<parts.length; ++i) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static List<Point> getIntegerPoints(List<String> data, String separator){
        return data.stream().map(p -> getIntegerPoint(p, separator)).collect(Collectors.toList());
    }

    public static Point getIntegerPoint(String s, String separator) {
        String[] parts = s.split(separator);
        return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }


}
