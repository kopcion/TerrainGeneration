package modules.normalMapping;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import core.texturing.Texture2D;

public class NormalMappingRenderer {

	private Texture2D normalmap; // texture to store generated mapping
	private NormalMappingShader computeShader; // Shader to compute normal mapping:
	private int resolution;
	
	public NormalMappingRenderer(int resolution){
		
		this.resolution = resolution;
		normalmap = new Texture2D();
		computeShader = NormalMappingShader.getInstance();
		
		// generating texture 
		normalmap.generate();
		normalmap.bind();
		// using our function for linear transition
		normalmap.bilinearFilter();
		// storage for our texture
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, (int) (Math.log(resolution)/Math.log(2)), GL30.GL_RGBA32F, resolution, resolution);
	}
	
	public void render(Texture2D heightmap){
		
		computeShader.bind();
		computeShader.updateVariables(heightmap, resolution);
		
		GL42.glBindImageTexture(0, normalmap.getId(), 0, false, 0, GL15.GL_WRITE_ONLY, GL30.GL_RGBA32F);
		GL43.glDispatchCompute(resolution/16,resolution/16,1);
		GL11.glFinish();
		normalmap.bind();
		normalmap.bilinearFilter();
	}

	public Texture2D getNormalmap() {
		return normalmap;
	}
}