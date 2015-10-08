package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a filler square that cannot be entered or contain items.
 *
 * @author Robbie
 *
 */
public class BlankSquare extends Square {
	private static final long serialVersionUID = -2721273025234939894L;

	public boolean canEnter(Character character, Direction direction) {
		return false;
	}

	public boolean canExit(Character character, Direction direction) {
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
