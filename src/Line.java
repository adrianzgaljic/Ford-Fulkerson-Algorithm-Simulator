import java.awt.*;

/**
 * Created by adrianzgaljic on 04/12/15.
 */
public class Line {

    private Node start;
    private Node end;
    private int capacity;
    private int flow;
    private boolean direction;
    private Color color;



    public Line(Node start, Node end, int capacity, boolean direction) {
        this.start = start;
        this.end = end;
        this.capacity = capacity;
        this.direction = direction;
        flow = 0;
        color = Color.black;
    }


    public Node getStart() {
        return start;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }



    @Override
    public String toString(){
        if (direction){
            return start.getNumber()+"--"+capacity+"-->"+end.getNumber();
        } else {
            return start.getNumber()+"--"+capacity+"--"+end.getNumber();
        }
    }


    public boolean equals(Line l){
        //System.out.println(this.direction+" "+l.direction+" - "+this.flow+" "+l.flow+" - "+this.capacity+" "+l.capacity);
        if (start.getNumber()==l.start.getNumber() &&
                end.getNumber()==l.end.getNumber() &&
                direction == l.direction &&
                capacity == l.capacity &&
                 flow == l.flow){
            return true;
        } else {
            return false;
        }
    }




}
