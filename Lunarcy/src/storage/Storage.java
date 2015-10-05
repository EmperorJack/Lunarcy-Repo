package storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

		}
	}



}
