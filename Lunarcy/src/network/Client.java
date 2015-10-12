package network;

import game.GameState;
import ui.Frame;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * A class for
 *
 * @author denforjohn
 *
 */
public class Client {
	// Network related fields
	private String serverAddr;
	private static final int DEFAULT_PORT = 58627;
	private Socket socket;
	private ObjectInputStream inputFromServer;
	private ObjectOutputStream outputToServer;
	private int id = -1;
	private String name;
	private Frame frame;
	private Color colour;
	private int frameWidth;
	private int frameHeight;
	private boolean hardwareRenderer;

	public Client(String serverAddr, String name, Color colour, int frameWidth,
			int frameHeight, boolean hardwareRenderer)
			throws IllegalArgumentException, IllegalArgumentException {
		this.serverAddr = serverAddr;
		this.colour = colour;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.hardwareRenderer = hardwareRenderer;
		this.name = name;
		try {
			socket = new Socket(serverAddr, DEFAULT_PORT);
			System.out.println("bound socket");
			outputToServer = new ObjectOutputStream(socket.getOutputStream());
			inputFromServer = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't establish connection");
			throw new IllegalArgumentException("Bad IP");
		}
		negotiateConnection(name, colour);

		getInitialGamestate();

		System.out.println("Listening for gamestate");
		// listenForGameUpdates(); //listen for gamestates from the server
	}

	private void negotiateConnection(String name, Color colour)
			throws IllegalArgumentException {
		writeObject(name);
		this.id = readInt();
		// Send hex colour
		String hexColour = String.format("#%02x%02x%02x", colour.getRed(),
				colour.getGreen(), colour.getBlue());
		System.out.println("hex colour  " + hexColour);
		//showDialog();
		writeObject(hexColour);
		System.out.println("Name sent to server: " + name);

		// do
		// sendName
		// if response = -1
		// writeObject
		// writeObject(name);
		// int response = readInt();
		// if(response == -1)throw new
		// IllegalArgumentException("Not a valid Name");
		// id = response;
		// String hexColour = String.format("#%02x%02x%02x",
		// this.colour.getRed(), this.colour.getGreen(), this.colour.getBlue());

	}
	/**
	 * A dialog which prompts for an ammended username
	 * @return The value entered
	 */
	private String showDialog() {
		String s = (String) JOptionPane.showInputDialog(null,
				"To join this game, please enter the correct name",
				"Invalid Username", JOptionPane.PLAIN_MESSAGE, null,
				null, this.name);
		return s;
	}

	public void getInitialGamestate() {
		// get gamestate setup game
		GameState initialGameState = null;
		while (initialGameState == null) {
			initialGameState = getGameState(); // wait for the initial gamestate
		}
		this.frame = new Frame(this, initialGameState, frameWidth, frameHeight,
				hardwareRenderer);
	}

	public void listenForGameUpdates() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					GameState state = getGameState();
					if (state != null) {
						frame.getCanvas().setGameState(state);
					}
				}
			}
		}).start();
	}

	private GameState getGameState() {
		GameState state = null;
		try {
			// System.out.println("attempting to listen for game update");
			state = (GameState) inputFromServer.readObject();
			return state;
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendAction(NetworkAction action) {
		writeObject(action);
	}

	private int readInt() {
		System.out.println("trying to read ID");
		try {
			return inputFromServer.readInt();
			// System.out.println("My clientID is: " + id);
		} catch (IOException e) {
			System.err.println("cant read ID");
			e.printStackTrace();
		}
		return -1;
	}

	private boolean writeObject(Object o) {
		if (o != null) {
			try {
				outputToServer.reset();
				outputToServer.writeObject(o);
				outputToServer.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	public int getPlayerID() {
		return id;
	}

	public static void main(String[] args) {
		new Client("localhost", "JP" + (int) (Math.random() * 100), Color.RED,
				1280, 720, true);
	}
}
