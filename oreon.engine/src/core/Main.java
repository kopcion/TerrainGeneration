package core;

import core.kernel.*;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		game.getEngine().createWindow(600, 400);
		game.init();
		game.launch();
	}
}
