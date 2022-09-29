package advent.y2017;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

// https://www.redblobgames.com/grids/hexagons/
// http://devmag.org.za/2013/08/31/geometry-with-hex-coordinates/
public class Day11 {
    private static enum Direction {
        N, NE, SE, S, SW, NW;
    }

    private static class HexGridAxialOffset extends HexGridAxialPosition {
        public HexGridAxialOffset(int changeInQ, int changeInR) {
            super(changeInQ, changeInR);
        }
    }

    private static class HexGridAxialPosition {
        public int q;
        public int r;
        public HexGridAxialPosition(int startingQ, int startingR) {
            q = startingQ;
            r = startingR;
        }

        public HexGridAxialPosition move(Direction direction) {
            HexGridAxialPosition result = new HexGridAxialPosition(q,r);
            HexGridAxialOffset offset = OFFSET_DIRECTION.get(direction);
            result.q += offset.q;
            result.r += offset.r;
            return result;
        }
    }

    public static final Map<Direction, HexGridAxialOffset> OFFSET_DIRECTION = new HashMap<>();
    static {
        OFFSET_DIRECTION.put(Direction.N,  new HexGridAxialOffset(0, -1));
        OFFSET_DIRECTION.put(Direction.NE, new HexGridAxialOffset(1, -1));
        OFFSET_DIRECTION.put(Direction.SE, new HexGridAxialOffset(1, 0));
        OFFSET_DIRECTION.put(Direction.S,  new HexGridAxialOffset(0, 1));
        OFFSET_DIRECTION.put(Direction.SW, new HexGridAxialOffset(-1, 1));
        OFFSET_DIRECTION.put(Direction.NW, new HexGridAxialOffset(-1, 0));
    }

    public static void main(String[] args) throws IOException {
        boolean test = false;
        List<String> input = Util.readInput("2017", "Day11.txt");
        if (test)
            input.add(0, "se,sw,se,sw,sw");
        String[] directions = input.get(0).split(",");
        Util.log("%d steps", directions.length);
        HexGridAxialPosition here = new HexGridAxialPosition(0, 0);
        double maxDistance = -1.0;
        for (String directionString : directions) {
            Direction direction = Direction.valueOf(directionString.toUpperCase());
            HexGridAxialPosition start = here;
            here = here.move(direction);
            if (test) {
                Util.log("move %s from (%d, %d) to (%d, %d); distance is %s%n",
                    direction, start.r, start.q, here.r, here.q, hexDistance(new HexGridAxialPosition(0, 0), here));
            }
            maxDistance = Math.max(maxDistance, hexDistance(new HexGridAxialPosition(0, 0), here));
        }
        System.out.format("here: %d, %d; distance is %s; max was %f",
                here.r, here.q, hexDistance(new HexGridAxialPosition(0, 0), here), maxDistance);
    }

    private static double hexDistance(HexGridAxialPosition a, HexGridAxialPosition b) {
        return (Math.abs(a.q - b.q)
              + Math.abs(a.q + a.r - b.q - b.r)
              + Math.abs(a.r - b.r)) / 2.;
    }
}
