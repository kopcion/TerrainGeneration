package generators;

import generators.utils.Point;
import generators.utils.Utils;

import java.util.LinkedList;
import java.util.List;

import static generators.utils.Utils.within;

public class VoronoiNoiseGenerator extends gen {
    private List<Point> points = new LinkedList<>();

    private int norm = 1;
    public VoronoiNoiseGenerator(int i){
        norm = i;
    }

    private void generatePoints(){
        for(int i=0; i < Config.VORONOI_POINTS; i++){
            points.add(new Point(random.nextDouble() * mapSize, random.nextDouble() * mapSize));
        }
    }

    private double oneNormDist(Point a, Point b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private double twoNormDist(Point a, Point b){
        return (double)Math.sqrt( (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    private double threeNormDist(Point a, Point b){
        return (double)Math.cbrt( Math.abs((a.x - b.x) * (a.x - b.x)) + Math.abs((a.y - b.y) * (a.y - b.y)));
    }

    private double pNormDist(Point a, Point b, int norm){
        return Utils.nthRoot(norm, Utils.pow(Math.abs(a.x - b.x), norm) + Utils.pow(Math.abs(a.y - b.y), norm));
    }

    private double maxNormDist(Point a, Point b){
        return Math.max( Math.abs(a.x - b.x), Math.abs(a.y - b.y));
    }

    private double dist(Point a, Point b){
        switch (norm) {
            case 0:
                return maxNormDist(a, b);
            case 1:
                return oneNormDist(a, b);
            case 2:
                return twoNormDist(a, b);
            case 3:
                return threeNormDist(a, b);
            default:
                return pNormDist(a, b, norm);
        }
    }

    private double smallestDist(Point a){
        double out = values.length * values.length;
        for(Point point : points){
            out = Math.min(out, dist(a, point));
        }
        return out;
    }

    private double smallestDist1(Point a){
        int count = 0;
        for(Point point : points){
            if(within(dist(point, a), values[(int)a.x][(int)a.y])){
                count++;
            }
        }
        return (count > 1) ? 1f : 0f;
    }

    @Override
    void calculate(){
        for(int i=0; i < values.length; i++){
            for(int j=0; j < values[0].length; j++){
                values[i][j] = smallestDist(new Point(i,j));
            }
        }

        normalize();
    }

    @Override
    void init(){
        values = new double[mapSize][mapSize];
        generatePoints();
    }

    @Override
    void clear(){
        points.clear();
    }
}
