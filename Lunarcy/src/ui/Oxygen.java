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

	public Oxygen(PApplet p, GameState gameState) {
		super(p, gameState);
	}

	@Override
	public void update(GameState gameState) {
		// Todo: update local copy of oxygen level
	}

	@Override
	public void draw(float delta) {
		p.noStroke();

		// Draw the cylinder
		p.fill(139, 158, 162);
		p.rect(LEFT_PADDING, TOP_PADDING, CYLINDERWIDTH, CYLINDERHEIGHT);

		// Draw the knob
		p.rect(LEFT_PADDING, TOP_PADDING, 20, 10);

		// Draw the top section
		p.fill(0, 150, 104);
		p.rect(LEFT_PADDING, TOP_PADDING, 35, CYLINDERHEIGHT);
		//p.ellipse(CLYLINDERLEFTSIDE+30+CYLINDERHEIGHT/2, 25+CYLINDERHEIGHT/2, CYLINDERHEIGHT, CYLINDERHEIGHT);
		p.ellipse(LEFT_PADDING, TOP_PADDING+CYLINDERHEIGHT/2, CYLINDERHEIGHT, CYLINDERHEIGHT);

		// Draw the dial
		p.stroke(1);
		p.fill(255, 255, 255);
		p.ellipse(LEFT_PADDING-CYLINDERHEIGHT/2, TOP_PADDING+CYLINDERHEIGHT/2, DIALSIZE, DIALSIZE);

		// Draw the remaining amount of oxygen
		p.noStroke();
		p.fill(0, 0, 0, 100);
		p.rect(LEFT_PADDING+(CYLINDERWIDTH-oxygen), TOP_PADDING, CYLINDERWIDTH, CYLINDERHEIGHT);
		oxygen-=.5;
	}

}
