package testing;

import game.Direction;
import game.GameLogic;
import game.GameState;
import game.Player;

import java.awt.Color;
import java.lang.reflect.Method;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author evansben1
 *
 */
public class GameLogicTesting {

	/**
	 * Should be able to rotate a player left
	 */
	@Test
	public void validTurnPlayer_1(){
		GameLogic logic = createNewGameLogic(1);

		Player player = logic.getGameState().getPlayer(0);
		Direction original = player.getOrientation();
		logic.turnPlayerLeft(player.getId());
		assertEquals(original.left(), player.getOrientation());
	}


	/**
	 * Should be able to rotate a player right
	 */
	@Test
	public void validTurnPlayer_2(){
		GameLogic logic = createNewGameLogic(1);

		Player player = logic.getGameState().getPlayer(0);
		Direction original = player.getOrientation();
		logic.turnPlayerRight(player.getId());
		assertEquals(original.right(), player.getOrientation());
	}

	private GameLogic createNewGameLogic(int numPlayers) {
		GameState state = new GameState(numPlayers, "assets/maps/map.xml");

		for (int i = 0; i < numPlayers; i++) {
			state.addPlayer(i, "Player" + i, Color.black);
		}

		return new GameLogic(state);
	}

}
