import java.util.ArrayList;

/**
 * Created by adrianzgaljic on 08/12/15.
 */
public class GPath {



    public ArrayList<Line> getLines() {
        return lines;
    }

    public GPath(){};

    public GPath(ArrayList<Line> lines){
        this.lines = lines;
    };

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line){
        lines.add(line);
    }

    private ArrayList<Line> lines = new ArrayList<Line>();

    public boolean exists(Line line){

        for (Line l:lines){
            if (line.equals(l)){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        String str = "";
        if (lines.size()>0){
            for (int i=0; i<lines.size()-1; i++){
                str = str + lines.get(i)+"....";
            }
            str = str + lines.get(lines.size()-1);

        }
        return str;
    }
}
