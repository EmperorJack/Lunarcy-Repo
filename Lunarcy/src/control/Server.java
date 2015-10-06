package control;

import game.GameLogic;
import game.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.Color;

import storage.Storage;

/**
 * This class handles communication between all clients and the gameLogic over a
 * network
 *
 * @author JTFM
 *
 */
public class Server {
	// Network related fields
	private ServerSocket serverSocket;
	private int maxClients;
	private static final int PORT = 58627;
	private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
	private LinkedBlockingQueue<NetworkAction> actionQueue = new LinkedBlockingQueue<NetworkAction>();
	private GameLogic gameLogic;
	private int updateFreq;
	private boolean running = true;

	private int majorUpdateFreq = 100; // the number of moves before a major update
	private int majorUpdateTick = 0;

	public Server(int maxClients, int updateFreq, GameState gameState) {
		this.maxClients = gameState.getPlayers().length;
		this.updateFreq = updateFreq;
		try {
			serverSocket = new ServerSocket(PORT);
			listenForClients();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameLogic = new GameLogic(gameState);
	}

	public Server(int maxClients, int updateFreq) {
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;
		GameState gameState = new GameState(maxClients);

		try {
			serverSocket = new ServerSocket(PORT);
			listenForClients();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// add players to gamestate
		for (ClientConnection client : clientList) {
			gameState.addPlayer(client.clientID, client.username, Color.RED);
		}
		gameLogic = new GameLogic(gameState);
	}

	/**
	 * Blocking method to gracefully shutdown server
	 */
	public void stop() {
		for (ClientConnection client : clientList) {
			client.stop(); // stop every client connection
			System.out.println("stopped " + client.username);
		}
		System.out.println("Stopping server");
		running = false; // stop main server

		while (clientList.size() > 0) {
			sleep(50);
		}
	}

	/**
	 * Gracefully stop the server and close all sockets/connections
	 */
	public void stopAndSave(String filename) {
		stop();
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
	private void listenForClients() throws IOException {

		System.out.println("Listeneing for clients");
		// wait for all clients to connect
		while (clientList.size() < maxClients) {
			Socket s = serverSocket.accept();
			int clientID = clientList.size();

			ClientConnection client = new ClientConnection(s, clientID);
			clientList.add(client);
			client.listenToClient();
		}

	}

	/**
	 * Run the client processing client actions and sending gamestates
	 */
	public void run() {
		// Make a new thread, as server.run() is non terminating
		new Thread(new Runnable() {
			public void run() {
				transmitState(); // transmit initially
				System.out.println("Server running fully");
				long lastUpdate = System.currentTimeMillis();
				while (running) {
					if (System.currentTimeMillis() > lastUpdate + updateFreq) {
						// game.tick();
						System.out.println("tranmitting state");
						transmitState();
						lastUpdate = System.currentTimeMillis();
					} else{
						processAction();
					}
				}
				System.out.println("Server shutting down");

				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("All clients disconnected \n closing down server");
			}
		}).start();
	}

	/**
	 * Send out the current gamestate to all clients
	 */
	private void transmitState() {
		// TODO This can be made more efficient by serialising once before
		// transmitting
		GameState state = gameLogic.getGameState();
		for (ClientConnection client : clientList) {
			// System.out.println("Transmitting gamestate to: "+
			// client.clientID);
			if (client.writeObject(state)) {
				// System.out.println("Sucessfully sent gamestate");
			}
		}
	}

	/**
	 * Process the next client action from the queue
	 */
	//TODO extend method to apply move to all clients if successful 
	private void processAction() {
		NetworkAction action = actionQueue.poll();
		if (action != null)
			action.applyAction(gameLogic);// interpreter.interpret(action);
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

	private class ClientConnection {
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private int clientID;
		private String username;
		private boolean clientRunning = true;

		ClientConnection(Socket socket, int id) throws IOException {
			this.socket = socket;
			clientID = id;
			outputToClient = new ObjectOutputStream(socket.getOutputStream());
			inputFromClient = new ObjectInputStream(socket.getInputStream());
			// Sleep for a bit
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			// Read the user name sent from the client
			try {
				this.username = (String) inputFromClient.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Server: new Client: " + username + " " + clientID);
			sendID(clientID);
			System.out.println("wrote id to client" + clientID);

			// Begin listening to this client

		}

		private void sendID(int clientID) {
			try {
				outputToClient.reset();
				outputToClient.writeInt(clientID); // send client their ID
				outputToClient.flush();
			} catch (IOException e) {
				System.err.println("Failed to send ID");
				e.printStackTrace();
			}
		}

		/**
		 * Listen to client and add any NetworkActions to the action queue to be
		 * processed
		 */
		public void listenToClient() {
			new Thread(new Runnable() {
				public void run() {
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
					disconnect(); // when not running, disconnect
				}
			}).start();
		}

		/**
		 * Send a message to the client
		 */
		public boolean writeObject(Object o) {
			if (o != null) {
				try {
					outputToClient.reset();
					outputToClient.writeObject(o);
					outputToClient.flush();
				} catch (SocketException e) { //critical, close client connection
					disconnect();
					//e.printStackTrace();
					return false;
				}catch(IOException e){
					return false;
				}
			}
			return true;
		}

		/**
		 * Remove Client from active client list and attempt to close socket
		 */
		private void disconnect() {
			System.out.println("Client " + clientID + "Disconnected");
			try {
				inputFromClient.close();
				outputToClient.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Unable to close client socket descriptor");
				e1.printStackTrace();
			}
			clientList.remove(this);
		}

		/**
		 * Gracefully close client connection
		 */
		private void stop() {
			this.clientRunning = false;
		}
	}

	public static void main(String[] args) {
		new Server(2, 1000);
	}

}
