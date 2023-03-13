package midyearproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.Arrays;
import java.util.List;

public class BOOM extends Application {
    private static final int TIMER = 60;
    private static final int MAX_TRIES = 3;
    private int currentPuzzle = -1;
    private int currentTry = 1;
    private Label timerLabel;
    private int remainingTime;
    private List<Puzzle> puzzles;
    private Button startButton;
    private GridPane gridPane = new GridPane();
    private Scene scene;

    public void start(Stage stage) throws Exception {
        remainingTime = TIMER;
        timerLabel = new Label("Time left: " + remainingTime + " seconds");

        Puzzle simon = new SimonPuzzle();
        Puzzle wireCutting = new WirePuzzle();
        Puzzle keypad = new KeypadPuzzle();
        puzzles = Arrays.asList(keypad, wireCutting, simon);

        startButton = new Button("Start");
        startButton.setOnAction(e -> startGame());
        scene = new Scene(new GridPane(), 800, 600);
        ((GridPane) scene.getRoot()).add(timerLabel, 0, 0);
        ((GridPane) scene.getRoot()).add(gridPane, 0, 1);
        ((GridPane) scene.getRoot()).add(startButton, 0, 2);

        stage.setScene(scene);
        stage.show();
    }

    private void addPuzzle(Puzzle puzzle) {
        gridPane.add(puzzle.setPuzzle(), 0, 2);
        puzzle.setPuzzleChecker(new PuzzleChecker() {
            public void success() {
                remainingTime += 10;
                nextPuzzle();
            }

            public void failure() {
                if (currentTry >= MAX_TRIES) {
                    showAlert("You failed to defuse the bomb!");
                    resetGame();
                } else {
                    currentTry++;
                    showAlert("Incorrect. You have " + (MAX_TRIES - currentTry + 1) + " tries left.");
                }
            }
        });
    }

    private void startGame() {
        startButton.setDisable(true);
        startTimer();
        nextPuzzle();
    }

    private void startTimer() {
        new Thread(() -> {
            while (remainingTime > 0) {
                remainingTime--;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timerLabel.setText("Time left: " + remainingTime + " seconds");
                ((GridPane) scene.getRoot()).add(timerLabel, 0, 0);
            }
            showAlert("Time's up! You failed to defuse the bomb.");
            resetGame();
        }).start();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void nextPuzzle() {
        currentPuzzle++;
        currentTry = 1;
        if (currentPuzzle >= puzzles.size()) {
            showAlert("Congratulations! You have successfully defused the bomb.");
            resetGame();
        } else {
            Puzzle puzzle = puzzles.get(currentPuzzle);
            gridPane.getChildren().remove(puzzle.setPuzzle());
            gridPane.add(puzzle.setPuzzle(), 0, 2);
            puzzle.puzzle();
        }
    }

    private void resetGame() {
        remainingTime = TIMER;
        timerLabel.setText("Time left: " + remainingTime + " seconds");
        currentPuzzle = -1;
        currentTry = 1;
        startButton.setDisable(false);
        nextPuzzle();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
