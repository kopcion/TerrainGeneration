package generators.utils;

import java.util.*;

import static generators.Config.EROSION_CONSTANT;

public class Point {
    public static int ID = 0;
    public static double timeInterval = 1d;

    public double x,y;
    public Point next = null;
    public Point secondNext = null;
    public Point parent = null;
    public List<Point> parentsFromLakes = new LinkedList<>();
    public double height = 0d;
    public double uplift = 0d;
    public boolean isEdge = false;
    public boolean isRiverMouth = false;
    public int lakeId = -1;
    public Lake lake = null;
    public double areaOfCell = 0d;
    public int lastUpdated = -1;
    public boolean isDone = false;
    public double value = 0d;

    public Set<Point> getNeighbours() {
        return neighbours;
    }

    public void compute(){
        if(isDone || isEdge || next == null && secondNext == null) return;
        isDone = true;

        if(next != null) next.compute();
        if(next == null && secondNext != null) secondNext.compute();

        Point tmp = (next != null) ? next : secondNext;
        value = (height + timeInterval
                    * (uplift +
                            EROSION_CONSTANT
                            * Math.sqrt(areaOfCell)
                            * tmp.value
                            / Utils.dist(this, tmp))
                ) / (1 + EROSION_CONSTANT * Math.sqrt(areaOfCell) / Utils.dist(this, tmp) * timeInterval);
    }

    public void updateChilds(double area){
        areaOfCell += area;
        if(next != null)
            next.updateChilds(area);

        if(secondNext != null)
            secondNext.updateChilds(area);
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
