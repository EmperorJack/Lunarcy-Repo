package game;

import java.util.Set;

public class Ship extends WalkableSquare {
	private static final long serialVersionUID = 8565928672749773866L;

	private Set<ShipPart> requiredParts;

	public Ship(Set<ShipPart> parts) {
		super("Escape Ship", "Find all the parts to repair the ship", false,
				null, null, null, null);
		requiredParts = parts;
	}

	/**
	 * Checks if the Player has all of the required parts, if they do then flag
	 * that the game has been won otherwise add them to the Square.
	 *
	 * @param player
	 *            The player that is attempting to enter
	 * @param direction
	 *            The direction the player is entering the room *FROM*
	 * @return True if player entered the room, False otherwise
	 */
	public boolean addPlayer(Player player) {
		if (player == null)
			return false;

		boolean hasParts = true;
		for(ShipPart part: requiredParts){
			if(!player.getInventory().contains(part)){
				hasParts = false;
			}
		}
		if(hasParts){
			// TODO if player has all parts the win the game
		}

		return super.addPlayer(player);
	}
}
