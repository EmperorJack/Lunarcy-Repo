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
 * This class listens for player input to allow interaction between the UI
 * componenets and player movement/actions.
 *
 * @author Ben and Jack
 *
 */
public class InteractionController implements KeyListener, MouseListener, MouseMotionListener {

	// Drawing components
	private Inventory inventory;
	private EntityView entityView;

	// Dragging/dropping items

	// Dragging out of inventory
	private Item draggedFromItem;

	// Dragging into inventory
	private Item draggedToItem;

	// The coordinates of the item currently being dragged
	private int draggedX;
	private int draggedY;

	// The coordinates for dragging if none are specified
	private final int DEFAULT_X = -1000;
	private final int DEFAULT_Y = -1000;

	// Client related fields
	private Client client;
	private Player player;

	// Canvas field
	private Canvas canvas;

	public InteractionController(Client client, GameState gamestate, Player player, Canvas canvas) {

		this.client = client;
		this.player = player;
		this.canvas = canvas;

		// Initialize the values for dragging/dropping items
		resetDragValues();

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

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}

	public void update(GameState gameState, Player player) {
		this.player = player;
	}

	/**
	 * If either the item being dragged from the inventory, or the item being
	 * dragged to the inventory is non null, returns this item (only one can be
	 * non null at a time).
	 *
	 * If both are null, returns null.
	 *
	 * @return
	 */
	public Item getDraggedItem() {
		
		if (draggedFromItem != null) {
			return draggedFromItem;
		}

		if (draggedToItem != null) {
			return draggedToItem;
		}

		return null;
	}

	public int getDraggedItemX() {
		return draggedX;
	}

	public int getDraggedItemY() {
		return draggedY;
	}

	/** Key and Mouse Listening methods **/

	@Override
	public void keyPressed(KeyEvent e) {

		// identify which key pressed
		switch (e.getKeyCode()) {

		// move forward
		case KeyEvent.VK_W:
			client.sendAction(new MoveAction(player.getId(), player.getOrientation()));
			break;

		// strafe left
		case KeyEvent.VK_A:
			client.sendAction(new MoveAction(player.getId(), player.getOrientation().left()));
			break;

		// move back
		case KeyEvent.VK_S:
			client.sendAction(new MoveAction(player.getId(), player.getOrientation().opposite()));
			break;

		// strafe right
		case KeyEvent.VK_D:
			client.sendAction(new MoveAction(player.getId(), player.getOrientation().right()));
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
		System.out.println(entity);

		// Do something with containers
		if (entity != null && entity instanceof Container) {

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// If the inventory was clicked
		if (inventory.onInventoryBar(x, y)) {
			// Get the item which was clicked
			draggedFromItem = inventory.getItemAt(x, y);
			draggedToItem = null;

			draggedX = x;
			draggedY = y;
			return;
		}

		Entity entity = entityView.getEntityAt(x, y);
		if (entity != null && entity instanceof Item) {
			// Set the item to be picked up
			draggedToItem = (Item) entity;
			draggedFromItem = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// Set the coordinates of the current item being dragged
		if (draggedFromItem != null || draggedToItem != null) {
			draggedX = x;
			draggedY = y;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// If the item was not released on the inventory bar, and there was an
		// item currently being dragged, drop it
		if (!inventory.onInventoryBar(x, y) && draggedFromItem != null) {

			// Drop the dragged from item
			dropItem(draggedFromItem.entityID);

		}

		// If it was released on the inventory bar, check if theres a dragged to
		// item
		else if (inventory.onInventoryBar(x, y) && draggedToItem != null) {

			// Pickup the item which was dragged onto the inventory
			pickupItem(draggedToItem.entityID);
		}

		resetDragValues();

	}

	/**
	 * Set all the fields related to dragging back to their default.
	 */
	private void resetDragValues() {
		draggedToItem = null;
		draggedFromItem = null;
		draggedX = DEFAULT_X;
		draggedY = DEFAULT_Y;
	}

	/* Unused listener methods */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
