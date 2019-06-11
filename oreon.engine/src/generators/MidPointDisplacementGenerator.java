package generators;

public class MidPointDisplacementGenerator extends gen {

	public MidPointDisplacementGenerator(double i) {
		param *= (0.8d + i/8);
	}

	double param = Config.SMOOTHING_PARAM;
	
	private void recStep(int i1, int j1, int i2, int j2, double jitterMult) {
		int iMid = (i1 + i2) / 2;
		int jMid = (j1 + j2) / 2;
		if(!isSet[iMid][j1]) values[iMid][j1] = ( values[i1][j1] + values[i2][j1] ) / 2 + jitter(jitterMult);
		if(!isSet[iMid][j2]) values[iMid][j2] = ( values[i1][j2] + values[i2][j2] ) / 2 + jitter(jitterMult);
		if(!isSet[i1][jMid]) values[i1][jMid] = ( values[i1][j1] + values[i1][j2] ) / 2 + jitter(jitterMult);
		if(!isSet[i2][jMid]) values[i2][jMid] = ( values[i2][j1] + values[i2][j2] ) / 2 + jitter(jitterMult);
		isSet[iMid][jMid] = isSet[iMid][j1] = isSet[iMid][j2] = isSet[i1][jMid] = isSet[i2][jMid] = true;
		
		values[iMid][jMid] = ( values[iMid][j1] + values[iMid][j2] + values[i1][jMid] + values[i2][jMid]) / 4 + jitter(jitterMult);
		
		if(j2 - j1 != 2) {
			recStep(i1, j1, iMid, jMid, jitterMult * param);
			recStep(i1, jMid, iMid, j2, jitterMult * param);
			recStep(iMid, j1, i2, jMid, jitterMult * param);
			recStep(iMid, jMid, i2, j2, jitterMult * param);
		}
	}

	@Override
    void init(){
        isSet = new Boolean[mapSize][mapSize];
        values = new double[mapSize][mapSize];
        clear();


        values[0][0] = random.nextDouble();
        values[mapSize-1][0] = random.nextDouble();
        values[0][mapSize-1] = random.nextDouble();
        values[mapSize-1][mapSize-1] = random.nextDouble();
    }

    @Override
    void calculate(){
        recStep(0, 0, mapSize-1, mapSize-1, param);
        normalize();
    }
}
