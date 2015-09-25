package mapbuilder;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import game.*;


/**
 *  Class for the MapBuilder Logic
 * @author Kelly
 *
 */
public class MapBuilder {
	public Square[][] map;

	private Location selected = null;
	public static final int GRID_LEFT = 340;
	public static final int GRID_TOP = 60;
	public static final int GRID_SIZE = 30;

	public MapBuilder(){
		map = new Square[20][20];
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[0].length;j++){
				map[i][j] = new WalkableSquare("Empty", "Empty", true, null, null, null, null);
			}
		}
	}

	public void setTile(int x, int y){
		if (x >= GRID_LEFT && y >= GRID_TOP) {
			int selectedX = (x - GRID_LEFT) / GRID_SIZE;
			int selectedY = (y - GRID_TOP) / GRID_SIZE;
			selected = new Location(selectedX, selectedY);
		} else {
			selected = null;
		}
	}

	public void setWall(Direction dir){
		if (selected != null){
			WalkableSquare currentSquare = (WalkableSquare) map[selected.getY()][selected.getX()];
			currentSquare.toggleWall(dir);
			if (dir == Direction.North && selected.getY() > 0){
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected.getY() - 1][selected.getX()];
				adjacentSquare.toggleWall(Direction.South);
			}
			if (dir == Direction.South && selected.getY() < map.length -1){
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected.getY()+1][selected.getX()];
				adjacentSquare.toggleWall(Direction.North);
			}
			if (dir == Direction.East && selected.getX() < map[0].length - 1){
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected.getY()][selected.getX() + 1];
				adjacentSquare.toggleWall(Direction.West);
			}
			if (dir == Direction.West && selected.getX() > 0){
				WalkableSquare adjacentSquare = (WalkableSquare) map[selected.getY()][selected.getX()-1];
				adjacentSquare.toggleWall(Direction.East);
			}
		}
	}

	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (selected != null && (j == selected.getX() && i == selected.getY())) {
					g.setColor(Color.BLUE);
					g.fillRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i * GRID_SIZE, GRID_SIZE, GRID_SIZE);
					g.setColor(Color.BLACK);
				}
				g.drawRect(GRID_LEFT + j * GRID_SIZE, GRID_TOP + i * GRID_SIZE, GRID_SIZE, GRID_SIZE);
				drawSquare(g, (WalkableSquare) map[i][j], GRID_LEFT + j * GRID_SIZE, GRID_TOP + i * GRID_SIZE);
			}
		}
	}

	public void drawSquare(Graphics g, WalkableSquare square, int x, int y){
		Map<Direction, Wall> walls = square.getWalls();
		if (walls.get(Direction.North) instanceof SolidWall){
			g.fillRect(x, y, GRID_SIZE, 3);
		}
		if (walls.get(Direction.East) instanceof SolidWall){
			g.fillRect(x + GRID_SIZE-3, y, 3, GRID_SIZE);
		}
		if (walls.get(Direction.South) instanceof SolidWall){
			g.fillRect(x, y + GRID_SIZE - 3, GRID_SIZE, 3);
		}
		if (walls.get(Direction.West) instanceof SolidWall){
			g.fillRect(x, y, 3, GRID_SIZE);
		}
	}
}
