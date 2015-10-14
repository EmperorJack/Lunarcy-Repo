package game;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Ship extends WalkableSquare {
	private static final long serialVersionUID = 8565928672749773866L;

	private Set<ShipPart> requiredParts;
	private boolean hasLaunched;
	private Player pilot;

	public Ship(ShipPart... parts) {
		super(true, null, null,
				null, null);
		hasLaunched = false;
		pilot = null;
		requiredParts = new HashSet<ShipPart>();

		for (ShipPart part : parts) {
			requiredParts.add(part);
		}
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

		if (playerHasWinningParts(player)) {
			hasLaunched = true;
			// Make sure we don't overwrite the first player to win
			pilot = pilot == null ? player : pilot;
		}
		return super.addPlayer(player);
	}

	/**
	 * Ignores the walls on the square and only lets a player enter if they have
	 * the required parts
	 */
	public boolean canEnter(Character character, Direction direction) {
		if (character == null || direction == null
				|| !(character instanceof Player)) {
			return false;
		}
		return playerHasWinningParts((Player) character);
	}

	public boolean canExit(Character character, Direction direction) {
		return false;
	}

	private boolean playerHasWinningParts(Player player) {
		boolean hasParts = true;
		List<ShipPart> playerParts = player.getShipParts();

		for (ShipPart reqPart : requiredParts) {
			boolean hasPart = false;
			for (ShipPart part : playerParts) {
				// Cannot use equals() method as that checks EntityID
				if (part.getTypeID() == reqPart.getTypeID()) {
					hasPart = true;
					break;
				}
			}
			if (!hasPart) {
				// Player is missing a part, no need to keep checking
				hasParts = false;
				break;
			}
		}
		return hasParts;
	}

	/**
	 * @return True if a winning player has been added to the square, False if
	 *         no winner yet
	 */
	public boolean hasLaunched() {
		return hasLaunched;
	}

	/**
	 * @return The winning player if there is one, Null if not
	 */
	public Player getPilot() {
		return pilot;
	}

	public Set<ShipPart> getParts() {
		return new HashSet<>(requiredParts);
	}
}
