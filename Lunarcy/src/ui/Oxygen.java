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

	private float oxygen = 200; //TEMP
	
	//Where the oxygen bar starts (x)
	private final int LEFT_PADDING = (int)(p.width*0.9);
	//Where the oxygen bar starts (y)
	private final int TOP_PADDING = (int)(p.height*0.9);
	
	//Sizing of the oxygen tank
	private final int CYLINDERWIDTH = 200;
	private final int CYLINDERHEIGHT = 55;
	
	//Size of the dial on the oxygen tank
	private final int DIALSIZE = 15;

	public Oxygen(PApplet p, GameState gameState, PGraphics g) {
		super(p, gameState, g);
	}

	@Override
	public void update(GameState gameState) {
		// Todo: update local copy of oxygen level
	}

	@Override
	public void draw(float delta) {
		g.noStroke();

		// Draw the cylinder
		g.fill(139, 158, 162);
		g.rect(LEFT_PADDING, TOP_PADDING, CYLINDERWIDTH, CYLINDERHEIGHT);

		// Draw the knob
		g.rect(LEFT_PADDING, TOP_PADDING, 20, 10);

		// Draw the top section
		g.fill(0, 150, 104);
		g.rect(LEFT_PADDING, TOP_PADDING, 35, CYLINDERHEIGHT);
		//p.ellipse(CLYLINDERLEFTSIDE+30+CYLINDERHEIGHT/2, 25+CYLINDERHEIGHT/2, CYLINDERHEIGHT, CYLINDERHEIGHT);
		g.ellipse(LEFT_PADDING, TOP_PADDING+CYLINDERHEIGHT/2, CYLINDERHEIGHT, CYLINDERHEIGHT);

		// Draw the dial
		g.stroke(1);
		g.fill(255, 255, 255);
		g.ellipse(LEFT_PADDING-CYLINDERHEIGHT/2, TOP_PADDING+CYLINDERHEIGHT/2, DIALSIZE, DIALSIZE);

		// Draw the remaining amount of oxygen
		g.noStroke();
		g.fill(0, 0, 0, 100);
		g.rect(LEFT_PADDING+(CYLINDERWIDTH-oxygen), TOP_PADDING, CYLINDERWIDTH, CYLINDERHEIGHT);
		oxygen-=.5;
	}

}
