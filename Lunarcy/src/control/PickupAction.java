package control;

import game.GameLogic;

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

	@Override
	public void applyAction(GameLogic logic) {
		// TODO Auto-generated method stub
		System.out.println("Player " + playerID + " picking up " + objectID);
	}
}
