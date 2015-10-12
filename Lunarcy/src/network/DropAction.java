package network;

import game.GameLogic;
import java.io.Serializable;

public class DropAction implements NetworkAction, Serializable {

	private static final long serialVersionUID = 1L;

	int playerID;
	int itemID;

	public DropAction(int playerID, int itemID) {
		this.playerID = playerID;
		this.itemID = itemID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getItemID() {
		return itemID;
	}

	public boolean applyAction(GameLogic logic) {
		return logic.dropItem(playerID, itemID);
	}
}
