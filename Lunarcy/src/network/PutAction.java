package network;

import game.GameLogic;
import java.io.Serializable;

public class PutAction implements NetworkAction, Serializable {

	private static final long serialVersionUID = 1L;

	int playerID;
	int itemID;
	int containerID;

	public PutAction(int playerID, int itemID, int containerID){
		this.playerID = playerID;
		this.itemID = itemID;
		this.containerID = containerID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getItemID() {
		return itemID;
	}

	public boolean applyAction(GameLogic logic){
		return logic.putItemIntoContainer(playerID, itemID, containerID);
	}
}
