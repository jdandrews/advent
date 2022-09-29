package advent.y2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Day18 extends Application {
    private static boolean limit = false; // false to run life for a long time; true to solve the problem
    
    private static final int PER_FRAME_MS_DELAY = limit ? 200 : 150;
    private static final int PIXEL_X = 7;
    private static final int PIXEL_Y = 7;
    private static final float BORDER = 2;
    private GraphicsContext gc;
    private static boolean[][] lights;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Day18");

        Group root = new Group();

        Canvas canvas = new Canvas(100 * PIXEL_X + 2*BORDER, 100 * PIXEL_Y + 2*BORDER);
        gc = canvas.getGraphicsContext2D();
        drawShapes();
        root.getChildren().add(canvas);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(PER_FRAME_MS_DELAY), ae -> tick()));
        timeline.setCycleCount(100 * (limit ? 1 : 100));
        timeline.play();
    }

    public static void main(String[] args) throws IOException {
        boolean[][] sample;
        List<String> sampleData = new ArrayList<String>() {{
            add(".#.#.#");
            add("...##.");
            add("#....#");
            add("..#...");
            add("#.#..#");
            add("####..");
        }};
        sample = parse(sampleData);
        lightCorners(sample);
        for (int i=0; i<5; ++i) {
            Util.log("%s", toString(sample));
            step(sample);
            countLit(sample);
        }
        Util.log("%s", toString(sample));

        List<String> data = Util.readInput("2015", "Day18.txt");
        lights = parse(data);
        lightCorners(lights);
    
        launch(args);
    }

    private static String toString(boolean[][] data) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<data.length; ++i) {
            for (int j=0; j<data[i].length; ++j) {
                result.append(data[i][j] ? '#' : '.');
            }
            result.append('\n');
        }

        return result.toString();
    }

    private static void step(boolean[][] data) {
        boolean[][] result = new boolean[data.length][];

        for (int i=0; i<data.length; ++i) {
            result[i] = new boolean[data[i].length];
            for (int j=0; j<data[i].length; ++j) {
                int n = countNeighbors(data, i, j);
                if (data[i][j]) {
                    result[i][j] = (n==2 || n==3) ? true : false;
                }
                else {
                    result[i][j] = (n==3) ? true : false;
                }
            }
        }
        for (int i=0; i<data.length; ++i) {
            for (int j=0; j<data[i].length; ++j) {
                data[i][j] = result[i][j];
                if (result[i][j]) {
                }
            }
        }
        lightCorners(data);
    }

    private static void lightCorners(boolean[][] data) {
        if (! limit) {
            return;
        }
        data[0][0] = true;
        data[data.length-1][0] = true;
        data[0][data[0].length-1] = true;
        data[data.length-1][data[data.length-1].length-1] = true;
    }

    private static int countNeighbors(boolean[][] data, int i, int j) {
        int result = 0;
        for (int col = i-1; col<=i+1; ++col) {
            for (int row = j-1; row<=j+1; ++row) {
                if ( col==i && row==j ) continue;
                if (       row >= 0 
                        && col >= 0 
                        && col < data.length 
                        && row < data[col].length
                        && data[col][row]) {
                    ++result;
                }
            }
        }
        return result;
    }

    private static boolean[][] parse(List<String> data) {
        boolean[][] result = new boolean[data.size()][];
        for (int i=0; i < data.size(); ++i) {
            char[] datum = data.get(i).toCharArray();
            result[i] = new boolean[datum.length];
            for (int j=0; j<datum.length; ++j) {
                switch (datum[j]) {
                case '#':
                    result[i][j] = true; 
                    break;

                case '.':
                    result[i][j] = false;
                    break;

                default: 
                    throw new UnsupportedOperationException(String.format("can't map character %s", datum[j]));
                }
            }
        }
        return result;
    }

    private KeyValue[] tick() {
        step(lights);
        countLit(lights);
        drawShapes();
        return new KeyValue[0];
    }

    private static void countLit(boolean data[][]) {
        if (! limit) {
            return;
        }
        int nLit = 0;
        for (int i=0; i<data.length; ++i) {
            for (int j=0; j<data[i].length; ++j) {
                if (data[i][j]) {
                    ++nLit;
                }
            }
        }
        Util.log("%d lit", nLit);
    }

    private void drawShapes() {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);

        for (int i=0; i<lights.length; ++i) {
            for (int j=0; j<lights[i].length; ++j) {
                double y = i*PIXEL_X+BORDER;
                double x = j*PIXEL_Y+BORDER;
                double w = PIXEL_X-1;
                double h = PIXEL_Y-1;
                double r = 2;
                if (lights[i][j]) {
                    gc.clearRect    (x, y, w, h);
                    gc.fillRoundRect(x, y, w, h, r, r);
                } else {
                    gc.clearRect      (x, y, w, h);
                }
            }
        }
    }
}
