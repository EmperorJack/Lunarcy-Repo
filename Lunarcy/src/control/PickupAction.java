package control;

import game.GameLogic;

import java.io.Serializable;

public class PickupAction implements NetworkAction, Serializable {

	int playerID;
	int itemID;

	//int containerID;
	//int objectID;

	public PickupAction(int playerID, int itemID){
		this.playerID = playerID;
		this.itemID = itemID;
	}

	/*public PickupAction(int playerID, int containerID, int objectID) {
		this.playerID = playerID;
		this.containerID = containerID;
		this.objectID = objectID;
	}
	 */
	public int getPlayerID() {
		return playerID;
	}

	public int getItemID() {
		return itemID;
	}

	/*public int getContainerID() {
		return containerID;
	}

	public int getObjectID() {
		return objectID;
	}*/

	@Override
	public boolean applyAction(GameLogic logic) {
		return logic.pickUpItem(playerID, itemID);
	}
}
