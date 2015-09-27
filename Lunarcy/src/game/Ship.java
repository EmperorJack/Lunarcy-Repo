package game;

public class Ship extends WalkableSquare {

	// public Ship(Set<ShipPart> parts) {
	public Ship() {
		super("Escape Ship", "Find all the parts to repair the ship", false,
				null, null, null, null);
	}

	/**
	 * Checks if the Player has all of the required parts, if they do then flag
	 * that the game has been won otherwise just let them enter.
	 * 
	 * @param player
	 *            The player that is attempting to enter
	 * @param direction
	 *            The direction the player is entering the room *FROM*
	 * @return True if player entered the room, False otherwise
	 * @throws IllegalArgumentException
	 *             if either argument is null
	 */
	public boolean enter(Player player, Direction direction) {
		if (player == null)
			throw new IllegalArgumentException(
					"Parameter 'player' may not be null");
		if (direction == null)
			throw new IllegalArgumentException(
					"Parameter 'direction' may not be null");
		// TODO if player has all parts the win the game
		return true;
	}
}
