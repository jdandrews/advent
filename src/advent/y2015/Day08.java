package advent.y2015;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Day08 {

    public static void main(String[] args) throws IOException {
        String test = "\"\" "
                + "\"abc\" "
                + "\"aaa\\\"aaa\" "
                + "\"\\x27\" ";/*
                + "\"b\\\\w\\\\\" "
                + "\"\\\\\\\\zkisyjpbzandqikqjqvee\" "
                + "\"lhyjky\\\\m\\\"pvnm\\\\xmynpxnlhndmahjl\"";
*/        
        try (Scanner testScanner = new Scanner(test)) {
            scan(testScanner);
        }
        try (Scanner testScanner = new Scanner(test)) {
            encode(testScanner);
        }

        try (Scanner scanner = new Scanner(new File("src/advent/y2015/Day08.txt"))) {
            scan(scanner);
        }
        try (Scanner scanner = new Scanner(new File("src/advent/y2015/Day08.txt"))) {
            encode(scanner);
        }
    }

    private static void scan(Scanner scanner) {
        int inMemory=0;
        int inFile = 0;
        int nTokens = 0;

        while (scanner.hasNext()) {
            String entry = scanner.next();
            ++nTokens;
            int nFile = entry.length();

            entry = entry.substring(1, entry.length()-1);
            StringBuilder out = new StringBuilder();
            for (int i=0; i<entry.length(); ++i) {
                char ch = entry.charAt(i);
                out.append(ch);
                if (ch=='\\') {
                    char next = entry.charAt(i+1);
                    if (next=='\\' || next=='\"') {
                        i+=1;
                    }
                    else if (next=='x') {
                        i+=3;
                    }
                }
            }

            inMemory += out.length();
            inFile += nFile;

            // System.out.println(String.format("%3d: %d/%d \"%s\"", nTokens, out.length(), nFile, entry));
        }
        
        System.out.println(String.format("1: %d tokens; in memory = %d, in file = %d; delta = %d", 
                nTokens, inMemory, inFile, inFile-inMemory));
    }
    

    private static void encode(Scanner scanner) {
        int inFile=0;
        int inMemory = 0;
        int nTokens = 0;

        while (scanner.hasNext()) {
            String entry = scanner.next();
            ++nTokens;
            int nMem = entry.length();

            StringBuilder out = new StringBuilder();
            for (int i=0; i<entry.length(); ++i) {
                char ch = entry.charAt(i);
                out.append(ch);
                if (ch=='\"' || ch=='\\') {
                    out.append(".");
                }
            }

            inFile += out.length()+2;
            inMemory += nMem;

            // System.out.println(String.format("%3d: %d/%d \"%s\"", nTokens, out.length(), nFile, entry));
        }
        
        System.out.println(String.format("2: %d tokens; in file = %d, in memory = %d; delta = %d", 
                nTokens, inFile, inMemory, -inMemory+inFile));
    }

}
