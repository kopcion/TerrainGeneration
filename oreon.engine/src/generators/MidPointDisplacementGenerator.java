package generators;

import java.util.Random;

public class MidPointDisplacementGenerator implements HeightMapGenerator {
	static final Random random = new Random();
	private static float jitterEnabled = 0f;
	static float multiplier;
	private Boolean[][] isSet;
	private float[][] values;
	
	public MidPointDisplacementGenerator() {}
	
	private void clear() {
		for(int i=0; i < isSet.length; i++) {
			for(int j=0; j < isSet[0].length; j++) {
				isSet[i][j] = false;
			}
		}
	}
	
	private float getMax() {
		float out = 0f;
		for(int i=0; i < values.length; i++) {
			for(int j=0; j < values[0].length; j++) {
				out = Math.max(out, values[i][j]);
			}
		}
		return out;
	}
	
	private float getMin() {
		float out = 1f;
		for(int i=0; i < values.length; i++) {
			for(int j=0; j < values[0].length; j++) {
				out = Math.min(out, values[i][j]);
			}
		}
		return out;
	}

	private void normalize() {
		float max = getMax();
		float min = getMin();
		float range = max - min;
		
		for(int i=0; i < values.length; i++) {
			for(int j=0; j < values[0].length; j++) {
				values[i][j] -= min;
				values[i][j] /= range;
				values[i][j] = Math.max(values[i][j], 0f);
				values[i][j] = Math.min(values[i][j], 1f);
			}
		}
	}
	
	private void recStep(int i1, int j1, int i2, int j2, float jitterMult) {
		int iMid = (i1 + i2) / 2;
		int jMid = (j1 + j2) / 2;
		if(!isSet[iMid][j1]) values[iMid][j1] = ( values[i1][j1] + values[i2][j1] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		if(!isSet[iMid][j2]) values[iMid][j2] = ( values[i1][j2] + values[i2][j2] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		if(!isSet[i1][jMid]) values[i1][jMid] = ( values[i1][j1] + values[i1][j2] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		if(!isSet[i2][jMid]) values[i2][jMid] = ( values[i2][j1] + values[i2][j2] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		isSet[iMid][jMid] = isSet[iMid][j1] = isSet[iMid][j2] = isSet[i1][jMid] = isSet[i2][jMid] = true;
		
		
		
		values[iMid][jMid] = ( values[iMid][j1] + values[iMid][j2] + values[i1][jMid] + values[i2][jMid]) / 4 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		
		
		if(j2 - j1 != 2) {
			recStep(i1, j1, iMid, jMid, jitterMult * multiplier);
			recStep(i1, jMid, iMid, j2, jitterMult * multiplier);
			recStep(iMid, j1, i2, jMid, jitterMult * multiplier);
			recStep(iMid, jMid, i2, j2, jitterMult * multiplier);
		}
	}
	
	@Override
	public void generate(int mapSize) {
		this.isSet = new Boolean[mapSize][mapSize];
		
		for(int k=0; k < 25; k++) {
			for(int i=0; i < 4; i++) {
				this.values = new float[mapSize][mapSize];
				clear();
				jitterEnabled = Config.JITTER_ENABLED;
				multiplier = 1f + k*0.15f;
				values[0][0] = random.nextFloat();
				values[mapSize-1][0] = random.nextFloat();
				values[0][mapSize-1] = random.nextFloat();
				values[mapSize-1][mapSize-1] = random.nextFloat();
				
				recStep(0, 0, mapSize-1, mapSize-1, 1);
				normalize();
				
				FileGenerator.generateFile(values);
			}
		}
	}

}
