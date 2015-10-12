package network;

import game.GameLogic;
import java.io.Serializable;

public class OpenAction implements NetworkAction, Serializable {

	private static final long serialVersionUID = 1L;

	int playerID;

	public OpenAction(int playerID) {
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public boolean applyAction(GameLogic logic) {
		return logic.openContainer(playerID);
	}

}
