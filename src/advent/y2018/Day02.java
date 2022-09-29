package advent.y2018;

import java.util.ArrayList;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day02 {

    public static void main(String[] args) {
        List<String> stringData = FileIO.getFileAsList("src/advent/y2018/Day02.txt");
        List<String> exactly2 = new ArrayList<>();
        List<String> exactly3 = new ArrayList<>();

        for (String s : stringData) {
            boolean done2 = false;
            boolean done3 = false;
            for (String s1 = s; s1.length()>0 && (!done2 || !done3); ) {
                String os1 = s1;
                s1 = s1.replaceAll(s1.substring(0,1), "");
                if (!done2 && os1.length() - s1.length() == 2) {
                    exactly2.add(s);
                    done2 = true;
                }
                else if (!done3 && os1.length() - s1.length() ==  3) {
                    exactly3.add(s);
                    done3 = true;
                }
            }
        }
        
        Util.log("checksum is %d", exactly2.size()*exactly3.size());

        for (int i=0; i<stringData.size()-1; ++i) {
            for (int j=i+1; j<stringData.size(); ++j) {
                String undiff = undiff(stringData.get(i), stringData.get(j));
                if (undiff.length() == stringData.get(i).length()-1) {
                    Util.log("found %s", undiff);
                }
            }
        }
    }

    private static String undiff(String s1, String s2) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<s1.length(); ++i) {
            if (s1.charAt(i)==s2.charAt(i)) {
                result.append(s1.charAt(i));
            }
        }

        return result.toString();
    }

}
