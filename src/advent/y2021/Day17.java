package advent.y2021;

import java.util.ArrayList;
import java.util.List;
import advent.Util;
import advent.Util.Point;

public class Day17 {

    private static record Target(Point ul, Point lr) {
        public boolean contains(Point p) {
            return p.x() >= ul.x() && p.x() <= lr.x() && p.y() >= lr.y() && p.y() <= ul.y();
        }
    }
    private static record V(int dx, int dy) {}

    private static Target sampleTarget = new Target(new Point(20, -5), new Point(30, -10));
    private static Target puzzleTarget = new Target(new Point(70, -121), new Point(125, -159));

    public static void main(String[] args) {
        List<Track> tracks = getAllTracks(sampleTarget);
        Track result = getMaxY(tracks);
        Util.log("part 1: Vo for maxY = %s for sample; max Y = %d.", result.v0(), result.maxY());
        Util.log("part 2: there are %d initial velocities resulting in a target hit.", tracks.size());
        Util.log("--------");
        tracks = getAllTracks(puzzleTarget);
        result = getMaxY(tracks);
        Util.log("part 1: Vo for maxY = %s for puzzle; max Y = %d.", result.v0(), result.maxY());
        Util.log("part 2: there are %d initial velocities resulting in a target hit.", tracks.size());
    }

    private static Track getMaxY(List<Track> tracks) {
        Track maxTrack = tracks.get(0);
        int maxY = maxTrack.maxY();
        for (Track track : tracks) {
            int y = track.maxY();
            if (y > maxY) {
                maxTrack = track;
                maxY = y;

            }
        }
        return maxTrack;
    }

    private static List<Track> getAllTracks(Target target) {
        List<Track> results = new ArrayList<>();

        for (int y = target.lr().y(); y < 500; ++y) {
            for (int x = 0; x <= target.lr().x(); ++x) {
                Track track = generateTrack(new V(x,y), target);
                if (track != null && track.hitsTarget) {
                    results.add(track);
                }
            }
        }
        return results;
    }

    private static record Track(V v0, List<Point> points, boolean hitsTarget) {
        public int maxY() {
            return points.stream().map(p -> p.y()).reduce(0, (a, b)->Math.max(a, b));
        }
    }

    private static Track generateTrack(V v, Target t) {
        V v0 = v;
        int maxY = Integer.MIN_VALUE;
        List<Point> result = new ArrayList<>();
        Point p = new Point(0,0);
        while (p.y() >= t.lr().y()) {
            p = new Point(p.x() + v.dx(), p.y() + v.dy());
            result.add(p);
            maxY = p.y() > maxY ? p.y() : maxY;
            if (t.contains(p)) {
                return new Track(v0, result, true);
            }

            v = new V(Math.max(0, v.dx()-1), v.dy()-1);
        }

        return new Track(v0, result, false);
    }
}
