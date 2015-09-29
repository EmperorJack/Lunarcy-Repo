package game;

/**
 * Represents one of the four directions on a compass, used to denote orientation in the game world.
 * 
 * @author Robbie
 *
 */
public enum Direction {
	North, East, South, West;
	
	/**
	 * @return The Direction opposite from current
	 */
	public static Direction opposite(Direction current){
		if(current==North){
			return South;
		}else if(current==East){
			return West;
		}else if(current==South){
			return North;
		}else{//Else West
			return East;
		}
	}
	/**
	 * @return The Direction that you would be facing if you turned left from current
	 */
	public static Direction left(Direction current){
		if(current==North){
			return West;
		}else if(current==East){
			return North;
		}else if(current==South){
			return East;
		}else{//Else West
			return South;
		}
	}
	
	/**
	 * @return The Direction that you would be facing if you turned right from current
	 */
	public static Direction right(Direction current){
		if(current==North){
			return East;
		}else if(current==East){
			return South;
		}else if(current==South){
			return West;
		}else{//Else West
			return North;
		}
	}
}
