package generators;

import generators.utils.Smoother;

import java.io.IOException;

public class MainGenerator {
    public static void generate() throws IOException {
        Smoother.smooth("VoronoiNoise\\1-Norm\\1.bmp");
        /*double[][] values = FileGenerator.loadFromFile("PerlinNoise\\6.bmp");
        double[][] values1 = FileGenerator.loadFromFile("PerlinNoise\\2.bmp");
        double[][] values2 = FileGenerator.loadFromFile("VoronoiNoise\\4-Norm\\1.bmp");
        double[][] out = new double[values.length][values[0].length];

        for(int i=0; i < values.length; i++){
            for(int j=0; j < values[0].length; j++){
                out[i][j] = (values2[i][j] + values[i][j] * values1[i][j] + 0.5d * (1 - values2[i][j]) * values1[i][j]) / 2.5d;
            }
        }
        FileGenerator.generateFile(out);*/
        /*List<gen> generators = new LinkedList<>();
        generators.add(new RandomNoiseGenerator());
        generators.add(new MidPointDisplacementGenerator());
        generators.add(new DiamondSquareGenerator());
        generators.add(new PerlinNoiseGenerator());
        for(gen gen : generators){
            gen.generate(Config.MAP_SIZE);
        }

        gen gen = new VoronoiNoiseGenerator();
        for(int i=0; i < 5; i++){
            Config.VORONOI_NORM = i;
            gen.generate(Config.MAP_SIZE);
        }*/
    }
}
