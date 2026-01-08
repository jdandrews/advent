package advent;

import java.util.function.Function;

public final class Grid {
    private Grid() { }
    /**
     * Updates the supplied grid by applying the function f to each member.
     * <em>warning:</em> this method updates the original supplied grid.
     * @param grid the grid to update
     * @param f the function to apply to each member of the grid.
     * @return number of cells updated.
     */
    public static int set(int[][] grid, Function<Integer,Integer> f){
        int updated = 0;
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                int v = f.apply(grid[row][col]);
                if (v != grid[row][col]) {
                    ++updated;
                    grid[row][col] = f.apply(grid[row][col]);
                }
            }
        }
        return updated;
    }

    /**
     * Updates the supplied grid by applying the function f to each member.
     * <em>warning:</em> this method updates the original supplied grid.
     * @param grid the grid to update
     * @param f the function to apply to each member of the grid.
     * @return number of cells updated.
     */
    public static int set(long[][] grid, Function<Long,Long> f){
        int updated = 0;
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[0].length; ++col) {
                long v = f.apply(grid[row][col]);
                if (v != grid[row][col]) {
                    ++updated;
                    grid[row][col] = f.apply(grid[row][col]);
                }
            }
        }
        return updated;
    }

    public static int setAdjacent(int row, int col, int[][] map, Function<Integer, Integer> f) {
        int updated = 0;
        for (int r = row - 1; r <= row + 1; ++r) {
            for (int c = col - 1; c <= col + 1; ++c) {
                if ((r != row || c != col) && r >= 0 && r < map.length &&  c >= 0 && c < map[0].length) {
                    int v = f.apply(map[r][c]);
                    if (v != map[r][c]) {
                        ++updated;
                        map[r][c] = v;
                    }
                }
            }
        }
        return updated;
    }

}
