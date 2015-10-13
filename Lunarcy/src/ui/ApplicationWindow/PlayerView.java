package ui.ApplicationWindow;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameState;
import game.Player;
import ui.DrawingComponent;
import ui.renderer.Canvas;

public class PlayerView extends DrawingComponent {

	// player drawing fields
	private final int PLAYER_WIDTH = 200;
	private final int PLAYER_HEIGHT = 400;
	private final int TOP_PADDING = 400;
	private final PImage playerImage;

	public PlayerView(Canvas p, GameState gameState, int playerID) {
		super(p, gameState, playerID);

		// load the player image
		playerImage = p.loadImage("/assets/characters/Player.png");
	}

	@Override
	public void draw(GameState gameState, float delta) {
		// push matrix and style information onto the stack
		p.pushMatrix();
		p.pushStyle();

		// get the player from the current game state
		Player thisPlayer = gameState.getPlayer(playerID);

		// get the list of players from the game state
		Player[] players = gameState.getPlayers();

		ArrayList<Player> playersInSquare = new ArrayList<Player>();

		// for each player
		for (int i = 0; i < players.length; i++) {

			// if the player is in the current square and not this player
			if (players[i].getLocation().equals(thisPlayer.getLocation())
					&& !players[i].equals(thisPlayer)) {

				// add the player to list of players in this square
				playersInSquare.add(players[i]);
			}
		}

		// translate to allow for top padding
		p.translate(0, TOP_PADDING);

		p.imageMode(PApplet.CENTER);

		// for each player
		for (int i = 0; i < playersInSquare.size(); i++) {

			Player currentPlayer = players[i];

			// tint the image with the current player colour
			p.tint(currentPlayer.getColour().getRGB());

			// compute the x position to place the image
			int xPos = (int) ((i + 1) / (float) (playersInSquare.size() + 1) * Canvas.TARGET_WIDTH);

			// draw the item image
			p.image(playerImage, xPos, PLAYER_HEIGHT / 2, PLAYER_WIDTH,
					PLAYER_HEIGHT);
		}
	}
}
