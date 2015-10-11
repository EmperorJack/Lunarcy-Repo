package game;

/**
 * Represents one of the four directions on a compass, used to denote orientation in the game world.
 *
 * @author Robbie
 *
 */
public enum Direction {
	NORTH, EAST, SOUTH, WEST;

	/**
	 * @return The Direction opposite from this one
	 */
	public Direction opposite(){
		if(this==NORTH){
			return SOUTH;
		}else if(this==EAST){
			return WEST;
		}else if(this==SOUTH){
			return NORTH;
		}else{//Else West
			return EAST;
		}
	}
	/**
	 * @return The Direction that you would be facing if you turned left from this one
	 */
	public Direction left(){
		if(this==NORTH){
			return WEST;
		}else if(this==EAST){
			return NORTH;
		}else if(this==SOUTH){
			return EAST;
		}else{//Else West
			return SOUTH;
		}
	}

	/**
	 * @return The Direction that you would be facing if you turned right from this one
	 */
	public Direction right(){
		if(this==NORTH){
			return EAST;
		}else if(this==EAST){
			return SOUTH;
		}else if(this==SOUTH){
			return WEST;
		}else{//Else West
			return NORTH;
		}
	}
	
	/**
	 * Returns a random direction
	 * @return
	 */
	
	public static Direction randomDirection(){
		Direction[] dirs = values();
		return dirs[(int)(Math.random()*dirs.length)];
	}
}
