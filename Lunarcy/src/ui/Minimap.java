package ui;

import game.GameState;
import processing.core.*;

/**
 * The minimap overlay that displays a 2D top down view of the game world.
 * 
 * @author Jack & Ben
 *
 */
public class Minimap extends DrawingComponent {

	public Minimap(PApplet p, GameState gameState) {
		super(p, gameState);
	}

	@Override
	public void update(GameState state) {
		// TODO update the minimap

	}

	@Override
	public void draw() {
		p.pushMatrix();
		p.pushStyle();
		
		// TODO draw the minimap
		p.fill(150);
		p.rect(50, 50, 100, 100);
		
		p.popStyle();
		p.popMatrix();
	}
}