package advent.y2016;

public class Day16 {
    public static final String SAMPLE_START = "10000";
    public static final int SAMPLE_SIZE = 20;

    public static final String PART_1_START = "11110010111001001";
    public static final int PART_1_SIZE = 272;

    public static void main(String[] args) {
        String data = generateData(SAMPLE_START, SAMPLE_SIZE);
        String checksum = generateChecksum(data);
        System.out.println("Sample checksum: " + checksum);

        data = generateData(PART_1_START, PART_1_SIZE);
        checksum = generateChecksum(data);
        System.out.println("Part 1 checksum: " + checksum);

        data = generateData(PART_1_START, 35651584);
        checksum = generateChecksum(data);
        System.out.println("part 2 checksum: " + checksum);

    }

    private static String generateData(String sampleStart, int sampleSize) {
        StringBuilder result = new StringBuilder(sampleStart);
        while (result.length() < sampleSize) {
            String end = invert(reverse(result.toString()));
            result.append("0").append(end);
        }

        return result.substring(0, sampleSize);
    }

    private static String reverse(String string) {
        StringBuilder reverse = new StringBuilder();
        for (int i = string.length() - 1; i >= 0; --i) {
            reverse.append(string.charAt(i));
        }
        return reverse.toString();
    }
    private static String invert(String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<string.length(); ++i) {
            if (string.charAt(i) == '0')
                result.append('1');
            else if (string.charAt(i) == '1')
                result.append('0');
            else
                throw new UnsupportedOperationException("can't invert string[" + i + "] = " + string.charAt(i));
        }
        return result.toString();
    }

    private static String generateChecksum(String data) {
        do {
            StringBuilder checksum = new StringBuilder();
            for (int i = 0; i < data.length(); i += 2) {
                if (data.charAt(i) == data.charAt(i+1))
                    checksum.append('1');
                else
                    checksum.append('0');
            }
            data = checksum.toString();
        } while (data.length() % 2 == 0);
        return data;
    }

}
