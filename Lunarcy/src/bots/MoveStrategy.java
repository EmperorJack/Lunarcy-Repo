package bots;

import java.util.List;

import game.Location;
import game.Square;

/**
 * Base class for movement. Various implementations of Movement should implement
 * this interface, or alternatively if they rely on a shortest path algorithm
 * when moving they should extend ShortestPathMover.
 *
 */
public interface MoveStrategy {
	
	public Location nextStep(Rover rover, Square[][] board);
	
}