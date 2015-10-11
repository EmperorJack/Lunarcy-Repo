package bots;

import java.io.Serializable;
import game.GameState;
import game.Location;

/**
 * Base class for movement. Various implementations of Movement should implement
 * this interface, or alternatively if they rely on a shortest path algorithm
 * when moving they should extend ShortestPathMover.
 *
 */
public interface MoveStrategy extends Serializable {
	public Location nextStep(Rover rover, GameState gamestate);
}