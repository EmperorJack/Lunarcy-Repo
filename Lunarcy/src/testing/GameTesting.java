package testing;

import java.awt.Color;

import game.*;

import org.junit.Test;

/**
 * This class should contain all the methods
 * necessary to test Gamestate/Game logic.
 *
 * "We would expect at least one test per non-trivial (public)
 * method in the game world package."
 *
 * @author Ben & Robbie
 *
 */
public class GameTesting {

	/* Black box tests (ie for everyone but Robbie) */

	/* White box tests (ie for Robbie) */
	@Test
	public void test(){
		GameLogic logic = createNewGameLogic(1);
	}

	private GameLogic createNewGameLogic(int numPlayers) {
		GameState state = new GameState(numPlayers);
		for(int i = 0; i < numPlayers; i++){
			state.addPlayer(i, "Player"+i, Color.black);
		}

		return new GameLogic(state);
	}

}
