package advent.y2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advent.FileIO;
import advent.Util;

public class Day08 {

    public static void main(String[] args) {
        char[][] map = loadMap(Arrays.asList(SAMPLE));
        antennaTypeToSet = inventoryMap(map);
        char[][] antinodeMap = mapAntinodes(map);
        Util.log("Sample part 1: Found %d distinct antinode locations.", countAntinodes(antinodeMap));

        map = loadMap(FileIO.getFileAsList("src/advent/y2024/Day08.txt"));
        antennaTypeToSet = inventoryMap(map);
        antinodeMap = mapAntinodes(map);
        Util.log("Input part 1: Found %d distinct antinode locations.", countAntinodes(antinodeMap));

        map = loadMap(Arrays.asList(SAMPLE));
        antennaTypeToSet = inventoryMap(map);
        antinodeMap = mapPart2Antinodes(map);
        Util.log(antinodeMap);
        Util.log("Sample part 2: Found %d distinct antinode locations.", countAntinodes(antinodeMap));

        map = loadMap(FileIO.getFileAsList("src/advent/y2024/Day08.txt"));
        antennaTypeToSet = inventoryMap(map);
        antinodeMap = mapPart2Antinodes(map);
        Util.log("Input part 2: Found %d distinct antinode locations.", countAntinodes(antinodeMap));
    }

    private static Map<Character, Set<Antenna>> inventoryMap(char[][] map) {
        Map<Character, Set<Antenna>> result = new HashMap<>();
        for (int row = 0; row < map.length; ++row) {
            for (int col = 0; col < map[row].length; ++col) {
                char c = map[row][col];
                if (c != '.') {

                    if (!result.containsKey(c)) {
                        result.put(c, new HashSet<>());
                    }
                    result.get(c).add(new Antenna(new Cell(row, col), map[row][col]));
                }
            }
        }
        return result;
    }

    private static record Cell(int row, int col) { }

    private static record Antenna(Cell location, char type) { }

    private static Map<Character, Set<Antenna>> antennaTypeToSet = new HashMap<>();

    private static int countAntinodes(char[][] antinodeMap) {
        int antinodes = 0;
        for (int row = 0; row < antinodeMap.length; ++row) {
            for (int col = 0; col < antinodeMap[row].length; ++col) {
                if (antinodeMap[row][col] == '#')
                    ++antinodes;
            }
        }
        return antinodes;
    }

    private static char[][] mapAntinodes(char[][] map) {
        char[][] antinodes = filledMap(map.length, map[0].length, '.');
        for (Map.Entry<Character, Set<Antenna>> entry : antennaTypeToSet.entrySet()) {
            for (Antenna a0 : entry.getValue()) {
                for (Antenna a1 : entry.getValue()) {
                    if (a0 == a1)
                        continue;

                    int dRow = a0.location.row - a1.location.row;
                    int dCol = a0.location.col - a1.location.col;

                    Cell antinode = new Cell(a0.location.row + dRow, a0.location.col + dCol);

                    if (antinode.row >= 0 && antinode.row < antinodes.length && antinode.col >= 0
                            && antinode.col < antinodes[antinode.row].length) {
                        antinodes[antinode.row][antinode.col] = '#';
                    }
                }
            }
        }
        return antinodes;
    }

    private static char[][] mapPart2Antinodes(char[][] map) {
        char[][] antinodes = filledMap(map.length, map[0].length, '.');
        for (Map.Entry<Character, Set<Antenna>> entry : antennaTypeToSet.entrySet()) {
            for (Antenna a0 : entry.getValue()) {
                boolean found2antennas = false;
                for (Antenna a1 : entry.getValue()) {
                    found2antennas = true;
                    if (a0 == a1)
                        continue;

                    int dRow = a0.location.row - a1.location.row;
                    int dCol = a0.location.col - a1.location.col;
                    dRow /= commonFactor(dRow, dCol);
                    dCol /= commonFactor(dRow, dCol);

                    Cell antinode = new Cell(a0.location.row + dRow, a0.location.col + dCol);
                    while (antinode.row >= 0
                            && antinode.row < antinodes.length
                            && antinode.col >= 0
                            && antinode.col < antinodes[antinode.row].length) {

                        antinodes[antinode.row][antinode.col] = '#';
                        antinode = new Cell(antinode.row + dRow, antinode.col + dCol);
                    }
                }
                if (found2antennas) {
                    antinodes[a0.location.row][a0.location.col] = '#';
                }
            }
        }
        return antinodes;
    }

    static final int[] primes = { 2, 3, 5, 7, 11 };

    private static int commonFactor(int m0, int m1) {
        if (m0 > 121) {
            throw new UnsupportedOperationException("m0 or m1 > 11^2: m0=" + m0 + ", m1=" + m1);
        }
        int maxFactor = 1;
        for (int n = 0; n < primes.length; ++n) {
            while (m0 % primes[n] == 0 && m1 % primes[n] == 0) {
                m0 /= primes[n];
                m1 /= primes[n];
                maxFactor *= primes[n];
            }
        }
        return maxFactor;
    }

    private static char[][] filledMap(int rows, int cols, char c) {
        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; ++i) {
            Arrays.fill(map[i], c);
        }
        return map;
    }

    private static char[][] loadMap(List<String> rows) {
        char[][] map = new char[rows.size()][rows.get(0).length()];
        for (int row = 0; row < rows.size(); ++row) {
            String rowText = rows.get(row);
            for (int col = 0; col < rowText.length(); ++col) {
                map[row][col] = rowText.charAt(col);
            }
        }
        return map;
    }

    private static final String[] SAMPLE = { "............", "........0...", ".....0......", ".......0....",
            "....0.......", "......A.....", "............", "............", "........A...", ".........A..",
            "............", "............" };
}
