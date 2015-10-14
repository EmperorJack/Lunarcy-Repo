package network;

import game.GameLogic;
import game.GameState;
import game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.Color;

//import network.NewServer.ClientConnection;
import storage.Storage;

/**
 * This class handles communication between all clients and the gameLogic over a
 * network
 *
 * @author JTFM
 *
 */
public class Server extends Thread {
	// Network related fields
	private ServerSocket serverSocket;
	private int maxClients;
	private static final int PORT = 58627;
	private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
	private LinkedBlockingQueue<NetworkAction> actionQueue = new LinkedBlockingQueue<NetworkAction>();
	private GameLogic gameLogic;
	private int updateFreq;
	private boolean running = true;
	private boolean fromSavedGame = false;

	public static int INVALID_USERNAME = -1;
	public static int USERNAME_TAKEN = -2;

	/**
	 * Create a new server with an existing gamestate
	 *
	 * @param maxClients
	 * @param updateFreq
	 * @param gameState
	 * @throws IOException
	 */
	public Server(int updateFreq, GameState gameState) throws IOException {
		this.maxClients = gameState.getPlayers().length;
		this.updateFreq = updateFreq;
		gameLogic = new GameLogic(gameState);
		serverSocket = new ServerSocket(PORT);
		fromSavedGame = true;
	}

	public Server(int maxClients, int updateFreq, String map)
			throws IOException {
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;
		GameState gameState = new GameState(maxClients, map);
		gameLogic = new GameLogic(gameState);
		serverSocket = new ServerSocket(PORT);
	}

	/**
	 * Blocking method to gracefully shutdown server
	 */
	public void stopServer() {
		running = false;
		for (ClientConnection client : clientList) {
			client.stopClient(); // stop every client connection
			System.out.println("stopped " + client.username);
		}
		System.out.println("Stopping server");
		// stop main server
		while (clientList.size() > 0) {
			sleep(50);
		}
	}

	/**
	 * Gracefully stop the server and close all sockets/connections
	 */
	public void stopAndSave(String filename) {
		stopServer();
		Storage.saveState(gameLogic.getGameState(), filename);
	}

	/**
	 * Sleep the server for the given
	 *
	 * @param time
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	/**
	 *
	 * @throws IOException
	 */
	public void listenForClients() {

		System.out.println("Listeneing for clients");
		// wait for all clients to connect
		while (clientList.size() < maxClients) {
			Socket s;
			try {
				s = serverSocket.accept();
			} catch (IOException e) {
				continue;
			}

			// set -1 if from saved game
			int clientID = (fromSavedGame) ? -1 : clientList.size();
			ClientConnection client;

			try {
				client = new ClientConnection(s, clientID);
			} catch (IOException e) {
				try {
					s.close();
				} catch (IOException e1) {
				}
				continue;
			}
			clientList.add(client);
			client.start();
		}

	}

	/**
	 * Run the client processing client actions and sending gamestates
	 */
	public void run() {
		try {
			listenForClients();
			waitForClients(); // wait for all clients to be ready
			transmitState(); // transmit initially
			System.out.println("Server running fully");

			long lastUpdate = System.currentTimeMillis();

			// run server
			while (running && clientList.size() > 0) {
				if (System.currentTimeMillis() > lastUpdate + updateFreq) {
					gameLogic.tickGameState();
					if(gameLogic.getWinner() != null){
						transmitState();
						break;
					}
					transmitState();
					lastUpdate = System.currentTimeMillis();
				} else {
					processAction();
				}
			}
		} catch (IOException e) {
		} finally {
			System.out.println("Server shutting down");
			for (ClientConnection c : clientList)
				c.stopClient(); // stop all clients
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
			System.out.println("Closing down server");
			//System.exit(1);
		}
	}

	/**
	 * Wait for all clients to be ready
	 */
	private void waitForClients() {
		while (!allClientsReady()) {
			sleep(50);
		}
	}

	/**
	 * Check that all clients in the client list are ready to begin the game
	 *
	 * @return <tt>true</tt> if all clients ready to begin
	 */
	private boolean allClientsReady() {
		ArrayList<ClientConnection> clients = getClientConnectionsClone();
		for (ClientConnection c : clients) {
			if (!c.isRunning())
				return false;
		}
		return true;
	}

	/**
	 * Send out the current gamestate to all clients
	 *
	 * @throws IOException
	 */
	private void transmitState() throws IOException {
		// TODO This can be made more efficient by serialising once before
		// transmitting
		GameState state = gameLogic.getGameState();
		for (int i = 0; i < clientList.size(); i++) {
			ClientConnection client = clientList.get(i);
			client.writeObject(state);
		}
	}

