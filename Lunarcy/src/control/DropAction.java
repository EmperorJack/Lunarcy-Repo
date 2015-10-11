package control;

import game.GameLogic;
import java.io.Serializable;

public class DropAction implements NetworkAction, Serializable {

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
