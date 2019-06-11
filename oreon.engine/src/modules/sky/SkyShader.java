package modules.sky;

import core.scene.Node;
import core.shaders.Shader;
import core.utils.ResourceLoader;;

// Shader for Sky to be able to generate Sky in OpenGL
public class SkyShader extends Shader {
	
	private static SkyShader instance = null;
	
	public static SkyShader getInstance() 
	{
		if(instance == null) 
		{
			instance = new SkyShader();
		}
		return instance;
	}
		
	protected SkyShader()
	{
		super();
		
		// VertexShader -> FragmentShader
		
		// Adding OpenGL files to generate Vertexes of sky and to color them:
		addVertexShader(ResourceLoader.loadShader("shaders/sky/atmosphereVertexShader.glsl")); 
		addFragmentShader(ResourceLoader.loadShader("shaders/sky/atmosphereFragmentShader.glsl")); 
		compileShader();
		
		// Adding variables to our OpenGL VertexShader to be able to generate Vertexes in right place: 
		addUniform("modelViewProjectionMatrix");
		addUniform("worldMatrix");
	}
	
	public void update(Node object)
	{
		// Updating variables:
		setUniform("modelViewProjectionMatrix", object.getWorldTransform().getModelViewProjectionMatrix());
		setUniform("worldMatrix", object.getWorldTransform().getWorldTransformMatrix());
	}
	
}
