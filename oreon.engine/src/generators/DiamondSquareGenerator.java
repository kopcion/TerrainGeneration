package generators;

public class DiamondSquareGenerator extends gen {

    public DiamondSquareGenerator() {}

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
						values[i1][jMid] = (values[i1][j1] + values[iMid][jMid] + values[i1][j2] + values[2 * i1 - iMid][jMid]) / 4 + jitter();
					} else {
						values[i1][jMid] = (values[i1][j1] + values[iMid][jMid] + values[i1][j2]) / 3 + jitter();
					}
				}

				if (!isSet[i2][jMid]) {
					if (2 * i2 - iMid < values.length) {
						values[i2][jMid] = (values[i2][j1] + values[iMid][jMid] + values[i2][j2] + values[2 * i2 - iMid][jMid]) / 4 + jitter();
					} else {
						values[i2][jMid] = (values[i2][j1] + values[iMid][jMid] + values[i2][j2]) / 3 + jitter();
					}
				}
				
				if (!isSet[iMid][j1]) {
					if (2 * j1 - jMid >= 0) {
						values[iMid][j1] = (values[i1][j1] + values[iMid][jMid] + values[i2][j1] + values[iMid][2 * j1 - jMid]) / 4 + jitter();
					} else {
						values[iMid][j1] = (values[i1][j1] + values[iMid][jMid] + values[i2][j1]) / 3 + jitter();
					}
				}

				if (!isSet[iMid][j2]) {
					if (2 * j2 - jMid < values[0].length) {
						values[iMid][j2] = (values[i1][j2] + values[iMid][jMid] + values[i2][j2] + values[iMid][2 * j2 - jMid]) / 4 + jitter();
					} else {
						values[iMid][j2] = (values[i1][j2] + values[iMid][jMid] + values[i2][j2]) / 3 + jitter();
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
					values[iMid][jMid] = (values[i1][j1] + values[i1][j2] + values[i2][j1] + values[i2][j2]) / 4 + jitter();
				}

				isSet[iMid][jMid] = true;
			}
		}
	}

	@Override
    void calculate() {

		int squareSize = values.length - 1;

		while (squareSize >= 2) {
			diamondStep(squareSize);
			squareStep(squareSize);

			squareSize /= 2;
			jitterMult *= Config.SMOOTHING_PARAM;
		}

		normalize();
	}

	@Override
    void init(){
		isSet = new Boolean[mapSize][mapSize];
		values = new double[mapSize][mapSize];
		clear();

		jitterMult = Config.SMOOTHING_PARAM;

        values[0][0] = random.nextDouble();
		values[mapSize-1][0] = random.nextDouble();
		values[0][mapSize-1] = random.nextDouble();
		values[mapSize-1][mapSize-1] = random.nextDouble();
	}
}
