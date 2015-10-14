package mapbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import storage.Storage;
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
	private List<Location> playerSpawnPoints;
	private List<Location> roverSpawnPoints;
	private boolean dragging = false;
	private Rectangle selectedArea = null;
	private Rectangle gridArea;
	public static final int GRID_LEFT = 340;
	public static final int GRID_TOP = 60;
	public static final int GRID_SIZE = 20;
	boolean addWalls = false;
	int addDoors = -1; // -1 if removing, 0-3 for unlocked to red doors
	int addContainers = -1;// -1 if removing, 0-3 for unlocked to red containers
	boolean addFurniture = false;
	boolean removeFurniture = false;
	private BufferedImage rocketImage;
	final JFileChooser fc = new JFileChooser();

	public MapBuilder() {
		selectedTiles = new HashSet<Location>();
		playerSpawnPoints = new ArrayList<Location>();
		roverSpawnPoints = new ArrayList<Location>();
		squares = new Square[30][30];
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
						true, null, null, null, null);
			} else {
				squares[currentLoc.getY()][currentLoc.getX()] = new WalkableSquare(
						false, null, null, null, null);
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

			squares[selectedTile.getY()][selectedTile.getX()] = new Ship(
					new ShipPart(2001, 0), new ShipPart(2002, 1), new ShipPart(
							2003, 2), new ShipPart(2004, 3), new ShipPart(2005,
							4));

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
						g.setColor(Color.GREEN.darker());
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
				if (playerSpawnPoints.contains(new Location(j, i))) {
					g.setColor(Color.RED);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (roverSpawnPoints.contains(new Location(j, i))) {
					g.setColor(Color.RED.darker());
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
				if (selectedTile != null
						&& (j == selectedTile.getX() && i == selectedTile
								.getY())) {
					g.setColor(Color.BLUE.darker());
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i
							* GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				if (squares[i][j] instanceof Ship) {
					g.setColor(Color.BLACK);
					g.drawImage(rocketImage, GRID_LEFT + j * GRID_SIZE,
							GRID_TOP + i * GRID_SIZE, GRID_SIZE, GRID_SIZE,
							null);
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
				map.setPlayerSpawnPoints(playerSpawnPoints);
				map.setRoverSpawnPoints(roverSpawnPoints);
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
				playerSpawnPoints = map.getPlayerSpawnPoints();
				roverSpawnPoints = map.getRoverSpawnPoints();
				if (playerSpawnPoints == null)
					playerSpawnPoints = new ArrayList<Location>();
				if (roverSpawnPoints == null)
					roverSpawnPoints = new ArrayList<Location>();
			}
		}
	}

	public void drawSquare(Graphics g, WalkableSquare square, int x, int y) {
		Map<Direction, Wall> walls = square.getWalls();
		if (walls.containsKey(Direction.NORTH)
				&& !(walls.get(Direction.NORTH) instanceof EmptyWall)) {
			setWallColor(walls.get(Direction.NORTH), g);
			g.fillRect(x, y, GRID_SIZE, 3);
		}
		if (walls.containsKey(Direction.EAST)
				&& !(walls.get(Direction.EAST) instanceof EmptyWall)) {
			setWallColor(walls.get(Direction.EAST), g);
			g.fillRect(x + GRID_SIZE - 3, y, 3, GRID_SIZE);
		}
		if (walls.containsKey(Direction.SOUTH)
				&& !(walls.get(Direction.SOUTH) instanceof EmptyWall)) {
			setWallColor(walls.get(Direction.SOUTH), g);
			g.fillRect(x, y + GRID_SIZE - 3, GRID_SIZE, 3);
		}
		if (walls.containsKey(Direction.WEST)
				&& !(walls.get(Direction.WEST) instanceof EmptyWall)) {
			setWallColor(walls.get(Direction.WEST), g);
			g.fillRect(x, y, 3, GRID_SIZE);
		}

		Container toDraw = square.getContainer(Direction.NORTH);
		if (toDraw != null) {
			setContainerColor(toDraw, g);
			g.fillRect(x + GRID_SIZE / 2, y + 3, 5, 5);
		}
		toDraw = square.getContainer(Direction.EAST);
		if (toDraw != null) {
			setContainerColor(toDraw, g);
			g.fillRect(x + GRID_SIZE - 8, y + GRID_SIZE / 2, 5, 5);
		}
		toDraw = square.getContainer(Direction.SOUTH);
		if (toDraw != null) {
			setContainerColor(toDraw, g);
			g.fillRect(x + GRID_SIZE / 2, y + GRID_SIZE - 8, 5, 5);
		}
		toDraw = square.getContainer(Direction.WEST);
		if (toDraw != null) {
			setContainerColor(toDraw, g);
			g.fillRect(x + 3, y + GRID_SIZE / 2, 5, 5);
		}

		g.setColor(Color.MAGENTA);
		Furniture furnitureToDraw = square.getFurniture(Direction.NORTH);
		if (furnitureToDraw != null && !(furnitureToDraw instanceof Container)) {
			g.fillRect(x + GRID_SIZE / 2, y + 3, 5, 5);
		}
		furnitureToDraw = square.getFurniture(Direction.EAST);
		if (furnitureToDraw != null&& !(furnitureToDraw instanceof Container)) {
			g.fillRect(x + GRID_SIZE - 8, y + GRID_SIZE / 2, 5, 5);
		}
		furnitureToDraw = square.getFurniture(Direction.SOUTH);
		if (furnitureToDraw != null&& !(furnitureToDraw instanceof Container)) {
			g.fillRect(x + GRID_SIZE / 2, y + GRID_SIZE - 8, 5, 5);
		}
		furnitureToDraw = square.getFurniture(Direction.WEST);
		if (furnitureToDraw != null&& !(furnitureToDraw instanceof Container)) {
			g.fillRect(x + 3, y + GRID_SIZE / 2, 5, 5);
		}

		g.setColor(Color.BLACK);

	}

	private void setWallColor(Wall wall, Graphics g) {
		if (wall instanceof LockedDoor) {
			if (((LockedDoor) wall).getKeyCode() == 1) {
				g.setColor(Color.GREEN);
			} else if (((LockedDoor) wall).getKeyCode() == 2) {
				g.setColor(Color.ORANGE);
			} else if (((LockedDoor) wall).getKeyCode() == 3) {
				g.setColor(Color.RED);
			}
		} else if (wall instanceof Door) {
			g.setColor(Color.ORANGE.darker());
		} else if (wall instanceof SolidWall) {
			g.setColor(Color.black);
		}
	}

	private void setContainerColor(Container container, Graphics g) {
		if (container instanceof LockedChest) {
			if (((LockedChest) container).getAccessLevel() == 1) {
				g.setColor(Color.GREEN);
			} else if (((LockedChest) container).getAccessLevel() == 2) {
				g.setColor(Color.ORANGE);
			} else if (((LockedChest) container).getAccessLevel() == 3) {
				g.setColor(Color.RED);
			}
		}	else {
			g.setColor(Color.BLACK);
		}
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
		} else if (addDoors == 0) {
			addDoor(dir);
		} else if (addDoors > 0) {
			addLockedDoor(dir, addDoors);
		} else if (addContainers == 0) {
			addContainer(dir);
		} else if (addContainers > 0) {
			addLockedContainer(dir, addContainers);
		} else if (addFurniture){
			addFurniture(dir);
		} else if (removeFurniture) {
			removeFurniture(dir);
		} else {
			removeDoor(dir);
		}
	}

	private void addFurniture(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			WalkableSquare currentSquare = (WalkableSquare) squares[highlightedTile
					.getY()][highlightedTile.getX()];
			if (!currentSquare.hasContainer(dir)) {
				if (currentSquare.isInside()){
					currentSquare.setFurniture(dir, new Monitor());
				} else {
					currentSquare.setFurniture(dir, new Rock());
				}
				map.increaseEntityCount();
			}
		}
	}

	private void addLockedContainer(Direction dir, int access) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			WalkableSquare currentSquare = (WalkableSquare) squares[highlightedTile
					.getY()][highlightedTile.getX()];
			if (!currentSquare.hasContainer(dir)) {
				currentSquare.setFurniture(dir,
						new LockedChest(map.getEntityCount() + 500, access));
				map.increaseEntityCount();
			}
		}

	}

	private void addLockedDoor(Direction dir, int access) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			Square currentSquare = squares[highlightedTile.getY()][highlightedTile
					.getX()];
			currentSquare.addLockedDoor(dir, access);
			if (dir == Direction.NORTH && highlightedTile.getY() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY() - 1][highlightedTile
						.getX()];
				adjacentSquare.addLockedDoor(Direction.SOUTH, access);
			}
			if (dir == Direction.SOUTH
					&& highlightedTile.getY() < squares.length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY() + 1][highlightedTile
						.getX()];
				adjacentSquare.addLockedDoor(Direction.NORTH, access);
			}
			if (dir == Direction.EAST
					&& highlightedTile.getX() < squares[0].length - 1) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() + 1];
				adjacentSquare.addLockedDoor(Direction.WEST, access);
			}
			if (dir == Direction.WEST && highlightedTile.getX() > 0) {
				Square adjacentSquare = squares[highlightedTile.getY()][highlightedTile
						.getX() - 1];
				adjacentSquare.addLockedDoor(Direction.EAST, access);
			}
		}

	}

	private void addContainer(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			WalkableSquare currentSquare = (WalkableSquare) squares[highlightedTile
					.getY()][highlightedTile.getX()];
			if (!currentSquare.hasContainer(dir)) {
				currentSquare.setFurniture(dir, new Chest(
						map.getEntityCount() + 500));
				map.increaseEntityCount();
			}
		}
	}

	private void removeFurniture(Direction dir) {
		if (highlightedTile != null
				&& squares[highlightedTile.getY()][highlightedTile.getX()] instanceof WalkableSquare) {
			WalkableSquare currentSquare = (WalkableSquare) squares[highlightedTile
					.getY()][highlightedTile.getX()];
			if (currentSquare.hasFurniture(dir)) {
				currentSquare.removeFurniture(dir);
				map.decreaseEntityCount();
			}
		}
	}

	// TODO Auto-generated method stub

	public void wallsOn() {
		addWalls = true;
	}

	public void wallsOff() {
		addWalls = false;
	}

	public void doorsOn(int accessLevel) {
		addDoors = accessLevel;
	}

	public void doorsOff() {
		addDoors = -1;
	}

	public void containersOn(int accessLevel) {
		addContainers = accessLevel;
	}

	public void containersOff() {
		addContainers = -1;
	}

	public void removeFurnitureOn() {
		removeFurniture = true;
	}

	public void removeFurnitureOff() {
		removeFurniture = false;
	}

	public void addPlayerSpawnPoint() {
		boolean added = false;
		if (selectedTile != null && !playerSpawnPoints.contains(selectedTile)) {
			Square squareToAdd = squares[selectedTile.getY()][selectedTile
					.getX()];
			if (squareToAdd instanceof WalkableSquare
					&& ((WalkableSquare) squareToAdd).isInside()) {
				playerSpawnPoints.add(selectedTile);
				added = true;
			}
		}
		if (!added) {
			JOptionPane.showMessageDialog(null,
					"Invalid selected tile for player spawn point!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void removePlayerSpawnPoint() {
		if (selectedTile != null && playerSpawnPoints.contains(selectedTile)) {
			playerSpawnPoints.remove(selectedTile);
		}
	}

	public void addRoverSpawnPoint() {
		boolean added = false;
		if (selectedTile != null && !roverSpawnPoints.contains(selectedTile)) {
			Square squareToAdd = squares[selectedTile.getY()][selectedTile
					.getX()];
			if (squareToAdd instanceof WalkableSquare
					&& !((WalkableSquare) squareToAdd).isInside()) {
				roverSpawnPoints.add(selectedTile);
				added = true;
			}
		}
		if (!added) {
			JOptionPane.showMessageDialog(null,
					"Invalid selected tile for rover spawn point!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void removeRoverSpawnPoint() {
		if (selectedTile != null && roverSpawnPoints.contains(selectedTile)) {
			roverSpawnPoints.remove(selectedTile);
		}
	}

	public void initialiseItems() {
		map.initTierDictionary();
	}

	public void furnitureOn() {
		addFurniture = true;
	}

	public void furnitureOff() {
		addFurniture = false;
	}

}
