package advent.y2017;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day13 {
    private static class Layer {
        private int depth;
        private int location;
        public int direction = 1;

        public Layer(int range) {
            depth = range;
            location = 0;
        }

        public Layer(Layer layer) {
            depth = layer.depth;
            location = layer.location;
            direction = layer.direction;
        }

        public void step() {
            location += direction;
            if (location >= depth) {
                location = depth - 2;
                direction = -direction;
            }
            if (location < 0) {
                location = 1;
                direction = -direction;
            }
        }

        public boolean caught() {
            return location == 0;
        }

        @Override
        public String toString() {
            return "scanning " + location;
        }

        public int getDepth() {
            return depth;
        }

        public void reset() {
            location = 0;
        }
    }

    private static final List<String> TEST_DATA = Arrays.asList("0: 3", "1: 2", "4: 4", "6: 4");
    private static final List<String> INPUT = Arrays.asList(
            "0: 3", "1: 2", "2: 4", "4: 4", "6: 5", "8: 8", "10: 6", "12: 6", "14: 6", "16: 6", "18: 8", "20: 8",
            "22: 12", "24: 10", "26: 9", "28: 8", "30: 8", "32: 12", "34: 12", "36: 12", "38: 12", "40: 8", "42: 12",
            "44: 14", "46: 14", "48: 10", "50: 12", "52: 12", "54: 14", "56: 14", "58: 14", "62: 12", "64: 14",
            "66: 14", "68: 14", "70: 12", "74: 14", "76: 14", "78: 14", "80: 18", "82: 17", "84: 30", "88: 14");

    public static void main(String[] args) {
        Map<Integer, Layer> data = mapLayers(TEST_DATA);
        int score = getScore(data, 0);
        Util.log("TEST: score starting at 0 is %d", score);
        int homeFree = 1;
        while (score != 0) {
            score = getScore(data, homeFree++);
        }
        Util.log("TEST: wait %d to get 0.", homeFree-1);

        data = mapLayers(INPUT);
        score = getScore(data, 0);
        Util.log("INPUT: score starting at 0 is %d", score);
        homeFree = 1;
        while (score != 0) {
            score = getScore(data, homeFree++);
            if (homeFree%1000 == 0) {
                System.out.print(".");
                if (homeFree%100000 == 0)
                    System.out.println(". "+homeFree);
            }
        }
        Util.log("INPUT: wait %d to get 0.", homeFree-1);
    }

    private static int cachedStartAt = 0;
    private static Map<Integer, Layer> cached;

    private static int getScore(Map<Integer, Layer> layerNumberToLayer, int startAt) {
        Map<Integer, Layer> state = layerNumberToLayer;

        int lastLayer = 0;
        for (int layer : state.keySet()) {
            if (layer > lastLayer) {
                lastLayer = layer;
            }
            state.get(layer).reset();
        }

        if (startAt > cachedStartAt) {
            state = cached;
            for (int i = cachedStartAt; i<startAt; ++i) {
                step(state);
            }
            cachedStartAt = startAt;
            cached = copyState(state);
        } else if (startAt == 0) {
            cachedStartAt = startAt;
            cached = copyState(state);
        } else {
            throw new UnsupportedOperationException("didn't code for this case");
        }

        if (layersZeroed(state) && startAt > 0) {
            throw new IllegalStateException("searched the whole space with startAt: "+startAt);
        }

        int score = 0;
        boolean caught = false;
        for (int me = 0; me <= lastLayer; ++me) {
            if (state.containsKey(me) && state.get(me).caught()) {
                int depth = state.get(me).getDepth();
                // Util.log("start: %d, caught at %d; depth is %d; score += %d", startAt, me, depth, depth * me);
                score += depth * me;
                caught = true;
            }
            step(state);
        }
        if (caught && score==0) score = -1;
        return score;
    }

    private static boolean layersZeroed(Map<Integer, Layer> state) {
        for (Layer layer : state.values()) {
            if (layer.location!=0) return false;
        }
        return true;
    }

    private static Map<Integer, Layer> copyState(Map<Integer, Layer> state) {
        Map<Integer, Layer> copy = new HashMap<>();
        for (Map.Entry<Integer, Layer> entry : state.entrySet()) {
            copy.put(entry.getKey(), new Layer(entry.getValue()));
        }
        return copy;
    }

    private static void step(Map<Integer, Layer> layerNumberToLayer) {
        for (Layer layer : layerNumberToLayer.values()) {
            layer.step();
        }
    }

    private static Map<Integer, Layer> mapLayers(List<String> data) {
        Map<Integer, Layer> result = new HashMap<>();
        for (String layerDefinition : data) {
            String[] def = layerDefinition.split(": ");
            result.put(Integer.parseInt(def[0]), new Layer(Integer.parseInt(def[1])));
        }

        return result;
    }
}
