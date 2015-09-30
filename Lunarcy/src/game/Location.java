package game;

import java.io.Serializable;

/**
 * This class will store an x,y coordinate corresponding to a location on the
 * game grid
 * 
 * @author b
 *
 */
public class Location implements Serializable{

	private int x;
	private int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	/**
	 * Returns the location adjacent to this one in the direction specified.
	 * South is positive y and East is positive x
	 * @param direction Direction to get the new location from
	 * @return
	 */
	public Location getAdjacent(Direction direction){
		if(direction==Direction.North){
			return new Location(x, y - 1);
		}else if(direction==Direction.East){
			return new Location(x + 1, y);
		}else if(direction==Direction.South){
			return new Location(x, y + 1);
		}else{//Else West
			return new Location(x - 1, y);
		}
	}

}


