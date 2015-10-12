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

import javax.swing.SwingUtilities;
import ui.ApplicationWindow.ContainerView;
import ui.ApplicationWindow.InventoryView;
import ui.renderer.Canvas;
import ui.renderer.EntityView;
import network.Client;
import network.CloseAction;
import network.DropAction;
import network.MoveAction;
import network.OpenAction;
import network.OrientAction;
import network.PickupAction;
import network.PutAction;

/**
 * This class listens for player input to allow interaction between the UI
 * componenets and player movement/actions.
 *
 * @author evansben1 and Jack
 *
 */
public class InteractionController implements KeyListener, MouseListener,
		MouseMotionListener {

	// Drawing components
	private InventoryView inventoryView;
	private EntityView entityView;
	private ContainerView containerView;

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


	public InteractionController(Client client, GameState gamestate,
			Player player, Canvas canvas) {
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

	private void dropItem(int itemID) {
		client.sendAction(new DropAction(player.getId(), itemID));
	}

	private void putItem(int itemID) {
		client.sendAction(new PutAction(player.getId(), itemID));
	}

	private void pickupItem(int itemID) {
		client.sendAction(new PickupAction(player.getId(), itemID));
	}

	private void openContainer() {
		client.sendAction(new OpenAction(player.getId()));
	}

	private void closeContainer() {
		client.sendAction(new CloseAction(player.getId()));
	}

	public void setInventory(InventoryView inventory) {
		this.inventoryView = inventory;
	}

	/** Update methods */

	public void setContainerView(ContainerView containerView) {
		this.containerView = containerView;
	}

	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}


	public void update(Player player) {
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

		// attempt to get a container from entity view
		Container clickedContainer = entityView.getContainerAt(x, y);

		// If an open container is clicked, shut it
		if (clickedContainer != null && !clickedContainer.isOpen()) {
			// Hide/show it
			openContainer();
		} else if (clickedContainer != null && clickedContainer.isOpen()) {
			closeContainer();
		}
		
		//On a right click, show item  descriptions
		if(SwingUtilities.isRightMouseButton(e)){
			
			Item item = entityView.getItemAt(x, y);
			
			if(item!=null){
				
			}
			
		}
		

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// If the inventory was clicked
		if (inventoryView.onBar(x, y)) {
			// Get the item which was clicked
			draggedFromItem = inventoryView.getItemAt(x, y);
			draggedToItem = null;

			draggedX = x;
			draggedY = y;
			return;
		}

		// If the container menu was clicked
		if (containerView.getContainer() != null && containerView.onBar(x, y)) {

			System.out.println("pressed");

			// Get the item which was clicked
			draggedToItem = containerView.getItemAt(x, y);
			draggedFromItem = null;

			return;
		}

		Entity entity = entityView.getItemAt(x, y);
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

		// If they drop an item onto a container
		Container container = entityView.getContainerAt(x, y);

		if (draggedFromItem != null && container != null) {
			// Put the item in the container
			putItem(draggedFromItem.getEntityID());
		}
		// If the item was not released on the inventory bar, and there was an
		// item currently being dragged, drop it
		else if (draggedFromItem != null && !inventoryView.onBar(x, y)) {
			// Drop the dragged from item
			dropItem(draggedFromItem.getEntityID());
		}

		// If it was released on the inventory bar, check if theres a dragged to
		// item
		else if (inventoryView.onBar(x, y) && draggedToItem != null) {

			// Pickup the item which was dragged onto the inventory
			pickupItem(draggedToItem.getEntityID());
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
