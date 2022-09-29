package advent.y2015;

import java.util.ArrayList;
import java.util.List;

import advent.FileIO;

/**
 * @author /u/Philboyd_Studge on 12/18/2015.
 */
public class Day19Studge {

    public static String replace(String s, String in, String out, int position) {
        return s.substring(0, position) + out + s.substring(position + in.length());
    }
    public static void main(String[] args) {
        List<String[]> input = FileIO.getFileLinesSplit("src/advent/y2015/Day19.txt", " => ");
        List<String> output = new ArrayList<>();
        String molecule = "CRnCaCaCaSiRnBPTiMgArSiRnSiRnMgArSiRnCaFArTiTiBSiThFYCaFArCaCaSiThCaPBSiThSiThCaCaPTiRn"
                + "PBSiThRnFArArCaCaSiThCaSiThSiRnMgArCaPTiBPRnFArSiThCaSiRnFArBCaSiRnCaPRnFArPMgYCaFArCaPTiTiTiBPB"
                + "SiThCaPTiBPBSiRnFArBPBSiRnCaFArBPRnSiRnFArRnSiRnBFArCaFArCaCaCaSiThSiThCaCaPBPTiTiRnFArCaPTiBSi"
                + "AlArPBCaCaCaCaCaSiRnMgArCaSiThFArThCaSiThCaSiRnCaFYCaSiRnFYFArFArCaSiRnFYFArCaSiRnBPMgArSiThPRn"
                + "FArCaSiRnFArTiRnSiRnFYFArCaSiRnBFArCaSiRnTiMgArSiThCaSiThCaFArPRnFArSiRnFArTiTiTiTiBCaCaSiRnCaCa"
                + "FYFArSiThCaPTiBPTiBCaSiThSiRnMgArCaF";

        for (String[] each : input) {
            int position = 0;
            while ((position = molecule.indexOf(each[0], position)) >= 0) {
                output.add(replace(molecule, each[0], each[1], position));
                position += each[0].length();
            }
        }

        long count = output.stream()
                .distinct()
                .count();

        System.out.println(count);

        int count2 = 0;
        while(!molecule.equals("e")) {
            for (String[] each : input) {
                if (molecule.contains(each[1])) {
                    molecule = replace(molecule, each[1], each[0], molecule.lastIndexOf(each[1]));
                    count2++;
                }
            }
        }
        System.out.println(count2);
    }
}