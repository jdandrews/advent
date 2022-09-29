package advent.y2016;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Day11 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int FLOORS = 4;
    /*
     * part 1:
     *
    The first floor contains
    	a thulium generator,				TmG
    	a thulium-compatible microchip,		TmM
    	a ruthenium generator,				RuG
    	a ruthenium-compatible microchip,	RuM
    	a cobalt generator, and				CoG
    	a cobalt-compatible microchip.		CoM
    	a polonium generator				PoG
    	a promethium generator,				PrG
    The second floor contains
    	a polonium-compatible microchip and PoM
    	a promethium-compatible microchip.	PrM
    The third floor contains nothing relevant.
    The fourth floor contains nothing relevant.
    *
    * part 2: add
    *
    The first floor contains
    	a elerium generator,				ElG
    	a elerium-compatible microchip,		ElM
    	a dilithium generator,				DiG
    	a dilithium-compatible microchip,	DiM
    */

    private List<ToggleButton> buttons = new ArrayList<>();
    Map<ToggleButton, Integer> rows = new HashMap<>();

    // Thulium
    private ToggleButton TmG = new ToggleButton("TmG");
    private ToggleButton TmM = new ToggleButton("TmM");
    {
        TmG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(TmG);
        buttons.add(TmM);
        rows.put(TmG, 0);
        rows.put(TmM, 0);
    }

    // Ruthenium
    private ToggleButton RuG = new ToggleButton("RuG");
    private ToggleButton RuM = new ToggleButton("RuM");
    {
        RuG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(RuG);
        buttons.add(RuM);
        rows.put(RuG, 0);
        rows.put(RuM, 0);
    }

    // Cobalt
    private ToggleButton CoG = new ToggleButton("CoG");
    private ToggleButton CoM = new ToggleButton("CoM");
    {
    	CoG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(CoG);
        buttons.add(CoM);
        rows.put(CoG, 0);
        rows.put(CoM, 0);
    }

    // Polonium
    private ToggleButton PoG = new ToggleButton("PoG");
    private ToggleButton PoM = new ToggleButton("PoM");
    {
    	PoG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(PoG);
        buttons.add(PoM);
        rows.put(PoG, 0);
        rows.put(PoM, 1);
    }

    // Promethium
    private ToggleButton PrG = new ToggleButton("PrG");
    private ToggleButton PrM = new ToggleButton("PrM");
    {
    	PrG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(PrG);
        buttons.add(PrM);
        rows.put(PrG, 0);
        rows.put(PrM, 1);
    }

    // part 2:
    // Elerium
    private ToggleButton ElG = new ToggleButton("ElG");
    private ToggleButton ElM = new ToggleButton("ElM");
    {
    	ElG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(ElG);
        buttons.add(ElM);
        rows.put(ElG, 0);
        rows.put(ElM, 0);
    }

    // Dilithium
    private ToggleButton DiG = new ToggleButton("DiG");
    private ToggleButton DiM = new ToggleButton("DiM");
    {
    	DiG.setStyle("-fx-base: #b6e7c9;");
        buttons.add(DiG);
        buttons.add(DiM);
        rows.put(DiG, 0);
        rows.put(DiM, 0);
    }

    private Button up = new Button("up");
    private Button dn = new Button("dn");
    private Button car = new Button("car");
    {
        up.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleUp);
        dn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleDn);
    }

    private Label countLabel = new Label("count = ");
    private int count = 0;

    private void handleUp(MouseEvent e) {
    	System.out.println(e);
    	boolean carMoved = false;
        for (ToggleButton button : buttons) {
            if (button.isSelected()) {
                button.setSelected(false);
                GridPane.setRowIndex(button, GridPane.getRowIndex(button)-1);
                carMoved = true;
            }
        }

        if (carMoved) {
        	countLabel.setText("count = "+ ++count);
        	GridPane.setRowIndex(car,  GridPane.getRowIndex(car)-1);
        }
    }

    private void handleDn(MouseEvent e) {
    	System.out.println(e);
    	boolean carMoved = false;
        for (ToggleButton button : buttons) {
            if (button.isSelected()) {
                button.setSelected(false);
                GridPane.setRowIndex(button, GridPane.getRowIndex(button)+1);
                carMoved = true;
            }
        }

        if (carMoved) {
            countLabel.setText("count = "+ ++count);
        	GridPane.setRowIndex(car,  GridPane.getRowIndex(car)+1);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();
        int col = 2;
        for (ToggleButton button : buttons) {
            root.add(button, col++, 3-rows.get(button));
        }
        for (int i=0; i<FLOORS; ++i) {
            root.add(new Label(i+""), 0, FLOORS-i);
        }
        root.add(dn, 0, FLOORS);
        root.add(up, 0, 0);
        root.add(car, 1, FLOORS-1);
        root.add(countLabel, 1, FLOORS, 4, 1);
        countLabel.setText("count = "+count);
        primaryStage.setTitle("Day 11");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
