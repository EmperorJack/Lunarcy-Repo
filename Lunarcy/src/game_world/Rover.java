package game_world;

import java.util.List;
import java.util.ArrayList;

/**
 * A rover is a 'bot' in the game.
 * Each rover can be following one of three
 * movement strategies, at a given time. 
 * 
 * 1. Roam: Follow a seemingly random path (prioritising popular squares), change to #3 when a player is spotted
 * 2. Protect: Move around the map on a set path, this will not differ throughout the game (ie will not change to hunt)
 * 3. Hunt: Track and follow a specific Player, using shortest path to attempt to capture them
 * 
 * @author b
 *
 */
public class Rover {

	MoveStrategy movementStrategy; //Defines how the rover should move around the map
	List<Location> path; //Which squares the rover is going to follow
	Location currentLocation; //Mantains the rovers position at any given time
	
	Rover(MoveStrategy movementStrategy){
		this.movementStrategy = movementStrategy;
		this.path = new ArrayList<Location>();
	}
	
	public void move(){
		if(path.isEmpty()){ //We have finished our current path, so get a new one
			path = movementStrategy.move(currentLocation);
		}
		
		currentLocation = path.remove(0); //Move to the next location in the path, removing this location as we do

		
	}
	
	public void updateMoveStrategy(MoveStrategy movementStrategy){
		this.movementStrategy = movementStrategy;
	}
	

}
