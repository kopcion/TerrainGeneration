package modules.terrain;

import core.kernel.Camera;
import core.scene.Node;

public class Terrain extends Node {
	
	private TerrainConfiguration configuration;
	
	public void init() {
		configuration = new TerrainConfiguration();
		
		addNode(new Quadtree(configuration));
	}

	// Updating terrain is updating only one related node which is the main Quadtree:
	public void updateQuadtree() {
		if(Camera.getInstance().isCameraMoved()) {
			((Quadtree) getRelatedNodes().get(0)).updateQuadtree();
		}
	}

	public TerrainConfiguration getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(TerrainConfiguration configuration) {
		this.configuration = configuration;
	}
}
