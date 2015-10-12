package network;

import game.GameLogic;
import game.GameState;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;



public class NewServer {
	private ServerSocket serverSocket;
	private static final int DEFAULT_PORT = 58627;

	private LinkedBlockingQueue<NetworkAction> actionQueue = new LinkedBlockingQueue<NetworkAction>();//actions to be processed by the server
	private volatile ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();//connnected clients
	private volatile boolean running = true;//used to stop server
	private int updateFreq; //how many milliseconds between transmitting the gamestate
	private int maxClients;

	private GameLogic gameLogic; //the server version of gamestate
	private volatile boolean fromSavedGame;

	/**
	 * @param maxClients How many clients the server will wait for
	 * @param updateFreq how many milliseconds between transmitting the gamestate
	 * @param gameState game state to initialise into, null if new game to be created
	 * @throws IOException Likely port is already in use
	 */
	public NewServer(int maxClients, int updateFreq, GameState gameState) throws IOException{
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;
		this.gameLogic = new GameLogic(gameState);

		this.serverSocket = new ServerSocket(DEFAULT_PORT);
	}

	private void listenForClients(){
		while(clientList.size() < maxClients){
			Socket socket;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				continue; //try again
			}

			ClientConnection cc;
			try {
				cc = new ClientConnection(socket);
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					//ignore
				}
				continue; //ignore and try again
			}
			cc.clientId = addClientConnection(cc);
			cc.start(); //start the negotiation with the client
		}
		waitForAllClients();
	}
	/**
	 * A method which blocks until all clients have negotiated their ids and are ready
	 */
	private void waitForAllClients() {
		//keep checking up the clientlist until all clients are ready
		for(int i = 0; i< clientList.size();){ //TODO clientList should have synchronized size() method
			if(!getClientConnection(i).isReady()){
				sleep(50); //wait for a bit
				continue;
			}
			i++;
		}
	}

	private void stop(){
		this.running = false;
	}

	private void transmitState(){
		for(ClientConnection cc : clientList){
			cc.sendObject(gameLogic.getGameState());
		}
	}

	private void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			//ignore
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

	private class ClientConnection extends Thread{
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private volatile boolean clientIsRunning = true;
		private boolean ready = false; //true when client has negotiated id and username

		private int clientId = -1;
		private String username;
		private Color colour;

		ClientConnection(Socket socket) throws IOException {
			this.socket = socket;
			inputFromClient = new ObjectInputStream(socket.getInputStream());
			outputToClient = new ObjectOutputStream(socket.getOutputStream());
		}

		public boolean isReady() {
			return this.ready;
		}

		private void setId(int id){
			this.clientId = id;
		}

		private void sendObject(Object o){
			try {
				outputToClient.writeObject(o);
			} catch (IOException e) {
				disconnect(); //connection is broken
			}
		}
		private void negotiateId() {
			// TODO Auto-generated method stub
			String name = "";
//			do {
//				name = readStringFromClient();
//				clientId = addPlayerToGamestate(name);
//				if(clientId == -1){
//					sendIntToClient(-1);
//				}
//			} while (clientId == -1);
			name = readStringFromClient();
			clientId = addPlayerToGamestate(name);
			if(clientId == -1){
				disconnect();
				return;
			}
			username = name;
			sendIntToClient(clientId);
			readColourFromClient();
			ready = true;
		}

		private Color readColourFromClient() {
			Color colour = null;
			try {
				colour = (Color)inputFromClient.readObject();
			} catch (ClassNotFoundException | IOException e) {
				stopClient();
				disconnect();
			}
			return colour;
		}

		private void sendIntToClient(int val) {
			try {
				outputToClient.write(val);
			} catch (IOException e) {
				disconnect();
			}
		}

		private String readStringFromClient() {
			String val = null;
			try {
				val = (String) inputFromClient.readObject();
			} catch (IOException e) {
				disconnect();
			}catch(ClassNotFoundException e){
				//ignore
			}
			return val;
		}

		/**
		 * Add a player to the gamestate
		 * If they are already present based on their username then returns their ID
		 *
		 * @return the id of the player or -1 if the name conflicts or is invalid
		 */
		private int addPlayerToGamestate(String name) {
			// TODO Auto-generated method stub
			if(fromSavedGame){
				return gameLogic.getGameState().getPlayerID(name);
			}else{
				gameLogic.getGameState().addPlayer(clientId, name, colour);
				return clientId;
			}
			//if from saved game
				//if player name already exists
					//return id of player
				//else return -1
			//else
				//add player to game
				//return id
		}



		private void listenToClient(){
			while (clientIsRunning) {
				NetworkAction action = null;
				try {
					action = (NetworkAction) inputFromClient.readObject();
				} catch (IOException | ClassNotFoundException e) {
					//ignore //TODO maybe need to close client if failed here
				}
				if (action != null)
					actionQueue.add(action);
			}
		}

		private void disconnect(){
			//TODO close connections etc
			System.out.println("Client " + clientId + " has disconnected.");
			try {
				inputFromClient.close();
				outputToClient.close();
				socket.getInputStream().close();
				socket.getOutputStream().close();
				socket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close client connection");
			}
			//TODO remove player from gamestate
			removeClientConnection(this);
		}

		public void run(){
			negotiateId();
			listenToClient();
			disconnect();
		}

		private void stopClient(){
			this.clientIsRunning = false;
		}
	}

}


























