package generators;

public class RandomNoiseGenerator extends gen {

	public RandomNoiseGenerator() {}

	@Override
	void init() {
		values = new double[mapSize][mapSize];
	}

	@Override
	void calculate() {
		for(int i=0; i < mapSize; i++) {
			for(int j=0; j < mapSize; j++) {
				values[i][j] = random.nextDouble();
			}
		}
	}
}
