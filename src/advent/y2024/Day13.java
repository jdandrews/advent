package advent.y2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day13 {
    private static record ButtonPresses(long a, long b) {
        public long cost() {
            if (a() == NO_SOLUTION)
                return 0;
            return 3*a() + b();
        }
    }

    private static record Machine(long dxA, long dyA, long dxB, long dyB, long pX, long pY) {
        private long detM() {
            return dxA * dyB - dxB * dyA;
        }

        // matrix solution; see long comment at the end of the outer class
        public ButtonPresses solution() {
            long detM = detM();
            if (detM == 0L) {
                return new ButtonPresses(NO_SOLUTION, NO_SOLUTION);
            }

            // adjoint * P; don't divide until we're sure integer division comes out even
            long a = pX * dyB - pY * dxB;
            long b = pY * dxA - pX * dyA;

            if (a % detM == 0 && b % detM == 0) {
                return new ButtonPresses(a / detM, b / detM);
            }

            // no integer solution exists
            return new ButtonPresses(NO_SOLUTION, NO_SOLUTION);
        }
    }

    /**
     * part 1:
     *  solve: Px = n * dxA + m * dxB
     *         Py = n * dyA + m * dyB
     *  minimize 3n + m
     *
     * @param args
     */
    public static void main(String[] args) {
        ButtonPresses bp = presses(94, 34, 22, 67, 8400, 5400);
        Util.log("SAMPLE machine 1 result: A: %d B: %d cost: %d", bp.a(), bp.b(), bp.cost());

        Set<Machine> machines = parse(SAMPLE);
        long tokens = 0;
        long n = 0;
        for (Machine m : machines) {
            bp = presses(m);
            Util.log("SAMPLE machine %d result 1: A: %d B: %d cost: %d", ++n, bp.a(), bp.b(), bp.cost());
            Util.log("SAMPLE machine %d result 2: A: %d B: %d cost: %d", ++n, m.solution().a(), m.solution().b(), m.solution().cost());
            tokens += bp.cost();
        }
        Util.log("SAMPLE: %d tokens.\n", tokens);

        machines = reparse(SAMPLE);
        tokens = 0;
        n = 0;
        for (Machine m : machines) {
            bp = m.solution();
            Util.log("SAMPLE machine %d part 2 result: A: %d B: %d cost: %d", ++n, bp.a(), bp.b(), bp.cost());
            tokens += bp.cost();
        }
        Util.log("SAMPLE: %d tokens.\n", tokens);

        machines = parse(FileIO.getFileAsString("src/advent/y2024/Day13.txt"));
        tokens = 0;
        for (Machine m : machines) {
            bp = presses(m);
            tokens += bp.cost();
        }
        Util.log("part 1: %d tokens.", tokens);

        machines = reparse(FileIO.getFileAsString("src/advent/y2024/Day13.txt"));
        tokens = 0;
        for (Machine m : machines) {
            bp = m.solution();
            tokens += bp.cost();
        }
        Util.log("part 2: %d tokens.", tokens);
    }

    /**
     * Parse a list of these:
     *     Button A: X+94, Y+34
     *     Button B: X+22, Y+67
     *     Prize: X=8400, Y=5400
     *
     * @param in
     * @return
     */
    private static Set<Machine> parse(String in) {
        Set<Machine> machines = new HashSet<>();
        String[] lines = in.split("\n");
        for (int i = 0; i<lines.length; ++i) {
            try {
                long dxA = Long.valueOf(lines[i].substring(lines[i].indexOf("X+") + 2, lines[i].indexOf(",")));
                long dyA = Long.valueOf(lines[i].substring(lines[i].indexOf("Y+") + 2, lines[i].length()));
                ++i;
                long dxB = Long.valueOf(lines[i].substring(lines[i].indexOf("X+") + 2, lines[i].indexOf(",")));
                long dyB = Long.valueOf(lines[i].substring(lines[i].indexOf("Y+") + 2, lines[i].length()));
                ++i;
                long pX = Long.valueOf(lines[i].substring(lines[i].indexOf("X=") + 2, lines[i].indexOf(",")));
                long pY = Long.valueOf(lines[i].substring(lines[i].indexOf("Y=") + 2, lines[i].length()));
                ++i;
                machines.add(new Machine(dxA, dyA, dxB, dyB, pX, pY));
            } catch (RuntimeException e) {
                Util.log("%d: %s", i, lines[i]);
            }
        }
        return machines;
    }

    private static final long OFFSET = 10_000_000_000_000L;

    private static Set<Machine> reparse(String in){
        Set<Machine> machines = parse(in);
        Set<Machine> realMachines = new HashSet<>();
        for (Machine m : machines) {
            realMachines.add(new Machine(m.dxA(), m.dyA(), m.dxB(), m.dyB(), m.pX + OFFSET, m.pY + OFFSET));
        }
        return realMachines;
    }

    private static final long NO_SOLUTION = Long.MIN_VALUE;

    private static ButtonPresses presses(Machine m) {
        return presses(m.dxA(), m.dyA(), m.dxB(), m.dyB(), m.pX(), m.pY());
    }

    private static ButtonPresses presses(long dxA, long dyA, long dxB, long dyB, long pX, long pY) {
        List<ButtonPresses> results = new ArrayList<>();
        for (long aPresses = 0; aPresses < pX / dxA /* && aPresses <= 100 */; ++aPresses) {

            long bMoveX = pX - aPresses * dxA;
            long bMoveY = pY - aPresses * dyA;

            if (       bMoveX >= 0 && bMoveX % dxB == 0
                    && bMoveY >= 0 && bMoveY % dyB == 0
                    && bMoveX / dxB == bMoveY / dyB
                    /* && bMoveX / dxB <= 100 */ ) {

                results.add(new ButtonPresses(aPresses, bMoveX / dxB));
            }
        }

        ButtonPresses minCost = null;

        for (ButtonPresses bp : results) {
            if (minCost == null || bp.cost() < minCost.cost()) {
                minCost = bp;
            }
        }

        if (results.size() > 1) {
            Util.log("Machine( A(%d, %d), B(%d, %d), P(%d, %d)-- results: %s; chose %d.",
                    dxA, dyA, dxB, dyB, pX, pY, results, minCost);
            Util.log("\t%d (%d) =? %d * %d + %d * %d",
                    pX, minCost.a() * dxA + minCost.b() * dxB, minCost.a(), dxA, minCost.b(), dxB);
            Util.log("\t%d (%d) =? %d * %d + %d * %d",
                    pY, minCost.a() * dyA + minCost.b() * dyB, minCost.a(), dyA, minCost.b(), dyB);
        }

        if (minCost == null) {       // no solution with < 100 presses
            return new ButtonPresses(NO_SOLUTION, NO_SOLUTION);
        }
        return minCost;
    }

    private static final String SAMPLE = """
                    Button A: X+94, Y+34
                    Button B: X+22, Y+67
                    Prize: X=8400, Y=5400

                    Button A: X+26, Y+66
                    Button B: X+67, Y+21
                    Prize: X=12748, Y=12176

                    Button A: X+17, Y+86
                    Button B: X+84, Y+37
                    Prize: X=7870, Y=6450

                    Button A: X+69, Y+23
                    Button B: X+27, Y+71
                    Prize: X=18641, Y=10279
                    """;
}
/*
 * Matrix solution: restate the problem as a matrix equation:
 *
 * pX = a * dxA + b * dxB
 * pY = a * dyA + b * dyB
 *
 * pX, pY => coordinates of the prize
 * dxA, dxB => change in x for one button press of button A or B
 * dyA, dyB => change in y for one button press of button A or B
 * a, b => button presses of button A or B
 *
 * restated matrixly:
 *
 *  P = A * D
 *
 *  A = the vector (a, b)
 *                  _        _
 *  D = the matrix | dxA  dxB |
 *                 | dyA  dyB |
 *                  —        –
 *  Since we're using ascii text here, let's define "det(D)" as the determinate
 *  of D, adj(D) as the adjoint of D, and (1/D) as the inverse of D. The solution
 *  to this equation is then (1/D)*P, where (1/D) = adj(D)/det(D).
 *  The solution also has to make sure that there is no fractional part in the
 *  result, and that the result exists, which is true of the determinate of D
 *  is non-zero (zero would be in the denominator of the solution if det(D) = 0
 *
 *  The code for this turns out to be pretty tight; see "detM()" and "solution()"
 *  in the "Machine" class above.
 */

