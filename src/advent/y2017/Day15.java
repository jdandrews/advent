package advent.y2017;

import advent.Util;

public class Day15 {
    private static class Generator {
        private static final long DIVISOR = 2147483647;
        private long factor;
        private long value;
        private int resultDivisor;

        public Generator(int startingValue, int multiplyingFactor, int resultDivisor) {
            factor = multiplyingFactor;
            value = startingValue;
            this.resultDivisor = resultDivisor;
        }

        public int nextValue() {
            do {
                value = (value * factor) % DIVISOR;
            } while (value % resultDivisor != 0);
            return (int)value;
        }
    }

    public static void main(String[] args) {
        // sample input
//        Generator a = new Generator(65, 16807);
//        Generator b = new Generator(8921, 48271);

        // my input
        Generator a = new Generator(679, 16807, 1);
        Generator b = new Generator(771, 48271, 1);

        int judgesCount = 0;
        for (int i=0; i<40000000; ++i) {
            int aValue = a.nextValue();
            int bValue = b.nextValue();
            if (isMatch(aValue, bValue)) {
                ++judgesCount;
            }
        }

        Util.log("judges count is %d for multiples of 1", judgesCount);

        // sample input
        a = new Generator(65, 16807, 4);
        b = new Generator(8921, 48271, 8);

        // my input
        a = new Generator(679, 16807, 4);
        b = new Generator(771, 48271, 8);

        judgesCount = 0;
        boolean show = true;
        for (int i=0; i<5000000; ++i) {
            int aValue = a.nextValue();
            int bValue = b.nextValue();
            if (isMatch(aValue, bValue)) {
                ++judgesCount;
                if (show) {
                    Util.log("%d\t%d\t%d",  i, aValue, bValue);
                    show = false;
                }
            }
        }

        Util.log("judges count is %d for multiples of non-1", judgesCount);
    }

    private static boolean isMatch(int a, int b) {
        return (a & 0xffff) == (b & 0xffff);
    }
}
