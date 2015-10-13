package ui.ApplicationWindow;

import processing.core.PApplet;
import ui.DrawingComponent;
import ui.renderer.Canvas;
import game.GameState;
import game.Player;

/**
 * Displays the oxygen bar in the bottom right corner of the canvas
 *
 * @author evansben1
 *
 */
public class Oxygen extends DrawingComponent {

	// Where the oxygen bar starts (x)
	private final int LEFT_PADDING = (int) (Canvas.TARGET_WIDTH * 0.8);
	// Where the oxygen bar starts (y)
	private final int TOP_PADDING = (int) (Canvas.TARGET_HEIGHT * 0.85);

	// Sizing of the oxygen tank
	private final int CYLINDERWIDTH = 200;
	private final int CYLINDERHEIGHT = 35;

	// Size of the dial on the oxygen tank
	private final int DIALSIZE = 10;

	//Turns the oxygen level red if it gets below this point
	private final int LOW_OXYGEN = 30;

	public Oxygen(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// get the player from the current game state
		Player player = gameState.getPlayer(playerID);

		p.pushMatrix();
		p.pushStyle();

		p.noStroke();

		// Translate drawing to match location
		p.translate(LEFT_PADDING, TOP_PADDING);

		// Draw the cylinder
		p.fill(0, 0, 0, 100);
		p.rect(0, 0, CYLINDERWIDTH, CYLINDERHEIGHT);

		// Gey the remaining amount of oxygen
		int oxygen = player.getOxygen();

		p.noStroke();

		// Set the colour to red if there is little oxygen left
		if (oxygen < LOW_OXYGEN) {
			p.fill(255, 0, 0, 100);
		} else {
			p.fill(139, 158, 162);
		}

		//Draw the remaining amount of oxygen
		p.rect(0, 0, oxygen, CYLINDERHEIGHT);

		p.fill(139, 158, 162);

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

		// Dim based on the oxygen level if there is little oxygen left
		if (oxygen < LOW_OXYGEN) {
			dimScreen(oxygen);
		}

		// pop matrix and style information from the stack
		p.popStyle();
		p.popMatrix();
	}

	/**
	 * Darkens the screen to show the player they are low on oxygen
	 */
	private void dimScreen(int oxygen) {
		p.noStroke();
		p.fill(0, 0, 0, PApplet.map(oxygen, 0, 50, 255, 0));
		p.rect(-LEFT_PADDING, -TOP_PADDING, Canvas.TARGET_WIDTH, Canvas.TARGET_HEIGHT);
	}

}
