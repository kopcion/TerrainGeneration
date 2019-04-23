package generators;

import java.util.Random;

public class DiamondSquareGenerator implements HeightMapGenerator {
	static final Random random = new Random();
	private static float jitterEnabled = 0f;
	static float multiplier;
	private Boolean[][] isSet;
	private float[][] values;
	private float jitterMult;

	@SuppressWarnings(value = { "unused" })
	private void print() {
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				System.out.print(values[i][j] + " ");
			}
			System.out.print('\n');
		}
	}

	private void clear() {
		for (int i = 0; i < isSet.length; i++) {
			for (int j = 0; j < isSet[0].length; j++) {
				values[i][j] = 0f;
				isSet[i][j] = false;
			}
		}
	}

	private void squareStep(int squareSize) {
		int mid = squareSize / 2;

		for (int i1 = 0; i1 < values.length - squareSize; i1 += squareSize) {
			for (int j1 = 0; j1 < values[0].length - squareSize; j1 += squareSize) {
				int iMid = i1 + mid;
				int jMid = j1 + mid;
				int i2 = i1 + squareSize;
				int j2 = j1 + squareSize;

			
				if (!isSet[i1][jMid]) {
					if (2 * i1 - iMid >= 0) {
						values[i1][jMid] = (values[i1][j1] + values[iMid][jMid] + values[i1][j2]
								+ values[2 * i1 - iMid][jMid]) / 4
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					} else {
						values[i1][jMid] = (values[i1][j1] + values[iMid][jMid] + values[i1][j2]) / 3
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					}
				}

				if (!isSet[i2][jMid]) {
					if (2 * i2 - iMid < values.length) {
						values[i2][jMid] = (values[i2][j1] + values[iMid][jMid] + values[i2][j2]
								+ values[2 * i2 - iMid][jMid]) / 4
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					} else {
						values[i2][jMid] = (values[i2][j1] + values[iMid][jMid] + values[i2][j2]) / 3
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					}
				}
				
				if (!isSet[iMid][j1]) {
					if (2 * j1 - jMid >= 0) {
						values[iMid][j1] = (values[i1][j1] + values[iMid][jMid] + values[i2][j1]
								+ values[iMid][2 * j1 - jMid]) / 4
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					} else {
						values[iMid][j1] = (values[i1][j1] + values[iMid][jMid] + values[i2][j1]) / 3
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					}
				}

				if (!isSet[iMid][j2]) {
					if (2 * j2 - jMid < values[0].length) {
						values[iMid][j2] = (values[i1][j2] + values[iMid][jMid] + values[i2][j2]
								+ values[iMid][2 * j2 - jMid]) / 4
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					} else {
						values[iMid][j2] = (values[i1][j2] + values[iMid][jMid] + values[i2][j2]) / 3
								+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
					}
				}

				isSet[iMid][j1] = isSet[iMid][j2] = isSet[i1][jMid] = isSet[i2][jMid] = true;
			}
		}
	}

	private void diamondStep(int squareSize) {
		int mid = squareSize / 2;

		for (int i1 = 0; i1 < values.length - squareSize; i1 += squareSize) {
			for (int j1 = 0; j1 < values[0].length - squareSize; j1 += squareSize) {
				int iMid = i1 + mid;
				int jMid = j1 + mid;
				int i2 = i1 + squareSize;
				int j2 = j1 + squareSize;
				if (!isSet[iMid][jMid]) {
					values[iMid][jMid] = (values[i1][j1] + values[i1][j2] + values[i2][j1] + values[i2][j2]) / 4
							+ jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
				}

				isSet[iMid][jMid] = true;
			}
		}
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
				values[i][j] = Math.max(values[i][j], 0f);
				values[i][j] = Math.min(values[i][j], 1f);
			}
		}
	}

	private void calculate() {

		int squareSize = values.length - 1;

		while (squareSize >= 2) {
			// print();
			diamondStep(squareSize);
			squareStep(squareSize);

			squareSize /= 2;
			jitterMult *= multiplier;
		}

		normalize();
	}

	@Override
	public void generate(int mapSize) {
		this.isSet = new Boolean[mapSize][mapSize];
		this.values = new float[mapSize][mapSize];
		
		for(int k=0; k < 25; k++) {
			for(int i=0; i < 4; i++) {
				clear();
				
				jitterMult = 2f;
				jitterEnabled = Config.JITTER_ENABLED;
				multiplier = 1f + k*0.15f;
				
				values[0][0] = random.nextFloat();
				values[mapSize-1][0] = random.nextFloat();
				values[0][mapSize-1] = random.nextFloat();
				values[mapSize-1][mapSize-1] = random.nextFloat();
		  
				calculate();
				
				FileGenerator.generateFile(values);
			}
		}
	}

}
