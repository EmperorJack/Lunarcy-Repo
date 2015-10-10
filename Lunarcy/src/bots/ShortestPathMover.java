package bots;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import game.Direction;
import game.Location;
import game.Square;
import game.WalkableSquare;

/**
 * A special instance of MoveStrategy, where the movement is defined by
 * following the shortest path to a given destination. You should extend
 * this class directly if the movement relies on following a path.
 *
 * @author b
 *
 */
abstract class ShortestPathMover implements MoveStrategy, Serializable {

	/**
	 * Returns true if path is outdated and needs to be updated, false
	 * otherwise.
	 *
	 * @param path
	 * @return
	 */
	public abstract boolean mustUpdate(List<Location> path);

	private List<Location> getNeighbours(Rover rover, Square[][] board, Location loc) {
		List<Location> neighbours = new ArrayList<Location>();

		//NOT CURRENTLY CORRECT AS IT CAN MOVE THROUGH WALLS
		for(Direction direction: game.Direction.values()){
			if(validMove(rover, board, direction)){
				neighbours.add(loc.getAdjacent(direction));
			}
		}

		return neighbours;
	}

	/**
	 * Checks if the rover may move in the direction specified
	 * @param rover The rover to move
	 * @param direction The Direction the Rover wants to move in
	 * @return True if the Rover may move, False otherwise
	 */
	public boolean validMove(Rover rover, Square[][] board, Direction direction){
		if(rover == null && direction == null){
			return false;
		}

		Location currentLocation = rover.getLocation();

		//We can assume this is a valid square since the rover is in it already
		Square src = board[currentLocation.getY()][currentLocation.getY()];

		//Make sure the next location is valid
		Location nextLocation = currentLocation.getAdjacent(direction);

		//Make sure the nextLocation will be a valid square
		if(!validSquare(board, nextLocation)){
			return false;
		}

		Square dest = board[nextLocation.getY()][nextLocation.getX()];

		if(src!=null && dest!=null){
			if(dest instanceof WalkableSquare){
				WalkableSquare square = (WalkableSquare) dest;
				return !square.isInside();
			}
			//return src.canExit(rover,direction) && dest.canEnter(rover, direction.opposite());
		}
		return false;
	}


	private boolean validSquare(Square[][] board, Location loc){
		if(loc.getX() < 0 || loc.getX() >=  board[0].length){
			return false;
		}

		if(loc.getY() < 0 || loc.getY() >=  board.length){
			return false;
		}

		return true;
	}

	/**
	 * Manhattan distance between start and end, -1 if either values are null
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	private int estimate(Location start, Location end) {
		if (start == null || end == null){
			return -1;
		}

		return Math.abs(start.getX() - end.getX()) + // Horizontal difference +
				Math.abs(start.getY() - end.getY()); // Vertical Difference
	}

	/**
	 * Uses a* search to find the shortest path from start to end.
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	protected List<Location> findPath(Rover rover, Square[][] board, Location start, Location end) {

		List<Location> path = new ArrayList<Location>();
		PriorityQueue<LocationWrapper> fringe = new PriorityQueue<>();

		// A set of all the locations we have visited, to avoid revisiting
		Set<Location> visited = new HashSet<Location>();

		// Add the start node onto our queue
		fringe.offer(new LocationWrapper(start, null, 0, estimate(start, end)));

		while (!fringe.isEmpty()) {
			// Get the first item off the queue
			LocationWrapper item = fringe.poll();
			Location current = item.getLocation();

			// If we havent been to the current location
			if (visited.add(current)) {
				// If we are at the final location
				if (current.equals(end)) {
					LocationWrapper temp = item;
					// Add the path we have just found (in reverse order)
					while (temp.getFrom() != null) {
						path.add(temp.getLocation());
						temp = temp.getFrom();
					}
					// Reverse the path, so it is in the correct order to follow
					Collections.reverse(path);
					return path;
				}

				// Add all of this nodes valid neighbours onto the queue
				for (Location neighbour : getNeighbours(rover, board, current)) {
					// The cost to a neighbouring node, will be the cost to here
					// + 1
					int costToNeigh = item.getCostToHere() + 1;
					int estTotal = costToNeigh + estimate(neighbour, end);
					fringe.offer(new LocationWrapper(neighbour, item, costToNeigh, estTotal));
				}
			}
		}

		System.out.println("No path found");
		return path;

	}

	/**
	 * A wrapper class for a location, necessary for findPath() to function
	 *
	 */
	private class LocationWrapper implements Comparable<LocationWrapper> {
		private final Location location;
		private final LocationWrapper from;
		private final int costToHere;
		private final int totalCostToGoal;

		LocationWrapper(Location location, LocationWrapper from, int costToHere, int totalCostToGoal) {
			this.location = location;
			this.from = from;
			this.costToHere = costToHere;
			this.totalCostToGoal = totalCostToGoal;
		}

		/**
		 * Will return
		 * -Positive if this is closer to the final location
		 * -Zero if they are equal
		 * -Negative if the other location is closer
		 *
		 * @return
		 */

		@Override
		public int compareTo(LocationWrapper other) {

			return totalCostToGoal - other.totalCostToGoal;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LocationWrapper other = (LocationWrapper) obj;

			// A wrapper is equal if the two locations inside are equal
			return other.getLocation().equals(location);
		}

		@Override
		public int hashCode() {
			return location.hashCode();
		}

		public Location getLocation() {
			return location;
		}

		public LocationWrapper getFrom() {
			return from;
		}

		public int getCostToHere() {
			return costToHere;
		}

	}

}