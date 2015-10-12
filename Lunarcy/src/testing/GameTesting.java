package testing;

import static org.junit.Assert.*;

import java.awt.Color;

import game.*;

import org.junit.Test;

import bots.Rover;

/**
 * This class should contain all the methods necessary to test Gamestate/Game
 * logic.
 *
 * "We would expect at least one test per non-trivial (public) method in the
 * game world package."
 *
 * @author Ben & Robbie
 *
 */
public class GameTesting {

	/* Outside tests (ie for everyone but Robbie) */

	/* Square Checks */
	/*------------*/
	/**
	 * The board should start at 0,0 so make sure -1,-1 is returned as null
	 */
	@Test
	public void testInvalidSquare_1() {
		GameState gameState = new GameState(1, "map.xml");
		Square square = gameState.getSquare(new Location(-1, -1));
		assertNull("Should be null", square);
	}

	/**
	 * An illegal argument exception should be raised, when trying to get the
	 * square at null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void tesInvalidSquare_2() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.getSquare(null);
	}

	@Test
	public void testValidSquare() {
		GameState gameState = new GameState(1, "map.xml");
		Square square = gameState.getSquare(new Location(0, 0));
		assertNotNull("Square at 0,0 should be a valid square", square);
	}

	/**
	 * Should throw an illegal argument exception if trying to set a square when
	 * location is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void tesInvalidSetSquare_1() {
		GameState gameState = new GameState(1, "map.xml");
		Square square = gameState.getSquare(new Location(0, 0));
		gameState.setSquare(null, square);
	}

	/**
	 * Should throw an illegal argument exception if trying to set a square to
	 * null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void tesInvalidSetSquare_2() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.setSquare(new Location(0, 0), null);
	}

	/**
	 * Should return null when trying to access a square which is off the grid.
	 */
	@Test
	public void tesInvalidSetSquare_3() {
		GameState gameState = new GameState(1, "map.xml");
		Square[][] board = gameState.getBoard();
		Square square = gameState.setSquare(new Location(board[0].length, 0), new BlankSquare());
		assertNull("Should be null", square);
	}

	/**
	 * Should be able to set the Square at 0,0 to a blank square.
	 */
	@Test
	public void ValidSetSquare_1() {
		GameState gameState = new GameState(1, "map.xml");
		Square square = gameState.setSquare(new Location(0, 0), new BlankSquare());
		assertNotNull("Shouldn't be null", square);
	}

	/**
	 * If a square is outside, gamestate.isOutside should return true
	 */
	@Test
	public void validOutsideSquare() {
		GameState gameState = new GameState(1, "map.xml");
		WalkableSquare square = new WalkableSquare("ben", "A test square", false, null, null, null, null);
		Location location = new Location(0, 0);
		gameState.setSquare(location, square);
		assertTrue(gameState.isOutside(location));
	}

	/**
	 * If a square is inside, gamestate.isOutside should return false
	 */
	@Test
	public void invalidOutsideSquare_1() {
		GameState gameState = new GameState(1, "map.xml");
		WalkableSquare square = new WalkableSquare("ben", "A test square", true, null, null, null, null);
		Location location = new Location(0, 0);
		gameState.setSquare(location, square);
		assertFalse(gameState.isOutside(location));
	}

	/**
	 * If a square is non walkable, gamestate.isOutside should return false
	 */
	@Test
	public void invalidOutsideSquare_2() {
		GameState gameState = new GameState(1, "map.xml");
		Square square = new BlankSquare();
		Location location = new Location(0, 0);
		gameState.setSquare(location, square);
		assertFalse(gameState.isOutside(location));
	}
	/*------------*/

	/* Rover tests */

	/**
	 * A rover should have caught a player if they are in the same square
	 */
	@Test
	public void validRoverCatch() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.addRover(new Rover());
		gameState.addPlayer(0, "Ben", Color.RED);

		Rover rover = gameState.getRovers().iterator().next();
		Player player = gameState.getPlayers()[0];

		player.setLocation(rover.getLocation());

