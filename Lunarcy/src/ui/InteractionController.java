package ui;

import game.GameState;
import game.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import control.Client;
import control.DropAction;
import control.MoveAction;
import control.OrientAction;
import control.PickupAction;
import control.PutAction;

/**
 * This holds all the UI componenets which are related to Entitities ie
 * Inventory, Menus and drawing entities. Listens for all player input to
 * perform interaction between UI componenets and player movement.
 *
 * @author Ben and Jack
 *
 */
public class InteractionController implements KeyListener, MouseListener {

	// Drawing components
	private Inventory inventory;
	private EntityView entityView;

	// Menu fields
	private Menu menu;
	private boolean menuActive;

	// Client related field
	private Client client;
	private GameState gameState;
	private Player player;

	// Canvas field
	private Canvas canvas;

	public InteractionController(Client client, GameState gamestate,
			Player player, Canvas canvas) {
		this.client = client;
		this.gameState = gamestate;
		this.player = player;
		this.canvas = canvas;

		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}

	public void update(GameState gameState, Player player) {
		this.gameState = gameState;
		this.player = player;
	}

	/** Action methods **/

	public void dropItem(int itemID) {
		client.sendAction(new DropAction(player.getId(), itemID));
	}

	public void putItem(int itemID, int containerID) {
		client.sendAction(new PutAction(player.getId(), itemID, containerID));
	}

	public void pickupItem(int itemID) {
		client.sendAction(new PickupAction(player.getId(), itemID));
	}

	/** Menu methods **/

	public boolean menuActive() {
		return menuActive;
	}

	public void menuActive(boolean menu) {
		this.menuActive = menu;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;

		// Set the menu to visible if menu is non null
		menuActive = menu != null;
	}

	/** Key and Mouse Listening methods **/

	@Override
	public void keyPressed(KeyEvent e) {

		// identify which key pressed
		switch (e.getKeyCode()) {

		// move forward
		case KeyEvent.VK_W:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation()));
			break;

		// strafe left
		case KeyEvent.VK_A:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation().left()));
			break;

		// move back
		case KeyEvent.VK_S:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation().opposite()));
			break;

		// strafe right
		case KeyEvent.VK_D:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation().right()));
			break;

		// turn left
		case KeyEvent.VK_Q:
			client.sendAction(new OrientAction(player.getId(), true));
			break;

		// turn right
		case KeyEvent.VK_E:
			client.sendAction(new OrientAction(player.getId(), false));
			break;

		// Hide/Show menu
		case KeyEvent.VK_SPACE:
			if (menu == null) {
				setMenu(new PickMenu(canvas, this, gameState, player.getId()));
			} else {
				setMenu(null);
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(entityView.getEntityAt(e.getX(), e.getY()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
