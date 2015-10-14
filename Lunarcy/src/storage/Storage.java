package storage;

import java.io.BufferedReader;

import game.Furniture;
import game.Item;
import game.Square;
import game.Wall;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import mapbuilder.GameMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
			//XStream xstream = new XStream();
			//xstream.toXML(map, file);
			GsonBuilder gson = new GsonBuilder();
			gson.registerTypeAdapter(Square.class, new SquareAdapter());
			gson.registerTypeAdapter(Wall.class, new WallAdapter());
			gson.registerTypeAdapter(Furniture.class, new FurnitureAdapter());
			gson.registerTypeAdapter(Item.class, new ItemAdapter());
			file.write( gson.enableComplexMapKeySerialization().setPrettyPrinting().create().toJson(map).getBytes());
		} catch (IOException e) {

		}
	}

	public static final GameMap loadGameMap(File fileName){
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			GsonBuilder gson = new GsonBuilder();
			gson.registerTypeAdapter(Square.class, new SquareAdapter());
			gson.registerTypeAdapter(Wall.class, new WallAdapter());
			gson.registerTypeAdapter(Furniture.class, new FurnitureAdapter());
			gson.registerTypeAdapter(Item.class, new ItemAdapter());
			GameMap gameMap = gson.setPrettyPrinting().create().fromJson(file, GameMap.class);
			return gameMap;
		} catch (FileNotFoundException e) {

		}
		return null;
	}


}
