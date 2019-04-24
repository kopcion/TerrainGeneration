package generators;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VoronoiNoiseGenerator implements HeightMapGenerator {
    private static int numberOfPoints;
    private float[][] values;
    private static Random random = new Random();
    private List<Point> points = new LinkedList<>();

    private class Point{
        float x,y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private void generatePoints(int mapSize){
        for(int i=0; i < numberOfPoints; i++){
            points.add(new Point(random.nextFloat() * mapSize, random.nextFloat() * mapSize));
        }
    }

    private float oneNormDist(Point a, Point b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private float twoNormDist(Point a, Point b){
        return (float)Math.sqrt( (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    private float threeNormDist(Point a, Point b){
        return (float)Math.cbrt( Math.abs((a.x - b.x) * (a.x - b.x)) + Math.abs((a.y - b.y) * (a.y - b.y)));
    }

    private float pNormDist(Point a, Point b, int norm){
        return (float)Utils.nthRoot(norm, Utils.pow(Math.abs(a.x - b.x), norm) + Utils.pow(Math.abs(a.y - b.y), norm));
    }

    private float maxNormDist(Point a, Point b){
        return Math.max( Math.abs(a.x - b.x), Math.abs(a.y - b.y));
    }

    private float dist(Point a, Point b){
        switch (Config.VORONOI_NORM) {
            case 0:
                return maxNormDist(a, b);
            case 1:
                return oneNormDist(a, b);
            case 2:
                return twoNormDist(a, b);
            case 3:
                return threeNormDist(a, b);
            default:
                return pNormDist(a, b, Config.VORONOI_NORM);
        }
    }

    private float smallestDist(Point a){
        float out = values.length * values.length;
        for(Point point : points){
            out = Math.min(out, dist(a, point));
        }
        return out;
    }

    private void calculate(){
        for(int i=0; i < values.length; i++){
            for(int j=0; j < values[0].length; j++){
                values[i][j] = smallestDist(new Point(i,j));
            }
        }

        normalize();
    }

    private float getMax() {
        float out = 0f;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                out = Math.max(out, values[i][j]);
            }
        }
        return out;
    }

    private float getMin() {
        float out = 1f;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                out = Math.min(out, values[i][j]);
            }
        }
        return out;
    }

    private void normalize() {
        float max = getMax();
        float min = getMin();
        float range = max - min;

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] -= min;
                values[i][j] /= range;
                values[i][j] = Math.max(values[i][j], 0f);
                values[i][j] = Math.min(values[i][j], 1f);
            }
        }
    }

    private void init(int mapSize){
        values = new float[mapSize][mapSize];
        generatePoints(mapSize);
    }

    private void clear(){
        points.clear();
    }

    @Override
    public void generate(int mapSize) {
        numberOfPoints = Config.VORONOI_POINTS;
        init(mapSize);

        calculate();

        FileGenerator.generateFile(values);

        clear();
    }
}
