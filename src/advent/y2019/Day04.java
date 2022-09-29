package advent.y2019;

public class Day04 {
    private static final String lo = "145852";
    private static final int hi = 616942;

    private static final int[] TEST1= {1,1,1,1,1,1};
    private static final int[] TEST2= {0,1,1,1,1,1};
    private static final int[] TEST3= {1,1,1,1,1,2};

    public static void main(String[] args) {
        System.out.println(isNewCandidate(TEST1));
        System.out.println(isNewCandidate(TEST2));
        System.out.println(isNewCandidate(TEST3));


        int count = 0;
        int[] currentDigits = digitize(lo);
        while (lessThan(hi, currentDigits)) {
            currentDigits = calculateNextValue(currentDigits);
            if (isCandidate(currentDigits)) {
                ++count;
            }
        }
        System.out.println("Part 1: found "+count);

        count = 0;
        currentDigits = digitize(lo);
        while (lessThan(hi, currentDigits)) {
            currentDigits = calculateNextValue(currentDigits);
            if (isNewCandidate(currentDigits)) {
                ++count;
            }
        }
        System.out.println("Part 2: found "+count);
    }

    private static int[] calculateNextValue(int[] start) {
        int value = getValue(start);
        ++value;

        return digitize(value);
    }

    private static boolean isCandidate(int[] digits) {
        boolean pairs = false;
        for (int i=0; i<digits.length - 1; ++i) {
            if (digits[i] > digits[i+1])
                return false;

            pairs = pairs || (digits[i]==digits[i+1]);
        }
        return pairs;
    }

    private static boolean isNewCandidate(int[] digits) {
        for (int i=0; i<digits.length - 1; ++i) {
            if (digits[i] > digits[i+1])
                return false;
        }

        boolean pairs = false;
        for (int i=0; i<digits.length - 1; ++i) {
            boolean foundPair = (digits[i]==digits[i+1]);
            if (foundPair) {
                for (int j = i+2; j < digits.length; ++j) {
                    if (digits[i]==digits[j]) {
                        foundPair = false;
                        ++i;
                    }
                    else {
                        break;
                    }
                }
            }
            pairs = pairs || foundPair;
        }
        return pairs;
    }

    private static boolean lessThan(int limit, int[] digits) {
        int value = getValue(digits);
        return value < limit;
    }

    private static int getValue(int[] digits) {
        int value = 0;
        for (int digit : digits) {
            value = value*10 + digit;
        }
        return value;
    }

    private static int[] digitize(int value) {
        return digitize(Integer.toString(value));
    }

    private static int[] digitize(String n) {
        int[] digits = new int[n.length()];
        for (int i=0; i<digits.length; ++i) {
            digits[i] = Integer.valueOf(n.substring(i,i+1));
        }
        return digits;
    }

}
