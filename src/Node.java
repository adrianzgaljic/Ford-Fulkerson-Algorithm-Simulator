import java.awt.*;

/**
 * Created by adrianzgaljic on 07/12/15.
 */
public class Node {

    private int number;
    private int serial;
    private Color color;

    public Node(int number, int serial, Color color) {
        this.number = number;
        this.serial = serial;
        this.color = color;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }



}
