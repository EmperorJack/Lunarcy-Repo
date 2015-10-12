package network;

import game.GameLogic;

import java.io.Serializable;

public class OrientAction implements NetworkAction, Serializable{

	private static final long serialVersionUID = 1L;

	int playerID;
	private boolean turnLeft;

	public OrientAction(int playerID, boolean turnLeft) {
		this.playerID = playerID;
		this.turnLeft = turnLeft;
	}

	@Override
	public boolean applyAction(GameLogic logic) {
		if (turnLeft) {
			logic.turnPlayerLeft(playerID);
		} else {
			logic.turnPlayerRight(playerID);
		}
		return true;
	}

}
