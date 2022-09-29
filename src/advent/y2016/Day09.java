package advent.y2016;

import java.io.IOException;

import advent.Util;

public class Day09 {
    public static void main(String[] args) throws IOException {
        Day09 it = new Day09();

        test(it, false);
        solve(it, false);

        test(it, true);
        solve(it, true);
    }

	private static void solve(Day09 it, boolean compressedMarkers) throws IOException {
		String fileData = Util.readInput("2016", "Day09.txt").get(0);
    	String result = it.decompress(fileData, 0, compressedMarkers);
    	Util.log("file data expands to %d characters, problem %d", it.size, compressedMarkers ? 2 : 1);
    	Util.log("%s", result);
	}

	private static void test(Day09 it, boolean compressedMarkers) {
        Util.log("---------- begin test, problem " + (compressedMarkers ? "2" : "1") + " ---------");
		String[] data = {"ADVENT", "A(1x5)BC", "(3x3)XYZ", "A(2x2)BCD(2x2)EFG", "(6x1)(1x3)A", "X(8x2)(3x3)ABCY",
				"(27x12)(20x12)(13x14)(7x10)(1x12)A", "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"
		};

		for (int i=0; i<data.length; ++i) {
        	String result = it.decompress(data[i], 0, compressedMarkers);
        	Util.log("%s expands to %d characters", data[i], it.size);
        	Util.log("%s", result);
        	it.size = 0L;
        }
        Util.log("---------- end test, problem " + (compressedMarkers ? "2" : "1") + " ---------");
	}

    long size = 0;

    private String decompress(String input, int depth, boolean compressedMarkers) {
        StringBuilder decompressed = new StringBuilder();
        for (int position = 0; position < input.length(); ++position) {
            if (input.charAt(position)=='(') {
                String command = getCommand(input, position);
                int length = getLength(command);
                int repeat = getRepeat(command);
                int offset = position+command.length();
                String segment = input.substring(offset, offset+length);

                if (compressedMarkers && segment.contains("(")) {
                    Util.log("redecompressing %d %s", depth, segment);
                    segment = decompress(segment, depth+1, compressedMarkers);
                }

                for (int i=0; i<repeat; ++i) {
                    if (depth>0) {
                        decompressed.append(segment);
                    } else {
                        size += segment.length();
                    }
                }
                position += command.length()+length-1;
            }
            else {
                if (depth>0) {
                    decompressed.append(input.charAt(position));
                } else {
                    ++size;
                }
            }
        }
        return decompressed.toString();
    }

    private int getRepeat(String command) {
        return Integer.parseInt(command.substring(command.indexOf('x')+1, command.length()-1));
    }

    private int getLength(String command) {
        return Integer.parseInt(command.substring(1, command.indexOf('x')));
    }

    private String getCommand(String s, int position) {
        int n0 = s.indexOf(')',position);
        return s.substring(position, n0+1);
    }
}
