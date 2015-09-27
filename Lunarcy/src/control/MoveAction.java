package control;

import java.io.Serializable;

public class MoveAction implements NetworkAction, Serializable {
	int playerID;
	private int destX;
	private int destY;
	
	public MoveAction(int playerID, int destX, int destY) {
		this.playerID = playerID;
		this.destX = destX;
		this.destY = destY;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getDestX() {
		return destX;
	}

	public int getDestY() {
		return destY;
	}

}
