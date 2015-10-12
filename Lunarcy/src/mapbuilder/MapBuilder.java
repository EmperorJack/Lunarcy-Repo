package mapbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import storage.Storage;

import com.thoughtworks.xstream.XStream;

import game.*;

/**
 * Class for the MapBuilder Logic
 *
 * @author Kelly
 *
 */
public class MapBuilder {
	public GameMap map;
	public Square[][] squares;

	boolean insideTiles = false;
	private Location highlightedTile = null;
	private Location selectedTile = null;
	private Set<Location> selectedTiles;
	private boolean dragging = false;
	private Rectangle selectedArea = null;
	private Rectangle gridArea;
	public static final int GRID_LEFT = 340;
	public static final int GRID_TOP = 60;
	public static final int GRID_SIZE = 30;
	boolean addWalls = false;
	boolean addDoors = false;
	boolean addContainers = false;
	boolean removeContainers = false;
	private BufferedImage rocketImage;
	final JFileChooser fc = new JFileChooser();

	public MapBuilder() {
		selectedTiles = new HashSet<Location>();
		squares = new Square[20][20];
		map = new GameMap();

		fc.setCurrentDirectory(new File(System.getProperty("user.dir")
				+ "/assets/maps"));

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				squares[i][j] = new BlankSquare();
				// map[i][j] = new WalkableSquare("Empty", "Empty", true, null,
				// null, null, null);
			}
		}
		gridArea = new Rectangle(GRID_LEFT, GRID_TOP, GRID_SIZE
				* squares[0].length, GRID_SIZE * squares.length);

		try {
			// TODO: Replace with creative commons image
			rocketImage = ImageIO
					.read(new File("assets/mapbuilder/rocket.png"));
		} catch (IOException e) {
			// Error loading image
			return;
		}

	}

	public void setHighlighted(int x, int y) {
		if (x >= GRID_LEFT && y >= GRID_TOP) {
			int selectedX = (x - GRID_LEFT) / GRID_SIZE;
			int selectedY = (y - GRID_TOP) / GRID_SIZE;
			highlightedTile = new Location(selectedX, selectedY);
		} else {
			highlightedTile = null;
		}
	}

	public void setSelected() {
		if (highlightedTile != null) {
			selectedTile = new Location(highlightedTile.getX(),
					highlightedTile.getY());
		}
	}

	public void setWalkable() {
		Iterator<Location> tileIterator = selectedTiles.iterator();
		while (tileIterator.hasNext()) {
			Location currentLoc = tileIterator.next();
			if (insideTiles) {
				squares[currentLoc.getY()][currentLoc.getX()] = new WalkableSquare(
						"Empty", "Empty", true, null, null, null, null);
			} else {
				squares[currentLoc.getY()][currentLoc.getX()] = new WalkableSquare(
						"Empty", "Empty", false, null, null, null, null);
			}
		}
		selectedTiles.clear();
	}

	public void setBlank() {
		Iterator<Location> tileIterator = selectedTiles.iterator();
		while (tileIterator.hasNext()) {
			Location currentLoc = tileIterator.next();
			squares[currentLoc.getY()][currentLoc.getX()] = new BlankSquare();
		}
		selectedTiles.clear();
	}

	public void setShip() {
		if (selectedTile != null) {
			if (squares[selectedTile.getY()][selectedTile.getX()] instanceof Ship) {
				squares[selectedTile.getY()][selectedTile.getX()] = new BlankSquare();
			}
			for (int i = 0; i < squares.length; i++) {
				for (int j = 0; j < squares[0].length; j++) {
					if (squares[i][j] instanceof Ship) {
						JOptionPane.showMessageDialog(null,
								"Ship already present!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}

			squares[selectedTile.getY()][selectedTile.getX()] = new Ship();

		}
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
				if (endX >= squares[0].length)
					endX = squares[0].length - 1;
				if (endY >= squares.length)
					endY = squares[0].length - 1;
				for (int i = startY; i <= endY; i++) {
					for (int j = startX; j <= endX; j++) {
						selectedTiles.add(new Location(j, i));
					}
				}
			}
		}
	}

	public void addWall(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			Square currentSquare = squares[highlightedTile.getY()][highlightedTile
					.getX()];
			currentSquare.addWall(dir);
			if (dir == Direction.NORTH && highlightedTile.getY() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY() - 1][highlightedTile
						.getX()];
				adjacentSquare.addWall(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH
					&& highlightedTile.getY() < squares.length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY() + 1][highlightedTile
						.getX()];
				adjacentSquare.addWall(Direction.NORTH);
			}
			if (dir == Direction.EAST
					&& highlightedTile.getX() < squares[0].length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() + 1];
				adjacentSquare.addWall(Direction.WEST);
			}
			if (dir == Direction.WEST && highlightedTile.getX() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() - 1];
				adjacentSquare.addWall(Direction.EAST);
			}
		}
	}

	public void removeWall(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			Square currentSquare = squares[highlightedTile.getY()][highlightedTile
					.getX()];
			currentSquare.removeWall(dir);
			if (dir == Direction.NORTH && highlightedTile.getY() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY() - 1][highlightedTile
						.getX()];
				adjacentSquare.removeWall(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH
					&& highlightedTile.getY() < squares.length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY() + 1][highlightedTile
						.getX()];
				adjacentSquare.removeWall(Direction.NORTH);
			}
			if (dir == Direction.EAST
					&& highlightedTile.getX() < squares[0].length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() + 1];
				adjacentSquare.removeWall(Direction.WEST);
			}
			if (dir == Direction.WEST && highlightedTile.getX() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() - 1];
				adjacentSquare.removeWall(Direction.EAST);
			}
		}
	}

	public void addDoor(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			Square currentSquare = squares[highlightedTile.getY()][highlightedTile
					.getX()];
			currentSquare.addDoor(dir);
			if (dir == Direction.NORTH && highlightedTile.getY() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY() - 1][highlightedTile
						.getX()];
				adjacentSquare.addDoor(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH
					&& highlightedTile.getY() < squares.length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY() + 1][highlightedTile
						.getX()];
				adjacentSquare.addDoor(Direction.NORTH);
			}
			if (dir == Direction.EAST
					&& highlightedTile.getX() < squares[0].length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() + 1];
				adjacentSquare.addDoor(Direction.WEST);
			}
			if (dir == Direction.WEST && highlightedTile.getX() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() - 1];
				adjacentSquare.addDoor(Direction.EAST);
			}
		}
	}

	public void removeDoor(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			Square currentSquare = squares[highlightedTile.getY()][highlightedTile
					.getX()];
			currentSquare.removeDoor(dir);
			if (dir == Direction.NORTH && highlightedTile.getY() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY() - 1][highlightedTile
						.getX()];
				adjacentSquare.removeDoor(Direction.SOUTH);
			}
			if (dir == Direction.SOUTH
					&& highlightedTile.getY() < squares.length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY() + 1][highlightedTile
						.getX()];
				adjacentSquare.removeDoor(Direction.NORTH);
			}
			if (dir == Direction.EAST
					&& highlightedTile.getX() < squares[0].length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() + 1];
				adjacentSquare.removeDoor(Direction.WEST);
			}
			if (dir == Direction.WEST && highlightedTile.getX() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() - 1];
				adjacentSquare.removeDoor(Direction.EAST);
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				if (squares[i][j] instanceof WalkableSquare) {
					if (((WalkableSquare) squares[i][j]).isInside()) {
						g.setColor(Color.WHITE);
					} else {
						g.setColor(Color.GREEN);
					}
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (squares[i][j] instanceof Ship) {
					g.setColor(Color.WHITE);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
					g.drawImage(rocketImage, GRID_LEFT + j * GRID_SIZE,
							GRID_TOP + i * GRID_SIZE, GRID_SIZE, GRID_SIZE,
							null);
				}
				if (selectedTiles.contains(new Location(j, i))) {
					g.setColor(Color.PINK);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (highlightedTile != null
						&& (j == highlightedTile.getX() && i == highlightedTile
								.getY())) {
					g.setColor(Color.BLUE);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (squares[i][j] instanceof WalkableSquare
						&& !(squares[i][j] instanceof Ship)) {
					drawSquare(g, (WalkableSquare) squares[i][j], GRID_LEFT + j
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
			int returnValue = fc.showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				map.setSquares(squares);
				Storage.saveGameMap(map, fc.getSelectedFile());
			}
		}
	}

	public void load() {
		int n = JOptionPane.showConfirmDialog(null, "Would you like to load?",
				"Load", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			int returnValue = fc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				map = Storage.loadGameMap(fc.getSelectedFile());
				squares = map.getSquares();
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
		g.setColor(Color.yellow);
		Container toDraw = square.getContainer(Direction.NORTH);
		if (toDraw != null) {
			g.fillRect(x + GRID_SIZE / 2, y + 3, 5, 5);
		}
		toDraw = square.getContainer(Direction.EAST);
		if (toDraw != null) {
			g.fillRect(x + GRID_SIZE - 8, y + GRID_SIZE / 2, 5, 5);
		}
		toDraw = square.getContainer(Direction.SOUTH);
		if (toDraw != null) {
			g.fillRect(x + GRID_SIZE / 2, y + GRID_SIZE - 8, 5, 5);
		}
		toDraw = square.getContainer(Direction.WEST);
		if (toDraw != null) {
			g.fillRect(x + 3, y + GRID_SIZE / 2, 5, 5);
		}
		g.setColor(Color.BLACK);

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
		} else if (addDoors) {
			addDoor(dir);
		} else if (addContainers) {
			addContainer(dir);
		} else if (removeContainers){
			removeContainer(dir);
		} else {
			removeDoor(dir);
		}
	}

	private void addContainer(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			WalkableSquare currentSquare = (WalkableSquare) squares[highlightedTile
					.getY()][highlightedTile.getX()];
			if (!currentSquare.hasContainer(dir)) {
				currentSquare
						.setContainer(dir, new Chest(map.getEntityCount()));
				map.increaseEntityCount();
			}
		}
	}

	private void removeContainer(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			WalkableSquare currentSquare = (WalkableSquare) squares[highlightedTile
					.getY()][highlightedTile.getX()];
			if (currentSquare.hasContainer(dir)) {
				currentSquare.removeContainer(dir);
				map.decreaseEntityCount();
			}
		}
	}

	public void wallsOn() {
		addWalls = true;
	}

	public void wallsOff() {
		addWalls = false;
	}

	public void doorsOn() {
		addDoors = true;
	}

	public void doorsOff() {
		addDoors = false;
	}

	public void containersOn() {
		addContainers = true;
	}

	public void containersOff() {
		addContainers = false;
	}

	public void removeContainersOn() {
		removeContainers = true;
	}

	public void removeContainersOff() {
		removeContainers = false;
	}
}
