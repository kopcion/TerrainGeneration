package generators;

import java.util.Random;

public abstract class gen {

    static final Random random = new Random();

    int mapSize;
    double[][] values;
    Boolean[][] isSet;
    double jitterMult;

    void generate(int mapSize){
        this.mapSize = mapSize;

        init();

        calculate();

        FileGenerator.generateFile(values);
    }

    void clear(){
        for(int i=0; i < isSet.length; i++) {
            for(int j=0; j < isSet[0].length; j++) {
                isSet[i][j] = false;
            }
        }
    }

    double jitter(){
        return Config.JITTER_ENABLED * Config.AMPLITUDE * (2*random.nextDouble() - 1d) * jitterMult;
    }

    double getMax() {
        double out = 0f;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                out = Math.max(out, values[i][j]);
            }
        }
        return out;
    }

    double getMin() {
        double out = 1f;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                out = Math.min(out, values[i][j]);
            }
        }
        return out;
    }

    void normalize() {
        double max = getMax();
        double min = getMin();
        double range = max - min;

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] -= min;
                values[i][j] /= range;
            }
        }
    }

    abstract void init();

    abstract void calculate();
}
