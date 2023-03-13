package midyearproject;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public abstract class Puzzle {
    public PuzzleChecker puzzleChecker;

    public void setPuzzleChecker(PuzzleChecker puzzleChecker) {
        this.puzzleChecker = puzzleChecker;
    }

    public abstract GridPane setPuzzle(); 

    public abstract void puzzle();
}