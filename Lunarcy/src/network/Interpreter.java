package network;

import game.GameLogic;

public class Interpreter {
	
	GameLogic gameController;
	
	Interpreter(GameLogic gameController){
		this.gameController = gameController;
	}

	public void interpret(NetworkAction action) {
		System.out.println("processing action");
		action.applyAction(gameController);
	}
}
