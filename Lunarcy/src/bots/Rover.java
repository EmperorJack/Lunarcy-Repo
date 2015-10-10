package bots;

import java.util.List;

import game.Direction;
import game.GameState;
import game.Location;
import game.Player;
import game.Character;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A rover is a 'bot' in the game. Each rover can be following one of three
 * movement strategies, at a given time.
 *
 * 1. Roam: Follow a random path, swapped to strategy #3 when it sees a player
 *
 * 2. Protect: Move around the map on a set path, this will not differ
 * throughout the game (ie will not change to Track or Roam)
 *
 * 3. Track: Track and follow a specific Player, using shortest path to attempt
 * to capture them. Gives up after a set number of ticks, and reverts to the
 * Roam Strategy.
 *
 * @author b
 *
 */
public class Rover implements Character, Serializable {

	private static final long serialVersionUID = 1L;

	// The current movement strategy we are following
	private MoveStrategy movementStrategy;

	// Which squares the rover is set to follow
	private List<Location> path;

	// Mantains the rovers position (x,y) at any given time
	private Location currentLocation;

	private Direction direction;

	public Rover(MoveStrategy movementStrategy) {
		this.movementStrategy = movementStrategy;
		this.path = new ArrayList<Location>();
		this.currentLocation = new Location(1, 1);
		this.direction = Direction.NORTH;
	}

	/**
	 * Moves the rover one step along its currentPath, generating a new path if
	 * necessary and also changing the movement strategy when applicable.
	 */

	public void tick(GameState gamestate) {

		// Move along one step in the path
		// removing the location we visit
		currentLocation = movementStrategy.nextStep(this, gamestate.getBoard());
		
		//Something went wrong and we are stuck
		if(currentLocation==null){
			return;
		}
		
		if(caughtPlayer(gamestate)){
			
		}
		
		System.out.println("Rover is at " + currentLocation);
		//updateStrategy();

	}

	/**
	 * Returns true if the bot is at the same
	 * location as any of the players currently in the game.
	 * @return
	 */
	private boolean caughtPlayer(GameState gamestate){
		for(Player player: gamestate.getPlayers()){
			if(currentLocation.equals(player.getLocation())){
				return true;
			}
		}
		
		return false;
	}
	/**
	 * Changes the movement strategy in one of two cases
	 *
	 * 1. If the Rover is roaming and spots a target, changes to TrackMovement.
	 *
	 * 2. If the Rover is tracking and has not reached the player after a
	 * certain amount of ticks, revert to roaming.
	 *
	 * Otherwise does not update the movement strategy.
	 *
	 */
	private void updateStrategy(GameState gamestate) {
		// If we are roaming, see if we can track anyone
		if (movementStrategy instanceof RoamMovement) {
			Player target = ((RoamMovement) movementStrategy).viewTarget(gamestate.getBoard(), this);

			// If we can see a target
			if (target != null) {
				// Change to tracking this target
				movementStrategy = new TrackMovement(target);
			}

		}
		// If we are in Track Mode
		else if (movementStrategy instanceof TrackMovement) {
			// If the tracker has been following for too long
			if (((TrackMovement) movementStrategy).shouldGiveup()) {
				// Change back to Roaming
				movementStrategy = new RoamMovement();
			}
		}
	}

	public void rotateRight() {
		direction = direction.right();
	}

	public Location getLocation() {
		return currentLocation;
	}

	@Override
	public Direction getOrientation() {
		return direction;
	}
}
