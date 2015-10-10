package control;

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
		System.out.println(playerID + " picked up item: " + itemID);
		return logic.pickUpItem(playerID, itemID);
	}
}
