package mapbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.thoughtworks.xstream.XStream;

import game.*;

/**
 * Class for the MapBuilder Logic
 * 
 * @author Kelly
 *
 */
public class MapBuilder {
	public Square[][] map;

	private Location selected = null;
	private Set<Location> selectedTiles;
	private boolean dragging = false;
	private Rectangle selectedArea = null;
	private Rectangle gridArea;
	public static final int GRID_LEFT = 340;
	public static final int GRID_TOP = 60;
	public static final int GRID_SIZE = 30;

	public MapBuilder() {
		selectedTiles = new HashSet<Location>();
		map = new Square[20][20];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j] = new WalkableSquare("Empty", "Empty", true, null,
						null, null, null);
			}
		}
		gridArea = new Rectangle(GRID_LEFT,GRID_TOP, GRID_SIZE*map[0].length, GRID_SIZE *map.length);
	}

	public void setTile(int x, int y) {
		if (x >= GRID_LEFT && y >= GRID_TOP) {
			int selectedX = (x - GRID_LEFT) / GRID_SIZE;
			int selectedY = (y - GRID_TOP) / GRID_SIZE;
			selected = new Location(selectedX, selectedY);
		} else {
			selected = null;
		}
	}
	
	public void getSelectedTiles(){
		selectedTiles.clear();
		if(selectedArea != null){
			if (selectedArea.intersects(gridArea)){
				int startX = (int)((selectedArea.getMinX() - GRID_LEFT)/ GRID_SIZE);
				int startY = (int)((selectedArea.getMinY() - GRID_TOP)/ GRID_SIZE);
				if (startX < 0) startX = 0;
				if (startY < 0) startY = 0;
				int endX = (int)((selectedArea.getMaxX() - GRID_LEFT)/ GRID_SIZE);
				int endY = (int)((selectedArea.getMaxY() - GRID_TOP)/ GRID_SIZE);
				if (endX >= map[0].length) endX = map[0].length -1;
				if (endY >= map.length) endY = map[0].length - 1;
				for (int i = startY; i<= endY; i++){
					for (int j = startX; j <= endX; j++){
						selectedTiles.add(new Location(j, i));
					}
				}
			}
		}
	}

	public void setWall(Direction dir) {
		if (selected != null) {
			WalkableSquare currentSquare = (WalkableSquare) map[selected.getY()][selected
					.getX()];
			currentSquare.toggleWall(dir);
			if (dir == Direction.North && selected.getY() > 0) {
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected
						.getY() - 1][selected.getX()];
				adjacentSquare.toggleWall(Direction.South);
			}
			if (dir == Direction.South && selected.getY() < map.length - 1) {
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected
						.getY() + 1][selected.getX()];
				adjacentSquare.toggleWall(Direction.North);
			}
			if (dir == Direction.East && selected.getX() < map[0].length - 1) {
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected
						.getY()][selected.getX() + 1];
				adjacentSquare.toggleWall(Direction.West);
			}
			if (dir == Direction.West && selected.getX() > 0) {
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected
						.getY()][selected.getX() - 1];
				adjacentSquare.toggleWall(Direction.East);
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (selectedTiles.contains(new Location(j,i))){
					g.setColor(Color.PINK);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (selected != null
						&& (j == selected.getX() && i == selected.getY())) {
					g.setColor(Color.BLUE);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				g.drawRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i * GRID_SIZE,
						GRID_SIZE, GRID_SIZE);
				drawSquare(g, (WalkableSquare) map[i][j], GRID_LEFT + j
						* GRID_SIZE, GRID_TOP + i * GRID_SIZE);
			}
		}
		if (dragging){
			g.setColor(Color.GREEN);
			g.draw(selectedArea);
			dragging = false;
		}
	}

	public void save() throws FileNotFoundException {
		try {
		      FileOutputStream file = new FileOutputStream("map.xml", false);
		      XStream xstream = new XStream();
		      xstream.toXML(map, file);
		} catch (FileNotFoundException e) {
			
		}
	}
	
	public void load() throws FileNotFoundException {
		try {
		      FileInputStream file = new FileInputStream("map.xml");
		      XStream xstream = new XStream();
		      map = (Square[][]) xstream.fromXML(file);
		} catch (FileNotFoundException e) {
			
		}
	}

	public void drawSquare(Graphics g, WalkableSquare square, int x, int y) {
		Map<Direction, Wall> walls = square.getWalls();
		if (walls.get(Direction.North) instanceof SolidWall) {
			g.fillRect(x, y, GRID_SIZE, 3);
		}
		if (walls.get(Direction.East) instanceof SolidWall) {
			g.fillRect(x + GRID_SIZE - 3, y, 3, GRID_SIZE);
		}
		if (walls.get(Direction.South) instanceof SolidWall) {
			g.fillRect(x, y + GRID_SIZE - 3, GRID_SIZE, 3);
		}
		if (walls.get(Direction.West) instanceof SolidWall) {
			g.fillRect(x, y, 3, GRID_SIZE);
		}
	}

	public void doSelect(int startX, int startY, int x, int y) {
		dragging = true;
		selectedArea = new Rectangle(Math.min(startX, x), Math.min(startY, y), Math.abs(x - startX), Math.abs(y - startY));
		getSelectedTiles();
	}
}
