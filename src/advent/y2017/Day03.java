package advent.y2017;

import advent.Util;

public class Day03 {

	/*
	 * spiral storage:
	 *                <---   59  58  57
	 *   37  36  35  34  33  32  31  56
	 *   38  17  16  15  14  13  30  55
     *   39  18   5   4   3  12  29  54
     *   40  19   6   1   2  11  28  53
     *   41  20   7   8   9  10  27  52
     *   42  21  22  23  24  25  26  51
     *   43  44  45  46  47  48  49  50
     *
	 * square:    size   low  high  distance
	 *   1         1       1    1    0
	 *   2         3       2    9    1 2 1 2 1 2 1 2
	 *   3         5      10   25    3 2 3 4 3 2 3 4 3 2 3 4 3 2 3 4
	 *   4         7      26   49    5 4 3 4 5 6 5 4 3 4 5 6...
	 *   5         9      50   81    7 6 5 4 5 6 7 8 7 6 5 4...
	 */
    public static void main(String[] args) {
        int[] targets = { 1, 12, 23, 43, 47, 1024, 265149 };

        for (int target : targets) {
            Util.log("target %d is %d squares from the origin", target, distanceTo(target));
            Util.log("sum of cells first value written > %d -> %d", target, firstSumThatExceeds(target));
        }
    }

	/**
	 * Finds first sum that exceeds the target. Spiral sums--cell contains sum of all adjacent cells "so far":
	 * 147  142  133  122   59
	 * 304    5    4    2   57
	 * 330   10    1    1   54
	 * 351   11   23   25   26
	 * 362  747  806--->   ...
	 *
	 * @param target
	 * @return
	 */
    private static int firstSumThatExceeds(int target) {
        // PARI C Library code:
        /*
        int m=5;            // # of loops in the spiral
        int h=2*m-1;        // length of a spiral side
        A=matrix(h, h);
        print1(A[m, m]=1, ", ");

        T=[[1, 0], [1, -1], [0, -1], [ -1, -1], [ -1, 0], [ -1, 1], [0, 1], [1, 1]];

        for(int n=1, (h-2)^2-1, g=sqrtint(n); r=(g+g%2)\2; q=4*r^2; d=n-q;
        if(n<=q-2*r, j=d+3*r; k=r,
                if(n<=q, j=r; k=-d-r,
                if(n<=q+2*r, j=r-d; k=-r, j=-r; k=d-3*r)));
        j=j+m;
        k=k+m;
        s=0;
        for(c=1, 8, v=[j, k]; v+=T[c]; s=s+A[v[1], v[2]]);
        A[j, k]=s;
        print1(s, ", ")
        )
        */
        if (target<1) throw new UnsupportedOperationException("numbering starts with 1");
        if (target==1) return 2;

        return 0;
    }

    private static int distanceTo(int target) {
        int square = 1;
        int size = 2 * square - 1;
        int low = 1;
        int high = size;
        while (high < target) {
            ++square;
            size = 2 * square - 1;
            low = high + 1;
            high += 2 * size + 2 * (size - 2);
        }

        int min = size / 2;
        int max = size - 1;
        int steps = max - 1;
        int dSteps = -1;
        for (int i = low; i < target; ++i) {
            steps += dSteps;
            if (steps == max)
                dSteps = -1;
            if (steps == min)
                dSteps = 1;
        }

        Util.log("square: %d size: %d low: %d high: %d distance: %d-%d", square, size, low, high, min, max);
        return steps;
    }
}
