package mapbuilder;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Swing canvas for the map builder. Currently displays a 10x10 grid.
 *
 * @author Kelly
 *
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel{
	MapBuilder mapBuilder;

	public Canvas(MapBuilder builder) {
		this.mapBuilder = builder;
		setSize(1280, 720);

		//Set up controls
		MapBuilderController controller = new MapBuilderController(mapBuilder, this);
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 1280, 720);
		mapBuilder.draw(g);
	}

}
