package control;

import game.GameLogic;
import game.GameState;
import game.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
	private Interpreter interpreter;
	private GameLogic gameLogic;
	private int updateFreq;
	private boolean stopServer = false;


	public Server(int maxClients, int updateFreq,GameState gameState) {
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;

		try {
			serverSocket = new ServerSocket(PORT);
			listenForClients();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// add players to gamestate
		for (ClientConnection client : clientList) {
			gameState.addPlayer(client.getClientID(), client.getName(),Color.RED);
		}
		gameLogic = new GameLogic(gameState);
		//interpreter = new Interpreter(gameLogic); // TODO initialise interpreter
		sleep(500);
		transmitState();
		run(); // send to all clients
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
			gameState.addPlayer(client.getClientID(), client.getName(),
					Color.RED);
		}
		gameLogic = new GameLogic(gameState);
		//interpreter = new Interpreter(gameLogic); // TODO initialise interpreter
		sleep(500);
		transmitState();
		run(); // send to all clients
	}

	/**
	 * Nicely shutdown server and return current gamestate
	 */
	public GameState stop() {
		stopServer = true;
		return gameLogic.getGameState();
	}

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
		}

	}

	/**
	 * Run the client processing client actions and sending gamestates
	 */
	private void run() {
		transmitState(); // transmit initially
		System.out.println("Server running fully");
		long lastUpdate = System.currentTimeMillis();
		while (clientList.size() > 0 && !stopServer) {
			if (System.currentTimeMillis() > lastUpdate + updateFreq) {
				// gameLogic.tick()
				transmitState();
				lastUpdate = System.currentTimeMillis();
			} else processAction();
		}
		for (ClientConnection client : clientList) {
			client.stop();
		}
		saveGamestate();
		System.out.println("All clients disconnected \n closing down server");
	}

	/**
	 * Send out the current gamestate to all clients
	 */
	private void transmitState() {
		// TODO This can be made more efficient by serialising once before
		// transmitting
		GameState state = gameLogic.getGameState();
		for (ClientConnection client : clientList) {
			//System.out.println("Transmitting gamestate to: "+ client.clientID);
			if(client.writeObject(state)){
				System.out.println("Sucessfully sent gamestate");
			}
		}
	}

	/**
	 * Process the next client action from the queue
	 */
	private void processAction() {
		NetworkAction action = actionQueue.poll();
		if (action != null)
			action.applyAction(gameLogic);// interpreter.interpret(action);
	}

	private class ClientConnection {
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private int clientID;
		private String username;
		private boolean stop = false;

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
			System.out.println("Server: new Client: " + username + " "
					+ clientID);
			sendID(clientID);

			System.out.println("wrote id to client" + clientID);

			// Begin listening to this client
			new Thread(new Runnable() {
				public void run() {
					listenToClient();
				}
			}).start();
		}

		public String getName() {
			return this.username;
		}

		public int getClientID() {
			return this.clientID;
		}

		private void sendID(int clientID2) {
			try {
				outputToClient.reset();
				outputToClient.writeInt(clientID);// write(clientID); //send
													// clients ID
				outputToClient.flush();
			} catch (IOException e) {
				System.err.println("Failed to send ID");
				e.printStackTrace();
			}
		}

		/**
		 * Listen to client and add any NetworkActions to the action queue If an
		 * io error occurs, remove client
		 */
		public void listenToClient() {
			// While the client is sending messages
			while (!stop) {
				NetworkAction action = null;
				try {
					// System.out.println("attempting to listen to client");
					action = (NetworkAction) inputFromClient.readObject();
				} catch (IOException | ClassNotFoundException e) {

				}
				if (action != null)
					actionQueue.add(action);
			}
			disconnect();
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
				} catch (IOException e) {
					e.printStackTrace();
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

		private void stop(){
			this.stop  = true;
		}
	}

	/**
	 * Saves the current gamestate to disc
	 */
	public void saveGamestate(){
		Storage.saveState(gameLogic.getGameState());
	}

	/**
	 * Loads the previous gamestate from disc
	 */
	public void loadGamestate(){
		gameLogic = new GameLogic(Storage.loadState());
	}

	public static void main(String[] args) {
		new Server(2, 1000);
	}

}