		assertNotNull("Rover should have caught a player", gameState.caughtPlayer(rover));
	}

	/**
	 * If the rover passed in is null, then the returning result should also be
	 * null.
	 */
	@Test
	public void invalidRoverCatch_1() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.addRover(new Rover());
		gameState.addPlayer(0, "Ben", Color.RED);

		assertNull("Rover should have caught a player", gameState.caughtPlayer(null));
	}

	/**
	 * Gamestate.caughtPlayer should return null if not rover is not in a square
	 * with a player
	 */

	@Test
	public void invalidRoverCatch_2() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.addRover(new Rover());
		gameState.addPlayer(0, "Ben", Color.RED);

		Rover rover = gameState.getRovers().iterator().next();
		Player player = gameState.getPlayers()[0];

		player.setLocation(rover.getLocation().getAdjacent(Direction.NORTH));

		assertNull("Rover should not have caught a player", gameState.caughtPlayer(rover));
	}

	/**
	 * If the rover passed in is null, then the returning result should also be
	 * null.
	 */
	@Test
	public void invalidRoverAdd() {
		GameState gameState = new GameState(1, "map.xml");
		assertFalse("Should not be able to add a null rover", gameState.addRover(null));
	}

	/* Spawn tests */

	/**
	 * A spawn location should not be able to be set to null.
	 */
	@Test
	public void invalidSpawn_1() {
		GameState gameState = new GameState(1, "map.xml");
		assertFalse("Should not be able to add null", gameState.addSpawn(null));
	}

	/**
	 * A spawn location should not be able to be set off the board.
	 */
	@Test
	public void invalidSpawn_2() {
		GameState gameState = new GameState(1, "map.xml");
		Square[][] board = gameState.getBoard();
		assertFalse("Should not be able to add off the board", gameState.addSpawn(new Location(board[0].length, 0)));
	}

	/**
	 * A spawn location should be able to be set to 0,0.
	 */
	@Test
	public void validSpawn() {
		GameState gameState = new GameState(1, "map.xml");
		assertTrue("Should be able to be set to 0,0", gameState.addSpawn(new Location(0, 0)));
	}

	/* Player tests */

	/**
	 * Should be able to retrieve a player if one is set
	 */

	@Test
	public void validAddPlayer_1() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.addPlayer(0, "Ben", Color.RED);
		Player player = gameState.getPlayer(0);
		assertNotNull("Should be able to get a valid player", player);
	}

	/**
	 * Shouldnt be able to retrieve a player if one has not been set
	 */

	@Test
	public void invalidAddPlayer_1() {
		GameState gameState = new GameState(1, "map.xml");
		Player player = gameState.getPlayer(0);
		assertNull("Shouldnt be able to get an invalid player", player);
	}

	/**
	 * Should be able to get a player ID by their name
	 */
	@Test
	public void validGetPlayer_1() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.addPlayer(0, "Ben", Color.RED);
		assertEquals(gameState.getPlayerID("Ben"), 0);
	}

	/**
	 * Should recieve -1 if a player name does not match any of the players
	 */
	@Test
	public void invalidGetPlayer_1() {
		GameState gameState = new GameState(1, "map.xml");
		assertEquals(gameState.getPlayerID("Ben"), -1);
	}

	/**
	 * Should be able to remove a valid player
	 **/
	@Test
	public void validRemovePlayer_1() {
		GameState gameState = new GameState(1, "map.xml");
		gameState.addPlayer(0, "Ben", Color.RED);
		gameState.removePlayer(0);
		assertNull(gameState.getPlayer(0));
	}

	/**
	 * Shouldnt be able to remove an in valid player
	 */
	@Test
	public void invalidRemovePlayer_1() {
		GameState gameState = new GameState(1, "map.xml");
		assertFalse(gameState.removePlayer(-1));
	}

	/* White box tests (ie for Robbie) */


	private GameLogic createNewGameLogic(int numPlayers) {
		GameState state = new GameState(numPlayers, "map.xml");

		for (int i = 0; i < numPlayers; i++) {
			state.addPlayer(i, "Player" + i, Color.black);
		}

		return new GameLogic(state);
	}

}
