package generators.utils;

import java.util.*;

public class Point {
    public static int ID = 0;

    public double x,y;
    public Point next = null;
    public Point parent = null;
    public double height = 0d;
    public double uplift = 0d;
    public boolean isEdge = false;
    public boolean isRiverMouth = false;
    public int lakeId = -1;
    public Lake lake = null;

    public Set<Point> getNeighbours() {
        return neighbours;
    }

    private Set<Point> neighbours = new HashSet<>();

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void raise(){
        height += uplift;
    }

    public void addNeighbour(Point point){
        neighbours.add(point);
    }

    public void markRiverMouth(){
        isRiverMouth = true;
        if(parent != null)
            parent.markRiverMouth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    public void markLake(Lake lake){
        lakeId = ID;
        this.lake = lake;
        lake.lakePoints.add(this);
        if(parent != null)
            parent.markLake(lake);
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }
}
