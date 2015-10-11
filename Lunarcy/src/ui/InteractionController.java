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

	// For dragging/dropping items
	private Item draggedFromItem;
	private int draggedFromX;
	private int draggedFromY;

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
		return draggedFromItem;
	}

	public int getDraggedItemX(){
		return draggedFromX;
	}

	public int getDraggedItemY(){
		return draggedFromY;
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
		System.out.println(entity);

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

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		//If the inventory was clicked
		if(inventory.onInventoryBar(x, y)){
			//Get the item which was clicked
			draggedFromItem = inventory.getItemAt(x,y);
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		//Set the coordinates of the current item being dragged
		if(draggedFromItem!=null){
			draggedFromX = x;
			draggedFromY = y;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//If the item was not released on the inventory bar, drop it
		if(!inventory.onInventoryBar(draggedFromX, draggedFromY) && draggedFromItem !=null){

			//Drop the dragged item
			dropItem(draggedFromItem.entityID);
			draggedFromItem = null;

			//Reset our dragged values
			draggedFromX = -1000;
			draggedFromY = -1000;
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

	}

}
