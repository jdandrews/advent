package advent.y2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;

public class Day01 {
    public static void main(String[] args) {
        List<String> stringData = FileIO.getFileAsList("src/advent/y2024/Day01.txt");
        Integer[][] data = parse(stringData);
        
        List<Integer> list1 = new ArrayList<>(Arrays.asList(data[0]));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(data[1]));
        
        list1.sort(null);
        list2.sort(null);
        
        int result = 0;
        for (int i=0; i < list1.size(); ++i) {
            result += Math.abs(list1.get(i) - list2.get(i));
        }
        
        System.out.println("result 1 = " + result);
        
        result = 0;
        for (int i=0; i < list1.size(); ++i) {
            for (int j=0; j < list2.size(); ++j) {
                if (list1.get(i).equals(list2.get(j))) {
                    result += list1.get(i);
                }
            }
        }
        System.out.println("result 2 = " + result);
    }

    private static Integer[][] parse(List<String> data) {
        Integer[][] result = new Integer[2][data.size()];
        for (int i=0; i<data.size(); ++i) {
            String[] elements = data.get(i).split("   ");
            if (elements.length != 2) {
                print(elements);
                throw new IllegalArgumentException("for i=" + i +", found " + elements.length + " elements.");
            }
            result[0][i] = Integer.valueOf(elements[0]);
            result[1][i] = Integer.valueOf(elements[1]);
        }
        return result;
    }

    private static void print(String[] elements) {
        StringBuilder out = new StringBuilder();
        for (int i=0; i<elements.length; ++i) {
            out.append("[");
            out.append(elements[i]);
            out.append("], ");
        }
        if (out.length() > 2) {
            out.delete(out.length()-2, out.length());
        }
        System.out.println(out.toString());
    }
}
