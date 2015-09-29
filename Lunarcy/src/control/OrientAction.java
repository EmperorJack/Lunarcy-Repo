package control;

import game.Direction;
import game.GameLogic;

import java.io.Serializable;

public class OrientAction implements NetworkAction, Serializable{
	int playerID;
	private Direction direction;
	
	
	
	public OrientAction(int playerID, Direction direction) {
		this.playerID = playerID;
		this.direction = direction;
	}

	/*public int getPlayerID() {
		return playerID;
	}

	public Direction getDirection() {
		return direction;
	}*/
	
	@Override
	public void applyAction(GameLogic logic) {
		// TODO apply logic for orienting player
		System.out.println("Orienting player "+ playerID + "towards" + direction);
	}

}
