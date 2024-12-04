package advent.y2024;

import advent.FileIO;

public class Day03 {
    public static void main(String[] args) {
        String memory = FileIO.getFileAsString("src/advent/y2024/Day03.txt");

        System.out.println("sum1 = " + part1(memory));

        memory = FileIO.getFileAsString("src/advent/y2024/Day03.txt");

        System.out.println("sum2 = " + part2(memory));
    }

    private static long part1(String memory) {
        long result = 0;

        do {
            int n0 = memory.indexOf("mul(");
            if (n0 < 0) {
                break;
            }
            int n1 = -1;
            boolean foundComma = false;
            boolean foundOp1 = false;
            boolean foundOp2 = false;

            for (int n = n0 + 4; n < memory.length(); ++n) {
                char c = memory.charAt(n);
                if (Character.isDigit(c)) {
                    foundOp1 = true;
                    if (foundComma) {
                        foundOp2 = true;
                    }
                } else if (c == ',') {
                    if (foundComma) {
                        break;
                    }
                    foundComma = true;

                } else if (c == ')') {
                    if (foundOp1 && foundOp2 && foundComma) {
                        n1 = n;
                    }
                    break;
                } else {
                    break;
                }
            }

            if (n1 > n0) {
                result += evaluate(memory, n0, n1);
                memory = memory.substring(n1 + 1);
            } else {
                memory = memory.substring(n0 + 1);
            }
        } while (memory.length() > 4);
        return result;
    }

    private static long part2(String memory) {
        long result = 0;

        do {
            int n0 = memory.indexOf("mul(");
            int nX = memory.indexOf("don't()");
            if (n0 < 0) {
                break;
            }
            if (nX < n0) {
                memory = memory.substring(nX + 1);
                nX = memory.indexOf("do()");
                if (nX < 0) break;  // we're don't to the end
                memory = memory.substring(nX + 1);
                continue;
            }

            int n1 = -1;
            boolean foundComma = false;
            boolean foundOp1 = false;
            boolean foundOp2 = false;

            for (int n = n0 + 4; n < memory.length(); ++n) {
                char c = memory.charAt(n);
                if (Character.isDigit(c)) {
                    foundOp1 = true;
                    if (foundComma) {
                        foundOp2 = true;
                    }
                } else if (c == ',') {
                    if (foundComma) {
                        break;
                    }
                    foundComma = true;

                } else if (c == ')') {
                    if (foundOp1 && foundOp2 && foundComma) {
                        n1 = n;
                    }
                    break;
                } else {
                    break;
                }
            }

            if (n1 > n0) {
                result += evaluate(memory, n0, n1);
                memory = memory.substring(n1 + 1);
            } else {
                memory = memory.substring(n0 + 1);
            }
        } while (memory.length() > 4);
        return result;
    }

    private static long evaluate(String memory, int n0, int n1) {
        String f = memory.substring(n0, n1+1);
        int n = f.indexOf(",");
        long m1 = Long.valueOf(f.substring(4, n));
        long m2 = Long.valueOf(f.substring(n+1, f.length()-1));

        return m1 * m2;
    }
}
