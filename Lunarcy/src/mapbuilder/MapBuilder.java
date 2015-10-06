package mapbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

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

	boolean insideTiles = false;
	private Location selected = null;
	private Set<Location> selectedTiles;
	private boolean dragging = false;
	private Rectangle selectedArea = null;
	private Rectangle gridArea;
	public static final int GRID_LEFT = 340;
	public static final int GRID_TOP = 60;
	public static final int GRID_SIZE = 30;
	boolean addWalls = false;
	boolean addDoors = false;


	public MapBuilder() {
		selectedTiles = new HashSet<Location>();
		map = new Square[20][20];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j] = new BlankSquare();
				// map[i][j] = new WalkableSquare("Empty", "Empty", true, null,
				// null, null, null);
			}
		}
		gridArea = new Rectangle(GRID_LEFT, GRID_TOP,
				GRID_SIZE * map[0].length, GRID_SIZE * map.length);
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

	public void setWalkable() {
		Iterator<Location> tileIterator = selectedTiles.iterator();
		while (tileIterator.hasNext()) {
			Location currentLoc = tileIterator.next();
			if (insideTiles) {
				map[currentLoc.getY()][currentLoc.getX()] = new WalkableSquare(
						"Empty", "Empty", true, null, null, null, null);
			} else {
				map[currentLoc.getY()][currentLoc.getX()] = new WalkableSquare(
						"Empty", "Empty", false, null, null, null, null);
			}
		}
			selectedTiles.clear();
	}

	public void setBlank() {
		Iterator<Location> tileIterator = selectedTiles.iterator();
		while (tileIterator.hasNext()) {
			Location currentLoc = tileIterator.next();
			map[currentLoc.getY()][currentLoc.getX()] = new BlankSquare();
		}
		selectedTiles.clear();
	}

	public void insideTilesOn() {
		insideTiles = true;

	}

	public void insideTilesOff() {
		insideTiles = false;

	}

	public void getSelectedTiles() {
		selectedTiles.clear();
		if (selectedArea != null) {
			if (selectedArea.intersects(gridArea)) {
				int startX = (int) ((selectedArea.getMinX() - GRID_LEFT) / GRID_SIZE);
				int startY = (int) ((selectedArea.getMinY() - GRID_TOP) / GRID_SIZE);
				if (startX < 0)
					startX = 0;
				if (startY < 0)
					startY = 0;
				int endX = (int) ((selectedArea.getMaxX() - GRID_LEFT) / GRID_SIZE);
				int endY = (int) ((selectedArea.getMaxY() - GRID_TOP) / GRID_SIZE);
				if (endX >= map[0].length)
					endX = map[0].length - 1;
				if (endY >= map.length)
					endY = map[0].length - 1;
				for (int i = startY; i <= endY; i++) {
					for (int j = startX; j <= endX; j++) {
						selectedTiles.add(new Location(j, i));
					}
				}
			}
		}
	}

	public void addWall(Direction dir) {
		if (selected != null
				&& map[selected.getY()][selected.getX()] instanceof WalkableSquare) {
			Square currentSquare = map[selected.getY()][selected.getX()];
			currentSquare.addWall(dir);
			if (dir == Direction.NORTH && selected.getY() > 0) {
				Square adjacentSquare = map[selected.getY() - 1][selected
						.getX()];
				adjacentSquare.addWall(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH && selected.getY() < map.length - 1) {
				Square adjacentSquare = map[selected.getY() + 1][selected
						.getX()];
				adjacentSquare.addWall(Direction.NORTH);
			}
			if (dir == Direction.EAST && selected.getX() < map[0].length - 1) {
				Square adjacentSquare = map[selected.getY()][selected.getX() + 1];
				adjacentSquare.addWall(Direction.WEST);
			}
			if (dir == Direction.WEST && selected.getX() > 0) {
				Square adjacentSquare = map[selected.getY()][selected.getX() - 1];
				adjacentSquare.addWall(Direction.EAST);
			}
		}
	}

	public void removeWall(Direction dir) {
		if (selected != null
				&& map[selected.getY()][selected.getX()] instanceof WalkableSquare) {
			Square currentSquare = map[selected.getY()][selected.getX()];
			currentSquare.removeWall(dir);
			if (dir == Direction.NORTH && selected.getY() > 0) {
				Square adjacentSquare = map[selected.getY() - 1][selected
						.getX()];
				adjacentSquare.removeWall(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH && selected.getY() < map.length - 1) {
				Square adjacentSquare = map[selected.getY() + 1][selected
						.getX()];
				adjacentSquare.removeWall(Direction.NORTH);
			}
			if (dir == Direction.EAST && selected.getX() < map[0].length - 1) {
				Square adjacentSquare = map[selected.getY()][selected.getX() + 1];
				adjacentSquare.removeWall(Direction.WEST);
			}
			if (dir == Direction.WEST && selected.getX() > 0) {
				Square adjacentSquare = map[selected.getY()][selected.getX() - 1];
				adjacentSquare.removeWall(Direction.EAST);
			}
		}
	}

	public void addDoor(Direction dir) {
		if (selected != null
				&& map[selected.getY()][selected.getX()] instanceof WalkableSquare) {
			Square currentSquare = map[selected.getY()][selected.getX()];
			currentSquare.addDoor(dir);
			if (dir == Direction.NORTH && selected.getY() > 0) {
				Square adjacentSquare = map[selected.getY() - 1][selected
						.getX()];
				adjacentSquare.addDoor(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH && selected.getY() < map.length - 1) {
				Square adjacentSquare = map[selected.getY() + 1][selected
						.getX()];
				adjacentSquare.addDoor(Direction.NORTH);
			}
			if (dir == Direction.EAST && selected.getX() < map[0].length - 1) {
				Square adjacentSquare = map[selected.getY()][selected.getX() + 1];
				adjacentSquare.addDoor(Direction.WEST);
			}
			if (dir == Direction.WEST && selected.getX() > 0) {
				Square adjacentSquare = map[selected.getY()][selected.getX() - 1];
				adjacentSquare.addDoor(Direction.EAST);
			}
		}
	}

	public void removeDoor(Direction dir) {
		if (selected != null
				&& map[selected.getY()][selected.getX()] instanceof WalkableSquare) {
			Square currentSquare = map[selected.getY()][selected.getX()];
			currentSquare.removeDoor(dir);
			if (dir == Direction.NORTH && selected.getY() > 0) {
				Square adjacentSquare = map[selected.getY() - 1][selected
						.getX()];
				adjacentSquare.removeDoor(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH && selected.getY() < map.length - 1) {
				Square adjacentSquare = map[selected.getY() + 1][selected
						.getX()];
				adjacentSquare.removeDoor(Direction.NORTH);
			}
			if (dir == Direction.EAST && selected.getX() < map[0].length - 1) {
				Square adjacentSquare = map[selected.getY()][selected.getX() + 1];
				adjacentSquare.removeDoor(Direction.WEST);
			}
			if (dir == Direction.WEST && selected.getX() > 0) {
				Square adjacentSquare = map[selected.getY()][selected.getX() - 1];
				adjacentSquare.removeDoor(Direction.EAST);
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] instanceof WalkableSquare) {
					if (((WalkableSquare) map[i][j]).isInside()) {
						g.setColor(Color.WHITE);
					} else {
						g.setColor(Color.GREEN);
					}
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (selectedTiles.contains(new Location(j, i))) {
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
				if (map[i][j] instanceof WalkableSquare) {
					drawSquare(g, (WalkableSquare) map[i][j], GRID_LEFT + j
							* GRID_SIZE, GRID_TOP + i * GRID_SIZE);
				}
				g.drawRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i * GRID_SIZE,
						GRID_SIZE, GRID_SIZE);
			}
		}
		if (dragging) {
			g.setColor(Color.GREEN);
			g.draw(selectedArea);
			dragging = false;
		}
	}

	public void save() {
		int n = JOptionPane.showConfirmDialog(null, "Would you like to save?",
				"Save", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			try {
				FileOutputStream file = new FileOutputStream("map.xml", false);
				XStream xstream = new XStream();
				xstream.toXML(map, file);
			} catch (FileNotFoundException e) {

			}
		}
	}

	public void load() {
		int n = JOptionPane.showConfirmDialog(null, "Would you like to load?",
				"Load", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			try {
				FileInputStream file = new FileInputStream("map.xml");
				XStream xstream = new XStream();
				map = (Square[][]) xstream.fromXML(file);
			} catch (FileNotFoundException e) {

			}
		}
	}

	public void drawSquare(Graphics g, WalkableSquare square, int x, int y) {
		Map<Direction, Wall> walls = square.getWalls();
		if (walls.get(Direction.NORTH) instanceof SolidWall) {
			g.fillRect(x, y, GRID_SIZE, 3);
		}
		if (walls.get(Direction.EAST) instanceof SolidWall) {
			g.fillRect(x + GRID_SIZE - 3, y, 3, GRID_SIZE);
		}
		if (walls.get(Direction.SOUTH) instanceof SolidWall) {
			g.fillRect(x, y + GRID_SIZE - 3, GRID_SIZE, 3);
		}
		if (walls.get(Direction.WEST) instanceof SolidWall) {
			g.fillRect(x, y, 3, GRID_SIZE);
		}
		g.setColor(Color.ORANGE.darker());
		if (walls.get(Direction.NORTH) instanceof Door) {
			g.fillRect(x, y, GRID_SIZE, 3);
		}
		if (walls.get(Direction.EAST) instanceof Door) {
			g.fillRect(x + GRID_SIZE - 3, y, 3, GRID_SIZE);
		}
		if (walls.get(Direction.SOUTH) instanceof Door) {
			g.fillRect(x, y + GRID_SIZE - 3, GRID_SIZE, 3);
		}
		if (walls.get(Direction.WEST) instanceof Door) {
			g.fillRect(x, y, 3, GRID_SIZE);
		}
		g.setColor(Color.black);

	}

	public void doSelect(int startX, int startY, int x, int y) {
		dragging = true;
		selectedArea = new Rectangle(Math.min(startX, x), Math.min(startY, y),
				Math.abs(x - startX), Math.abs(y - startY));
		getSelectedTiles();
	}

	public void setWall(Direction dir) {
		if (addWalls) {
			addWall(dir);
		} else {
			removeWall(dir);
		}
	}

	public void setDoor(Direction dir) {
		if (addDoors) {
			addDoor(dir);
		} else {
			removeDoor(dir);
		}
	}

	public void toggleWalls() {
		addWalls = !addWalls;
	}

	public void toggleDoors() {
		addDoors = !addDoors;
	}
}
