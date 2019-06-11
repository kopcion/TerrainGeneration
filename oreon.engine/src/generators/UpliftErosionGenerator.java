package generators;

import generators.utils.*;
import javafx.scene.effect.GaussianBlur;

import java.util.*;

import static generators.utils.Utils.det;
import static generators.utils.Utils.dist;
import static generators.utils.Utils.isInsideTriangle;

public class UpliftErosionGenerator extends gen {

    private static int TIME = 0;
    private int counter = 0;

    private List<Line> lines = new LinkedList<>();
    private static Random random = new Random();
    private List<Point> points = new LinkedList<>();
    private List<Point> hull = new LinkedList<>();
    private List<Triangle> triangles = new LinkedList<>();
    private List<Point> roots = new LinkedList<>();
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
        for(int i=0; i < 4*Config.VORONOI_POINTS; i++){
//        for(int i=0; i < 20; i++){
            int x = random.nextInt(mapSize);
            int y = random.nextInt(mapSize);

            Point newPoint = new Point((double)x, (double)y);
            boolean canAdd = true;
            for(Point point : points){
                if(point.equals(newPoint)) {
                    canAdd = false;
                    break;
                }
            }
            if(!checkIfPointsAreCollinear(x, y)){
                for(Point point : points){
                    lines.add(new Line(newPoint, point));
                }

                if(canAdd)points.add(newPoint);
            } else {
                i--;
            }
        }
        values = new PerlinNoiseGenerator(2).calculateAndGetValues(mapSize);
        for(Point point : points){
            point.uplift = values[(int)point.x][(int)point.y];
            point.height = 10 * point.uplift;
        }
    }

    //returns true if there is  line containing this point
    private boolean checkIfPointsAreCollinear(int x, int y){
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

    private void calculateAreasOfVoronoiCells() {
        for(Triangle triangle : triangles){
            triangle.calculateCircumcentre();
//            System.out.println(triangle.circumcentre);
        }
        System.out.println("calculated circumcentres");
        for(Point point : points){
            point.calculateVoronoiCellArea();
        }
    }

    private void computeStreamPowerEquation(){
        for(Point point : points){
            point.compute();
        }
        System.out.println("done computing");
        for(Point point : points){
            point.isDone = false;
        }
    }

    private void updateAreasOfPoints(){
        for(Point point : points){
            point.updateChilds(point.areaOfCell);
        }
    }

    private void constructLakeGraph() {
        for(Point point : points){
            if(!point.isRiverMouth){
                for(Point neighbour : point.getNeighbours()){
//                    System.out.println(neighbour.x + " " + neighbour.y + " " + (neighbour.lake == null));
                    if(neighbour.lakeId == point.lakeId){
                        continue;
                    }
                    LakePass tmp = new LakePass(point.lake, neighbour.lake);
                    LakePass newLake = new LakePass(point.lake, neighbour.lake, point, neighbour);
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

        Queue<Lake> bfsQueue = new LinkedList<>();
        for(Point point : hull){
            if(!point.lake.isOnEdge){
                bfsQueue.add(point.lake);
            }
            point.lake.isOnEdge = true;
        }

        while(!bfsQueue.isEmpty()){
            Lake top = bfsQueue.poll();
            for(LakePass pass : top.neighbours){
                if(pass.first.equals(top)){
                    if(pass.second.next != null){
                        pass.second.next = top;
                        pass.second.pass = pass;
                        bfsQueue.add(pass.second);
                    }
                } else {
                    if(pass.first.next != null){
                        pass.first.next = top;
                        pass.first.pass = pass;
                        bfsQueue.add(pass.first);
                    }
                }
            }
        }

        //extrapolate connections from lakeGraph into point graph
        for(Lake lake : lakes){
            if(lake.pass != null){
                if(!lake.pass.first.equals(lake)){ //make it so that the edge is from first to second
                    Lake tmp = lake.pass.first;
                    lake.pass.first = lake.pass.second;
                    lake.pass.second = tmp;
                }
                if(lake.pass.a.lake.equals(lake.pass.first)){ //pooint a is from lake first, so edge from a to b
                    lake.pass.a.secondNext = lake.pass.b;
                    lake.pass.b.parentsFromLakes.add(lake.pass.a);
                } else { //point b os from lake first so edge from b to a
                    lake.pass.b.secondNext = lake.pass.a;
                    lake.pass.a.parentsFromLakes.add(lake.pass.b);
                }
            }
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
        for(Point point : points){
            if(point.isRiverMouth){
                lakes.add(new Lake(Point.ID));
                point.lake = lakes.get(lakes.size()-1);
                Point.ID++;
                continue;
            }
            if(point.lakeId != -1)
                continue;
            if(point.next == null){
                lakes.add(new Lake(Point.ID));
                point.markLake(lakes.get(lakes.size()-1));
                Point.ID++;
            }
        }
    }

    private void constructTrees() {
        for(Point point : points){
            if(point.isEdge)
                continue;

            point.next = getLowestNeighbour(point);
//            System.out.println(point.next);
            if(point.next != null)
                point.next.parents.add(point);
        }
    }

    private Point getLowestNeighbour(Point point) {
        double minHeight = Double.MAX_VALUE;
        Point out = null;
        for(Point neighbour : point.getNeighbours()){
            if(minHeight > neighbour.height){
                out = neighbour;
                minHeight = neighbour.height;
            }
        }
        if(minHeight < point.height)
            return out;
        return null;
    }

    private void checkTriangles(){
        for(Triangle triangle : triangles){
            if(triangle.a.equals(triangle.b) || triangle.a.equals(triangle.c) || triangle.b.equals(triangle.c)){
                throw new RuntimeException("degenerated triangule " + triangle.toString());
            }
            if(triangle.ab != null) if(!(triangle.equals(triangle.ab.ab) || triangle.equals(triangle.ab.bc) || triangle.equals(triangle.ab.ca)))
                throw new RuntimeException("neighbour error");

            if(triangle.bc != null) if(!(triangle.equals(triangle.bc.ab) || triangle.equals(triangle.bc.bc) || triangle.equals(triangle.bc.ca)))
                throw new RuntimeException("neighbour error");

            if(triangle.ca != null)if(!(triangle.equals(triangle.ca.ab) || triangle.equals(triangle.ca.bc) || triangle.equals(triangle.ca.ca)))
                throw new RuntimeException("neighbour error");
        }
    }

    private void triangulate() {
        convexHull();
        Point base = hull.get(0);
        Point first = hull.get(1);
        Point second = hull.get(2);
        Point first2 = first;
        Point second2 = second;
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
        hull.add(0, second2);
        hull.add(0, first2);
        hull.add(0, base);

        checkTriangles();

        for(Point point : points){
            for (Triangle triangle : triangles) {
                if (isInsideTriangle(point, triangle)) {
                    checkTriangles();
                    splitTriangle(point, triangle);
                    checkTriangles();
                    break;
                }
            }
        }

        for(Point point : hull){
            point.markRiverMouth();
        }
    }

    public boolean checkIfPointLiesInsideTrianglesCircle(Triangle triangle, Point point){
        double AxmDx = (triangle.a.x - point.x);
        double BxmDx = (triangle.b.x - point.x);
        double CxmDx = (triangle.c.x - point.x);

        double AymDy = (triangle.a.y - point.y);
        double BymDy = (triangle.b.y - point.y);
        double CymDy = (triangle.c.y - point.y);

        double[][] matrix = {
                {AxmDx, AymDy, (AxmDx*AxmDx + AymDy*AymDy)},
                {BxmDx, BymDy, (BxmDx*BxmDx + BymDy*BymDy)},
                {CxmDx, CymDy, (CxmDx*CxmDx + CymDy*CymDy)}
        };

        return det(matrix) > 0;
    }

    public enum ORIENTATION{
        COLLINEAR, CLOCKWISE, COUNTERCLOCKWISE
    }

    private boolean canFlip(){
        checkTriangles();
        boolean out = false;
        for(int k=0; k < triangles.size(); k++){
            Triangle triangle = triangles.get(k);
            for(int i=0; i < 3; i++){
                if(orientation(triangle) == ORIENTATION.CLOCKWISE){
                    triangle.reverse();
                }

                if(triangle.ca != null) {
                    Point oppositePoint = triangle.getOpositePoint(triangle.a, triangle.c);
                    if (oppositePoint == null){ //opposite point doesnt exist
                        triangle.rotateTriangle();
                        continue;
                    }
                    if(triangle.contains(oppositePoint)){
                        throw new RuntimeException();
                    }
                    if(orientation(triangle) == ORIENTATION.COUNTERCLOCKWISE){
                        if(checkIfPointLiesInsideTrianglesCircle(triangle, oppositePoint)){
                            checkTriangles();
                            counter++;
                            flipTriangles(triangle, triangle.ca);
                            out = true;
                            checkTriangles();
                        }
                    }
                }
                triangle.rotateTriangle();
            }

        }
        return out;
    }

    private ORIENTATION orientation(Triangle triangle) {
        return orientation(triangle.a, triangle.b, triangle.c);
    }

    public void flipTriangles(Triangle primary, Triangle secondary) {
        Triangle one;
        Triangle two;

        Triangle.orientate(primary, secondary);
        Triangle.orientate(secondary, primary);

        //both have common edge AB
        if(!primary.a.equals(secondary.a)) {
            secondary.reverse();
            Triangle.orientate(primary, secondary);
            Triangle.orientate(secondary, primary);
        }

        //both are orientated so that common edge is ab, and primary.a == secondary.a and primary.b == secondary.b
        one = new Triangle(primary.a, secondary.c, primary.c);
        two = new Triangle(primary.b, primary.c, secondary.c);
        one.bc = secondary;
        two.bc = primary;
        one.ab = secondary.ca;
        one.ca = primary.ca;
        two.ab = primary.bc;
        two.ca = secondary.bc;

        if(primary.bc != null){
            primary.updateTriangleNeighbour(primary.bc, secondary);
        }

        if(secondary.ca != null){
            secondary.updateTriangleNeighbour(secondary.ca, primary);
        }

        primary.copyFrom(one);
        secondary.copyFrom(two);
    }

    @Deprecated
    private double getOpositeAngle(Triangle primary, Triangle secondary){
        if(secondary.ab == primary){
            return angle(secondary.a, secondary.c, secondary.b);
        } else if(secondary.bc == primary){
            return angle(secondary.b, secondary.a, secondary.c);
        } else {
            return angle(secondary.a, secondary.b, secondary.c);
        }
    }

    @Deprecated
    private double angle(Point a, Point b, Point c){
        Point BA = new Point(a.x - b.x, a.y - b.y);
        Point BC = new Point(c.x - b.x, c.y - b.y);

        double dotProduct = BA.x * BC.x + BA.y * BC.y;
        double baLength = Math.sqrt( BA.x * BA.x + BA.y * BA.y);
        double bcLength = Math.sqrt( BC.x * BC.x + BC.y * BC.y);

        double cos = dotProduct / (baLength * bcLength);

        return  Math.acos(cos);
    }

    //TODO refactor, use updateTriangleNeighbour method from Triangle
    private void splitTriangle(Point point, Triangle triangle){
        Triangle one = new Triangle(triangle.a, triangle.b, point);
        Triangle two = new Triangle(point, triangle.b, triangle.c);
        Triangle three = new Triangle(triangle.a, point, triangle.c);

        one.ab = triangle.ab;
        one.bc = two;
        one.ca = three;
        if(triangle.ab != null){
            if(triangle.equals(triangle.ab.ab)) {
                triangle.ab.ab = one;
            } else if(triangle.equals(triangle.ab.bc)){
                triangle.ab.bc = one;
            } else if(triangle.equals(triangle.ab.ca)){
                triangle.ab.ca = one;
            }
        }

        two.ab = one;
        two.bc = triangle.bc;
        two.ca = three;
        if(triangle.bc != null){
            if(triangle.equals(triangle.bc.ab)) {
                triangle.bc.ab = two;
            } else if(triangle.equals(triangle.bc.bc)){
                triangle.bc.bc = two;
            } else if(triangle.equals(triangle.bc.ca)){
                triangle.bc.ca = two;
            }
        }

        three.ab = one;
        three.bc = two;
        three.ca = triangle.ca;
        if(triangle.ca != null) {
            if(triangle.equals(triangle.ca.ab)) {
                triangle.ca.ab = three;
            } else if(triangle.equals(triangle.ca.bc)){
                triangle.ca.bc = three;
            } else if(triangle.equals(triangle.ca.ca)){
                triangle.ca.ca = three;
            }
        }

        triangles.remove(triangle);
        triangles.add(one);
        triangles.add(two);
        triangles.add(three);

        assert (!one.equals(two) || !one.equals(three) || !two.equals(three));
    }

    private void convexHull(){
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
                if (orientation(points.get(p), points.get(i), points.get(q)) == ORIENTATION.COUNTERCLOCKWISE)
                    q = i;
            }
            p = q;

        } while (p != l);

        for(Point point : hull){
            point.isEdge = true;
        }
    }

    private ORIENTATION orientation(Point a, Point b, Point c){
        int val = (int) ((b.y - a.y) * (c.x - b.x) -
                (b.x - a.x) * (c.y - b.y));

        if (val == 0) return ORIENTATION.COLLINEAR;  // colinear
        return (val > 0) ? ORIENTATION.CLOCKWISE: ORIENTATION.COUNTERCLOCKWISE; // clock or counterclock wise
    }

    @Override
    void init() {
        values = new double[mapSize][mapSize];
        for(int i=0; i < values.length; i++){
            for(int j=0; j < values[0].length;j++){
                values[i][j] = 0;
            }
        }
        generatePoints();
    }

    @Override
    void calculate() {
        DelaunayTriangulation();

        for(Point point : hull){
            point.isEdge = true;
            roots.add(point);
        }

        findPointNeighbours();

        System.out.println("extracted neighbours");

        calculateAreasOfVoronoiCells();

        System.out.println("calculated areas");

        int iterations = 100;
        while(iterations-- > 0){
            System.out.println("\niteration no. " + (100-iterations) + "\n");
            constructTrees();

            findLakes();

            constructLakeGraph();

            updateAreasOfPoints();

            computeStreamPowerEquation();
        }

        for(Point point : points){
            for(int i=0; i < mapSize; i++){
                for(int j=0; j < mapSize; j++){
                    values[i][j] += point.height * Math.abs(Gaussian.pdf(dist(point, new Point(i, j)), 0, 85));
                }
            }
        }

        normalize();
    }
}