package advent.y2018;

import java.util.ArrayList;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day03 {

    private static class Claim {
        public Claim(int i, int l, int t, int width, int height) {
            id = i;
            left = l;
            top = t;
            right = l+width-1;
            bottom = t+height-1;
        }
        public int id;
        public int left;
        public int top;
        public int right;
        public int bottom;
    }

    public static void main(String[] args) {
        List<String> stringData = FileIO.getFileAsList("src/advent/y2018/Day03.txt");
        List<Claim> data = parse(stringData);
        List<Claim> sample = parse(SAMPLE_DATA);
        
        Util.log("found %d overlapping cells in sample", overlap(sample));
        Util.log("found %d overlapping cells in data", overlap(data));
    }

    private static int overlap(List<Claim> data) {
        int maxRow = 0;
        int maxCol = 0;
        for (Claim claim : data) {
            maxRow = Math.max(maxRow, claim.bottom);
            maxCol = Math.max(maxCol, claim.right);
        }

        int[][] world = new int[maxCol+1][];
        for (int x=0; x<=maxCol; ++x) {
            world[x] = new int[maxRow+1];
            for (int y=0; y<=maxRow; ++y) {
                world[x][y] = 0;
            }
        }

        for (Claim claim : data) {
            for (int x=claim.left; x<=claim.right; ++x) {
                for (int y=claim.top; y<=claim.bottom; ++y) {
                    ++world[x][y];
                }
            }
        }

        for (Claim claim : data) {
            boolean overlaps = false;
            for (int x=claim.left; x<=claim.right && !overlaps; ++x) {
                for (int y=claim.top; y<=claim.bottom && !overlaps; ++y) {
                    if (world[x][y] > 1) overlaps = true;
                }
            }
            if (! overlaps) {
                Util.log("Claim #%d does not overlap any other claim", claim.id);
            }
        }

        int n=0;
        for (int x=0; x<=maxCol; ++x) {
            for (int y=0; y<=maxRow; ++y) {
                if (world[x][y]>1)
                    ++n;
            }
        }

        return n;
    }

    private static List<Claim> parse(List<String> stringData) {
        List<Claim> result = new ArrayList<>();
        for (String entry : stringData) {
            String[] chunks = entry.split(" ");
            String[] topLeft = chunks[2].trim().split(",");
            String[] size = chunks[3].trim().split("x");

            Claim claim = new Claim(Integer.valueOf(chunks[0].substring(1)), 
                    Integer.valueOf(topLeft[0]), Integer.valueOf(topLeft[1].substring(0, topLeft[1].length()-1)),
                    Integer.valueOf(size[0]), Integer.valueOf(size[1]));
            result.add(claim);
        }

        return result;
    }

    @SuppressWarnings("serial")
    private static final List<String> SAMPLE_DATA = new ArrayList<String>(){{
        add("#1 @ 1,3: 4x4");
        add("#2 @ 3,1: 4x4");
        add("#3 @ 5,5: 2x2");
    }};
}
