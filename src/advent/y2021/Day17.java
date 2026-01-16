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
        Track result = getMaxY(sampleTarget);
        Util.log("part 1: Vo for maxY = %s for sample; max Y = %d.", result.v0(), result.maxY());
        Util.log("part 2: there are %d initial velocities resulting in a target hit.", tracks.size());
        for (int i = 0; i<tracks.size(); ++i) {
            Util.log("v=%s", tracks.get(i).v0());
        }
        Util.log("--------");
        tracks = getAllTracks(puzzleTarget);
        result = getMaxY(puzzleTarget);
        Util.log("part 1: Vo for maxY = %s for puzzle; max Y = %d.", result.v0(), result.maxY());
        Util.log("part 2: there are %d initial velocities resulting in a target hit.", tracks.size());
    }

    private static List<Track> getAllTracks(Target target) {
        List<Track> results = new ArrayList<>();

        for (int y = target.lr().y(); y < 500; ++y) {
            for (int x = 0; x < target.lr().x(); ++x) {
                Track track = generateTrack(new V(x,y), target);
                if (track != null && track.hitsTarget) {
                    results.add(track);
                }
            }
        }
        return results;
    }

    private static Track getMaxY(Target target) {
        // search of the solution space.
        //      vx varies from 0 to t.lr().x()
        //      vy varies from a very large positive value to t.lr().y()
        //      ... but since real cases always put the target to the right of the submarine, the highest trajectory
        //      will likely always have x decaying to zero and then dropping the probe vertically, but we'll be
        //      constrained to fit dy between 2 integer x values that allow the probe to actually fall into the
        //      the target. So a search strategy would be to start dY at some small positive value, calculate any
        //      dx that works, and increase dy in binary fashion until we find a y with no possible corresponding x.
        int maxY = Integer.MIN_VALUE;
        Track maxTrack = null;
        for (int y = 1; y < 500; ++y) {
            Track track = findX(y, target);

            if (track != null && track.maxY() > maxY) {
                maxTrack = track;
                maxY = track.maxY();
            }
        }
        return maxTrack == null ? null : maxTrack;
    }

    private static Track findX(int y, Target target) {
        for (int x = 1; x <= target.lr().x(); ++x) {
            Track track = generateTrack(new V(x, y), target);
            if (track.requiredDx() == 0 && track.hitsTarget()) {
                return track;
            }
        }
        return null;
    }

    private static record Track(V v0, List<Point> points, boolean hitsTarget, int requiredDx) {
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
            p = new Point(p.x() + v.dx(), p.y());
            result.add(p);
            maxY = p.y() > maxY ? p.y() : maxY;
            if (t.contains(p)) {
                return new Track(v0, result, true, 0);
            }

            p = new Point(p.x(), p.y() + v.dy());
            result.add(p);
            maxY = p.y() > maxY ? p.y() : maxY;
            if (t.contains(p)) {
                return new Track(v0, result, true, 0);
            }

            v = new V(Math.max(0, v.dx()-1), v.dy()-1);
        }

        return new Track(v0, result, false, generateDx(result, t));
    }

    private static int generateDx(List<Point> track, Target t) {
        Point pn = track.getLast();
        Point pn_1 = track.get(track.size() - 2);

        if (pn_1.x() > t.lr().x()) {
            return -1;
        }
        if (pn.x() < t.ul().x()) {
            return 1;
        }
        if (pn.x() == pn_1.x() && pn_1.y() > t.ul().y() && pn.y() < t.lr().y()) {
            return 0;
        }

        throw new UnsupportedOperationException("not finished yet: " + t + "; " + track);
    }

}
