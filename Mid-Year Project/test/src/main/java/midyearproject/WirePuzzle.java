package midyearproject;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class WirePuzzle extends Puzzle {
    private static final int WIRE_COUNT = 5;

    private Wire[] wires;
    private ArrayList<Integer> cuttableWires;

    public WirePuzzle() {
        wires = new Wire[WIRE_COUNT];
        for (int i = 0; i < WIRE_COUNT; i++) {
            wires[i] = new Wire(i);
            int wireIndex = wires[i].getIndex();
            wires[i].setOnAction(e -> {
                if (wires[wireIndex].getConnection()) {
                    for (int cut : cuttableWires) {
                        if (cut == wireIndex) {
                            wires[wireIndex].cutWire();
                            cuttableWires.remove(cut);
                            if (cuttableWires.size() == 0) {
                                puzzleChecker.success();
                            }
                        }
                    }
                }
            });
        }
    }

    public GridPane setPuzzle() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        for (int i = 0; i < wires.length; i++) {
            gridPane.add(wires[i], 0, i);
        }
        return gridPane;
    }

    public void puzzle() {
        cuttableWires.add(wires[(int) (Math.random() * wires.length)].getIndex());
        while (cuttableWires.size() < wires.length) {
            Wire temp = wires[(int) (Math.random() * wires.length)];
            if (!cuttableWires.contains(temp)) {
                cuttableWires.add(temp.getIndex());
            }
        }
    }
}