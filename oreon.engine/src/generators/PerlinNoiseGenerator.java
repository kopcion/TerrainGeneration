package generators;

import java.util.Random;

public class PerlinNoiseGenerator extends gen{
    private double[][] gradientsX;
    private double[][] gradientsY;

    public PerlinNoiseGenerator() {}

    private void normalizeVector(int i, int j){
        double length = (double) Math.sqrt( gradientsX[i][j] * gradientsX[i][j] + gradientsY[i][j] * gradientsY[i][j] );
        gradientsX[i][j] /= length;
        gradientsY[i][j] /= length;
    }

    private void generateGradients(){
        for(int i=0; i <gradientsX.length; i++){
            for(int j=0; j < gradientsX[0].length; j++){
                gradientsX[i][j] = random.nextDouble() - 0.5f;
                gradientsY[i][j] = random.nextDouble() - 0.5f;
                normalizeVector(i, j);
            }
        }
    }

    private double calculateValueAt(double x, double y){
        int i = (int)x;
        int j = (int)y;
        double u = x - i;
        double v = y - j;

        double X00 = gradientsX[i][j] * u;
        double Y00 = gradientsY[i][j] * v;
        double n00 = X00 + Y00;

        double X10 = gradientsX[i+1][j] * (u-1);
        double Y10 = gradientsY[i+1][j] * v;
        double n10 = X10 + Y10;

        double X01 = gradientsX[i][j+1] * u;
        double Y01 = gradientsY[i][j+1] * (v-1);
        double n01 = X01 + Y01;

        double X11 = gradientsX[i+1][j+1] * (u-1);
        double Y11 = gradientsY[i+1][j+1] * (v-1);
        double n11 = X11 + Y11;

        double nX0 = n00 * (1 - f(u)) + n10 * f(u);
        double nX1 = n01 * (1 - f(u)) + n11 * f(u);
        double nXY = nX0 * (1 - f(v)) + nX1 * f(v);
        return nXY;
    }

    //f(x) = 6x^5 -15x^4 + 10x^3
    private double f(double x){
        return x*x*x*(10 - 15*x + 6*x*x);
    }

    @Override
    void calculate(){
        double step = 1d / Config.RESOLUTION;
        for(int i=0; i < values.length; i++){
            for (int j=0; j < values[0].length; j++){
                values[i][j] = calculateValueAt(i * step, j * step);
            }
        }
        normalize();
    }

    @Override
    void init(){
        values = new double[mapSize][mapSize];
        gradientsX = new double[ (int)(mapSize / Config.RESOLUTION) + 2][ (int)(mapSize / Config.RESOLUTION) + 2];
        gradientsY = new double[ (int)(mapSize / Config.RESOLUTION) + 2][ (int)(mapSize / Config.RESOLUTION) + 2];
        generateGradients();
    }

    public double[][] calculateAndGetValues(int mapSize) {
        this.mapSize = mapSize;

        init();

        calculate();

        return values;
    }
}
