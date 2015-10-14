package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import game.GameState;
import game.Location;
import game.Square;
import mapbuilder.GameMap;

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
		String fileToLoad = "assets/maps/testmap.json";
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

}
