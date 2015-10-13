package ui.ApplicationWindow;

import processing.core.PApplet;
import game.GameState;
import ui.DrawingComponent;
import ui.renderer.Canvas;

/**
 * Can be used for displaying a Title
 * and Description in the middle of the
 * Canvas.
 *
 *
 * @author evansben1
 *
 */
public class PopupDisplay extends DrawingComponent {

	//The main heading of the display
	private String title;

	//The subtext of the display
	private String desc;

	//To deal with concurrency when updating
	private String updatedTitle;
	private String updatedDesc;
	private boolean stateUpdated;

	//Size of the display
	private final int WIDTH = 500;
	private final int HEIGHT = 150;
	private final int TITLE_HEIGHT = HEIGHT/4;

	//X,Y Coordinates for the top left
	private final int LEFT_PADDING = p.TARGET_WIDTH/2 - WIDTH/2;
	private final int TOP_PADDING = p.TARGET_HEIGHT/2 - HEIGHT/2;


	public PopupDisplay(String title, String desc, Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);
		this.title = title;
		this.desc = desc;
	}

	public synchronized void set(String title, String desc){
		this.updatedTitle = title;
		this.updatedDesc = desc;
		this.stateUpdated = true;
	}

	public synchronized void update() {
		//If we need to update
		if (stateUpdated) {

			title = updatedTitle;
			desc = updatedDesc;

			//the state has been updated
			stateUpdated = false;
		}
	}

	/**
	 * Returns true if both Title
	 * and description are non null.
	 */
	public boolean isSet(){
		return title!=null && desc!=null;
	}
	@Override
	public void draw(GameState gameState, float delta) {

		update();

		//Dont draw unless the fields have been set
		if(desc==null || title == null){
			return;
		}

		//So these transformations are independent of the other components
		p.pushMatrix();
		p.pushStyle();

		//Translate to create the padding
		p.translate(LEFT_PADDING, TOP_PADDING);

		//Draw the background box
		p.fill(0,0,0, 200);
		p.rect(0, 0, WIDTH, HEIGHT);

		//Draw the title box
		p.fill(255,255,255,100);
		p.rect(0, 0, WIDTH, TITLE_HEIGHT);

		//Draw the Title
		p.textSize(25);
		p.fill(0,0,0,255);
		p.textAlign(PApplet.CENTER, PApplet.CENTER);
		p.text(title, 0, 0, WIDTH, TITLE_HEIGHT);

		//Draw the description
		p.fill(255,255,255,100);
		p.textSize(15);
		p.text(desc, 0, TITLE_HEIGHT, WIDTH, HEIGHT-TITLE_HEIGHT);


		//Finished with these transformations now, so take them off the stack
		p.popStyle();
		p.popMatrix();
	}





}
