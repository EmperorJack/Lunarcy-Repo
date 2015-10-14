package bots;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import game.Direction;
import game.GameState;
import game.Location;
import game.Square;

/**
 * A special instance of MoveStrategy, where the movement is defined by
 * following the shortest path to a given destination. You should extend this
 * class directly if the movement relies on following a path.
 *
 * @author evansben1
 *
 */
@SuppressWarnings("serial")
abstract class ShortestPathMover implements MoveStrategy, Serializable {

	private List<Location> getNeighbours(Rover rover, GameState gamestate,
			Location loc) {

		List<Location> neighbours = new ArrayList<Location>();

		for (Direction direction : game.Direction.values()) {
			if (validMove(gamestate, rover, loc, direction)) {
				neighbours.add(loc.getAdjacent(direction));
			}
		}

		return neighbours;
	}

	private boolean validMove(GameState state, Rover rover, Location loc,
			Direction direction) {

		Square src = state.getSquare(loc);
		Square dest = state.getSquare(loc.getAdjacent(direction));

		if (src != null && dest != null) {
			return src.canExit(rover, direction)
					&& dest.canEnter(rover, direction.opposite());
		}

		return false;
	}

	/**
	 * Manhattan distance between start and end, -1 if either values are null
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	protected int estimate(Location start, Location end) {
		if (start == null || end == null) {
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
	protected List<Location> findPath(Rover rover, GameState gamestate,
			Location start, Location end) {

		List<Location> path = new ArrayList<Location>();
		PriorityQueue<LocationWrapper> fringe = new PriorityQueue<>();

		// A set of all the locations we have visited, to avoid revisiting
		Set<Location> visited = new HashSet<Location>();

		// Add the start node onto our queue
		fringe.offer(new LocationWrapper(start, null, 0, estimate(start, end)));

		while (!fringe.isEmpty()) {

			// We are stuck
			if (fringe.size() > 100) {
				return null;
			}

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
				for (Location neighbour : getNeighbours(rover, gamestate,
						current)) {

					// The cost to a neighbouring node, will be the cost to here
					// + 1 since it is a grid where you can only move
					// horizontally/vertically

					int costToNeigh = item.getCostToHere() + 1;

					int estimate = estimate(neighbour, end);

					// If the estimates invalid, one of the squares must have
					// been invalid so skip over this neighbour
					if (estimate < 0) {
						continue;
					}

					int estTotal = costToNeigh + estimate;

					fringe.offer(new LocationWrapper(neighbour, item,
							costToNeigh, estTotal));
				}
			}
		}

		return path;

	}

	/**
	 * A wrapper class for location, necessary for findPath() to function
	 *
	 */
	private class LocationWrapper implements Comparable<LocationWrapper> {
		private final Location location;
		private final LocationWrapper from;
		private final int costToHere;
		private final int totalCostToGoal;

		LocationWrapper(Location location, LocationWrapper from,
				int costToHere, int totalCostToGoal) {
			this.location = location;
			this.from = from;
			this.costToHere = costToHere;
			this.totalCostToGoal = totalCostToGoal;
		}

		/**
		 * Will return -Positive if this is closer to the final location
		 *
		 * -Zero if they are equal
		 *
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