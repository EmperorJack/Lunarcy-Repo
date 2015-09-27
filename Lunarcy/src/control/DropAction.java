package control;

import java.io.Serializable;

public class DropAction implements NetworkAction, Serializable {
	
	int playerID;
	int itemID;
	
	public int getPlayerID() {
		return playerID;
	}

	public int getItemID() {
		return itemID;
	}
}
