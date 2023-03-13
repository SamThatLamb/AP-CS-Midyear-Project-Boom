package midyearproject;

import javafx.scene.control.Button;

public class SimonButton extends Button{
    private static final String[] colors = {"-fx-background-color: #0000ff", "-fx-background-color: #ff0000", "-fx-background-color: #ffff00", "-fx-background-color: #00ffff", "-fx-background-color: #ff00ff", "-fx-background-color: #9900ff", "-fx-background-color: #ff9900"};
    private int index; 
    
    public SimonButton(int i) {
        super("Button " + i);
        index = i;
        setStyle(colors[i % colors.length]);
    }

    public int getIndex() {
        return index;
    }

    public void setColor() {
        setStyle(colors[index % colors.length]);
    }
}
