package generators.utils;

import java.util.LinkedList;
import java.util.List;

public class Lake {
    public List<Point> lakePoints = new LinkedList<>();
    public List<LakePass> neighbours = new LinkedList<>();
    public int id;

    public Lake(int id){
        this.id = id;
    }
}
