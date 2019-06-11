package generators.utils;

import generators.FileGenerator;

import java.io.IOException;

import static generators.Config.SMOOTHIG_RANGE;

public class Smoother {
    /*
     * @deprecated Use smooth(double[][]) and then generate file with those values
     */
    @Deprecated()
    public static void smooth(String fileName) throws IOException {
        double values[][] = FileGenerator.loadFromFile(fileName);
        double smoothedValues[][] = new double[values.length][values[0].length];

        for(int i=0; i < smoothedValues.length; i++){
            for(int j=0; j < smoothedValues[0].length; j++){
                smoothedValues[i][j] = avarage(values, i, j);
            }
        }

//        FileGenerator.generateFile(smoothedValues, fileName.substring(0, fileName.length()-4) + "Smoothed");
    }

    public static double[][] smooth(double[][] values){
        double smoothedValues[][] = new double[values.length][values[0].length];

        for(int i=0; i < smoothedValues.length; i++){
            for(int j=0; j < smoothedValues[0].length; j++){
                smoothedValues[i][j] = avarage(values, i, j);
            }
        }

        return smoothedValues.clone();
    }

    private static double avarage(double[][] values, int x, int y) {
        int range = SMOOTHIG_RANGE;
        double value = 0d;
        double weight = 0d;
        for(int i= -range; i < range + 1; i++){
            for(int j= -range; j < range + 1; j++){
                if(x-i >= 0 && x-i < values.length && y-j >=0 && y-j < values[0].length){
                    if(i!=0 && j!=0){
//                        System.out.println("value: " + value + " weight: " + weight);
                        value += values[x-i][y-j] / Math.sqrt(i*i + j*j);
                        weight += 1 / Math.sqrt(i*i + j*j);
                    } else {
                        value += values[x][y];
                        weight++;
                    }
                }
            }
        }

        return value/weight;
    }
}
