package modules.terrain;


import org.lwjgl.opengl.GL13;

import core.kernel.Camera;
import core.scene.Node;
import core.shaders.Shader;
import core.utils.ResourceLoader;

// Terrain Shader for drawing terrain using OpenGL:
public class TerrainShader extends Shader {
	
	private static TerrainShader instance = null;
	
	public static TerrainShader getInstance() {
		if(instance == null) {
			instance = new TerrainShader();
		}
		
		return instance;
	}
	
	protected TerrainShader() {
		super();
		// OpenGL Drawing cycle:
		// VertexShader -> TessellationControlShader -> TessellationEvaluationShader ->
		// -> GeometryShader -> FragmentShader
		
		// Adding paths to shaders OpenGL implementations:
		addVertexShader(ResourceLoader.loadShader("shaders/terrain/terrainVertexShader.glsl"));
		addTessellationControlShader(ResourceLoader.loadShader("shaders/terrain/terrainTessellationControlShader.glsl"));
		addTessellationEvaluationShader(ResourceLoader.loadShader("shaders/terrain/terrainTessellationEvaluationShader.glsl"));
		addGeometryShader(ResourceLoader.loadShader("shaders/terrain/terrainGeometryShader.glsl"));
		addFragmentShader(ResourceLoader.loadShader("shaders/terrain/terrainFragmentShader.glsl"));
		
		// Compiling OpenGL programs:
		compileShader();
	
		// Creating variables with matrixes to transform position of node in space:
		addUniform("localTransformMatrix");
		addUniform("worldTransformMatrix");
		
		// Creating variables with View Projection Matrix of Camera to set vertexes into the right place:
		addUniform("cameraViewProjection");
		
		// Creating variables as in TerrainNode class for morphing functions:
		addUniform("index");
		addUniform("gap");
		addUniform("levelOfDetails");
		addUniform("scaleY");
		addUniform("location");
		addUniform("cameraPosition");
		for (int i=0; i<8; i++){
			addUniform("levOfDetailsMorphArea[" + i + "]");
		}

		
		// Creating variables for tessellation properties:
		addUniform("tessellationFactor");
		addUniform("tessellationSlope");
		addUniform("tessellationShift");
		
		// Creating variables with maps for drawing them:
		addUniform("heightMap");
		addUniform("normalMapping");
		
	} 

	public void update(Node object)
	{	
		// Passing the data to uniform variables in OpenGL programs:
		
		setUniform("localTransformMatrix", object.getLocalTransform().getWorldTransformMatrix());
		setUniform("worldTransformMatrix", object.getWorldTransform().getWorldTransformMatrix());
		
		setUniform("cameraViewProjection", Camera.getInstance().getViewProjectionMatrix());
		
		TerrainNode terrainNode = (TerrainNode) object;
		setUniform("index", terrainNode.getIndex());
		setUniformf("gap", terrainNode.getGap());
		setUniformi("levelOfDetails", terrainNode.getLevelOfDetails());
		setUniformf("scaleY", terrainNode.getConfig().getScaleY());
		setUniform("location", terrainNode.getLocation());
		setUniform("cameraPosition", Camera.getInstance().getPosition());
		
		for (int i = 0; i<8; i++){
			setUniformi("levOfDetailsMorphArea[" + i + "]", terrainNode.getConfig().getLevelOfDetailsMorphingArea()[i]);
		}  
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		terrainNode.getConfig().getHeightmap().bind();
		setUniformi("heightMap", 0); 
		 
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		terrainNode.getConfig().getNormalmapping().bind();
		setUniformi("normalMapping", 1);
		
		
		setUniformi("tessellationFactor", terrainNode.getConfig().getTessellationFactor());
		setUniformf("tessellationSlope", terrainNode.getConfig().getTessellationSlope());
		setUniformf("tessellationShift", terrainNode.getConfig().getTessellationShift());
	}
}
 