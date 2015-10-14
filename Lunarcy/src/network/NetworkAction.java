package network;

import java.io.Serializable;

import game.GameLogic;
/**
 * A network action for passing between client and server
 *
 * @author denforjohn
 *
 */
public interface NetworkAction extends Serializable{
	/**
	 * Apply the action to the game logic
	 * @param logic The game logic on which to apply the action
	 * @return true if successfull
	 */
	boolean applyAction(GameLogic logic);
}
