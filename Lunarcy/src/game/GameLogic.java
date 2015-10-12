package game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bots.*;

/**
 * This class controls the interaction between input and the GameState
 *
 * @author Robbie
 *
 */
public class GameLogic {
	private static final int dayLength = 300;
	private GameState state;
	private PlayerMove[] moves;

	private int tickCount;

	public GameLogic(GameState state) {
		tickCount = 0;
		this.state = state;
		moves = new PlayerMove[state.getPlayers().length];
	}

	/**
	 * Updates all the characters inside the GameState e.g Moves the Rovers and
	 * changes Player Oxygen
	 */
	public synchronized void tickGameState() {
		//Move all the rovers
		if(tickCount % 4 == 0){
			for(Rover r: state.getRovers()){
				r.tick(state);
			}
		}
		//Move all the players
		for(int i = 0; i < moves.length; i++){
			if(moves[i]!=null){
				moves[i].move();
				moves[i] = null;
			}
		}
		Set<Location> locations = new HashSet<Location>();
		// Update player oxygen
		for (Player player : state.getPlayers()) {
			if (player != null) {
				if(player.getOxygen()==0){
					killPlayer(player);
				}
				locations.add(player.getLocation());
			}
		}
		for (Location loc : locations) {
			Square square = state.getSquare(loc);
			square.tick();
		}
		Ship ship = state.getShip();
		if(ship.hasLaunched()){
			System.out.println(ship.getPilot().getName() + " has won the game!!");
		}
		tickCount++;
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

		if(validMove(player, direction)){
			Square src = state.getSquare(player.getLocation());
			Square dest = state.getSquare(player.getLocation().getAdjacent(direction));

			moves[playerID] = new PlayerMove(player,direction,src,dest);
			return true;
		}
		return false;
	}

	/**
	 * Checks if the Character may move in the direction specified
	 * @param character The Character to move
	 * @param direction The Direction the Character wants to move in
	 * @return True if the Character may move, False otherwise
	 */
	public boolean validMove(Character character, Direction direction){
		if(character == null || direction == null){
			return false;
		}

		Square src = state.getSquare(character.getLocation());
		Square dest = state.getSquare(character.getLocation().getAdjacent(direction));

		if(src!=null && dest!=null){
			return src.canExit(character,direction) && dest.canEnter(character, direction.opposite());
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
		Player player = state.getPlayer(playerID);
		if(player!=null){
			player.turnLeft();
		}
	}

	/**
	 * Turn a player to the right of their current orientation
	 *
	 * @param playerID
	 *            the ID of the player to be turned
	 */
	public void turnPlayerRight(int playerID) {
		Player player = state.getPlayer(playerID);
		if(player!=null){
			player.turnRight();
		}
	}

	private void killPlayer(Player player){
		if(player==null)return;
		List<Item> inventory = player.getInventory();

		if(inventory.size() > 0){
			int i = (int)(inventory.size() * Math.random());
			dropItem(player.getId(), inventory.get(i).entityID);
		}

		List<Location> spawns = state.getSpawnPoints();
		Location loc = spawns.get((int)(spawns.size() * Math.random()));

		((WalkableSquare)state.getSquare(player.getLocation())).removePlayer(player);
		((WalkableSquare)state.getSquare(loc)).addPlayer(player);
		player.setLocation(loc);
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

			// Check all Items in the square to find a matching item
			for (Item i : wSquare.getItems(player.getOrientation())) {
				if (i.entityID == itemID) {
					// Found the matching item
					Item item = wSquare.takeItem(player.getOrientation(),itemID);
					return player.giveItem(item);
				}
			}
			//There must be no matching item in the square so check the container
			Container container = wSquare.getContainer(player.getOrientation());
			if (container !=null && container.isOpen() && container.canAccess(player) && container.hasItem(itemID)) {
				Item item = container.takeItem(itemID);
				if (item != null) {
					return player.giveItem(item);
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
				wSquare.addItem(player.getOrientation(), item);
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
	public boolean putItemIntoContainer(int playerID, int itemID) {
		Player player = state.getPlayer(playerID);
		if (player == null || itemID < 0)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			Container container = wSquare.getContainer(player.getOrientation());
			if(container != null && container.isOpen()){
				Item item = player.removeItem(itemID);
				if (item != null) {
					container.addItem(item);
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the player with the matching playerID can open a container and if they can then opens it
	 * @param playerID The ID of the player who is opening the container
	 * @return
	 */
	public boolean openContainer(int playerID){
		Player player = state.getPlayer(playerID);
		if (player == null)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			Container container = wSquare.getContainer(player.getOrientation());
			if(container != null){
				container.open(player);
				return container.isOpen();
			}
		}
		return false;
	}

	/**
	 * Checks if the player with the matching playerID can open a container and if they can then opens it
	 * @param playerID The ID of the player who is opening the container
	 * @return
	 */
	public boolean closeContainer(int playerID){
		Player player = state.getPlayer(playerID);
		if (player == null)
			return false;
		Square square = state.getSquare(player.getLocation());

		// Otherwise cannot contain players/items
		// Shouldn't need to check as it's from Player location, but to be safe
		if (square instanceof WalkableSquare) {
			WalkableSquare wSquare = (WalkableSquare) square;

			Container container = wSquare.getContainer(player.getOrientation());
			if(container != null){
				container.close();
				return true;
			}
		}
		return false;
	}

	public GameState getGameState() {
		return state;
	}

	public Player getWinner(){
		if(state.getShip().hasLaunched()){
			return state.getShip().getPilot();
		}
		return null;
	}

	/**
	 * Returns the time as a percentage of the day
	 * @return 0-100% of how much the day night cycle has gone through
	 */
	public int getTime(){
		return (int)((tickCount % dayLength) / (dayLength/100f));
	}

	private class PlayerMove{
		private final Player player;
		private final Direction direction;
		private final Square source;
		private final Square destination;

		public PlayerMove(Player player, Direction direction, Square source, Square destination){
			this.player = player;
			this.direction = direction;
			this.source = source;
			this.destination = destination;
		}

		public void move(){
			if(player==null||direction==null||source==null||destination==null){
				return;
			}
			source.removePlayer(player);
			destination.addPlayer(player);
			player.move(direction);
		}
	}
}