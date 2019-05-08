package generators;

import generators.utils.Point;
import generators.utils.Triangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TransferQueue;

import static generators.utils.Utils.isInsideTriangle;
import static generators.utils.Utils.within;

public class UpliftErosionGenerator extends gen {

    private int[][] howMany;
    private double[][] values;
    private List<Line> lines = new LinkedList<>();
    private static Random random = new Random();
    private List<Point> points = new LinkedList<>();
    private List<Point> hull = new LinkedList<>();
    private List<Triangle> triangles = new LinkedList<>();
    private List<Point> stack = new LinkedList<>();

    public UpliftErosionGenerator(){}

    //TODO make it so that points are non collinear, check, mby refactor
    private void generatePoints(){
        for(int i=0; i < Config.VORONOI_POINTS; i++){
            int x = random.nextInt(mapSize);
            int y = random.nextInt(mapSize);

            if(!checkLines(x, y)){
                Point newPoint = new Point((double)x, (double)y);
                for(Point point : points){
                    lines.add(new Line(newPoint, point));
                }
                points.add(newPoint);
            } else {
                i--;
            }
        }
    }

    //returns true if there is  line containing this point
    private boolean checkLines(int x, int y){
        for(Line line : lines){
            if(line.checkPoint(x, y)){
                return true;
            }
        }
        return false;
    }

    private void DelaunayTriangulation(){
        triangulate();

        while(canFlip()){}
    }

    private void triangulate() {
        convexHull();
        Point base = hull.get(0);
        Point first = hull.get(1);
        Point second = hull.get(2);
        hull.remove(base);
        hull.remove(first);
        hull.remove(second);
        Triangle prev = new Triangle(base, first, second);
        triangles.add(prev);
        for(Point point : hull){
            first = second;
            second = point;
            Triangle current = new Triangle(base, first, second);
            triangles.add(current);
            prev.ca = current;
            current.ab = prev;
            prev = current;
        }

        for(Point point : points){
            for(Triangle triangle : triangles){
                if(isInsideTriangle(point, triangle)){
                    splitTriangle(point, triangle);
                    break;
                }
            }
        }
    }

    //TODO change method for determining wether triangles are Delaunay
    private boolean canFlip(){
        for(Triangle triangle : triangles){
            if(triangle.ab != null) {
                if (angle(triangle.a, triangle.c, triangle.b) + getOpositeAngle(triangle, triangle.ab) > Math.PI) {
                    flipTriangles(triangle, triangle.ab);
                    return true;
                }
            }
            if(triangle.bc != null){
                    if(angle(triangle.c, triangle.a, triangle.b) + getOpositeAngle(triangle, triangle.bc) > Math.PI){
                    flipTriangles(triangle, triangle.bc);
                    return true;
                }
            }
            if(triangle.ca != null) {
                if (angle(triangle.a, triangle.b, triangle.c) + getOpositeAngle(triangle, triangle.ca) > Math.PI) {
                    flipTriangles(triangle, triangle.ca);
                    return true;
                }
            }
        }
        return false;
    }

    private void flipTriangles(Triangle primary, Triangle secondary) {
        Triangle one = null;
        Triangle two = null;

        Triangle.orientate(primary, secondary);
        Triangle.orientate(secondary, primary);
        //both have common edge AB
        if(primary.a != secondary.a)
            secondary.reverse();

        //both are orientated so that common edge is ab, and primary.a == secondary.a and primary.b == secondary.b

        one = new Triangle(primary.a, primary.c, secondary.c);
        two = new Triangle(primary.b, primary.c, secondary.c);
        one.bc = two;
        two.bc = one;
        one.ab = primary.ca;
        one.ca = secondary.ca;
        two.ab = primary.bc;
        two.ca = secondary.bc;
        triangles.remove(primary);
        triangles.remove(secondary);
        triangles.add(one);
        triangles.add(two);
    }

    private double getOpositeAngle(Triangle primary, Triangle secondary){
        if(secondary.ab == primary){
            return angle(secondary.a, secondary.c, secondary.b);
        } else if(secondary.bc == primary){
            return angle(secondary.b, secondary.a, secondary.c);
        } else {
            return angle(secondary.a, secondary.b, secondary.c);
        }
    }

    private double angle(Point a, Point b, Point c){
        Point BA = new Point(a.x - b.x, a.y - b.y);
        Point BC = new Point(c.x - b.x, c.y - b.y);

        double dotProduct = BA.x * BC.x + BA.y * BC.y;
        double baLength = Math.sqrt( BA.x * BA.x + BA.y * BA.y);
        double bcLength = Math.sqrt( BC.x * BC.x + BC.y * BC.y);

        double cos = dotProduct / (baLength * bcLength);

        return  Math.acos(cos);
    }

    private void splitTriangle(Point point, Triangle triangle){
        Triangle one = new Triangle(point, triangle.a, triangle.b);
        Triangle two = new Triangle(point, triangle.b, triangle.c);
        Triangle three = new Triangle(point, triangle.c, triangle.a);

        one.ab = three;
        one.bc = triangle.ab;
        one.ca = two;

        two.ab = one;
        two.bc = triangle.bc;
        two.ca = three;

        three.ab = two;
        three.bc = triangle.ca;
        three.ca = one;

        triangles.remove(triangle);
        triangles.add(one);
        triangles.add(two);
        triangles.add(three);
    }

    private void convexHull() {
        /*points.sort((a,b) -> {
            if(a.x != b.x){
                if(a.x < b.x)
                    return -1;
                if(a.x > b.x)
                    return 1;
            } else if(a.y != b.y){
                if(a.y < b.y)
                    return -1;
                if(a.y > b.y)
                    return 1;
            }
            return 0;
        });*/

        int l = 0;
        for (int i = 1; i < points.size(); i++)
            if (points.get(i).x < points.get(l).x)
                l = i;

        int p = l, q;

        do
        {
            hull.add(points.get(p));
            q = (p + 1) % points.size();

            for (int i = 0; i < points.size(); i++)
            {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2)
                    q = i;
            }
            p = q;

        } while (p != l);

        for(Point point : hull){
            points.remove(point);
        }
    }

    private int orientation(Point a, Point b, Point c){
        int val = (int) ((b.y - a.y) * (c.x - b.x) -
                        (b.x - a.x) * (c.y - b.y));

        if (val == 0) return 0;  // colinear
        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    @Override
    void init() {
        howMany = new int[mapSize][mapSize];
        values = new double[mapSize][mapSize];
        generatePoints();
    }

    @Override
    void calculate() {
        generatePoints();

        DelaunayTriangulation();
    }
}
