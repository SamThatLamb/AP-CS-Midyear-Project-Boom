package midyearproject;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class KeypadPuzzle extends Puzzle {
    private static final int CODE_LENGTH = 4;
    private int[] code;
    private Keypad[] keypad = new Keypad[10];
    private int[] enteredDigits;
    private int currentIndex;
    
    public KeypadPuzzle() {
        code = new int[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = (int) (Math.random() * 10);
        }
        for (int i = 0; i < keypad.length; i++) {
            keypad[i] = new Keypad(i);
            keypad[i].setOnAction(e -> {
                enteredDigits[currentIndex] = ((Keypad) e.getSource()).getDigit();
                currentIndex++;
                if (currentIndex >= CODE_LENGTH) {
                    for (int j = 0; j < CODE_LENGTH; j++) {
                        if (enteredDigits[j] != code[j]) {
                            puzzleChecker.failure();
                        } else {
                            puzzleChecker.success();
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
        for (int i = 0; i < 10; i++) {
            int row = i / 3;
            int col = i % 3;
            gridPane.add(keypad[i], col, row);
        }
        return gridPane;
    }

    public void puzzle() {
        currentIndex = 0;
        enteredDigits = new int[CODE_LENGTH];
    }
}