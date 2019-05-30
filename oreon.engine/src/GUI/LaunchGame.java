package GUI;

import core.kernel.Game;

public class LaunchGame{

	public static void launch(){
		Game game = new Game();
		game.getEngine().createWindow(1600, 1400);
		game.init();
		game.launch();
	}
}
