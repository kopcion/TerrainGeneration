package modules.sky;

import core.model.Mesh;
import core.utils.objloader.OBJLoader;
import core.buffers.MeshVBO;
import core.configs.CCW;
import core.renderer.RenderInfo;
import core.renderer.Renderer;
import core.scene.GameObject;
import core.utils.Constants;

public class Skydome extends GameObject{
	
	public Skydome()
	{
		getWorldTransform().setScaling(Constants.ZFAR*0.5f, Constants.ZFAR*0.5f, Constants.ZFAR*0.5f);
		
		// Loading model data into application memory
		Mesh mesh = new OBJLoader().load("./res/models/dome", "dome.obj", null)[0].getMesh();
		
		// Allocating mesh data into GPU memory
		MeshVBO meshBuffer = new MeshVBO();
		meshBuffer.allocate(mesh);
		
		Renderer renderer = new Renderer();
		renderer.setVbo(meshBuffer);
		renderer.setRenderInfo(new RenderInfo(new CCW(), AtmosphereShader.getInstance()));
		addComponent(Constants.RENDERER_COMPONENT, renderer);
		
	}
}
