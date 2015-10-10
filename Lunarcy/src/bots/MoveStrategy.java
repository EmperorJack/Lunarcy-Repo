package bots;

import java.io.Serializable;
import java.util.List;

import game.GameState;
import game.Location;
import game.Square;

/**
 * Base class for movement. Various implementations of Movement should implement
 * this interface, or alternatively if they rely on a shortest path algorithm
 * when moving they should extend ShortestPathMover.
 *
 */
public interface MoveStrategy extends Serializable {
	public List<Location> path(Rover rover, GameState gamestate, Location currentLocation);
	public boolean mustUpdate(List<Location> path);
}