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
				builder.setWall(Direction.NORTH);
		}
		if (e.getKeyChar() == 'a') {
				builder.setWall(Direction.WEST);

		}
		if (e.getKeyChar() == 's') {
				builder.setWall(Direction.SOUTH);

		}
		if (e.getKeyChar() == 'd') {
				builder.setWall(Direction.EAST);
		}
		if (e.getKeyChar() == 'x') {
				builder.save();
		}
		if (e.getKeyChar() == 'c') {
				builder.load();
		}
		if (e.getKeyChar() == 'm') {
			builder.setWalkable();
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
		builder.setHighlighted(e.getX(), e.getY());
		canvas.repaint();
	}

}
