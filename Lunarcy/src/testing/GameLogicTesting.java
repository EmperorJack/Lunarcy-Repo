package testing;

import game.Bag;
import game.Container;
import game.Direction;
import game.GameLogic;
import game.GameState;
import game.Key;
import game.Player;
import game.SolidContainer;

import java.awt.Color;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the GameLogic class
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


	/**
	 * Player should be able to move to 0,1
	 */
	@Test
	public void valiMovePlayer_1(){
		GameLogic logic = createNewGameLogic(1);
		assertTrue(logic.movePlayer(0, Direction.SOUTH));
	}

	/**
	 * Player shouldn't be able to move to 0,-1
	 */
	@Test
	public void invaliMovePlayer_1(){
		GameLogic logic = createNewGameLogic(1);
		assertFalse(logic.movePlayer(0, Direction.NORTH));
	}

	/**
	 * Invalid player ID should return false
	 */
	@Test
	public void invaliMovePlayer_2(){
		GameLogic logic = createNewGameLogic(1);
		assertFalse(logic.movePlayer(2, Direction.NORTH));
	}

	/**
	 * Null Direction should return false
	 */
	@Test
	public void invaliMovePlayer_3(){
		GameLogic logic = createNewGameLogic(1);
		assertFalse(logic.movePlayer(2, null));
	}

	/**
	 * Should be able to drop an item up which is
	 * in your inventory
	 */
	@Test
	public void validDrop_1(){
		GameLogic logic = createNewGameLogic(1);
		logic.getGameState().getPlayer(0).giveItem(new Key(100, 1));
		assertTrue(logic.dropItem(0, 100));
	}

	/**
	 * Shouldnt be able to drop an item up which is not
	 * in your inventory
	 */
	@Test
	public void invalidDrop_1(){
		GameLogic logic = createNewGameLogic(1);
		logic.getGameState().getPlayer(0).giveItem(new Key(100, 1));
		assertFalse(logic.dropItem(0, 101));
	}

	/**
	 * Should be able to pick up an item up which
	 * you have dropped
	 */
	@Test
	public void validPickup_1(){
		GameLogic logic = createNewGameLogic(1);
		logic.getGameState().getPlayer(0).giveItem(new Key(100, 1));
		logic.dropItem(0, 100);
		assertTrue(logic.pickUpItem(0, 100));
	}

	/**
	 * Shouldnt be able to pick up an item up which
	 * is not in the square
	 */
	@Test
	public void invalidPickup_1(){
		GameLogic logic = createNewGameLogic(1);
		assertFalse(logic.pickUpItem(0, 55));
	}

	/**
	 * Should be an open container at 0,0
	 * North.
	 */
	@Test
	public void validOpen_1(){
		GameLogic logic = createNewGameLogic(1);
		assertTrue(logic.openContainer(0));
	}

	/**
	 * Should be an open container at 0,0
	 * North.
	 */
	@Test
	public void validClose_1(){
		GameLogic logic = createNewGameLogic(1);
		logic.openContainer(0);
		assertTrue(logic.closeContainer(0));
	}


	/**
	 * Shouldn't be an open container to the east
	 * North.
	 */
	@Test
	public void invalidOpen_1(){
		GameLogic logic = createNewGameLogic(1);
		logic.turnPlayerRight(0);
		assertFalse(logic.openContainer(0));
	}

	/**
	 * Should be able to add an item from inventory
	 * into container
	 */
	@Test
	public void validPut_1(){
		GameLogic logic = createNewGameLogic(1);
		logic.openContainer(0);

		GameState gamestate = logic.getGameState();
		Player player = gamestate.getPlayer(0);

		player.giveItem(new Key(100, 1));

		SolidContainer container = (SolidContainer) gamestate.getSquare(player.getLocation()).getFurniture(player.getOrientation());
		assertTrue(logic.putItemIntoContainer(0, container.getEntityID(), player.getInventory().get(0).getEntityID()));
	}

	@Test
	public void validBagToInventory_1(){
		GameLogic logic = createNewGameLogic(1);

		GameState gamestate = logic.getGameState();
		Player player = gamestate.getPlayer(0);

		Bag bag = new Bag(100);
		Key key = new Key(101, 1);
		bag.addItem(key);

		player.giveItem(bag);

		assertTrue(logic.pickUpItem(0, 101));

	}



	private GameLogic createNewGameLogic(int numPlayers) {
		GameState state = new GameState(numPlayers, "assets/maps/testmap.json");

		for (int i = 0; i < numPlayers; i++) {
			state.addPlayer(i, "Player" + i, Color.black);
		}

		return new GameLogic(state);
	}

}
