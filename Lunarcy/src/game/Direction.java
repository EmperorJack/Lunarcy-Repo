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
	 * @return The Direction opposite from current
	 */
	public static Direction opposite(Direction current){
		if(current==NORTH){
			return SOUTH;
		}else if(current==EAST){
			return WEST;
		}else if(current==SOUTH){
			return NORTH;
		}else{//Else West
			return EAST;
		}
	}
	/**
	 * @return The Direction that you would be facing if you turned left from current
	 */
	public static Direction left(Direction current){
		if(current==NORTH){
			return WEST;
		}else if(current==EAST){
			return NORTH;
		}else if(current==SOUTH){
			return EAST;
		}else{//Else West
			return SOUTH;
		}
	}
	
	/**
	 * @return The Direction that you would be facing if you turned right from current
	 */
	public static Direction right(Direction current){
		if(current==NORTH){
			return EAST;
		}else if(current==EAST){
			return SOUTH;
		}else if(current==SOUTH){
			return WEST;
		}else{//Else West
			return NORTH;
		}
	}
}
