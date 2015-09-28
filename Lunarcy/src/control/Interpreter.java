package control;

import game.GameLogic;

public class Interpreter {
	
	GameLogic gameController;
	
	Interpreter(GameLogic gameController){
		this.gameController = gameController;
	}
	
	void interpret(DropAction dropAction){
		//GameController.movePlayer(player, direction)
		System.out.println("moved player");
	}
	
	void interpret(MoveAction action){
		
	}
	
	void interpret(PickupAction action){
		
	}
	
	void interpret(OrientAction action){
		
	}
	
}
