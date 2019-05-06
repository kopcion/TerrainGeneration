package generators;

import java.util.LinkedList;
import java.util.List;

public class MainGenerator {
    public static void generate(){
        List<gen> generators = new LinkedList<>();
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
        }
    }
}
