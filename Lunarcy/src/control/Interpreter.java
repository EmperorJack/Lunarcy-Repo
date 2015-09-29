package control;

import game.GameLogic;

public class Interpreter {
	
	GameLogic gameController;
	
	Interpreter(GameLogic gameController){
		this.gameController = gameController;
	}

	public void interpret(NetworkAction action) {
		action.applyAction(gameController);
	}
}
