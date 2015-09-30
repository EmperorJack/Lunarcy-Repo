package control;

import game.Direction;
import game.GameLogic;

import java.io.Serializable;

/**
 * A class for representing a players movement on the board,
 * Can be sent over the network
 * @author denforjohn
 *
 */
public class MoveAction implements NetworkAction, Serializable {

	int playerID;
	private Direction direction;

	/**
	 *
	 * @param playerID ID of the player to move
	 * @param direction Desired direction
	 */
	public MoveAction(int playerID, Direction direction) {
		this.playerID = playerID;
		this.direction = direction;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public void applyAction(GameLogic logic) {
		// TODO Auto-generated method stub
		System.out.println("moving player "+ playerID + " " + direction );
		logic.movePlayer(playerID, direction);
	}
}
