package midyearproject;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

public class Wire extends Button{
    private static final Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.CYAN};

    private boolean connection = true;
    private int index;
    
    public Wire(int i) {
        super("Wire " + i, new Rectangle(20, 5, COLORS[i % COLORS.length]));
        index = i;
    }

    public void cutWire() {
        if (connection == true) {
            connection = false;
            setGraphic(Rectangle.subtract(getShape(), new Rectangle(((Rectangle) getShape()).getX() + 5, ((Rectangle) getShape()).getY(), ((Rectangle)getShape()).getWidth() - 10, ((Rectangle) getShape()).getHeight())));
        }
    }

    public boolean getConnection() {
        return connection;
    }

    public int getIndex() {
        return index;
    }
}
