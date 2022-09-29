package advent.y2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import advent.Util;

public class Day04 {
    public static void main(String[] args) throws IOException {
        List<String> passphrases = Util.readInput("2017", "Day04.txt");

        int noAnagrams = 0;
        int validPassphrases = 0;
        int passPhrases = 0;
        // first part
        for (String passphrase : passphrases) {
            String[] words = passphrase.split(" ");
            Arrays.sort(words);
            boolean duplicatedWord = false;
            for (int i=0; i<words.length-1; ++i) {
                if (words[i].equals(words[i+1])) {
                    duplicatedWord = true;
                    break;
                }
            }
            if (! duplicatedWord)
                ++validPassphrases;
            ++passPhrases;
        }
        // second part
        for (String passphrase : passphrases) {
            String[] words = passphrase.split(" ");
            for (int i=0; i<words.length; ++i) {
                char[] chars = words[i].toCharArray();
                Arrays.sort(chars);
                words[i] = new String(chars);
            }

            Arrays.sort(words);
            boolean anagram = false;
            for (int i=0; i<words.length-1; ++i) {
                if (words[i].equals(words[i+1])) {
                    anagram = true;
                    break;
                }
            }
            if (! anagram)
                ++noAnagrams;
        }

        Util.log("there are %d passphrases; %s have no repeated words; %s have no anagrams",
                passPhrases, validPassphrases, noAnagrams);
    }
}
