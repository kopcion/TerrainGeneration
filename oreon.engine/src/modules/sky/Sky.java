package modules.sky;

import core.model.Mesh;
import core.utils.objloader.OBJLoader;
import core.buffers.MeshVertexBufferObject;
import core.configs.CCW;
import core.math.Vec3f;
import core.renderer.Renderer;
import core.scene.Node;
import core.utils.Constants;

public class Sky extends Node {
	
	public Sky()
	{
		getWorldTransform().setScale(new Vec3f(Constants.ZFAR*0.5f, Constants.ZFAR*0.5f, Constants.ZFAR*0.5f));
		
		// Loading model data into application memory using OBJLoader from engine template
		// generated dome object consisting only from one mesh that's why we get [0]th element
		Mesh mesh = new OBJLoader().load("./oreon.engine/res/models/dome", "dome.obj", null)[0].getMesh();
		
		// Storing mesh data in the GPU memory
		MeshVertexBufferObject meshBuffer = new MeshVertexBufferObject();
		meshBuffer.allocate(mesh);
		
		// Creating renderer object to be able to draw Sky
		Renderer renderer = new Renderer(meshBuffer);
		renderer.setRenderInfo(new CCW(), SkyShader.getInstance());
		addComponent(Constants.RENDERER_COMPONENT, renderer);
		
	}
}
