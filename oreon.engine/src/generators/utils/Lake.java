package generators.utils;

import java.util.LinkedList;
import java.util.List;

public class Lake {
    public List<Point> lakePoints = new LinkedList<>();
    public List<LakePass> neighbours = new LinkedList<>();
    public Lake next = null;
    public LakePass pass = null;
    private int id;
    public boolean isOnEdge = false;

    public Lake(int id){
        this.id = id;
    }
}
