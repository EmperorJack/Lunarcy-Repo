package control;

import java.io.Serializable;

public class PickupAction implements NetworkAction, Serializable {
	
	int playerID;
	int objectID;
	
	public PickupAction(int playerID, int objectID) {
		this.playerID = playerID;
		this.objectID = objectID;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public int getObjectID() {
		return objectID;
	}
}
