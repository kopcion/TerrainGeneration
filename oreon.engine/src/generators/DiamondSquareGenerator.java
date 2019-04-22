package generators;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class DiamondSquareGenerator implements HightMapGenerator{
	static final Random random = new Random();
	private static float jitterEnabled = 0f;
	static float multiplier;
	private Boolean[][] isSet;
	private float[][] values;
	//private float jitterMult;
	private Queue<Args> queue = new LinkedList<>();
	
	private class Args{
		public Args(int i1, int j1, int i2, int j2, float jitterMult) {
			this.i1 = i1;
			this.j1 = j1;
			this.i2 = i2;
			this.j2 = j2;
			this.jitterMult = jitterMult;
		}

		int i1;
		int j1;
		int i2;
		int j2;
		float jitterMult;
	}
	
	private void clear() {
		for(int i=0; i < isSet.length; i++) {
			for(int j=0; j < isSet[0].length; j++) {
				isSet[i][j] = false;
			}
		}
	}
	
	
	private void recStep() {
		while(!queue.isEmpty()) {
			int i1 = queue.peek().i1;
			int j1 = queue.peek().j1;
			int i2 = queue.peek().i2;
			int j2 = queue.peek().j2;
			float jitterMult = queue.peek().jitterMult;
			queue.remove();
			int iMid = (i1 + i2) / 2;
			int jMid = (j1 + j2) / 2;
			
			if(!isSet[iMid][jMid]) {
				values[iMid][jMid] = ( values[i1][j1] + values[i1][j2] + values[i2][j1] + values[i2][j2] ) / 4 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;	
			}
			
			if(!isSet[iMid][j1]) {
				if(2* j1 - jMid >= 0) {
					values[iMid][j1] = ( values[i1][j1] + values[iMid][jMid] + values[i2][j1] + values[iMid][2* j1 - jMid] ) / 4 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
				} else {
					values[iMid][j1] = ( values[i1][j1] + values[iMid][jMid] + values[i2][j1] ) / 3 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;	
				}
			}
			
			if(!isSet[i1][jMid]) {
				if(2 * i1 - iMid >= 0) {
					values[i1][jMid] = ( values[i1][j1] + values[iMid][jMid] + values[i1][j2] + values[2 * i1 - iMid][jMid] ) / 4 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
				} else {
					values[i1][jMid] = ( values[i1][j1] + values[iMid][jMid] + values[i1][j2] ) / 3 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;	
				}
			}
			
			if(!isSet[i2][jMid]) {
				if(2 * i2 - iMid < values.length) {
					values[i2][jMid] = ( values[i2][j1] + values[iMid][jMid] + values[i2][j2] + values[2 * i2 - iMid][jMid] ) / 4 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
				} else {
					values[i2][jMid] = ( values[i2][j1] + values[iMid][jMid] + values[i2][j2] ) / 3 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;	
				}
			}
			
			if(!isSet[iMid][j2]) {
				if(2 * j2 - jMid < values[0].length) {
					values[iMid][j2] = ( values[i1][j2] + values[iMid][jMid] + values[i2][j2] + values[iMid][2 * j2 - jMid] ) / 4 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;
				} else {
					values[iMid][j1] = ( values[i1][j2] + values[iMid][jMid] + values[i2][j2] ) / 3 + jitterEnabled * (random.nextFloat() - 0.5f) / jitterMult;	
				}
			}
			
			
			
			isSet[iMid][jMid] = isSet[iMid][j1] = isSet[iMid][j2] = isSet[i1][jMid] = isSet[i2][jMid] = true;
			
			
			values[iMid][j1] = Math.min(values[iMid][j1], 1f);
			values[iMid][j2] = Math.min(values[iMid][j2], 1f);
			values[i1][jMid] = Math.min(values[i1][jMid], 1f);
			values[i2][jMid] = Math.min(values[i2][jMid], 1f);
			values[iMid][j1] = Math.max(values[iMid][j1], 0f);
			values[iMid][j2] = Math.max(values[iMid][j2], 0f);
			values[i1][jMid] = Math.max(values[i1][jMid], 0f);
			values[i2][jMid] = Math.max(values[i2][jMid], 0f);
			values[iMid][jMid] = Math.min(values[iMid][jMid], 1f);
			values[iMid][jMid] = Math.max(values[iMid][jMid], 0f);
			
			if(j2 - j1 != 2) {
				queue.add(new Args(i1, j1, iMid, jMid, jitterMult*multiplier));
				queue.add(new Args(i1, jMid, iMid, j2, jitterMult*multiplier));
				queue.add(new Args(iMid, j1, i2, jMid, jitterMult*multiplier));
				queue.add(new Args(iMid, jMid, i2, j2, jitterMult*multiplier));
			}
		}
	}
	

	
	@Override
	public void generate(int mapSize) {
		this.isSet = new Boolean[mapSize][mapSize];
		this.values = new float[mapSize][mapSize];
		
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
				
				queue.add(new Args(0, 0, mapSize-1, mapSize-1, 0.5f));
				recStep();
				
				FileGenerator.generateFile(values);
			}
		}
		
	}

}
