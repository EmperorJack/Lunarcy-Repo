package game;

import java.util.HashSet;
import java.util.Set;

import bots.Rover;

/**
 * This class controls the interaction between input and the GameState
 *
 * @author Robbie
 *
 */
public class GameLogic {
	private GameState state;

	public GameLogic(GameState state) {
		this.state = state;
	}

	/**
	 * Checks if a player can enter a square and if they can will move them into
	 * that square
	 *
	 * @param playerId
	 *            The ID of the Player to be moved
	 * @param direction
	 *            The direction to move the Player in
	 * @return True if the player was moved, False if they were not
	 */
	public boolean movePlayer(int playerID, Direction direction) {
		if (direction == null)
			return false;
		Player player = state.getPlayer(playerID);
		if (player == null)
			return false;

		Square src = state.getSquare(player.getLocation());
		Square dest = state.getSquare(player.getLocation().getAdjacent(direction));

		if(dest != null && src != null && src.canExit(player,direction) && dest.canEnter(player, direction.opposite())){
			src.removePlayer(player);
			dest.addPlayer(player);
			player.move(direction);
			return true;
		}
		return false;
	}

	/**
	 * Turn a player to the left of their current orientation
	 *
	 * @param playerID
	 *            the ID of the player to be turned
	 */
	public void turnPlayerLeft(int playerID) {
		state.getPlayer(playerID).turnLeft();
	}

	/**
	 * Turn a player to the right of their current orientation
	 *
	 * @param playerID
	 *            the ID of the player to be turned
	 */
	public void turnPlayerRight(int playerID) {
		state.getPlayer(playerID).turnRight();
	}

	/**
	 * Picks up the item corresponding to the itemID and gives it to the Player.
	 * Will only take the Item if it is in the same Square as Player and is on
	 * the side they are facing.
	 *
	 * @param player
	 *            The Player that is picking up the Item
	 * @param itemID
	 *            The entityID of the Item to pick up
	 * @return True if Item was picked up, false otherwise (Invalid playerID or
	 *         itemID)
	 */
	public boolean pickUpItem(int playerID, int itemID) {
		Player player = state.getPlayer(playerID);
		if (player == null || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			// Check all Items/Containers in the square to find the matching
			// item
			for (Entity e : wSquare.getEntities(player.getOrientation())) {
				if (e instanceof Item && e.entityID == itemID) {
					// Found the matching item
					Item item = wSquare.takeItem(player.getOrientation(),
							(Item) e);
					return player.giveItem(item);
				} else if (e instanceof Container) {
					// Have to check if the container has the item and can be
					// accessed
					Container container = (Container) e;
					if (container.hasItem(itemID)) {
						Item item = container.takeItem(player, itemID);
						if (item != null) {
							return player.giveItem(item);
						} else {
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Drops the specified Item from the Players inventory into the Square at
	 * the Players current location, in the Direction the Player is facing.
	 *
	 * @param player
	 *            The Player that is dropping the item
	 * @param itemID
	 *            The entityID of the Item being dropped
	 * @return True if the Item was dropped, false otherwise (Invalid playerID
	 *         or itemID)
	 */
	public boolean dropItem(int playerID, int itemID) {
		Player player = state.getPlayer(playerID);
		if (player == null || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			Item item = player.removeItem(itemID);
			if (item != null) {
				WalkableSquare wSquare = (WalkableSquare) square;
				wSquare.addEntity(player.getOrientation(), item);
			}
		}
		return false;
	}

	/**
	 * Puts the item corresponding to the itemID into a container. Will only put
	 * the Item if the container is in the same Square as Player and if it is on
	 * the side they are facing.
	 *
	 * @param player
	 *            The Player that is picking up the Item
	 * @param itemID
	 *            The entityID of the Item to pick up
	 * @return True if Item was picked up, false otherwise (Invalid playerID,
	 *         containerID or itemID)
	 */
	public boolean putItemIntoContainer(int playerID, int containerID,
			int itemID) {
		Player player = state.getPlayer(playerID);
		if (player == null || containerID < 0 || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			// Check all the containers to find a matching one
			for (Entity e : wSquare.getEntities(player.getOrientation())) {
				if (e instanceof Container && e.entityID == containerID) {
					Container container = (Container) e;
					if (!container.hasItem(itemID)) {
						Item item = player.removeItem(itemID);
						if (item != null) {
							return container.addItem(item);
						} else {
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Updates all the characters inside the GameState e.g Moves the Rovers and
	 * changes Player Oxygen
	 */
	public void tickGameState() {
		Set<Location> locations = new HashSet<Location>();
		// Update player oxygen
		for (Player player : state.getPlayers()) {
			if (player != null) {
				locations.add(player.getLocation());
			}
		}
		for (Location loc : locations) {
			Square square = state.getSquare(loc);
			square.tick();
		}

		//Move all the rovers
		for(Rover r: state.getRovers()){
			r.tick();
		}
	}

	public GameState getGameState() {
		return state;
	}
}