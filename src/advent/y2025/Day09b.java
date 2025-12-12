package advent.y2025;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Clever solution from https://github.com/zebalu/advent-of-code-2025/blob/master/Day09.java.
 */
public class Day09b {
    private static final PrintStream IO = System.out;

    public static void main(String[] args) {
        new Day09b().main();
    }

    void main() {
        var input = readInput();

        var rectangles = IntStream
                .range(0, input.size() - 1)
                .boxed()
                .flatMap(i -> IntStream
                        .range(i + 1, input.size())
                        .mapToObj(j -> Rectangle
                                .fromCoords(input.get(i), input.get(j))))
                .toList();
        IO.println(rectangles.stream()
                .mapToLong(Rectangle::area)
                .max().orElseThrow());

        var lines = IntStream
                .range(0, input.size())
                .mapToObj(i -> Rectangle
                        .fromCoords(input.get(i), input.get((i + 1) % input.size())))
                .toList();
        IO.println(rectangles.stream()
                .filter(r -> lines.stream()
                        .noneMatch(r::isOverlap))
                .mapToLong(Rectangle::area)
                .max().orElseThrow());
    }

    record Coord(long x, long y) {
        static Coord fromStr(String str) {
            var parts = str.split(",");
            return new Coord(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        }
    }

    record Rectangle(Coord min, Coord max) {
        boolean isOverlap(Rectangle other) {
            return min.x < other.max.x && max.x > other.min.x && min.y < other.max.y && max.y > other.min.y;
        }

        long area() {
            return (max.x - min.x + 1) * (max.y - min.y + 1);
        }

        static Rectangle fromCoords(Coord a, Coord b) {
            return new Rectangle(new Coord(Math.min(a.x, b.x), Math.min(a.y, b.y)),
                    new Coord(Math.max(a.x, b.x), Math.max(a.y, b.y)));
        }
    }

    List<Coord> readInput() {
        try (var stream = Files
                .lines(Path.of("src/advent/y2025/Day09.txt"), StandardCharsets.UTF_8)) {
            return stream.map(Coord::fromStr).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
