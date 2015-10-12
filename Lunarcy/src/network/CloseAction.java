package network;

import game.GameLogic;
import java.io.Serializable;

public class CloseAction implements NetworkAction, Serializable {

	private static final long serialVersionUID = 1L;

	int playerID;

	public CloseAction(int playerID) {
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public boolean applyAction(GameLogic logic) {
		return logic.closeContainer(playerID);
	}

}
