package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import game.BlankSquare;
import game.GameState;
import game.Location;
import game.Square;
import game.WalkableSquare;
import mapbuilder.GameMap;
import mapbuilder.MapBuilder;

import org.junit.Test;

import storage.Storage;

/**
 * This class should contain all the methods necessary to test the Storage and
 * map builder.
 *
 * "We would expect at least one test per non-trivial (public) method in the
 * storage package."
 *
 * @author Ben
 *
 */
public class StorageTesting {

	/* Black box tests (ie for everyone but Kelly) */

	/* White box tests (ie for Kelly) */
	@Test
	public void testGameMapLoadAndSave() {
		String fileToLoad = "assets/maps/map.json";
		String fileToSave = "assets/testing/testmapsave.json";
		GameMap mapOne = Storage.loadGameMap(new File(fileToLoad)); // load map from file
		Storage.saveGameMap(mapOne, new File("assets/testing/testmapsave.json"));
		try {
			Scanner scanLoad = new Scanner(new File(fileToLoad));
			Scanner scanSave = new Scanner(new File(fileToSave));
			while (scanLoad.hasNext() && scanSave.hasNext()) {
				String loadNext = scanLoad.next();
				String saveNext = scanSave.next();
				if (!loadNext.equals(saveNext)) {
					System.out.println(loadNext);
					System.out.println(saveNext);
					assertTrue(false);
				}
			}
			scanLoad.close();
			scanSave.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testGameStateLoadAndSave(){
		GameState gameState = new GameState(1, "assets/maps/map.json");
		String firstFileSaved = "assets/testing/teststatesave.json";
		String secondFileSaved = "assets/testing/teststatesave2.json";
		Storage.saveState(gameState, firstFileSaved);
		GameState loadedState = Storage.loadState(firstFileSaved);
		Storage.saveState(loadedState, secondFileSaved);

		try {
			Scanner scanLoad = new Scanner(new File(firstFileSaved));
			Scanner scanSave = new Scanner(new File(secondFileSaved));
			while (scanLoad.hasNext() && scanSave.hasNext()) {
				String loadNext = scanLoad.next();
				String saveNext = scanSave.next();
				if (!loadNext.equals(saveNext)) {
					assertTrue(false);
				}
			}
			scanLoad.close();
			scanSave.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testMapBuilderOne(){
		//No squares in the 2D array should be null.
		MapBuilder builder = new MapBuilder();
		Square[][] squares = builder.getSquares();
		for (int i = 0; i < squares.length; i++){
			for (int j = 0; j < squares.length; j++){
				assertNotNull(squares[i][j]);
			}
		}
	}

	@Test
	public void testMapBuilderTwo(){
		//No squares in the 2D array should be null.
		MapBuilder builder = new MapBuilder();
		GameMap map = builder.getMap();
		assertTrue(map.getTierDictionary().size() == 0); //Item dictionary size should be 0.
		builder.initialiseItems();
		assertTrue(map.getTierDictionary().size() > 0); //Item dictionary size should be greater than 0.
	}

	@Test
	public void testMapBuilderThree(){
		MapBuilder builder = new MapBuilder();
		Set<Location> selectedTiles = new HashSet<Location>();
		for (int y = 0; y < 10; y++){
			for (int x =0; x < 10; x++){
				selectedTiles.add(new Location(x,y));
			}
		}
		builder.setSelectedTiles(selectedTiles);
		builder.setWalkable();
		Square[][] squares = builder.getSquares();
		for (int y = 0; y < 10; y++){
			for (int x = 0; x < 10; x++){
				assertTrue(squares[y][x] instanceof WalkableSquare);
			}
		}
		for (int y = 0; y < 10; y++){
			for (int x =0; x < 10; x++){
				selectedTiles.add(new Location(x,y));
			}
		}
		builder.setBlank();
		for (int y = 0; y < 10; y++){
			for (int x = 0; x < 10; x++){
				assertTrue(squares[y][x] instanceof BlankSquare);
			}
		}
	}


}
