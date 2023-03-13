package midyearproject;

import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

public class SimonPuzzle extends Puzzle {
    private static final int SEQUENCE_LENGTH = 5;
    private static final int BUTTON_COUNT = 4;

    private SimonButton[] buttons;
    private int[] sequence;
    private int currentIndex;
    private int buttonIndex;

    public SimonPuzzle() {
        buttons = new SimonButton[BUTTON_COUNT];

        for (int i = 0; i < BUTTON_COUNT; i++) {
            buttons[i] = new SimonButton(i);
            buttons[i].setOnAction(e -> {
                if (buttonIndex == sequence[currentIndex]) {
                    currentIndex++;
                    if (currentIndex >= sequence.length) {
                        puzzleChecker.success();
                    }
                } else {
                    puzzleChecker.failure();
                }
            });
        }
    }

    public GridPane setPuzzle() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        for (int i = 0; i < buttons.length; i++) {
            gridPane.add(buttons[i], 0, i);
        }
        return gridPane;
    }

    public void puzzle() {
        sequence = new int[SEQUENCE_LENGTH];

        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
            sequence[i] = (int) (Math.random() * BUTTON_COUNT);
        }
        currentIndex = 0;

        try {
            for (int i = 0; i < sequence.length; i++) {
                buttonIndex = sequence[i];
                buttons[buttonIndex].setStyle("-fx-background-color: #00ff00");
                Thread.sleep(500);
                buttons[buttonIndex].setColor();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
