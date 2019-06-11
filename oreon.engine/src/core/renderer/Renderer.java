package core.renderer;

import core.buffers.VertexBufferObject;
import core.configs.RenderConfig;
import core.scene.Component;
import core.shaders.Shader;

//Renderer class for storing data (VertexBufferObject) and drawing it
public class Renderer extends Component {
	
	private RenderConfig config;
	private Shader shader;
	private VertexBufferObject vertexBufferObject;
	
	public Renderer(VertexBufferObject vbo)
	{
		this.vertexBufferObject = vbo;
	}
	
	public void render(){
		this.config.enable();
		this.shader.bind();	
		this.shader.update(getParent());
		this.vertexBufferObject.draw();
		this.config.disable();
	};

	public void setRenderInfo(RenderConfig config, Shader shader) {
		this.config = config;
		this.shader = shader;
	}
}