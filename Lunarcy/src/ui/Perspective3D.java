package ui;

import processing.core.*;
import game.GameState;

/**
 * The view that displays the player perspective of the game world in 3D.
 * 
 * @author Jack
 *
 */
public class Perspective3D extends DrawingComponent {

	// temporary background images
	private Animation shrek;

	public Perspective3D(PApplet p, GameState gameState) {
		super(p, gameState);

		shrek = new Animation("assets/animations/shrek/shrek_", 20);
	}

	@Override
	public void update(GameState state) {
		// TODO update the 3D perspective

	}

	@Override
	public void draw() {
		p.pushMatrix();
		p.pushStyle();

		// TODO draw the 3D perspective
		p.translate(0, 0, -500);
		p.rotateY(PApplet.radians(p.frameCount));
		p.scale(4);
		shrek.display(0, 0);

		p.popStyle();
		p.popMatrix();
	}

	public class Animation {
		private PImage[] images;
		private int imageCount;
		private int frame;
		private boolean halfRate = false;

		public Animation(String imagePrefix, int count) {
			imageCount = count;
			images = new PImage[imageCount];

			for (int i = 0; i < imageCount; i++) {
				String filename = imagePrefix + PApplet.nf(i, 4) + ".jpg";
				images[i] = p.loadImage(filename);
			}
		}

		public void display(float x, float y) {
			halfRate = !halfRate;
			if (halfRate)
				frame = (frame + 1) % imageCount;
			p.image(images[frame], x, y);
		}
	}
}
