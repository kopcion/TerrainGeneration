package generators;

import java.util.Random;

public class RandomNoiseGenerator implements HightMapGenerator{
	static final Random random = new Random();
	private float[][] values;
	
	public RandomNoiseGenerator() {}
	
	public void generate(int mapSize) {
		this.values = new float[mapSize][mapSize];
		for(int i=0; i < mapSize; i++) {
			for(int j=0; j < mapSize; j++) {
				values[i][j] = random.nextFloat();
			}
		}
		FileGenerator.generateFile(values);
	}
}
