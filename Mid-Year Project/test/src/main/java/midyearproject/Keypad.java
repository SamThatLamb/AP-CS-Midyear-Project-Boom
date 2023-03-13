package midyearproject;

import javafx.scene.control.Button;

public class Keypad extends Button{
    private int digit;
    
    public Keypad(Integer d) {
        super(d.toString());
        digit = d;
    }  

    public int getDigit() {
        return digit;
    }
}
