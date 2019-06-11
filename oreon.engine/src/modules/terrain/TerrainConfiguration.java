package modules.terrain;

import core.texturing.Texture2D;
import modules.normalMapping.NormalMappingRenderer;

public class TerrainConfiguration {
	
	private float scaleY; // vertical scale
	private float scaleXZ; // horizontal scale
	
	private Texture2D heightmap; // map with hights as bmp file
	private Texture2D normalmapping; // texture for covering our space
	
	private final int tessellationFactor = 600;
	private final float tessellationSlope = (float) 1.8;
	private final float tessellationShift = (float) 0.3;
	
	// Map with level of details areas:
	private int[] levelOfDetailsRanges = new int[8];
	private int[] levelOfDetailsMorphingAreas = new int[8];
	
	public TerrainConfiguration() {
		
		this.scaleY = 600;
		this.scaleXZ = 6000;
		
		this.heightmap = new Texture2D("./res/heightmap/2.bmp");	
		this.heightmap.bind();
		this.heightmap.bilinearFilter();
		
		// generating normal map using defined renderer and heightmap
		NormalMappingRenderer normalRenderer = new NormalMappingRenderer(this.heightmap.getWidth());
		normalRenderer.render(this.heightmap);
		
		setNormalmapping(normalRenderer.getNormalmap());
		
		int[] ranges = {1750, 874, 386, 192, 100, 50, 0, 0};
		for(int i = 0; i < 8; i++) {
			setLodRange(i, ranges[i]);
		}
	}
	
	private int updateMorphingArea(int lod) {
		return (int) ((scaleXZ/Quadtree.getRootNodes()) / (Math.pow(2, lod)));
	}

	private void setLodRange(int index, int lod_range) {
		this.levelOfDetailsRanges[index] = lod_range;
		this.levelOfDetailsMorphingAreas[index] = lod_range - updateMorphingArea(index + 1);
	}
	
	// Getters:
	
	public float getScaleY() {
		return scaleY;
	}

	public float getScaleXZ() {
		return scaleXZ;
	}

	public int[] getLod_range() {
		return levelOfDetailsRanges;
	}
	
	public int[] getLevelOfDetailsMorphingArea() {
		return levelOfDetailsMorphingAreas;
	}
	
	public Texture2D getHeightmap() {
		return heightmap;
	}

	public int getTessellationFactor() {
		return tessellationFactor;
	}

	public float getTessellationSlope() {
		return tessellationSlope;
	}

	public float getTessellationShift() {
		return tessellationShift;
	}

	public Texture2D getNormalmapping() {
		return normalmapping;
	}

	public void setNormalmapping(Texture2D normalmap) {
		this.normalmapping = normalmap;
	}
}
