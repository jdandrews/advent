package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day19 {
    private static final String MOLECULE = "CRnCaCaCaSiRnBPTiMgArSiRnSiRnMgArSiRnCaFArTiTiBSiThFYCaFArCaCaSiThCaPBSiThSiThCaCaPTiRnPBSiThRnFArArCaCaSiThCaSiThSiRnMgArCaPTiBPRnFArSiThCaSiRnFArBCaSiRnCaPRnFArPMgYCaFArCaPTiTiTiBPBSiThCaPTiBPBSiRnFArBPBSiRnCaFArBPRnSiRnFArRnSiRnBFArCaFArCaCaCaSiThSiThCaCaPBPTiTiRnFArCaPTiBSiAlArPBCaCaCaCaCaSiRnMgArCaSiThFArThCaSiThCaSiRnCaFYCaSiRnFYFArFArCaSiRnFYFArCaSiRnBPMgArSiThPRnFArCaSiRnFArTiRnSiRnFYFArCaSiRnBFArCaSiRnTiMgArSiThCaSiThCaFArPRnFArSiRnFArTiTiTiTiBCaCaSiRnCaCaFYFArSiThCaPTiBPTiBCaSiThSiRnMgArCaF";

    private static class Op {
        public String key;
        public String replacement;
        public Op(String[] keys) {
            key = keys[0];
            replacement = keys[1];
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> dataStrings = Util.readInput("2015", "Day19.txt");
        List<Op> ops = parse(dataStrings);

        Set<String> molecules = new HashSet<>();
        for (Op op : ops) {
            for (int start = MOLECULE.indexOf(op.key); start >= 0; start = MOLECULE.indexOf(op.key, start+1)) {
                String newMolecule = MOLECULE.substring(0,start) 
                        + op.replacement + MOLECULE.substring(start+op.key.length());
                molecules.add(newMolecule);
            }
        }
        
        Util.log("found %d distinct molecules", molecules.size());
    }

    private static List<Op> parse(List<String> dataStrings) {
        List<Op> result = new ArrayList<>();
        for (String s : dataStrings) {
            result.add(new Op(s.split(" => ")));
        }
        return result;
    }

}
