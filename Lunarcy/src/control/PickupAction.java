package control;

import game.GameLogic;

import java.io.Serializable;

public class PickupAction implements NetworkAction, Serializable {

	int playerID;
	int containerID;
	int objectID;

	public PickupAction(int playerID, int containerID, int objectID) {
		this.playerID = playerID;
		this.containerID = containerID;
		this.objectID = objectID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getContainerID() {
		return containerID;
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
