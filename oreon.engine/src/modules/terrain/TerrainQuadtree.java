package modules.terrain;

import core.buffers.PatchVBO;
import core.math.Vec2f;
import core.math.Vec3f;
import core.scene.Node;

public class TerrainQuadtree extends Node {
	
	private static int rootNodes = 8;

	public TerrainQuadtree(TerrainConfig config) {
		
		PatchVBO buffer = new PatchVBO();
		buffer.allocate(generatePatch());
		
		for(int i = 0 ; i < rootNodes; i++) {
			for(int j = 0; j < rootNodes; j++) {
				addChild(new TerrainNode(buffer, config, new Vec2f(i/(float) rootNodes, j/(float) rootNodes), 0, new Vec2f(i, j)));
			}
		}
		
		getWorldTransform().setScaling(new Vec3f(config.getScaleXZ(), config.getScaleY(), config.getScaleXZ()));
		getWorldTransform().setTranslation(new Vec3f(config.getScaleXZ()/2f, 0, config.getScaleXZ()/2f));
	}
	
	public void updateQuadtree() {
		for(Node child: getChildren()) {
			((TerrainNode) child).updateQuadtree();
		}
	}
	
	public Vec2f[] generatePatch() {
		Vec2f[] vertices = new Vec2f[16];
		
		int index = 0;
		
		vertices[index++] = new Vec2f(0, 0);
		vertices[index++] = new Vec2f(0.333f, 0);
		vertices[index++] = new Vec2f(0.666f, 0);
		vertices[index++] = new Vec2f(1, 0);
		
		vertices[index++] = new Vec2f(0, 0.333f);
		vertices[index++] = new Vec2f(0.333f, 0.333f);
		vertices[index++] = new Vec2f(0.666f, 0.333f);
		vertices[index++] = new Vec2f(1, 0.333f);
		
		vertices[index++] = new Vec2f(0, 0.666f);
		vertices[index++] = new Vec2f(0.333f, 0.666f);
		vertices[index++] = new Vec2f(0.666f, 0.666f);
		vertices[index++] = new Vec2f(1, 0.666f);
		
		vertices[index++] = new Vec2f(0, 1);
		vertices[index++] = new Vec2f(0.333f, 1);
		vertices[index++] = new Vec2f(0.666f, 1);
		vertices[index++] = new Vec2f(1, 1);
		
		return vertices;
	}
	
	public static int getRootNodes() {
		return rootNodes;
	}

	public static void setRootNodes(int rootNodes) {
		TerrainQuadtree.rootNodes = rootNodes;
	}

}
