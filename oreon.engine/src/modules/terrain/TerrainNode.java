package modules.terrain;

import core.buffers.VerticesVertexBufferObject;
import core.configs.Default;
import core.kernel.Camera;
import core.math.Vec2f;
import core.math.Vec3f;
import core.renderer.Renderer;
import core.scene.Node;
import core.utils.Constants;


public class TerrainNode extends Node {
	
	private boolean isleaf;
	private TerrainConfiguration config;
	private int leverOfDetails;
	private Vec2f location;
	private Vec3f worldPosition;
	private Vec2f index;
	private float gap; // side length of a patch
	private VerticesVertexBufferObject buffer;
	
	public TerrainNode(VerticesVertexBufferObject buffer, TerrainConfiguration terrainConfig, Vec2f location, int lod, Vec2f index) {
		this.buffer = buffer;
		this.config = terrainConfig;
		this.index = index;
		this.leverOfDetails = lod;
		this.location = location;
		
		// calculating sidelength of a patch
		this.gap = 1f/((float)(Math.pow(2, lod) * Quadtree.getRootNodes()));
		
		// transforming node within a quadtree:
		Vec3f localScaling = new Vec3f(gap, 0, gap);
		Vec3f localTranslation = new Vec3f(location.getX(), 0, location.getY());
		
		getLocalTransform().setScale(localScaling);
		getLocalTransform().setTranslation(localTranslation);
		
		// transforming quad within the world space:
		getWorldTransform().setScale(new Vec3f(terrainConfig.getScaleXZ(), terrainConfig.getScaleY(), terrainConfig.getScaleXZ()));
		getWorldTransform().setTranslation(new Vec3f(-terrainConfig.getScaleXZ()/2f, 0, -terrainConfig.getScaleXZ()/2f));
	
		// creating renderer to draw terrain nodes:
		Renderer renderer = new Renderer(buffer);
		renderer.setRenderInfo(new Default(), TerrainShader.getInstance());
		addComponent(Constants.RENDERER_COMPONENT, renderer);
		
		computeWorldPos();
		updateQuadtree();
	}
	
	public void render() {
		// rendering leafs and childs of nodes:
		if(isleaf) {
			getComponents().get(Constants.RENDERER_COMPONENT).render();
		}
		
		for(Node child : getRelatedNodes()) {
			child.render();
		}
	}
	
	public void updateQuadtree() {
		if(Camera.getInstance().getPosition().getY() > config.getScaleY()) {
			worldPosition.setY(config.getScaleY());
		}
		else {
			worldPosition.setY(Camera.getInstance().getPosition().getY());
		}
		
		updateRelatedNodes();
		
		for(Node child : getRelatedNodes()) {    
			((TerrainNode) child).updateQuadtree();
		}
	}
	
	// Adding childNodes while increasing level of details:
	private void addRelatedNodes(int lod) {
		if(isleaf) {
			isleaf = false;
		}
		if(getRelatedNodes().size() == 0) {
			for(int i = 0; i < 2; i++) {
				for(int j = 0 ; j < 2; j++) {
					addNode(new TerrainNode(buffer, config, location.add(new Vec2f(i*gap/2f, j*gap/2f)), lod, new Vec2f(i, j)));
				}
			}
		}
	}
	
	// Removing relatedNodes when we want to make level of details smaller:
	private void removeRelatedNodes() {
		if(!isleaf) {
			isleaf = true;
		}
		
		if(getRelatedNodes().size() != 0) {
			getRelatedNodes().clear();
		}
	}
	
	// Updating nodes while camera movement
	private void updateRelatedNodes() {
		// getting camera distance 
		float distance = (Camera.getInstance().getPosition().sub(worldPosition)).length();
		
		if(distance >= config.getLod_range()[leverOfDetails]) {
			removeRelatedNodes();
			
		} else {
			addRelatedNodes(leverOfDetails+1);
		}
	}
	
	// Setting world position:
	public void computeWorldPos() {
		Vec2f loc = location.add(gap/2f).mul(config.getScaleXZ()).sub(config.getScaleXZ()/2f);
		
		worldPosition = new Vec3f(loc.getX(), 0, loc.getY());
	}
	
	//Getters:
	public TerrainConfiguration getConfig() {
		return config;
	}
	
	public int getLevelOfDetails() {
		return leverOfDetails;
	}

	public Vec2f getLocation() {
		return location;
	}
	
	public Vec3f getWorldPos() {
		return worldPosition;
	}
	
	public Vec2f getIndex() {
		return index;
	}
	
	public float getGap() {
		return gap;
	}
	
	public VerticesVertexBufferObject getBuffer() {
		return buffer;
	}
}
