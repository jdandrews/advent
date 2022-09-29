package advent.y2015;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

public class Day06 extends Application {
    private static final int PER_FRAME_MS_DELAY = 200;
    private static final int COLS = 1000;
    private static final int ROWS = 1000;
    private GraphicsContext gc;

    private enum Action {
        TOGGLE,
        OFF,
        ON;
    }

    private static class Command {
        public Action action;
        public Point topLeft;
        public Point bottomRight;
    }

    private static boolean[][] lights = new boolean[ROWS][];
    private static int[][] lights2 = new int[ROWS][];
    static {
        for (int i=0; i<ROWS; ++i) {
            lights[i] = new boolean[COLS];
            lights2[i] = new int[COLS];
            for (int j=0; j<COLS; ++j) {
                lights[i][j] = false;
                lights2[i][j] = 0;
            }
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Day06");
        Group root = new Group();
        Canvas canvas = new Canvas(1050, 1050);
        gc = canvas.getGraphicsContext2D();
        drawShapes();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        commandIterator = commands.iterator();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(PER_FRAME_MS_DELAY), ae -> tick()));
        timeline.setCycleCount(commands.size()+1);
        timeline.play();
    }

    public static void main(String[] args) throws IOException {
        List<String> data = Util.readInput("2015", "Day06.txt");
        commands = parse(data);
    
        launch(args);
    }

    private static List<Command> commands;
    private static Iterator<Command> commandIterator;
    
    private KeyValue[] tick() {
        if (commandIterator.hasNext()) {
            Command command = commandIterator.next();
            executeCommand(command);
            drawShapes();
        } else {
            int n=0;
            int lumens = 0;
            for (int i=0; i<1000; ++i) {
                for (int j=0; j<1000; ++j) {
                    if (lights[i][j]) {
                        ++n;
                    }
                    lumens += lights2[i][j];
                }
            }
        
            Util.log("%d lights are on", n);
            Util.log("%d lumens", lumens);
        }
        return new KeyValue[0];
    }

    private void drawShapes() {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

        for (int i=0; i<ROWS; ++i) {
            for (int j=0; j<COLS; ++j) {
                double y = i+2.0;
                double x = j+2.0;
                double w = 1;
                double h = 1;
                if (lights[i][j]) {
                    gc.fillRect(x, y, w, h);
                } else {
                    gc.clearRect      (x, y, w, h);
                }
            }
        }
    }

    private static void executeCommand(Command command) {
        for (int i=command.topLeft.x; i<=command.bottomRight.x; ++i) {
            for (int j=command.topLeft.y; j<=command.bottomRight.y; ++j) {
                switch(command.action) {
                case OFF:
                    lights[i][j] = false;
                    lights2[i][j] = Math.max(0, lights2[i][j]-1);
                    break;
                case ON:
                    lights[i][j] = true;
                    ++lights2[i][j];
                    break;
                case TOGGLE:
                    lights[i][j] = ! lights[i][j];
                    ++lights2[i][j];
                    ++lights2[i][j];
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown action: "+command.action);
                }
            }
        }
    }

    private static List<Command> parse(List<String> data) {
        List<Command> commands = new ArrayList<>();
        for (String s : data) {
            Command cmd = new Command();
            String rect;
            if (s.startsWith("toggle")) {
                cmd.action = Action.TOGGLE;
                rect = s.substring("toggle ".length());
            } else if (s.startsWith("turn off")) {
                cmd.action = Action.OFF;
                rect = s.substring("turn off ".length());
            } else { // (s.startsWith("turn on")) {
                cmd.action = Action.ON;
                rect = s.substring("turn on ".length());
            }
            
            String[] pts = rect.split(" ");

            String[] coordsTL = pts[0].split(",");
            cmd.topLeft = new Point(Integer.parseInt(coordsTL[0]), Integer.parseInt(coordsTL[1]));

            String[] coordsBR = pts[2].split(",");
            cmd.bottomRight = new Point(Integer.parseInt(coordsBR[0]), Integer.parseInt(coordsBR[1]));

            commands.add(cmd);
        }
        return commands;
    }
}
