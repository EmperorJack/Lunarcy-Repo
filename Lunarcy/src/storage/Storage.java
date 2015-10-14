package storage;

import java.io.BufferedReader;

import game.Furniture;
import game.Item;
import game.Square;
import game.Wall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import mapbuilder.GameMap;
import bots.MoveStrategy;

import com.google.gson.GsonBuilder;

import game.GameState;

public class Storage {

	public static final GameState loadState(String fileName){
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			GsonBuilder gson = new GsonBuilder();
			gson.registerTypeAdapter(Square.class, new SquareAdapter());
			gson.registerTypeAdapter(Wall.class, new WallAdapter());
			gson.registerTypeAdapter(Furniture.class, new FurnitureAdapter());
			gson.registerTypeAdapter(Item.class, new ItemAdapter());
			gson.registerTypeAdapter(MoveStrategy.class, new MoveStrategyAdapter());
			GameState state = gson.setPrettyPrinting().create().fromJson(file, GameState.class);
			return state;
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	public static final void saveState(GameState state, String fileName){
		try {
			FileOutputStream file = new FileOutputStream(fileName, false);
			GsonBuilder gson = new GsonBuilder();
			gson.registerTypeAdapter(Square.class, new SquareAdapter());
			gson.registerTypeAdapter(Wall.class, new WallAdapter());
			gson.registerTypeAdapter(Furniture.class, new FurnitureAdapter());
			gson.registerTypeAdapter(Item.class, new ItemAdapter());
			gson.registerTypeAdapter(MoveStrategy.class, new MoveStrategyAdapter());
			file.write( gson.enableComplexMapKeySerialization().setPrettyPrinting().create().toJson(state).getBytes());
			file.close();
		} catch (IOException e) {
			System.out.println("FILE WRITE ERROR.");
		}
	}

	public static final void saveGameMap(GameMap map, File fileToWrite){
		try {
			FileOutputStream file = new FileOutputStream(fileToWrite, false);
			GsonBuilder gson = new GsonBuilder();
			gson.registerTypeAdapter(Square.class, new SquareAdapter());
			gson.registerTypeAdapter(Wall.class, new WallAdapter());
			gson.registerTypeAdapter(Furniture.class, new FurnitureAdapter());
			gson.registerTypeAdapter(Item.class, new ItemAdapter());
			file.write( gson.enableComplexMapKeySerialization().setPrettyPrinting().create().toJson(map).getBytes());
			file.close();
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
