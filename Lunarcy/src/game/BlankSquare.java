package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a filler square that cannot be entered or contain items.
 *
 * @author Robbie
 *
 */
@SuppressWarnings("serial")
public class BlankSquare extends Square {

	public boolean canEnter(Player player, Direction direction) {
		return false;
	}

	public boolean canExit(Player player, Direction direction) {
		return false;
	}

	public boolean addPlayer(Player player) {
		return false;
	}

	public void removePlayer(Player player) {
	}

	public void tick() {
	}

	@Override
	public List<Entity> getEntities(Direction side) {
		return new ArrayList<Entity>();
	}
}
