package game_world;

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

}


