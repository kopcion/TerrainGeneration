package generators;

import java.util.Random;

public class PerlinNoiseGenerator implements HeightMapGenerator {
    private float[][] values;
    private float[][] gradientsX;
    private float[][] gradientsY;
    private static float resolution = 100f;
    private static Random random = new Random();

    public PerlinNoiseGenerator() {}

    private void normalizeVector(int i, int j){
        float length = (float) Math.sqrt( gradientsX[i][j] * gradientsX[i][j] + gradientsY[i][j] * gradientsY[i][j] );
        gradientsX[i][j] /= length;
        gradientsY[i][j] /= length;
    }

    private void generateGradients(){
        for(int i=0; i <gradientsX.length; i++){
            for(int j=0; j < gradientsX[0].length; j++){
                gradientsX[i][j] = random.nextFloat() - 0.5f;
                gradientsY[i][j] = random.nextFloat() - 0.5f;
                normalizeVector(i, j);
            }
        }
    }

    private float calculateValueAt(float x, float y){
        int i = (int)x;
        int j = (int)y;
        float u = x - i;
        float v = y - j;

        float X00 = gradientsX[i][j] * u;
        float Y00 = gradientsY[i][j] * v;
        float n00 = X00 + Y00;

        float X10 = gradientsX[i+1][j] * (u-1);
        float Y10 = gradientsY[i+1][j] * v;
        float n10 = X10 + Y10;

        float X01 = gradientsX[i][j+1] * u;
        float Y01 = gradientsY[i][j+1] * (v-1);
        float n01 = X01 + Y01;

        float X11 = gradientsX[i+1][j+1] * (u-1);
        float Y11 = gradientsY[i+1][j+1] * (v-1);
        float n11 = X11 + Y11;

        float nX0 = n00 * (1 - f(u)) + n10 * f(u);
        float nX1 = n01 * (1 - f(u)) + n11 * f(u);
        float nXY = nX0 * (1 - f(v)) + nX1 * f(v);
        return nXY;
    }

    //f(x) = 6x^5 -15x^4 + 10x^3
    private float f(float x){
        return x*x*x*(10 - 15*x + 6*x*x);
    }

    private void calculate(){
        float step = 1/ resolution;
        for(int i=0; i < values.length; i++){
            for (int j=0; j < values[0].length; j++){
               //System.out.println("Calling for " + i * step + " " + j * step);
                values[i][j] = calculateValueAt(i * step, j * step);
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
            }
        }
    }

    private void init(int mapSize){
        values = new float[mapSize][mapSize];
        gradientsX = new float[ (int)(mapSize / resolution) + 2][ (int)(mapSize / resolution) + 2];
        gradientsY = new float[ (int)(mapSize / resolution) + 2][ (int)(mapSize / resolution) + 2];
        generateGradients();
    }

    @Override
    public void generate(int mapSize) {

        resolution = Config.RESOLUTION;
        init(mapSize);

        calculate();

        FileGenerator.generateFile(values);
    }
}
