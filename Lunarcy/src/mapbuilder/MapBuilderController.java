package mapbuilder;

import game.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;

public class MapBuilderController implements MouseListener,
		MouseMotionListener, KeyListener {
	MapBuilder builder;
	Canvas canvas;
	int startX;
	int startY;

	public MapBuilderController(MapBuilder builder, Canvas canvas) {
		this.builder = builder;
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'w') {
			builder.setWall(Direction.North);
		}
		if (e.getKeyChar() == 'a') {
			builder.setWall(Direction.West);

		}
		if (e.getKeyChar() == 's') {
			builder.setWall(Direction.South);

		}
		if (e.getKeyChar() == 'd') {
			builder.setWall(Direction.East);
		}
		if (e.getKeyChar() == 'x') {
			try {
				builder.save();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getKeyChar() == 'c') {
			try {
				builder.load();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getKeyChar() == 'm'){
			builder.toggleWalkable();
		}
		canvas.repaint();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		builder.doSelect(startX, startY, e.getX(), e.getY());
		canvas.repaint();

	}

	public void mouseMoved(MouseEvent e) {
		builder.setTile(e.getX(), e.getY());
		canvas.repaint();
	}

}
