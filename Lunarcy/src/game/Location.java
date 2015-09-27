package game;

/**
 * This class will store an x,y coordinate corresponding to a location on the
 * game grid
 * 
 * @author b
 *
 */
public class Location {

	private int x;
	private int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Location getAdjacent(Direction direction){
		if(direction==Direction.North){
			return new Location(x + 1, y);
		}else if(direction==Direction.East){
			return new Location(x, y + 1);
		}else if(direction==Direction.South){
			return new Location(x - 1, y);
		}else{//Else West
			return new Location(x, y - 1);
		}
	}

}


