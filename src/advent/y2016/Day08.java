package advent.y2016;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

public class Day08 extends Application {
    private static final int PER_FRAME_MS_DELAY = 50;
    private static final int COLS = 50;
    private static final int ROWS = 6;
    private static final Queue<String[]> commands = new LinkedList<>();

    private GraphicsContext gc;

    private static boolean[][] screen = new boolean[ROWS][];
    static {
        for (int i=0; i<ROWS; ++i) {
            screen[i] = new boolean[COLS];
        }
    }

    private enum Command {
        RECT("rect "), ROTATE_ROW_RIGHT("rotate row y="), ROTATE_COL_DOWN("rotate column x=");

        private String commandString;
        private Command(String cmdString) {
            commandString = cmdString;
        }

        public String getCommand() {
            return commandString;
        }

        public static Command commandFor(String string) {
            for (Command cmd : Command.values()) {
                if (string.startsWith(cmd.getCommand())) {
                    return cmd;
                }
            }
            throw new IllegalArgumentException("Unknown command string: "+string);
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> commandStrings = Util.readInput("2016", "Day08.txt");
        for (String commandString : commandStrings) {
            String[] command = parseCommand(commandString);
            try {
                commands.add(command);
            } catch (NumberFormatException e) {
                Util.log("NumberFormatException parsing command %s: %s",commandString,e.getMessage());
            }
        }
        launch(args);
    }

    private static String[] parseCommand(String cmd) {
        String[] result = new String[3];
        Command command = Command.commandFor(cmd);
        switch (command) {
        case RECT:
            int n0 = cmd.indexOf('x', command.getCommand().length());
            result[1] = cmd.substring(command.getCommand().length(), n0).trim();
            result[2] = cmd.substring(n0+1).trim();
            break;
        case ROTATE_COL_DOWN:
        case ROTATE_ROW_RIGHT:
            n0 = cmd.indexOf(" by ", command.getCommand().length());
            result[1] = cmd.substring(command.getCommand().length(), n0).trim();
            result[2] = cmd.substring(n0+4).trim();
            break;
        default:
            throw new IllegalArgumentException("unhandled command: "+cmd);
        }
        result[0] = command.getCommand();
        return result;
    }

    private void executeCommand(String cmd, int a, int b) {
        Command command = Command.commandFor(cmd);
        // Util.log("%s %d %d",command.getCommand(), A, B); // NOSONAR
        switch (command) {
        case RECT:
            drawRect(a, b);
            break;
        case ROTATE_COL_DOWN:
            rotateColumnDown(a, b);
            break;
        case ROTATE_ROW_RIGHT:
            rotateRowRight(a, b);
            break;
        default:
            throw new IllegalArgumentException("unhandled command: "+cmd);
        }
    }

    private static void rotateRowRight(int row, int count) {
        for (int n=0; n<count; ++n) {
            boolean carry = screen[row][COLS-1];
            for (int j=COLS-1; j>0; --j) {
                screen[row][j] = screen[row][j-1];
            }
            screen[row][0] = carry;
        }
    }

    private static void rotateColumnDown(int col, int count) {
        for (int n=0; n<count; ++n) {
            boolean carry = screen[ROWS-1][col];
            for (int i=ROWS-1; i>0; --i) {
                screen[i][col] = screen[i-1][col];
            }
            screen[0][col] = carry;
        }
    }

    private static void drawRect(int width, int height) {
        for (int i=0; i<ROWS && i<height; ++i) {
            for (int j=0; j<COLS && j<width; ++j) {
                screen[i][j] = true;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Day08");
        Group root = new Group();
        Canvas canvas = new Canvas(505, 65);
        gc = canvas.getGraphicsContext2D();
        drawShapes();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(PER_FRAME_MS_DELAY), ae -> tick()));
        timeline.setCycleCount(commands.size()+1);
        timeline.play();
    }

    private KeyValue[] tick() {
        String[] command = commands.poll();
        if (command!=null) {
            executeCommand(command[0], Integer.parseInt(command[1]), Integer.parseInt(command[2]));
            drawShapes();
        } else {
            int count = 0;
            for (int i=0; i<ROWS; ++i) {
                for (int j=0; j<COLS; ++j) {
                    count += screen[i][j] ? 1 : 0;
                }
            }
            Util.log("found %d pixels on",count);
        }
        return new KeyValue[0];
    }

    private void drawShapes() {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);

        for (int i=0; i<ROWS; ++i) {
            for (int j=0; j<COLS; ++j) {
                double y = i*10+2.0;
                double x = j*10+2.0;
                double w = 9;
                double h = 9;
                double r = 2;
                if (screen[i][j]) {
                    gc.clearRect    (x, y, w, h);
                    gc.fillRoundRect(x, y, w, h, r, r);
                } else {
                    gc.clearRect      (x, y, w, h);
                }
            }
        }
    }
}
