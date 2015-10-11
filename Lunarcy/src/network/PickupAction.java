package network;

import game.GameLogic;

import java.io.Serializable;

public class PickupAction implements NetworkAction, Serializable {

	int playerID;
	int itemID;

	public PickupAction(int playerID, int itemID){
		this.playerID = playerID;
		this.itemID = itemID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getItemID() {
		return itemID;
	}

	@Override
	public boolean applyAction(GameLogic logic) {
		return logic.pickUpItem(playerID, itemID);
	}
}
