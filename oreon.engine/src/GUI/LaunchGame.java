package GUI;

import core.kernel.Game;

public class LaunchGame{

	static void launch (){
		Game game = new Game();
		game.getEngine().createWindow(1400, 1000);
		game.init();
		game.launch();
	}
}
