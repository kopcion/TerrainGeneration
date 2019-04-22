package generators;

import java.util.Random;

public class MidPointDisplacementGenerator implements HightMapGenerator{
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
	
	private void recStep(int i1, int j1, int i2, int j2, float jitterMult) {
		int iMid = (i1 + i2) / 2;
		int jMid = (j1 + j2) / 2;
		if(!isSet[iMid][j1]) values[iMid][j1] = ( values[i1][j1] + values[i2][j1] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		if(!isSet[iMid][j2]) values[iMid][j2] = ( values[i1][j2] + values[i2][j2] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		if(!isSet[i1][jMid]) values[i1][jMid] = ( values[i1][j1] + values[i1][j2] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		if(!isSet[i2][jMid]) values[i2][jMid] = ( values[i2][j1] + values[i2][j2] ) / 2 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		isSet[iMid][jMid] = isSet[iMid][j1] = isSet[iMid][j2] = isSet[i1][jMid] = isSet[i2][jMid] = true;
		
		
		
		values[iMid][jMid] = ( values[iMid][j1] + values[iMid][j2] + values[i1][jMid] + values[i2][jMid]) / 4 + jitterEnabled * (random.nextFloat() - 0.5f)/jitterMult;
		
		values[iMid][j1] = Math.min(values[iMid][j1], 0.99f);
		values[iMid][j2] = Math.min(values[iMid][j2], 0.99f);
		values[i1][jMid] = Math.min(values[i1][jMid], 0.99f);
		values[i2][jMid] = Math.min(values[i2][jMid], 0.99f);
		values[iMid][j1] = Math.max(values[iMid][j1], 0f);
		values[iMid][j2] = Math.max(values[iMid][j2], 0f);
		values[i1][jMid] = Math.max(values[i1][jMid], 0f);
		values[i2][jMid] = Math.max(values[i2][jMid], 0f);
		values[iMid][jMid] = Math.min(values[iMid][jMid], 0.99f);
		values[iMid][jMid] = Math.max(values[iMid][jMid], 0f);
		
		
		if(j2 - j1 != 2) {
			recStep(i1, j1, iMid, jMid, jitterMult*multiplier);
			recStep(i1, jMid, iMid, j2, jitterMult*multiplier);
			recStep(iMid, j1, i2, jMid, jitterMult*multiplier);
			recStep(iMid, jMid, i2, j2, jitterMult*multiplier);
		}
	}
	
	@Override
	public void generate(int mapSize) {
		this.isSet = new Boolean[mapSize][mapSize];
		// TODO Auto-generated method stub
		//this.values = new float[1025][1025];
		/*
		 * System.out.println("Generated"); for(int i=0; i < 5; i++) { multiplier =
		 * 1.4f; this.values = new float[mapSize][mapSize]; values[0][0] = 0f;
		 * values[mapSize-1][0] = 0.5f; values[0][mapSize-1] = 0.5f;
		 * values[mapSize-1][mapSize-1] = 1.0f;
		 * 
		 * recStep(0, 0, mapSize-1, mapSize-1, 1);
		 * 
		 * FileGenerator.generateFile(values);
		 * 
		 * } System.out.println("Generated");
		 */
		for(int k=0; k < 20; k++) {
			for(int i=0; i < 3; i++) {
				this.values = new float[mapSize][mapSize];
				clear();
				jitterEnabled = Config.JITTER_ENABLED;
				multiplier = 1.65f + k*0.15f;
				values[0][0] = random.nextFloat();
				values[mapSize-1][0] = random.nextFloat();
				values[0][mapSize-1] = random.nextFloat();
				values[mapSize-1][mapSize-1] = random.nextFloat();
				
				recStep(0, 0, mapSize-1, mapSize-1, 1);
				
				FileGenerator.generateFile(values);
			}
		}
		/*
		for(int k=0; k < 20; k++) {
			for(int i=0; i < 3; i++) {
				multiplier = 1.65f + k*0.15f;
				this.values = new float[mapSize][mapSize];
				values[0][0] = 0f;
				values[mapSize-1][0] = 0.5f;
				values[0][mapSize-1] = 0.5f;
				values[mapSize-1][mapSize-1] = 1.0f;
				
				recStep(0, 0, mapSize-1, mapSize-1, 1);
				
				FileGenerator.generateFile(values);
			}
		}
		System.out.println("Finished first");
		
		for(int k=0; k < 20; k++) {
			for(int i=0; i < 3; i++) {
				multiplier = 1.65f + k*0.15f;
				this.values = new float[mapSize][mapSize];
				values[0][0] = 0f;
				values[mapSize-1][0] = 0.5f;
				values[0][mapSize-1] = 0.5f;
				values[mapSize-1][mapSize-1] = 1.0f;
				
				recStep(0, 0, mapSize-1, mapSize-1, 2);
				
				FileGenerator.generateFile(values);
			}
		}
		System.out.println("Finished second");
		
		for(int k=0; k < 20; k++) {
			for(int i=0; i < 3; i++) {
				multiplier = 1.65f + k*0.15f;
				this.values = new float[mapSize][mapSize];
				values[0][0] = 0f;
				values[mapSize-1][0] = 0.5f;
				values[0][mapSize-1] = 0.5f;
				values[mapSize-1][mapSize-1] = 1.0f;
				
				recStep(0, 0, mapSize-1, mapSize-1, 4);
				
				FileGenerator.generateFile(values);
			}
		}*/
		
		System.out.println("Finished all");
	}

}
