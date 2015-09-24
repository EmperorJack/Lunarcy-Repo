package mapbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * Swing canvas for the map builder. Currently displays a 10x10 grid.
 * @author Kelly
 *
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel implements MouseListener, MouseMotionListener{
	public static final int GRID_LEFT = 340;
	public static final int GRID_TOP = 60;
	private int selectedX = -1;
	private int selectedY = -1;
	
	public Canvas(){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setSize(1280,720);
	}
	
	public void paintComponent (Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(0, 0,1280,720);
		g.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++){
				if (j == selectedX && i == selectedY){
					g.setColor(Color.BLUE);
					g.fillRect(GRID_LEFT + j * 60,GRID_TOP + i * 60, 60, 60);
					g.setColor(Color.BLACK);
				}
				g.drawRect(GRID_LEFT + j * 60,GRID_TOP + i * 60, 60, 60);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		selectedX = (arg0.getX() - GRID_LEFT)/60;
		selectedY = (arg0.getY() - GRID_TOP)/60;
		repaint();
		//System.out.println(selectedX);
	}
	
}
