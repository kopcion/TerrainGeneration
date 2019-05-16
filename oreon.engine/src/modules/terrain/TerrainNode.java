package modules.terrain;

import core.buffers.PatchVBO;
import core.configs.Default;
import core.kernel.Camera;
import core.math.Vec2f;
import core.math.Vec3f;
import core.renderer.RenderInfo;
import core.renderer.Renderer;
import core.scene.GameObject;
import core.scene.Node;
import core.utils.Constants;


public class TerrainNode extends GameObject {
	
	private boolean isleaf;
	private TerrainConfig config;
	private int lod;
	private Vec2f location;
	private Vec3f worldPos;
	private Vec2f index;
	private float gap;
	private PatchVBO buffer;
	
	
	public TerrainNode(PatchVBO buffer, TerrainConfig terrainConfig, Vec2f location, int lod, Vec2f index) {
		this.buffer = buffer;
		this.config = terrainConfig;
		this.index = index;
		this.lod = lod;
		this.location = location;
		this.gap = 1f/(TerrainQuadtree.getRootNodes() * (float)(Math.pow(2, lod)));
		
		Vec3f localScaling = new Vec3f(gap, 0, gap);
		Vec3f localTranslation = new Vec3f(location.getX(), 0, location.getY());
		
		getLocalTransform().setScaling(localScaling);
		getLocalTransform().setTranslation(localTranslation);
		
		getWorldTransform().setScaling(new Vec3f(terrainConfig.getScaleXZ(), terrainConfig.getScaleY(), terrainConfig.getScaleXZ()));
		getWorldTransform().setTranslation(new Vec3f(-terrainConfig.getScaleXZ()/2f, 0, -terrainConfig.getScaleXZ()/2f));
	
		Renderer renderer = new Renderer();
		renderer.setVbo(buffer);
		renderer.setRenderInfo(new RenderInfo(new Default(), TerrainShader.getInstance()));
		
		addComponent(Constants.RENDERER_COMPONENT, renderer);
		
		computeWorldPos();
		updateQuadtree();
	}
	
	public void render() {
		if(isleaf) {
			getComponents().get(Constants.RENDERER_COMPONENT).render();
		}
		
		for(Node child : getChildren()) {
			child.render();
		}
	}
	
	public void updateQuadtree() {
		if(Camera.getInstance().getPosition().getY() > config.getScaleY()) {
			worldPos.setY(config.getScaleY());
		}
		else {
			worldPos.setY(Camera.getInstance().getPosition().getY());
		}
		
		updateChildNodes();
		
		for(Node child : getChildren()) {
			((TerrainNode) child).updateQuadtree();
		}
	}
	
	private void addChildNodes(int lod) {
		if(isleaf) {
			isleaf = false;
		}
		
		if(getChildren().size() == 0) {
			for(int i = 0; i < 2; i++) {
				for(int j = 0 ; j < 2; j++) {
					addChild(new TerrainNode(buffer, config, location.add(new Vec2f(i*gap/2f, j*gap/2f)), lod, new Vec2f(i, j)));
				}
			}
		}
	}
	
	private void removeChildNodes() {
		if(!isleaf) {
			isleaf = true;
		}
		
		if(getChildren().size() != 0) {
			getChildren().clear();
		}
	}
	
	private void updateChildNodes() {
	
		float distance = (Camera.getInstance().getPosition().sub(worldPos)).length();
		if(distance < config.getLod_range()[lod]) {
			addChildNodes(lod+1);
		} else if(distance >= config.getLod_range()[lod]) {
			removeChildNodes();
		}
	}
	
	public void computeWorldPos() {
		Vec2f loc = location.add(gap/2f).mul(config.getScaleXZ()).sub(config.getScaleXZ()/2f);
		
		worldPos = new Vec3f(loc.getX(), 0, loc.getY());
	}
	
	public boolean isIsleaf() {
		return isleaf;
	}
	public void setIsleaf(boolean isleaf) {
		this.isleaf = isleaf;
	}
	public TerrainConfig getConfig() {
		return config;
	}
	public void setConfig(TerrainConfig config) {
		this.config = config;
	}
	public int getLod() {
		return lod;
	}
	public void setLod(int lod) {
		this.lod = lod;
	}
	public Vec2f getLocation() {
		return location;
	}
	public void setLocation(Vec2f location) {
		this.location = location;
	}
	public Vec3f getWorldPos() {
		return worldPos;
	}
	public void setWorldPos(Vec3f worldPos) {
		this.worldPos = worldPos;
	}
	public Vec2f getIndex() {
		return index;
	}
	public void setIndex(Vec2f index) {
		this.index = index;
	}
	public float getGap() {
		return gap;
	}
	public void setGap(float gap) {
		this.gap = gap;
	}
	public PatchVBO getBuffer() {
		return buffer;
	}
	public void setBuffer(PatchVBO buffer) {
		this.buffer = buffer;
	}
	
}
