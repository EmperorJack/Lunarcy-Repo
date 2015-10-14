package network;

import game.GameLogic;
/**
 * A network message for removing a player from the gamestate
 * @author denforjohn
 *
 */
public class RemovePlayer implements NetworkAction {

	private static final long serialVersionUID = 1L;
	int id;

	RemovePlayer(int id){
		this.id = id;
	}

	@Override
	public boolean applyAction(GameLogic logic) {
		logic.getGameState().removePlayer(id);
		return true;
	}

}
