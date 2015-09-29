package control;

import game.Direction;
import game.GameLogic;

import java.io.Serializable;

public class OrientAction implements NetworkAction, Serializable{
	int playerID;
	private boolean turnLeft;



	public OrientAction(int playerID, boolean turnLeft) {
		this.playerID = playerID;
		this.turnLeft = turnLeft;
	}

	@Override
	public void applyAction(GameLogic logic) {
		if(this.turnLeft)System.out.println("Orienting player "+ playerID + " left");
		else System.out.println("Orienting player "+ playerID + " right");
	}

}
