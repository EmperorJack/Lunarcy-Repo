package mapbuilder;

import game.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MapBuilderController implements MouseListener,
		MouseMotionListener, KeyListener {
	MapBuilder builder;
	Canvas canvas;

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
		// TODO Auto-generated method stub

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
		canvas.repaint();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent e) {
		builder.setTile(e.getX(), e.getY());
		canvas.repaint();
	}

}
