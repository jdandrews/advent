package advent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.PatternSyntaxException;

/**
 * @author /u/Philboyd_Studge on 12/5/2015.
 */
public class FileIO {

    /**
     * Load file into one String
     * Assuming file has no line separators!
     * i.e. Day 1, Day 3
     * @param filename file in current working directory or full pathname
     * @return String
     */
    public static String getFileAsString(String filename) {
        String test = "";
        try  {
            test = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return test;

    }

    /**
     * Performs given Function on file, one line at a time, and summing the results
     * @param filename file in current working directory or full pathname
     * @param func Function that takes a String as parameter and returns an int
     * @return int summed result
     */
    public static int performIntActionOnLine(String filename, Function<String, Integer> func) {
        int result = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String input = br.readLine();
            while (input != null) {
                result += func.apply(input);
                input = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;

    }

    /**
     * Loads entire file, one line at a time, into List
     * @param filename file in current working directory or full pathname
     * @return ArrayList of strings
     */
    public static List<String> getFileAsList(String filename) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String input;
            while ((input = br.readLine()) != null) {
                list.add(input);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return list;
    }

    /**
     * Return an ArrayList of String Arrays, split using the given delimiter
     * @param filename file in current working directory or full pathname
     * @param delimiter REGEX string delimite. Catches PatternSyntaxException.
     * @return List of String Arrays
     */
    public static List<String[]> getFileLinesSplit(String filename, String delimiter) {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String input;
            while ((input = br.readLine()) != null) {
                try {
                    String[] s = input.split(delimiter);
                    list.add(s);
                } catch (PatternSyntaxException pse) {
                    System.out.println("Bad regex syntax. Delimiter \"" + delimiter + " \"");
                    return null;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return list;
    }
}