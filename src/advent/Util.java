package advent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private Util() {
        // don't instantiate this
    }

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
}
