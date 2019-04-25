package modules.sky;

import core.scene.GameObject;
import core.shaders.Shader;
import core.utils.ResourceLoader;;

public class AtmosphereShader extends Shader {
	private static AtmosphereShader instance = null;
	
	public static AtmosphereShader getInstance() 
	{
		if(instance == null) 
		{
			instance = new AtmosphereShader();
		}
		return instance;
	}
		
	protected AtmosphereShader()
	{
		super();

		addVertexShader(ResourceLoader.loadShader("shaders/atmosphere_VS.glsl")); 
		addFragmentShader(ResourceLoader.loadShader("shaders/atmosphere_FS.glsl")); 
		compileShader();
		
		addUniform("m_MVP");
		addUniform("m_World");
	}
		
	public void updateUniforms(GameObject object)
	{
		setUniform("m_MVP", object.getTransform().getMVP());
		setUniform("m_World", object.getTransform().getWorldMatrix());
	}
}
