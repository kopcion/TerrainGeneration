package core.kernel;

import core.kernel.Window;
import modules.sky.Sky;
import modules.terrain.Terrain;
import core.configs.Default;
import core.kernel.Camera;

/**
 * 
 * @author oreon3D
 * The RenderingEngine manages the render calls of all 3D entities
 * with shadow rendering and post processing effects
 *
 */
public class RenderingEngine {
	
	private Window window;
	private Sky sky;
	private Terrain terrain;
	

	public RenderingEngine()
	{
		window = Window.getInstance();
		sky = new Sky();
		terrain = new Terrain();
	}
	
	public void init()
	{
		window.init();
		terrain.init();
	}

	public void render()
	{	
		Camera.getInstance().update();
		
		Default.clearScreen();
		
		sky.render();
		terrain.updateQuadtree();
		terrain.render();
		
		// draw into OpenGL window
		window.render();
	}
	
	public void update(){}
	
	public void shutdown(){}
}
