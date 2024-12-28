package advent.y2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day12 {

    private record Cell(int row, int col, char crop) {}
    private static class Region {
        private Set<Cell> cells = new HashSet<>();
        private int perimeter = 0;
        private int sides = 0;

        private enum Side {
            TOP, RIGHT, BOTTOM, LEFT;
        }
        private Map<Side, Set<Cell>> borderToCell = new HashMap<>();
        {
            borderToCell.put(Side.TOP, new HashSet<>());
            borderToCell.put(Side.RIGHT, new HashSet<>());
            borderToCell.put(Side.BOTTOM, new HashSet<>());
            borderToCell.put(Side.LEFT, new HashSet<>());
        }

        public int area() {
            return cells.size();
        }

        public int perimeter() {
            if (perimeter == 0) {
                for (Cell cell : cells) {
                    perimeter += countBorder(cell);
                }
            }
            return perimeter;
        }

        public int sides() {
            if (sides == 0) {
                // collect the cells by side
                perimeter();

                countHorizontalSides(new ArrayList<>(borderToCell.get(Side.TOP)));
                countHorizontalSides(new ArrayList<>(borderToCell.get(Side.BOTTOM)));
                countVerticalSides(new ArrayList<>(borderToCell.get(Side.LEFT)));
                countVerticalSides(new ArrayList<>(borderToCell.get(Side.RIGHT)));
            }
            return sides;
        }

        private void countHorizontalSides(List<Cell> sideCells) {
            Collections.sort(sideCells, new CompareRowColumnAscending());
            for (int i = 0; i<sideCells.size(); ++i) {
                ++sides;
                int row = sideCells.get(i).row();
                int col = sideCells.get(i).col();
                while (++i < sideCells.size()) {
                    Cell nextCell = sideCells.get(i);
                    if (nextCell.col() != ++col ||  nextCell.row() != row) {
                        --i;
                        break;
                    }
                }
            }
        }

        private void countVerticalSides(List<Cell> sideCells) {
            Collections.sort(sideCells, new CompareColumnRowAscending());
            for (int i = 0; i<sideCells.size(); ++i) {
                ++sides;
                int row = sideCells.get(i).row();
                int col = sideCells.get(i).col();
                while (++i < sideCells.size()) {
                    Cell nextCell = sideCells.get(i);
                    if (nextCell.col() != col ||  nextCell.row() != ++row) {
                        --i;
                        break;
                    }
                }
            }
        }

        private int countBorder(Cell cell) {
            int borderCount = 0;
            Cell up = new Cell(cell.row - 1, cell.col, cell.crop);
            Cell right = new Cell(cell.row, cell.col + 1, cell.crop);
            Cell down = new Cell(cell.row + 1, cell.col, cell.crop);
            Cell left = new Cell(cell.row, cell.col - 1, cell.crop);

            if (!cells.contains(up)) {
                ++borderCount;
                borderToCell.get(Side.TOP).add(cell);
            }
            if (!cells.contains(right)) {
                ++borderCount;
                borderToCell.get(Side.RIGHT).add(cell);
            }
            if (!cells.contains(down)) {
                ++borderCount;
                borderToCell.get(Side.BOTTOM).add(cell);
            }
            if (!cells.contains(left)) {
                ++borderCount;
                borderToCell.get(Side.LEFT).add(cell);
            }

            return borderCount;
        }

        @Override
        public String toString() {
            return "Region{" + cells.iterator().next().crop
                    + "; p=" + perimeter() + "; a=" + area() + "; s=" + sides() + "}";
        }

        private static class CompareRowColumnAscending implements Comparator<Cell> {
            @Override
            public int compare(Cell o1, Cell o2) {
                if (o1.row() == o2.row()) {
                    return o1.col() == o2.col() ? 0 : (o1.col() < o2.col()) ? -1 : +1;
                }
                return o1.row < o2.row ? -1 : 1;
            }
        }

        private static class CompareColumnRowAscending implements Comparator<Cell> {
            @Override
            public int compare(Cell o1, Cell o2) {
                if (o1.col() == o2.col()) {
                    return o1.row() == o2.row() ? 0 : (o1.row() < o2.row()) ? -1 : +1;
                }
                return o1.col() < o2.col() ? -1 : 1;
            }
        }
    }

    public static void main(String[] args) {
        solve(parse(SAMPLE1), "SAMPLE1", true);
        Util.log("----------");
        solve(parse(SAMPLE2), "SAMPLE2", true);
        Util.log("----------");
        solve(parse(SAMPLE3), "SAMPLE3", true);
        Util.log("----------");
        solve(parse(SAMPLE4), "SAMPLE4", true);
        Util.log("----------");
        solve(parse(FileIO.getFileAsString("src/advent/y2024/Day12.txt")), "input", false);
        Util.log("----------");
    }

    private static void solve(Cell[][] map, String id, boolean printRegions) {
        Set<Region> regions = identifyRegions(map);
        long price = getFencingPrice(regions);
        long discountedPrice = getDiscountedFencingPrice(regions);
        if (printRegions) Util.log("%s: %s", id, regions);
        Util.log("%s: Found %d regions; total fencing price is %d; with discounts, %d",
                id, regions.size(), price, discountedPrice);

    }

    private static long getFencingPrice(Set<Region> regions) {
        long price = 0L;
        for (Region region : regions) {
            price += region.perimeter() * region.area();
        }
        return price;
    }

    private static long getDiscountedFencingPrice(Set<Region> regions) {
        long price = 0L;
        for (Region region : regions) {
            price += region.sides() * region.area();
        }
        return price;
    }

    private static Set<Region> identifyRegions(Cell[][] map) {
        Set<Region> regions = new HashSet<>();
        boolean[][] visited = new boolean[map.length][map[0].length];

        for (int row = 0; row < visited.length; ++row) {
            for (int col = 0; col < visited[0].length; ++col) {
                visited[row][col] = false;
            }
        }

        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[0].length; ++col) {
                if (! visited[row][col]) {
                    Region r = floodFill(map, row, col, visited);
                    regions.add(r);
                }
            }
        }
        return regions;
    }

    // Find the connected cells to the row,col pair.
    private static Region floodFill(Cell[][] map, int row, int col, boolean[][] visited) {
        Region region = new Region();
        char crop = map[row][col].crop;

        region.cells.add(map[row][col]);
        visited[row][col] = true;

        Set<Cell> newCells = new HashSet<>();
        do {
            newCells.clear();
            for (Cell cell : region.cells) {
                // left
                if (cell.col - 1 >= 0 && !visited[cell.row][cell.col - 1] && map[cell.row][cell.col - 1].crop == crop) {
                    newCells.add(map[cell.row][cell.col - 1]);
                    visited[cell.row][cell.col - 1] = true;
                }
                // right
                if (cell.col + 1 < map[0].length && !visited[cell.row][cell.col + 1] && map[cell.row][cell.col + 1].crop == crop) {
                    newCells.add(map[cell.row][cell.col + 1]);
                    visited[cell.row][cell.col + 1] = true;
                }
                // up
                if (cell.row - 1 >= 0 && !visited[cell.row - 1][cell.col] && map[cell.row - 1][cell.col].crop == crop) {
                    newCells.add(map[cell.row - 1][cell.col]);
                    visited[cell.row - 1][cell.col] = true;
                }
                // down
                if (cell.row + 1 < map.length && !visited[cell.row + 1][cell.col] && map[cell.row + 1][cell.col].crop == crop) {
                    newCells.add(map[cell.row + 1][cell.col]);
                    visited[cell.row + 1][cell.col] = true;
                }
            }
            region.cells.addAll(newCells);
        } while (newCells.size() != 0);


        return region;
    }

    private static Cell[][] parse(String data) {
        String[] rows = data.split("\n");
        Cell[][] map  = new Cell[rows.length][rows[0].length()];

        for (int row = 0; row<rows.length; ++row) {
            for (int col = 0; col < rows[row].length(); ++col) {
                map[row][col] = new Cell(row, col, rows[row].charAt(col));
            }
        }
        return map;
    }

    private static final String SAMPLE1 = """
                    AAAA
                    BBCD
                    BBCC
                    EEEC""";

    private static final String SAMPLE2 = """
                    OOOOO
                    OXOXO
                    OOOOO
                    OXOXO
                    OOOOO""";

    private static final String SAMPLE3 = """
                    RRRRIICCFF
                    RRRRIICCCF
                    VVRRRCCFFF
                    VVRCCCJFFF
                    VVVVCJJCFE
                    VVIVCCJJEE
                    VVIIICJJEE
                    MIIIIIJJEE
                    MIIISIJEEE
                    MMMISSJEEE""";

    private static final String SAMPLE4 = """
                    AAAAAA
                    AAABBA
                    AAABBA
                    ABBAAA
                    ABBAAA
                    AAAAAA""";
}
