package game_world;

public enum Direction {
	North, East, South, West;
	
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
