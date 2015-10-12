package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import mapbuilder.GameMap;

import com.thoughtworks.xstream.XStream;

import game.GameState;

public class Storage {

	public static final GameState loadState(String fileName){
		try {
			FileInputStream file = new FileInputStream(fileName);
			XStream xstream = new XStream();
			GameState state = (GameState) xstream.fromXML(file);
			return state;
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	public static final void saveState(GameState state, String fileName){
		try {
			FileOutputStream file = new FileOutputStream(fileName, false);
			XStream xstream = new XStream();
			xstream.toXML(state, file);
		} catch (FileNotFoundException e) {
			System.out.println("FILE WRITE ERROR.");
		}
	}

	public static final void saveGameMap(GameMap map, File fileToWrite){
		try {
			FileOutputStream file = new FileOutputStream(fileToWrite, false);
			XStream xstream = new XStream();
			xstream.toXML(map, file);
		} catch (FileNotFoundException e) {

		}
	}

	public static final GameMap loadGameMap(File fileName){
		try {
			FileInputStream file = new FileInputStream(fileName);
			XStream xstream = new XStream();
			GameMap gameMap = (GameMap) xstream.fromXML(file);
			return gameMap;
		} catch (FileNotFoundException e) {

		}
		return null;
	}


}
