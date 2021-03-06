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
 * A class for communicating with the Server
 * Handles Canvas creation and passing events back and forward
 *
 * @author denforjohn
 *
 */
public class Client {
	// Network related fields
	private static final int DEFAULT_PORT = 58627;
	private Socket socket;
	private ObjectInputStream inputFromServer;
	private ObjectOutputStream outputToServer;
	private int id = -1;
	private String name;
	private Frame frame;
	private int frameWidth;
	private int frameHeight;
	private boolean hardwareRenderer;

	/**
	 * Construct a Client
	 *
	 * @param serverAddr String address of the server
	 * @param name	Username of the client
	 * @param colour Colour of the client
	 * @param frameWidth Width of the canvas frame
	 * @param frameHeight Height of the canvas frame
	 * @param hardwareRenderer Whether or not to use a hardware renderer
	 * @throws IllegalArgumentException
	 */
	public Client(String serverAddr, String name, Color colour, int frameWidth,
			int frameHeight, boolean hardwareRenderer)
			throws IllegalArgumentException {

		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.hardwareRenderer = hardwareRenderer;
		this.name = name;
		try {
			socket = new Socket(serverAddr, DEFAULT_PORT);
			System.out.println("bound socket");
			outputToServer = new ObjectOutputStream(socket.getOutputStream());
			inputFromServer = new ObjectInputStream(socket.getInputStream());
			negotiateConnection(name, colour);
		} catch (IOException e) {
			System.out.println("Couldn't establish connection");
			throw new IllegalArgumentException("Bad IP");
		}

		getInitialGamestate();
		System.out.println("Listening for gamestate");
	}
	/**
	 * Negotiate the connection between the client and server
	 *
	 * @param name userName of this user
	 * @param colour Colour of this player
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void negotiateConnection(String name, Color colour) throws IllegalArgumentException, IOException {
		do {
			writeObject(name);
			this.id = readInt();
			if (this.id == Server.INVALID_USERNAME) {
				name = showDialog("To join this game, please enter the correct name");
			} else if (this.id == Server.USERNAME_TAKEN) {
				name = showDialog("Username already taken");
			}
		} while (this.id == Server.USERNAME_TAKEN || this.id == Server.INVALID_USERNAME);
		this.name = name;
		// Send hex colour
		String hexColour = String.format("#%02x%02x%02x", colour.getRed(), colour.getGreen(), colour.getBlue());
		writeObject(hexColour);
		System.out.println("Name sent to server: " + name);

	}

	/**
	 * A dialog which prompts for an ammended username
	 *
	 * @return The value entered
	 */
	private String showDialog(String message) {
		String s = (String) JOptionPane.showInputDialog(null, message,
				"Invalid Username", JOptionPane.PLAIN_MESSAGE, null, null,
				this.name);
		return s;
	}

	/**
	 *  Gets the initial gamestate from the server
	 */
	public void getInitialGamestate() {
		// get gamestate setup game
		GameState initialGameState = null;
		while (initialGameState == null) {
			initialGameState = getGameState(); // wait for the initial gamestate
		}
		this.frame = new Frame(this, initialGameState, frameWidth, frameHeight,
				hardwareRenderer);
	}
	/**
	 * Listen for updates sent from the server
	 * If recieved, update the state in the canvas
	 */
	public void listenForGameUpdates() {
		new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Object obj;

						obj = inputFromServer.readObject();

						if (obj instanceof String) {
							String val = (String) obj;
							if (val.equals("quit"))
								break;
						} else if (obj instanceof GameState) {
							GameState gameState = (GameState) obj;
							frame.getCanvas().setGameState(gameState);
						}
					}
				} catch (ClassNotFoundException | IOException e) {
				} finally {
					disconnect();
				}
			}
		}).start();
	}
	/**
	 * Return the gamestate from the server when received
	 * @return
	 */
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
	/**
	 * Send a specific action to the server
	 * @param action Action to be applied to the master game logic
	 */
	public boolean sendAction(NetworkAction action) {
		try {
			writeObject(action);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	/**
	 * Read an integer from the server
	 *
	 * @return value from server
	 * @throws IOException Faied to read value
	 */
	private int readInt() throws IOException {
			return inputFromServer.readInt();
	}
	/**
	 * Disconnect this client from the server
	 */
	private void disconnect() {
		JOptionPane.showMessageDialog(null, "Disconnected from Server");
		try {
			inputFromServer.close();
			outputToServer.close();
			socket.close();
		} catch (IOException e) {
		} finally {
			System.exit(1);
		}
	}
	/**
	 *
	 * @param o
	 * @return
	 * @throws IOException
	 */
	private void writeObject(Object o) throws IOException {
		outputToServer.reset();
		outputToServer.writeObject(o);
		outputToServer.flush();
	}

	public int getPlayerID() {
		return id;
	}

}
