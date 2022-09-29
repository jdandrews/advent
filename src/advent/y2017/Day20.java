package advent.y2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.Util;

public class Day20 {

    private static class Particle {

        private int particleId;

        private long x;
        private long y;
        private long z;

        private long dx;
        private long dy;
        private long dz;

        private long ddx;
        private long ddy;
        private long ddz;

        public Particle(int id, String dataString) {
            particleId = id;

            String[] dataBlocks = dataString.split(", ");
            if (dataBlocks.length != 3) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }

            if (!dataBlocks[0].startsWith("p=<")) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }
            String[] position = dataBlocks[0].substring("p=<".length(), dataBlocks[0].length() - 1).split(",");
            if (position.length != 3) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }
            x = Long.parseLong(position[0].trim());
            y = Long.parseLong(position[1].trim());
            z = Long.parseLong(position[2].trim());

            if (!dataBlocks[1].startsWith("v=<")) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }
            String[] velocity = dataBlocks[1].substring("v=<".length(), dataBlocks[1].length() - 1).split(",");
            if (velocity.length != 3) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }
            dx = Long.parseLong(velocity[0].trim());
            dy = Long.parseLong(velocity[1].trim());
            dz = Long.parseLong(velocity[2].trim());

            if (!dataBlocks[2].startsWith("a=<")) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }
            String[] acceleration = dataBlocks[2].substring("a=<".length(), dataBlocks[2].length() - 1).split(",");
            if (acceleration.length != 3) {
                throw new UnsupportedOperationException("can't parse " + dataString);
            }
            ddx = Long.parseLong(acceleration[0].trim());
            ddy = Long.parseLong(acceleration[1].trim());
            ddz = Long.parseLong(acceleration[2].trim());
        }

        @Override
        public String toString() {
            return String.format("%d p=<%d,%d,%d>, v=<%d,%d,%d>, a=<%d,%d,%d>", particleId, x, y, z, dx, dy, dz, ddx, ddy, ddz);
        }

        public void tick() {
            // Increase the X velocity by the X acceleration.
            dx += ddx;
            // Increase the Y velocity by the Y acceleration.
            dy += ddy;
            // Increase the Z velocity by the Z acceleration.
            dz += ddz;
            // Increase the X position by the X velocity.
            x += dx;
            // Increase the Y position by the Y velocity.
            y += dy;
            // Increase the Z position by the Z velocity.
            z += dz;
        }

        public long getA() {
            return Math.abs(ddx) + Math.abs(ddy) + Math.abs(ddz);
        }

        public boolean hit(Particle p) {
            return p.x == x && p.y == y && p.z == z;
        }
    }

    public static void main(String[] args) {
        List<Particle> testData = new ArrayList<>();
        testData.add(new Particle(0, "p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>"));
        testData.add(new Particle(1, "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>"));
        Particle closeToHome = leastAcceleration(testData);
        Util.log("%s stayed closest to home; %d particles left", closeToHome, resolveCollisions(testData));

        testData.clear();
        testData.add(new Particle(0, "p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>"));
        testData.add(new Particle(1, "p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>"));
        testData.add(new Particle(2, "p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>"));
        testData.add(new Particle(3, "p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>"));
        Util.log("%s stayed closest to home; %d particles left", closeToHome, resolveCollisions(testData));

        List<Particle> swarm = loadSwarm();
        closeToHome = leastAcceleration(swarm);
        Util.log("%s stayed closest to home; %d particles left", closeToHome, resolveCollisions(swarm));
    }

    private static int resolveCollisions(List<Particle> particles) {
        List<Particle> activeParticles = new ArrayList<>(particles);
        Set<Particle> collisions = new HashSet<>();

        for (int n=0; n<500; ++n) {
            for (Particle particle : activeParticles) {
                particle.tick();
            }

            for (int i=0; i<activeParticles.size(); ++i) {
                Particle p0 = activeParticles.get(i);
                for (int j=i+1; j<activeParticles.size(); ++j) {
                    Particle p1 = activeParticles.get(j);
                    if (p0.hit(p1)) {
                        collisions.add(p0);
                        collisions.add(p1);
                    }
                }
            }

            activeParticles.removeAll(collisions);
        }

        return activeParticles.size();
    }

    private static Particle leastAcceleration(List<Particle> particles) {
        long minA = Long.MAX_VALUE;
        Particle result = null;
        for (Particle p : particles) {
            if (p.getA() < minA) {
                minA = p.getA();
                result = p;
                Util.log("candidate: %s", p);
            }
        }
        return result;
    }

    private static List<Particle> loadSwarm() {
        List<String> input;
        try {
            input = Util.readInput("2017", "Day20.txt");
        } catch (IOException e) {
            throw new IllegalArgumentException("can't load data", e);
        }

        int id = 0;
        List<Particle> result = new ArrayList<>();
        for (String s : input) {
            if (s.trim().length() > 0) {
                result.add(new Particle(id++, s));
            }
        }
        return result;
    }

}
