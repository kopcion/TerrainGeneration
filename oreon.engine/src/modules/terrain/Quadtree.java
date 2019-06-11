package modules.terrain;

import core.buffers.VerticesVertexBufferObject;
import core.math.Vec2f;

import core.scene.Node;

public class Quadtree extends Node {
	
	private static int rootNodes = 8;

	public Quadtree(TerrainConfiguration config) {
		// Creating patch of 16 vertices on 1x1 square:
		Vec2f[] generatedVertices = new Vec2f[16];
		
		int index = 0;
		
		float[] verticesOfPatch = {0, 0.333f, 0.666f, 1};
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				generatedVertices[index++] = new Vec2f(verticesOfPatch[j], verticesOfPatch[i]);
			}
		}
		
		// Saving our vertices in buffer of GPU memory:
		VerticesVertexBufferObject buffer = new VerticesVertexBufferObject();
		buffer.allocate(generatedVertices);
		
		// Adding terrain nodes in 8 directions for each 8 nodes:
		for(int i = 0 ; i < rootNodes; i++) {
			for(int j = 0; j < rootNodes; j++) {
				addNode(new TerrainNode(buffer, config, new Vec2f(i/(float) rootNodes, j/(float) rootNodes), 0, new Vec2f(i, j)));
			}
		}
		
	}
	
	// method for updating vertices movement 
	public void updateQuadtree() {
		for(Node child: getRelatedNodes()) {
			((TerrainNode) child).updateQuadtree();
		}
	}
	
	public static int getRootNodes() {
		return rootNodes;
	}
}
