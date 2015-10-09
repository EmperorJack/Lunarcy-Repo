package control;

import game.GameLogic;
import game.GameState;
import game.Player;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import control.Server.ClientConnection;

public class NewServer {
    	// Network related fields
 	private ServerSocket serverSocket;
 	private int maxClients;
 	private static final int PORT = 58627;
 	private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
 	private LinkedBlockingQueue<NetworkAction> actionQueue = new LinkedBlockingQueue<NetworkAction>();
 	private GameLogic gameLogic;
 	private int updateFreq;
 	private boolean running = true;
 	private boolean fromSavedGame;
 	
 	public NewServer(int maxClients, int updateFreq, GameState gameState) throws SocketException {
		this.maxClients = gameState.getPlayers().length;
		this.updateFreq = updateFreq;
		gameLogic = new GameLogic(gameState);
		serverSocket = new ServerSocket(PORT); //throws SocketException
		listenForClients();
	}
 	/**
 	 * Adds a given client to the gamestate
 	 * @return if adding to a new game returns the existing id based on username, -1 if invalid
 	 *  or their id if they are added to a new game
 	 */
 	synchronized private int addToGameState(ClientConncection client){
 	    //if the mode is saved game
 	    if(fromSavedGame){
 		Player[] players = gameLogic.getGameState().getPlayers();
 		for (int i = 0; i < players.length; i++) {
		    Player p = players[i];
		    if(p != null){
			if(p.getName().equals(client.getName())){
			    return p.getId();
			}
		    }
		}
 		return -1;
 	    }else{
 		gameLogic.getGameState().addPlayer(playerID, client.getName(), client.getColour());
 	    }
 	}
 	
 	private void listenForClients() {
	    
		System.out.println("Listeneing for clients");
		// wait for all clients to connect
		while (clientList.size() < maxClients) {
			Socket s;
			try {
			    s = serverSocket.accept();
			} catch (IOException e) {
			   continue; //try again
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
 	
 	private class ClientConnection {
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private int clientID;
		private String username;
		private Color colour;
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
				//TODO negotiate username
				this.colour = Color.decode((String)inputFromClient.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Server: new Client: " + username + " " + clientID + " colour: "+ this.colour.toString());
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
		public boolean writeObject(Object o,boolean reset) {
			if (o != null) {
				try {
					if(reset) outputToClient.reset();
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
}
