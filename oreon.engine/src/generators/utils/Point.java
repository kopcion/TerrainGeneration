package generators.utils;

import generators.UpliftErosionGenerator;

import java.util.*;

import static generators.Config.EROSION_CONSTANT;

public class Point {
    public static int ID = 0;
    public static double timeInterval = 100d;

    public double x,y;
    public Point next = null;
    public Point secondNext = null;
    public List<Point> parents= new LinkedList<>();
    public List<Point> parentsFromLakes = new LinkedList<>();
    public Set<Point> neighbours = new HashSet<>();
    public double height = 0d;
    public double uplift = 0d;
    public boolean isEdge = false;
    public boolean isRiverMouth = false;
    public int lakeId = -1;
    public Lake lake = null;
    public double areaOfCell = 0d;
    public double drainageArea = 0d;
    public int lastUpdated = -1;
    public boolean isDone = false;
    public double value = 0d;
    public List<Point> voronoiCell = new LinkedList<>();
    public List<Point> hull = new LinkedList<>();

    public Set<Point> getNeighbours() {
        return neighbours;
    }

    public void compute(){
        System.out.println(isDone + " " + isEdge + " " + (next == null && secondNext == null));
        if(isDone || isEdge || next == null && secondNext == null) return;
        isDone = true;

        System.out.println("computing");
        if(next != null) next.compute();
        if(next == null && secondNext != null) secondNext.compute();

        Point tmp = (next != null) ? next : secondNext;
        value = (height + timeInterval
                    * (uplift +
                            EROSION_CONSTANT
                            * Math.sqrt(drainageArea)
                            * tmp.value
                            / Utils.dist(this, tmp))
                ) / (1 + EROSION_CONSTANT * Math.sqrt(drainageArea) / Utils.dist(this, tmp) * timeInterval);
        height += value;
    }

    public void calculateVoronoiCellArea(){
        if(isEdge || voronoiCell.size() < 3) return;

        System.out.println("size: " + voronoiCell.size());

        for(Point point : voronoiCell){
            System.out.println("\t (x,y) == (" + point.x + ", " + point.y + ")");
        }
        int l = 0;
        for (int i = 1; i < voronoiCell.size(); i++)
            if (voronoiCell.get(i).x < voronoiCell.get(l).x)
                l = i;

        int p = l, q;

        do
        {
//            System.out.println(p);
            hull.add(voronoiCell.get(p));
            q = (p + 1) % voronoiCell.size();

            for (int i = 0; i < voronoiCell.size(); i++)
            {
//                System.out.println("orientation : " + orientation(voronoiCell.get(p), voronoiCell.get(i), voronoiCell.get(q)));
                if (orientation(voronoiCell.get(p), voronoiCell.get(i), voronoiCell.get(q)) == UpliftErosionGenerator.ORIENTATION.COUNTERCLOCKWISE)
                    q = i;
            }
            p = q;

        } while (p != l);
        System.out.println("kappa");

        Point base = hull.get(0);
        Point first = hull.get(1);
        Point second = hull.get(2);
        Point base2 = base;
        Point first2 = first;
        Point second2 = second;
        hull.remove(base);
        hull.remove(first);
        hull.remove(second);
        for(Point point : hull){
            areaOfCell += new Triangle(base, first, second).area();
            first = second;
            second = point;
        }
        areaOfCell += new Triangle(base, second, first2).area();
        hull.add(0, second2);
        hull.add(0, first2);
        hull.add(0, base2);
    }

    private UpliftErosionGenerator.ORIENTATION orientation(Point a, Point b, Point c){
        int val = (int) ((b.y - a.y) * (c.x - b.x) -
                (b.x - a.x) * (c.y - b.y));

        if (val == 0) return UpliftErosionGenerator.ORIENTATION.COLLINEAR;  // colinear
        return (val > 0) ? UpliftErosionGenerator.ORIENTATION.CLOCKWISE: UpliftErosionGenerator.ORIENTATION.COUNTERCLOCKWISE; // clock or counterclock wise
    }

    public void updateChilds(double area){
        drainageArea += area;
        if(next != null)
            next.updateChilds(area);

        if(secondNext != null)
            secondNext.updateChilds(area);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

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
        for(Point point : parents){
            point.markRiverMouth();
        }
//        if(parent != null)
//            parent.markRiverMouth();
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
        for(Point point : parents){
            point.markLake(lake);
        }
//        if(parent != null)
//            parent.markLake(lake);
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }
}
