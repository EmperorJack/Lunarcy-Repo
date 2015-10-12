//package network;
//
//import game.GameLogic;
//import game.GameState;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.ArrayList;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.awt.Color;
//
//import storage.Storage;
//
///**
// * This class handles communication between all clients and the gameLogic over a
// * network
// *
// * @author JTFM
// *
// */
//public class Server {
//	// Network related fields
//	private ServerSocket serverSocket;
//	private int maxClients;
//	private static final int PORT = 58627;
//	private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
//	private LinkedBlockingQueue<NetworkAction> actionQueue = new LinkedBlockingQueue<NetworkAction>();
//	private GameLogic gameLogic;
//	private int updateFreq;
//	private boolean running = true;
//	private boolean fromSavedGame;
//
//
//	public Server(int maxClients, int updateFreq, GameState gameState) {
//		this.maxClients = gameState.getPlayers().length;
//		this.updateFreq = updateFreq;
//		gameLogic = new GameLogic(gameState);
//		try {
//			serverSocket = new ServerSocket(PORT);
//			listenForClients();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public Server(int maxClients, int updateFreq, String map) {
//		this.maxClients = maxClients;
//		this.updateFreq = updateFreq;
//		GameState gameState = new GameState(maxClients,map);
//		try {
//			serverSocket = new ServerSocket(PORT);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		listenForClients();
//		// add players to gamestate
//		for (ClientConnection client : clientList) {
//			gameState.addPlayer(client.clientID, client.username, client.colour);
//		}
//		gameLogic = new GameLogic(gameState);
//	}
//
//	/**
//	 * Blocking method to gracefully shutdown server
//	 */
//	public void stop() {
//		for (ClientConnection client : clientList) {
//			client.stop(); // stop every client connection
//			System.out.println("stopped " + client.username);
//		}
//		System.out.println("Stopping server");
//		running = false; // stop main server
//
//		while (clientList.size() > 0) {
//			sleep(50);
//		}
//	}
//
//	/**
//	 * Gracefully stop the server and close all sockets/connections
//	 */
//	public void stopAndSave(String filename) {
//		stop();
//		Storage.saveState(gameLogic.getGameState(), filename);
//	}
//
//	/**
//	 * Sleep the server for the given
//	 *
//	 * @param time
//	 */
//	private void sleep(int time) {
//		try {
//			Thread.sleep(time);
//		} catch (InterruptedException e) {
//		}
//	}
//
//	/**
//	 *
//	 * @throws IOException
//	 */
//	private void listenForClients() {
//
//		System.out.println("Listeneing for clients");
//		// wait for all clients to connect
//		while (clientList.size() < maxClients) {
//			Socket s;
//			try {
//			    s = serverSocket.accept();
//			} catch (IOException e) {
//			   continue;
//			}
//			int clientID = clientList.size();
//
//			ClientConnection client;
//			try {
//			    client = new ClientConnection(s, clientID);
//			} catch (IOException e) {
//			    try {
//				s.close();
//			    } catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			    }
//			    continue;
//			}
//			clientList.add(client);
//			client.listenToClient();
//		}
//
//	}
//
//	/**
//	 * Run the client processing client actions and sending gamestates
//	 */
//	public void run() {
//		// Make a new thread, as server.run() is non terminating
//		new Thread(new Runnable() {
//			public void run() {
//				transmitState(); // transmit initially
//				System.out.println("Server running fully");
//				long lastUpdate = System.currentTimeMillis();
//				while (running) {
//					if (System.currentTimeMillis() > lastUpdate + updateFreq) {
//						gameLogic.tickGameState();
//						//System.out.println("tranmitting state");
//						transmitState();
//						lastUpdate = System.currentTimeMillis();
//					} else{
//						processAction();
//					}
//				}
//				System.out.println("Server shutting down");
//
//				try {
//					serverSocket.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println("All clients disconnected \n closing down server");
//			}
//		}).start();
//	}
//
//	/**
//	 * Send out the current gamestate to all clients
//	 */
//	private void transmitState() {
//		// TODO This can be made more efficient by serialising once before
//		// transmitting
//		GameState state = gameLogic.getGameState();
//		for(int i = 0 ; i < clientList.size(); i++){
//			ClientConnection client = clientList.get(i);
//			boolean isNewObject = i == 0 ? true : false; //only reset output cache on first send
//			isNewObject = true; // only transmits new state to one player if this is false...
//			client.writeObject(state,isNewObject);
//		}
//	}
//
//	/**
//	 * Process the next client action from the queue
//	 */
//	//TODO extend method to apply move to all clients if successful
//	private void processAction() {
//		NetworkAction action = actionQueue.poll();
//		if (action != null){
//			action.applyAction(gameLogic);// interpreter.interpret(action);
//		}
//	}
//
//	/**
//	 * Saves the current gamestate to disc
//	 */
//	public void saveGamestate(String filename) {
//		Storage.saveState(gameLogic.getGameState(), filename);
//	}
//
//	public int getMaxClients() {
//		return maxClients;
//	}
//
//	private class ClientConnection {
//		private Socket socket;
//		private ObjectInputStream inputFromClient;
//		private ObjectOutputStream outputToClient;
//		private int clientID;
//		private String username;
//		private Color colour;
//		private boolean clientRunning = true;
//
//		ClientConnection(Socket socket, int id) throws IOException {
//			this.socket = socket;
//			clientID = id;
//			outputToClient = new ObjectOutputStream(socket.getOutputStream());
//			inputFromClient = new ObjectInputStream(socket.getInputStream());
//			// Sleep for a bit
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException ex) {
//				Thread.currentThread().interrupt();
//			}
//
//			// Read the user name sent from the client
//			try {
//				this.username = (String) inputFromClient.readObject();
//				sendID(clientID);
//				//TODO negotiate username
//				this.colour = Color.decode((String)inputFromClient.readObject());
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			System.out.println("Server: new Client: " + username + " " + clientID + " colour: "+ this.colour.toString());
//
//			System.out.println("wrote id to client" + clientID);
//
//			// Begin listening to this client
//		}
//
//		private void sendID(int clientID) {
//			try {
//				outputToClient.reset();
//				outputToClient.writeInt(clientID); // send client their ID
//				outputToClient.flush();
//			} catch (IOException e) {
//				System.err.println("Failed to send ID");
//				e.printStackTrace();
//			}
//		}
//
//		/**
//		 * Listen to client and add any NetworkActions to the action queue to be
//		 * processed
//		 */
//		public void listenToClient() {
//			new Thread(new Runnable() {
//				public void run() {
//					while (clientRunning) {
//						NetworkAction action = null;
//						try {
//							action = (NetworkAction) inputFromClient.readObject();
//						} catch (IOException | ClassNotFoundException e) {
//							// just catch
//						}
//						if (action != null)
//							actionQueue.add(action);
//					}
//					disconnect(); // when not running, disconnect
//				}
//			}).start();
//		}
//
//		/**
//		 * Send a message to the client
//		 */
//		public boolean writeObject(Object o,boolean reset) {
//			if (o != null) {
//				try {
//					if(reset) outputToClient.reset();
//					outputToClient.writeObject(o);
//					outputToClient.flush();
//				} catch (SocketException e) { //critical, close client connection
//					disconnect();
//					//e.printStackTrace();
//					return false;
//				}catch(IOException e){
//					return false;
//				}
//			}
//			return true;
//		}
//
//		/**
//		 * Remove Client from active client list and attempt to close socket
//		 */
//		private void disconnect() {
//			System.out.println("Client " + clientID + "Disconnected");
//			try {
//				inputFromClient.close();
//				outputToClient.close();
//				socket.close();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				System.out.println("Unable to close client socket descriptor");
//				e1.printStackTrace();
//			}
//			clientList.remove(this);
//		}
//
//		/**
//		 * Gracefully close client connection
//		 */
//		private void stop() {
//			this.clientRunning = false;
//		}
//	}
//
//	public static void main(String[] args) {
//		Server serv = new Server(1, 1000,"assets/maps/map.xml");
//		serv.run();
//		serv.stop();
//	}
//
//}

//------------------------------------------------------

package network;

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

//import network.NewServer.ClientConnection;
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
	private boolean fromSavedGame = false;

	public Server(int maxClients, int updateFreq, GameState gameState) {
		this.maxClients = gameState.getPlayers().length;
		this.updateFreq = updateFreq;
		gameLogic = new GameLogic(gameState);
		try {
			serverSocket = new ServerSocket(PORT);
			listenForClients();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server(int maxClients, int updateFreq, String map) {
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;
		GameState gameState = new GameState(maxClients, map);
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameLogic = new GameLogic(gameState);
		listenForClients();
		// add players to gamestate
//		for (ClientConnection client : clientList) {
//			gameState
//					.addPlayer(client.clientID, client.username, client.colour);
//		}

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
	private void listenForClients() {

		System.out.println("Listeneing for clients");
		// wait for all clients to connect
		while (clientList.size() < maxClients) {
			Socket s;
			try {
				s = serverSocket.accept();
			} catch (IOException e) {
				continue;
			}
			int clientID = clientList.size();

			ClientConnection client;
			try {
				client = new ClientConnection(s, clientID);
			} catch (IOException e) {
				try {
					s.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
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
				waitForClients(); //wait for all clients to be ready
				transmitState(); // transmit initially
				System.out.println("Server running fully");
				long lastUpdate = System.currentTimeMillis();
				while (running) {
					if (System.currentTimeMillis() > lastUpdate + updateFreq) {
						gameLogic.tickGameState();
						// System.out.println("tranmitting state");
						transmitState();
						lastUpdate = System.currentTimeMillis();
					} else {
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

			private void waitForClients() {
				boolean notReady = true;
				while(!allClientsReady()){
					sleep(50);
				}
			}

			private boolean allClientsReady(){
				ArrayList<ClientConnection> clients = getConnections();
				for(ClientConnection c : clients){
					if(!c.isRunning())return false;
				}
				return true;
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
		for (int i = 0; i < clientList.size(); i++) {
			ClientConnection client = clientList.get(i);
			boolean isNewObject = i == 0 ? true : false; // only reset output
															// cache on first
															// send
			isNewObject = true; // only transmits new state to one player if
								// this is false...
			client.writeObject(state, isNewObject);
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

	synchronized private int addPlayerToGame(ClientConnection c){
		if(fromSavedGame){
			return gameLogic.getGameState().getPlayerID(c.username); //TODO
		}else{
			gameLogic.getGameState().addPlayer(c.clientId, c.username, c.colour);
			return c.clientId;
		}
	}

	//methods for working with clientlist

	synchronized private int addClientConnection(ClientConnection cc){
		this.clientList.add(cc);
		return this.clientList.indexOf(cc);
	}

	synchronized private boolean removeClientConnection(ClientConnection cc){
		return this.clientList.remove(cc);
	}


	synchronized private ClientConnection getClientConnection(int index){
		return this.clientList.get(index);
	}

	synchronized private ArrayList<ClientConnection> getConnections(){
		return this.clientList;
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
		private int clientId;
		private String username;
		private Color colour;
		private boolean clientRunning = false;

		ClientConnection(Socket socket, int clientId) throws IOException {
			this.socket = socket;
			this.clientId = clientId;
			outputToClient = new ObjectOutputStream(socket.getOutputStream());
			inputFromClient = new ObjectInputStream(socket.getInputStream());
			//negotiateConnection();
		}

		public boolean isRunning() {
			return clientRunning;
		}

		private void negotiateConnection() {
			// Sleep for a bit
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException ex) {
//				Thread.currentThread().interrupt();// TODO what does this do??
//			}

//			name = readStringFromClient();
//			clientId = addPlayerToGamestate(name);
//			if(clientId == -1){
//				disconnect();
//				return;
//			}
//			username = name;
//			sendIntToClient(clientId);
//			readColourFromClient();

			// Read the user name sent from the client
			//TODO negotiate username
			try {
				this.username = (String) inputFromClient.readObject();
				sendInt(clientId);
				// TODO negotiate username
				this.colour = Color.decode((String) inputFromClient.readObject());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			System.out.println("Server: new Client: " + username + " "
					+ clientId + " colour: " + this.colour.toString());

			System.out.println("wrote id to client" + clientId);
			addPlayerToGame(this);
			// Begin listening to this client
			this.clientRunning = true; //ready to be sent to
		}

		private void sendInt(int val) {
			try {
				outputToClient.reset();
				outputToClient.writeInt(val); // send client their ID
				outputToClient.flush();
			} catch (IOException e) {
				disconnect();
			}
		}

		/**
		 * Listen to client and add any NetworkActions to the action queue to be
		 * processed
		 */
		public void listenToClient() {
			new Thread(new Runnable() {
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
					disconnect(); // when not running, disconnect
				}
			}).start();
		}

		/**
		 * Send a message to the client
		 */
		public boolean writeObject(Object o, boolean reset) {
			if (o != null) {
				try {
					if (reset)
						outputToClient.reset();
					outputToClient.writeObject(o);
					outputToClient.flush();
				} catch (SocketException e) { // critical, close client
												// connection
					disconnect();
					// e.printStackTrace();
					return false;
				} catch (IOException e) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Remove Client from active client list and attempt to close socket
		 */
		private void disconnect() {
			System.out.println("Client " + clientId + "Disconnected");
			try {
				inputFromClient.close();
				outputToClient.close();
				socket.getInputStream().close();
				socket.getOutputStream().close();
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
		Server serv = new Server(1, 1000, "assets/maps/map.xml");
		serv.run();
		serv.stop();
	}

}
