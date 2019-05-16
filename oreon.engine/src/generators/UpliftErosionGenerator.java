package generators;

import generators.utils.*;

import java.util.*;

import static generators.utils.Utils.isInsideTriangle;

public class UpliftErosionGenerator extends gen {

    private int[][] howMany;
    private double[][] values;
    private List<Line> lines = new LinkedList<>();
    private static Random random = new Random();
    private List<Point> points = new LinkedList<>();
    private List<Point> hull = new LinkedList<>();
    private List<Triangle> triangles = new LinkedList<>();
    private List<Point> stack = new LinkedList<>();
    private Map<Point, List<Triangle>>  neighboursOfPoint = new HashMap<>();
    private Map<Point, Double> areaOfVoronoiCell = new HashMap<>();
    private List<Lake> lakes = new LinkedList<>();
    private Set<LakePass> lakePasses = new HashSet<>();
    public UpliftErosionGenerator(){}

    private LakePass getPass(LakePass pass){
        for(LakePass tmp : lakePasses){
            if(tmp.equals(pass))
                return tmp;
        }
        return null;
    }

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

        for(Point point : hull){
            point.isEdge = true;
        }
        findPointNeighbours();


        //calculate both area of voroni cell for each point and its neighbours for later trees construction
        for(Point point : points){
            for(Triangle triangle : triangles){
                if(triangle.contains(point)){
                    if(!neighboursOfPoint.containsKey(point)){
                        neighboursOfPoint.put(point, new LinkedList<>());
                        areaOfVoronoiCell.put(point, (double) 0);
                    }
                    neighboursOfPoint.get(point).add(triangle);
                    areaOfVoronoiCell.put(point, areaOfVoronoiCell.get(point) + triangle.area());
                }
            }
        }

        constructTrees();

        findLakes();

        constructLakeGraph();
    }

    private void constructLakeGraph() {
        for(Point point : points){
            if(!point.isRiverMouth){
                for(Point neigbour : point.getNeighbours()){
                    if(neigbour.lakeId == point.lakeId){
                        continue;
                    }
                    LakePass tmp = new LakePass(point.lake, neigbour.lake);
                    LakePass newLake = new LakePass(point.lake, neigbour.lake, point, neigbour);
                    if(lakePasses.contains(tmp)){
                        //noinspection ConstantConditions
                        if(getPass(tmp).getPassHeight() > newLake.getPassHeight()){
                            lakePasses.remove(tmp);
                            lakePasses.add(newLake);
                        }
                    } else {
                        lakePasses.add(newLake);
                    }
                }
            }
        }

        for (LakePass pass : lakePasses){
            pass.updateLakes();
        }
    }

    private void findPointNeighbours(){
        for(Triangle triangle : triangles){
            triangle.a.addNeighbour(triangle.b);
            triangle.a.addNeighbour(triangle.c);

            triangle.b.addNeighbour(triangle.a);
            triangle.b.addNeighbour(triangle.c);

            triangle.c.addNeighbour(triangle.a);
            triangle.c.addNeighbour(triangle.b);
        }
    }

    private void findLakes() {
        //mark river mouths
        for(Point point : hull){
            point.markRiverMouth();
        }

        for(Point point : points){
            if(point.lakeId != -1 || point.isRiverMouth)
                continue;
            if(point.next == null){
                lakes.add(new Lake(Point.ID));
                point.markLake(lakes.get(lakes.size()-1));
                Point.ID++;
            }
        }
    }

    private void elevatePoints() {
        for(Point point : points){
            point.raise();
        }
    }

    private void constructTrees() {
        for(Point point : points){
            if(point.isEdge)
                continue;

            point.next = getLowestNeighbour(point);
            if(point.next != null)
                point.next.parent = point;
        }
    }

    //TODO refactor, use point.neighbours
    private Point getLowestNeighbour(Point point) {
        double minHeight = Double.MAX_VALUE;
        Point out = null;
        for(Triangle triangle : neighboursOfPoint.get(point)){
            if(triangle.a.equals(point)){
                if(minHeight > triangle.a.height)
                    out = triangle.a;
                minHeight = Math.min(minHeight, triangle.a.height);
            } else if(triangle.b.equals(point)){
                if(minHeight > triangle.b.height)
                    out = triangle.b;
                minHeight = Math.min(minHeight, triangle.b.height);
            } else if(triangle.c.equals(point)){
                if(minHeight > triangle.c.height)
                    out = triangle.c;
                minHeight = Math.min(minHeight, triangle.c.height);
            }
        }
        if(minHeight < point.height)
            return out;
        return null;
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