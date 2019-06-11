package modules.normalMapping;

import org.lwjgl.opengl.GL13;
import core.shaders.Shader;
import core.texturing.Texture2D;
import core.utils.ResourceLoader;

public class NormalMappingShader extends Shader{

	private static NormalMappingShader instance = null;

	public static NormalMappingShader getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new NormalMappingShader();
	    }
	      return instance;
	}
	
	protected NormalMappingShader()
	{
		super();
		
		// Adding path to OpenGL program for rendering Nnrmal mapping
		addComputeShader(ResourceLoader.loadShader("shaders/normalMapping/NormalMapping.glsl"));
		compileShader();
		
		// Adding variables we need for computing shader
		addUniform("heightMap");
		addUniform("resolution");
	}
	
	public void updateVariables(Texture2D heightmap, int resolution)
	{
		// Updating variables
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		heightmap.bind();
		setUniformi("heightMap", 0);
		setUniformi("resolution", resolution);
	}
}