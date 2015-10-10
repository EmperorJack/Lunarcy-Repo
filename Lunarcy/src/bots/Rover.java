package bots;

import game.Direction;
import game.GameState;
import game.Location;
import game.Player;
import game.Character;

import java.io.Serializable;

/**
 * A rover is a 'bot' in the game. Each rover can be following one of three
 * movement strategies, at a given time.
 *
 * 1. Roam: Follow a random path, change to #2 when a player is spotted
 *
 * 2. Track: Track and follow a specific Player, using shortest path to attempt
 * to capture them. Gives up after the player gets too far away, and reverts to the
 * Roam Strategy.
 *
 * @author Ben
 *
 */
public class Rover implements Character, Serializable {

	private static final long serialVersionUID = 1L;

	//The current movement strategy we are following
	private MoveStrategy movementStrategy;

	// The next location which the rover is set to follow
	private Location nextStep;

	// Mantains the rovers position (x,y) at any given time
	private Location currentLocation;
	private Direction orientation;

	public Rover() {
		this.movementStrategy = new RoamMovement();
		this.currentLocation = new Location(17, 5);
		this.orientation = Direction.NORTH;
	}

	/**
	 * Moves the rover one step along its currentPath,
	 * generating a new path if necessary and also
	 * changing the movement strategy when applicable.
	 */

	public void tick(GameState gameState) {
		
		//Set our nextStep to based on the strategys next step
		nextStep = movementStrategy.nextStep(this, gameState);
	
		if(nextStep==null){
			System.out.println("ROVER STUCK");
			return;
		}
		
		//At this point the nextStep must be valid so move the rover
		currentLocation = nextStep;

		//Face the direction the next square is in
		orientation = currentLocation.getDirection(nextStep);
		
		System.out.println("Facing " + orientation);

		// Move along one step in the path
		// removing the location we visit
		

		//updateStrategy();

	}

	/**
	 * Changes the movement strategy in one of two cases
	 *
	 * 1. If the Rover is roaming and spots a target, changes to
	 * TrackMovement.
	 *
	 * 2. If the Rover is tracking and has not reached the player
	 * after a certain amount of ticks, revert to roaming.
	 *
	 * Otherwise does not update the movement strategy.
	 *
	 */
	private void updateStrategy(GameState gameState){
		// If we are roaming, see if we can track anyone
		if (movementStrategy instanceof RoamMovement) {
			Player target = ((RoamMovement) movementStrategy).viewTarget(gameState.getBoard());

			// If we can see a target, and they're outside chase them
			if (target != null && gameState.isOutside(target.getLocation())) {
				System.out.println("Changing to track");
				// Change to tracking this target
				movementStrategy = new TrackMovement(target);
			}

		}
		// If we are in Track Mode
		else if (movementStrategy instanceof TrackMovement) {
			// If the tracker has been following for too long
			if (((TrackMovement) movementStrategy).shouldGiveup(gameState)) {
				//Change back to Roaming
				movementStrategy = new RoamMovement();
			}
		}
	}
	
	/**
	 * Rotate the rover to the right of there current direction.
	 */
	public void rotate(){
		orientation = orientation.right();
	}

	public Location getLocation(){
		return currentLocation;
	}
	
	public void setOrientation(Direction orientation) {
		this.orientation = orientation;
	}
	
	@Override
	public Direction getOrientation() {
		return orientation;
	}

}
