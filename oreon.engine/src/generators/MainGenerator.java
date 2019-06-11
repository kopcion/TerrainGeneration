package generators;

import java.util.LinkedList;
import java.util.List;

public class MainGenerator {
    public static boolean generate(){
        List<gen> generators = new LinkedList<>();
        for(int i=0; i < 5; i++){
            generators.add(new MidPointDisplacementGenerator(i));
            generators.add(new DiamondSquareGenerator());
            generators.add(new PerlinNoiseGenerator(i));
        }
        generators.add(new generators.UpliftErosionGenerator());

        List<Thread> threads = new LinkedList<>();

        for(gen gen : generators){
            Runnable runnable = () -> gen.generate(Config.MAP_SIZE);
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        for(int i=0; i < 5; i++){
            Config.VORONOI_NORM = i;
            gen gen = new VoronoiNoiseGenerator(i);
            Runnable runnable = () -> gen.generate(Config.MAP_SIZE);
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
