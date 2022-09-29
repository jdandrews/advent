package advent.y2018;

public class Day06 {

    private static class Grid {
        private int[][] seeds;

        public Grid(int max) {
            grid = new int[max][];
            for (int i=0; i<max; ++i) {
                grid[i] = new int[max];
                for (int j=0; j<max; ++j) {
                    grid[i][j] = -1;
                }
            }
        }

        private int[][] grid;
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int i=0; i<grid[0].length; ++i) {
                for (int j=0; j<grid[0].length; ++j) {
                    if (grid[i][j]<0)
                        result.append("    .");
                    else
                        result.append(String.format("%5d", grid[i][j]));
                }
                result.append("\n");
            }
            return result.toString();
        }

        public void setSeeds(int[][] s) {
            this.seeds = s;
        }

        public void markWithClosestSeeds() {
            for (int i=0; i<grid.length; ++i) {
                for (int j=0; j<grid[i].length; ++j) {
                    int[] distances = new int[seeds.length];
                    for (int k=0; k<seeds.length; ++k) {
                        distances[k] = Math.abs(seeds[k][1] - i) + Math.abs(seeds[k][0] - j);
                    }
                    int max=Integer.MAX_VALUE;
                    for (int k=0; k<distances.length; ++k) {
                        if (distances[k] < max) {
                            max = distances[k];
                            grid[i][j] = k;
                        } else if (distances[k] == max) {
                            grid[i][j] = -1;
                        }
                    }
                }
            }
        }

        public int findLargestArea() {
            int[] areas = new int[seeds.length];
            for (int k=0; k<seeds.length; ++k) {
                areas[k] = 0;
            }

            for (int i=0; i<grid.length; ++i) {
                for (int j=0; j<grid[i].length; ++j) {
                    if (isEdge(i, j) && grid[i][j]>=0) {
                        areas[grid[i][j]] = -1;
                    }
                    else if (grid[i][j] >= 0 && areas[grid[i][j]] >= 0) {
                        ++areas[grid[i][j]];
                    }
                }
            }
            
            int max = Integer.MIN_VALUE;
            for (int k=0; k<areas.length; ++k) {
                max = Math.max(max, areas[k]);
            }
            return max;
        }
        
        private boolean isEdge(int i, int j) {
            return i==0 || j==0 || i==grid.length-1 || j==grid[0].length-1;
        }

        public void markWithSumOfDistances() {
            for (int i=0; i<grid.length; ++i) {
                for (int j=0; j<grid[i].length; ++j) {
                    grid[i][j] = 0;
                    for (int k=0; k<seeds.length; ++k) {
                        grid[i][j] += Math.abs(seeds[k][1] - i) + Math.abs(seeds[k][0] - j);
                    }
                }
            }
        }

        public int findAreaWithThreshold(int threshold) {
            int area = 0;
            for (int i=0; i<grid.length; ++i) {
                for (int j=0; j<grid[i].length; ++j) {
                    if (grid[i][j] >= threshold) {
                        grid[i][j] = -1;
                    }
                    else if (isEdge(i, j) && grid[i][j]<threshold) {
                        throw new UnsupportedOperationException("world is too small at "+i+","+j+":"+grid[i][j]);
                    }
                    else {
                        ++area;
                    }
                }
            }
            return area;
        }
    }

    public static void main(String[] args) {
        Grid world = loadGrid(DATA);
//        System.out.println(world);
        world.markWithClosestSeeds();
//        System.out.println(world);
        System.out.println("part 1: largest non-infinite area is " + world.findLargestArea());

        world = loadGrid(DATA);
//        System.out.println(world);
        world.markWithSumOfDistances();
//        System.out.println(world);
        System.out.println("part 2: minimum total distance area is " + world.findAreaWithThreshold(10000));
//        System.out.println(world);
    }

    private static Grid loadGrid(int[][] data) {
        int max = 0;
        for (int i=0; i<data.length; ++i) {
            max = Math.max(max, data[i][0]);
            max = Math.max(max, data[i][1]);
        }

        Grid world = new Grid(max+1);
        world.setSeeds(data);

        for (int i=0; i<data.length; ++i) {
            world.grid[data[i][1]][data[i][0]] = i;
        }
        return world;
    }

    private static final int[][] SAMPLE = {
            {1, 1},
            {1, 6},
            {8, 3},
            {3, 4},
            {5, 5},
            {8, 9}
    };

    private static final int[][] DATA =  {
            {61, 90},
            {199, 205},
            {170, 60},
            {235, 312},
            {121, 290},
            {62, 191},
            {289, 130},
            {131, 188},
            {259, 82},
            {177, 97},
            {205, 47},
            {302, 247},
            {94, 355},
            {340, 75},
            {315, 128},
            {337, 351},
            {73, 244},
            {273, 103},
            {306, 239},
            {261, 198},
            {355, 94},
            {322, 69},
            {308, 333},
            {123, 63},
            {218, 44},
            {278, 288},
            {172, 202},
            {286, 172},
            {141, 193},
            {72, 316},
            {84, 121},
            {106, 46},
            {349, 77},
            {358, 66},
            {309, 234},
            {289, 268},
            {173, 154},
            {338, 57},
            {316, 95},
            {300, 279},
            {95, 285},
            {68, 201},
            {77, 117},
            {313, 297},
            {259, 97},
            {270, 318},
            {338, 149},
            {273, 120},
            {229, 262},
            {270, 136}
    };
}
