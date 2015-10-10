package ui;

import game.Container;
import game.Entity;
import game.GameState;
import game.Item;
import game.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
public class InteractionController implements KeyListener, MouseListener, MouseMotionListener{

	// Drawing components
	private Inventory inventory;
	private EntityView entityView;

	// Menu fields
	private Menu menu;
	private boolean menuActive;

	private Item draggedItem;
	private int draggedX;
	private int draggedY;

	// Client related field
	private Client client;
	private Player player;

	// Canvas field
	private Canvas canvas;

	public InteractionController(Client client, GameState gamestate,
			Player player, Canvas canvas) {
		this.client = client;
		this.player = player;
		this.canvas = canvas;

		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
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

	public Menu getMenu() {
		return menu;
	}

	/**
	 * Updates the menu and sets the menu to be active if is non null.
	 *
	 * @param menu
	 */
	public void setMenu(Menu menu) {
		this.menu = menu;

		// Set the menu to visible if menu is non null
		menuActive = menu != null;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}

	public void update(GameState gameState, Player player) {
		this.player = player;
	}

	public Item getDraggedItem(){
		return draggedItem;
	}

	public int getDraggedItemX(){
		return draggedX;
	}

	public int getDraggedItemY(){
		return draggedY;
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
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// adjust the x and y to the canvas scaling
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// attempt to get an entity from entity view
		Entity entity = entityView.getEntityAt(x, y);

		// if an entity was clicked on
		if (entity != null) {
			if (entity instanceof Item) {
				// attempt to pickup the item
				pickupItem(entity.entityID);
			} else if (entity instanceof Container) {
				// attempt to access container
			}
			return;
		}

		// perform check for click on inventory
		inventory.inventoryClicked(x, y);
	}

	/* Unused listener methods */

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		//If the inventory was clicked
		if(inventory.onInventoryBar(x, y)){
			draggedItem = inventory.getItemAt(x,y);
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		if(draggedItem!=null){
			draggedX = e.getX();
			draggedY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		//If the item was not reeleased on the inventory bar, drop it
		if(!inventory.onInventoryBar(draggedX, draggedY) && draggedItem !=null){
			dropItem(draggedItem.entityID);
			draggedItem = null;
		}


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

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