	/**
	 * Process the next client action from the queue
	 */
	// TODO extend method to apply move to all clients if successful
	private void processAction() {
		NetworkAction action = actionQueue.poll();
		if (action != null) {
			action.applyAction(gameLogic);// interpreter.interpret(action);
		}
	}
	/**
	 * Add a player to the gamestate given a client
	 *
	 * @param c ClientConnection holds details for new player
	 * @return If from saved game, returns player id else returns the clients id
	 */
//	synchronized private int addPlayerToGame(ClientConnection c) {
//		if (fromSavedGame) {
//			return gameLogic.getGameState().getPlayerID(c.username);
//		} else {
//			gameLogic.getGameState()
//					.addPlayer(c.clientId, c.username, c.colour);
//			return c.clientId;
//		}
//	}

	// methods for working with clientlist

	synchronized private int addClientConnection(ClientConnection cc) {
		this.clientList.add(cc);
		return this.clientList.indexOf(cc);
	}

	synchronized private boolean removeClientConnection(ClientConnection cc) {
		return this.clientList.remove(cc);
	}

	synchronized private ClientConnection getClientConnection(int index) {
		return this.clientList.get(index);
	}

	synchronized private ArrayList<ClientConnection> getClientConnectionsClone() {
		// TODO check this is all good
		return (ArrayList<ClientConnection>) this.clientList.clone();
	}

	/**
	 * Saves the current gamestate to disc
	 */
	public void saveGamestate(String filename) {
		Storage.saveState(gameLogic.getGameState(), filename);
	}

	public int getMaxClients() {
		return maxClients;
	}

	private class ClientConnection extends Thread {
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private int clientId;
		private String username;
		private Color colour;
		private boolean clientRunning = false;

		ClientConnection(Socket socket, int clientId) throws IOException {
			this.socket = socket;
			this.clientId = clientId;
			outputToClient = new ObjectOutputStream(socket.getOutputStream());
			inputFromClient = new ObjectInputStream(socket.getInputStream());
		}

		public boolean isRunning() {
			return clientRunning;
		}

		private void negotiateConnection() {
			try {
				String name = "";
				int tempId = -1;
				do {
					name = (String) inputFromClient.readObject();
					// clientId = addPlayerToGamestate(name);
					if (fromSavedGame) {
						// retrieve their previous name
						this.clientId = getClientIdFromGameState(name);
						if (this.clientId == -1) {
							sendInt(INVALID_USERNAME);
						}
					} else {
						// if name already used
						if ((tempId = getClientIdFromGameState(name)) != -1) {
							sendInt(USERNAME_TAKEN);
						}
					}
				} while ((fromSavedGame && this.clientId == -1)
						|| (!fromSavedGame && tempId != -1)); // TODO make this
																// loop nicer
				sendInt(this.clientId);
				System.out.println("sent id to client: " + this.clientId);
				try {
					this.colour = Color.decode((String) inputFromClient
							.readObject());
				} catch (NumberFormatException | ClassNotFoundException
						| IOException e) {
					// TODO disconnect();
					return;
				}
				this.username = name;
			} catch (ClassNotFoundException | IOException e) {
				// TODO disconnect();
			}
			gameLogic.getGameState().addPlayer(clientId, username, colour);//addPlayerToGame(this);
			// Begin listening to this client
			this.clientRunning = true; // ready to be sent to
		}

		/**
		 * Get the id of a given player name from the gamestate
		 *
		 * @param name
		 *            Username of player
		 * @return id if found, -1 if not present
		 */
		private int getClientIdFromGameState(String name) {
			Player[] players = gameLogic.getGameState().getPlayers();
			for (Player p : players) {
				if (p != null) {
					if (p.getName().equals(name))
						return p.getId();
				}
			}
			return -1;
		}

		/**
		 * Send an int to the client
		 *
		 * @param val
		 * @throws IOException
		 */
		private void sendInt(int val) throws IOException {
			outputToClient.reset();
			outputToClient.writeInt(val); // send client their ID
			outputToClient.flush();
		}

		/**
		 * Listen to client and add any NetworkActions to the action queue to be
		 * processed
		 */
		public void run() {
			negotiateConnection();
			while (clientRunning) {
				NetworkAction action = null;
				try {
					action = (NetworkAction) inputFromClient.readObject();
				} catch (IOException | ClassNotFoundException e) {
					// just catch
				}
				if (action != null)
					actionQueue.add(action);
			}
			close(); // when not running, disconnect
		}

		/**
		 * Send a message to the client
		 *
		 * @throws IOException
		 */
		public void writeObject(Object o) {
			try {
				outputToClient.reset();
				outputToClient.writeObject(o);
				outputToClient.flush();
			} catch (IOException e) {
				stopClient();
				close();
			}
		}

		/**
		 * Remove Client from active client list and attempt to close socket
		 */
		private void close() {
			System.out.println("Client " + clientId + "Disconnected");
			actionQueue.add(new RemovePlayer(this.clientId));
			try {
				inputFromClient.close();
				outputToClient.close();
				socket.close();
			} catch (IOException e){}
			removeClientConnection(this);
		}

		/**
		 * Gracefully close client connection
		 */
		private void stopClient() {
			this.clientRunning = false;
		}
	}

}
