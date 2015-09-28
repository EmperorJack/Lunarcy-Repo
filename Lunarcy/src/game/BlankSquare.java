package game;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a filler square that cannot be entered or contain items.
 * @author Robbie
 *
 */
public class BlankSquare extends Square {
	
	@Override
	public boolean canEnter(Player player, Direction direction) {
		return false;
	}
	@Override
	public boolean addPlayer(Player player) {
		return false;
	}
	@Override
	public void removePlayer(Player player) {
	}

}
