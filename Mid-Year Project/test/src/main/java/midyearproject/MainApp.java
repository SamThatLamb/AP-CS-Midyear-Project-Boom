package midyearproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainApp extends Application {

    // The duration of the timer in seconds
    private static final int TIMER_DURATION = 60;

    // The maximum number of tries the user has for each puzzle
    private static final int MAX_TRIES = 3;

    // The current puzzle being attempted
    private int currentPuzzle = 0;

    // The current try number for the current puzzle
    private int currentTry = 1;

    // The timer label that displays the remaining time
    private Label timerLabel;

    // The timer that counts down the remaining time
    private int remainingTime;

    // The list of puzzles in the game
    private List<Puzzle> puzzles;

    // The grid pane that contains the buttons and puzzles
    private GridPane gridPane;

    // The list of buttons in the game
    private List<Button> buttons;

    // The button used to start the game
    private Button startButton;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create the timer label and set the initial remaining time
        timerLabel = new Label("Time left: " + TIMER_DURATION + " seconds");
        remainingTime = TIMER_DURATION;

        // Create the puzzles and add them to the list
        Puzzle simon = new SimonPuzzle();
        Puzzle wireCutting = new WirePuzzle();
        Puzzle keypad = new KeypadPuzzle();
        puzzles = Arrays.asList(simon, wireCutting, keypad);

        // Create the grid pane and add the buttons and puzzles to it
        gridPane = new GridPane();
        buttons = new ArrayList<>();
        // addButton(new ToggleButton("Toggle 1", false), 0, 0);
        // addButton(new ToggleButton("Toggle 2", true), 1, 0);
        // addButton(new KeypadButton("Enter", "123"), 0, 1);
        // addButton(new WireButton("Wire 1", Color.RED, true), 1, 1);
        // addButton(new WireButton("Wire 2", Color.GREEN, false), 2, 1);
        addPuzzle(simon);
        addPuzzle(wireCutting);
        addPuzzle(keypad);

        // Create the start button
        startButton = new Button("Start");
        startButton.setOnAction(e -> startGame());

        // Add the timer label, grid pane, and start button to the scene
        Scene scene = new Scene(new GridPane(), 800, 600);
        ((GridPane) scene.getRoot()).add(timerLabel, 0, 0);
        ((GridPane) scene.getRoot()).add(gridPane, 0, 1);
        ((GridPane) scene.getRoot()).add(startButton, 0, 2);

        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Adds a button to the grid pane
    private void addButton(Button button, int column, int row) {
        gridPane.add(button, column, row);
        buttons.add(button);
    }

    // Adds a puzzle to the grid pane
    private void addPuzzle(Puzzle puzzle) {
        gridPane.add(puzzle.getNode(), 0, 2);
        puzzle.setListener(new PuzzleListener() {
            @Override
            public void onSuccess() {
                // The puzzle was successfully solved
                remainingTime += 10;
                nextPuzzle();
            }

            @Override
            public void onFailure() {
                // The puzzle was not solved
                if (currentTry >= MAX_TRIES) {
                    // The player has no more tries left, so they lose the game
                    showAlert("You failed to defuse the bomb!");
                    resetGame();
                } else {
                    // The player can try again
                    currentTry++;
                    showAlert("Incorrect. You have " + (MAX_TRIES - currentTry + 1) + " tries left.");
                }
            }
        });
    }

    // Starts the game
    private void startGame() {
        startButton.setDisable(true);
        startTimer();
        nextPuzzle();
    }

    // Starts the timer
    private void startTimer() {
        new Thread(() -> {
            while (remainingTime > 0) {
                remainingTime--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timerLabel.setText("Time left: " + remainingTime + " seconds");
            }
            showAlert("Time's up! You failed to defuse the bomb.");
            resetGame();
        }).start();
    }

    // Shows an alert dialog with the specified message
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Goes to the next puzzle in the list
    private void nextPuzzle() {
        currentPuzzle++;
        currentTry = 1;
        if (currentPuzzle >= puzzles.size()) {
            // The player has defused all the bombs, so they win the game
            showAlert("Congratulations! You have successfully defused the bomb.");
            resetGame();
        } else {
            // Show the next puzzle
            Puzzle puzzle = puzzles.get(currentPuzzle);
            gridPane.getChildren().remove(puzzle.getNode());
            gridPane.add(puzzle.getNode(), 0, 2);
            puzzle.start();
        }
    }

    // Resets the game
    private void resetGame() {
        remainingTime = TIMER_DURATION;
        timerLabel.setText("Time left: " + remainingTime + " seconds");
        currentPuzzle = -1;
        currentTry = 1;
        for (Button button : buttons) {
            button.setDisable(false);
        }
        startButton.setDisable(false);
        nextPuzzle();
    }

    // Entry point for the application
    public static void main(String[] args) {
        launch(args);
    }

    // The interface for a puzzle
    private interface Puzzle {
        Node getNode();

        void setListener(PuzzleListener listener);

        void start();
    }

    // The listener interface for a puzzle
    private interface PuzzleListener {
        void onSuccess();

        void onFailure();
    }

    // The Simon puzzle
    private class SimonPuzzle implements Puzzle {
        private static final int SEQUENCE_LENGTH = 5;
        private static final int BUTTON_COUNT = 4;

        private Button[] buttons;
        private List<Integer> sequence;
        private int currentIndex;
        private boolean inputEnabled;
        private PuzzleListener listener;

        public SimonPuzzle() {
            buttons = new Button[BUTTON_COUNT];
            for (int i = 0; i < BUTTON_COUNT; i++) {
                int buttonIndex = i + 1;
                buttons[i] = new Button("Button " + (i + 1));
                buttons[i].setOnAction(e -> {
                    if (inputEnabled) {
                        // If the button pressed is the correct one
                        if (buttonIndex == sequence.get(currentIndex)) {
                            currentIndex++;
                            if (currentIndex >= sequence.size()) {
                                // The player successfully solved the puzzle
                                inputEnabled = false;
                                listener.onSuccess();
                            }
                        } else {
                            // The player pressed the wrong button, they lose the puzzle
                            inputEnabled = false;
                            listener.onFailure();
                        }
                    }
                });
            }
        }

        @Override
        public Node getNode() {
            // Create a VBox to hold the buttons
            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(20));
            for (Button button : buttons) {
                vBox.getChildren().add(button);
            }
            return vBox;
        }

        @Override
        public void setListener(PuzzleListener listener) {
            this.listener = listener;
        }

        @Override
        public void start() {
            // Generate a random sequence
            sequence = new ArrayList<>();
            for (int i = 0; i < SEQUENCE_LENGTH; i++) {
                sequence.add((int) (Math.random() * BUTTON_COUNT));
            }
            currentIndex = 0;
            inputEnabled = false;

            // Show the sequence to the player
            new Thread(() -> {
                try {
                    for (int i = 0; i < sequence.size(); i++) {
                        int buttonIndex = sequence.get(i);
                        Button button = buttons[buttonIndex];
                        button.setStyle("-fx-background-color: #00ff00");
                        Thread.sleep(500);
                        button.setStyle("-fx-background-color: #808080");
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Enable input from the player
                inputEnabled = true;
            }).start();
        }
    }

    // The wire cutting puzzle
    private class WirePuzzle implements Puzzle {
        private static final int WIRE_COUNT = 5;
        private static final int CORRECT_WIRE_COUNT = 2;

        private Button[] wires;
        private boolean[] cutWires;
        private int cutWireCount;
        private PuzzleListener listener;

        public WirePuzzle() {
            wires = new Button[WIRE_COUNT];
            cutWires = new boolean[WIRE_COUNT];
            for (int i = 0; i < WIRE_COUNT; i++) {
                wires[i] = new Button("Wire " + (i + 1));
                wires[i].setOnAction(e -> {
                    int wireIndex = getButtonIndex(e.getSource(), wires);
                    if (!cutWires[wireIndex]) {
                        cutWires[wireIndex] = true;
                        wires[wireIndex].setStyle("-fx-background-color: #ff0000");
                        cutWireCount++;
                        if (cutWireCount >= CORRECT_WIRE_COUNT) {
                            // The player successfully solved the puzzle
                            listener.onSuccess();
                        }
                    }
                });
            }
        }

        @Override
        public Node getNode() {
            // Create a HBox to hold the wires
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPadding(new Insets(20));
            for (Button wire : wires) {
                hBox.getChildren().add(wire);
            }
            return hBox;
        }

        @Override
        public void setListener(PuzzleListener listener) {
            this.listener = listener;
        }

        @Override
        public void start() {
            cutWireCount = 0;
            Arrays.fill(cutWires, false);
        }
    }

    // The keypad puzzle
    private class KeypadPuzzle implements Puzzle {
        private static final int DIGIT_COUNT = 4;
        private int[] CORRECT_DIGITS = { 2, 4, 6, 8 };

        private Button[] keypadButtons;
        private List<Integer> enteredDigits;
        private int currentDigitIndex;
        private PuzzleListener listener;

        public KeypadPuzzle() {
            keypadButtons = new Button[DIGIT_COUNT * 3];
            enteredDigits = new ArrayList<>();
            currentDigitIndex = 0;
            for (int i = 0; i < DIGIT_COUNT * 3; i++) {
                Button button;
                if (i % 3 == 2) {
                    // Create a spacer button to separate digits on the keypad
                    button = new Button("");
                    button.setDisable(true);
                } else {
                    // Create a digit button
                    int digit = i / 3 + 1;
                    button = new Button(String.valueOf(digit));
                    button.setOnAction(e -> {
                        int buttonIndex = getButtonIndex(e.getSource(), keypadButtons);
                        int digitPressed = buttonIndex / 3 + 1;
                        enteredDigits.add(digitPressed);
                        currentDigitIndex++;
                        if (currentDigitIndex >= DIGIT_COUNT) {
                            // The player entered all the digits, check if they are correct
                            boolean correct = true;
                            for (int j = 0; j < DIGIT_COUNT; j++) {
                                if (enteredDigits.get(j) != CORRECT_DIGITS[j]) {
                                    correct = false;
                                    break;
                                }
                            }
                            if (correct) {
                                // The player successfully solved the puzzle
                                listener.onSuccess();
                            } else {
                                // The player entered the wrong digits, they lose the puzzle
                                listener.onFailure();
                            }
                        }
                    });
                }
                keypadButtons[i] = button;
            }
        }

        @Override
        public Node getNode() {
            // Create a GridPane to hold the keypad buttons
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(20));
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            for (int i = 0; i < DIGIT_COUNT * 3; i++) {
                int row = i / 3;
                int col = i % 3;
                gridPane.add(keypadButtons[i], col, row);
            }
            return gridPane;
        }

        @Override
        public void setListener(PuzzleListener listener) {
            this.listener = listener;
        }

        @Override
        public void start() {
            currentDigitIndex = 0;
            enteredDigits.clear();
        }
    }

    // Helper method to get the index of a button in an array
    private int getButtonIndex(Object button, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == button) {
                return i;
            }
        }
        return -1;
    }
}
