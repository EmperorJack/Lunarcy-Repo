package ui;

import java.awt.Color;

import game.GameState;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Displays the oxygen bar in the bottom right corner of the canvas
 *
 * @author b
 *
 */
public class Oxygen extends DrawingComponent {

	private float oxygen = 170; // TEMP

	// Where the oxygen bar starts (x)
	private final int LEFT_PADDING = (int) (p.width * 0.85);
	// Where the oxygen bar starts (y)
	private final int TOP_PADDING = (int) (p.height * 0.8);

	// Sizing of the oxygen tank
	private final int CYLINDERWIDTH = 170;
	private final int CYLINDERHEIGHT = 35;

	// Size of the dial on the oxygen tank
	private final int DIALSIZE = 10;

	public Oxygen(PApplet p, GameState gameState) {
		super(p, gameState);

		// set the initial game state
		update(gameState);
	}

	@Override
	public void update(GameState gameState) {
		// Todo: update local copy of oxygen level
	}

	@Override
	public void draw(float delta) {
		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the cylinder
		p.fill(139, 158, 162);
		p.rect(0, 0, CYLINDERWIDTH, CYLINDERHEIGHT);

		// Draw the knob
		p.rect(-35, CYLINDERHEIGHT / 2 - DIALSIZE / 4, 20, DIALSIZE / 2);

		// Draw the top section
		p.fill(0, 150, 104);
		p.rect(0, 0, 17, CYLINDERHEIGHT);
		p.ellipse(0, CYLINDERHEIGHT / 2, CYLINDERHEIGHT, CYLINDERHEIGHT);

		// Draw the dial
		p.stroke(1);
		p.fill(255, 255, 255);
		p.ellipse(-CYLINDERHEIGHT / 2, CYLINDERHEIGHT / 2, DIALSIZE, DIALSIZE);

		// Draw the remaining amount of oxygen
		p.noStroke();
		p.fill(0, 0, 0, 100);
		p.rect(CYLINDERWIDTH - oxygen, 0, CYLINDERWIDTH, CYLINDERHEIGHT);
		oxygen -= .5;

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

}
