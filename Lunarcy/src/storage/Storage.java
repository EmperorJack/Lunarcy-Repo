package storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

import game.GameState;

public class Storage {
	
	public static final GameState loadState(){
		try {
			FileInputStream file = new FileInputStream("state.xml");
			XStream xstream = new XStream();
			GameState state = (GameState) xstream.fromXML(file);
			return state;
		} catch (FileNotFoundException e) {

		}
		return null;
	}
	
	public static final void saveState(GameState state){
		try {
			FileOutputStream file = new FileOutputStream("state.xml", false);
			XStream xstream = new XStream();
			xstream.toXML(state, file);
		} catch (FileNotFoundException e) {

		}
	}
	
	

}
