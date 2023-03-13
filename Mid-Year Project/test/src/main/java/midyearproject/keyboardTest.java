package midyearproject;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.HashSet;



public class keyboardTest extends Application {

    static Scene theScene;
    static GraphicsContext gc;
    static int WIDTH = 400;
    static int HEIGHT = 200;
    public static void main(String[] args)

    {
        launch(args);
    }

    static HashSet<String> currentlyActiveKeys;

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Canvas Example");

        Group root = new Group();
        theScene = new Scene(root);
        theStage.setScene(theScene);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        prepareActionHandlers();

        gc = canvas.getGraphicsContext2D();

        loadGraphics(gc);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                tickAndRender();
            }
        }.start();

        theStage.show();
    }

    // loads the stage
    private static void loadGraphics(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 48);
        gc.setFont(theFont);
        gc.fillText("Hello, World!", 60, 50);
        gc.strokeText("Hello, World!", 60, 50);
    }

    // iterates through animation
    private static void tickAndRender()
    {
        // clear canvas
        // gc.clearRect(0, 0, WIDTH, HEIGHT);

        if (currentlyActiveKeys.contains("LEFT"))
        {
            gc.clearRect(0, 0, WIDTH, HEIGHT);
        }
        // else
        // {
        //     gc.drawImage(left, 64 ,64);
        // }

        // if (currentlyActiveKeys.contains("RIGHT"))
        // {
        //     gc.drawImage(rightGreen, 320, 64);
        // }
        // else
        // {
        //     gc.drawImage(right, 320, 64);
        // }
    }

    // sets up keyboard inputs
    private static void prepareActionHandlers()
    {
        // use a set so duplicates are not possible
        currentlyActiveKeys = new HashSet<String>();
        theScene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                currentlyActiveKeys.add(event.getCode().toString());
                System.out.println(event.getCharacter());
            }
        });
        theScene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                currentlyActiveKeys.remove(event.getCode().toString());
                System.out.println(event.getCharacter());
            }
        });
    }

}
