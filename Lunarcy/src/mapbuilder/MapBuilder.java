package mapbuilder;
import java.awt.Color;
import java.awt.Graphics;

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

	public MapBuilder(){
		map = new Square[10][10];
	}

	public void setTile(int x, int y){
		if (x >= GRID_LEFT && y >= GRID_TOP) {
			int selectedX = (x - GRID_LEFT) / 60;
			int selectedY = (y - GRID_TOP) / 60;
			selected = new Location(selectedX, selectedY);
		} else {
			selected = null;
		}
	}

	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (selected != null && (j == selected.getX() && i == selected.getY())) {
					g.setColor(Color.BLUE);
					g.fillRect(GRID_LEFT + j * 60, GRID_TOP + i * 60, 60, 60);
					g.setColor(Color.BLACK);
				}
				g.drawRect(GRID_LEFT + j * 60, GRID_TOP + i * 60, 60, 60);
			}
		}
	}
}
