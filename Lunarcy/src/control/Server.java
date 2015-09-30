package control;

import game.GameLogic;
import game.GameState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class handles communication between all clients and the gameLogic over a network
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

	Server(int maxClients,int updateFreq){
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;
		try {
			serverSocket = new ServerSocket(PORT);
			listenForClients();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameState gameState = new GameState(10,10);
		gameLogic = new GameLogic(gameState);
		interpreter = new Interpreter(gameLogic); //TODO initialise interpreter

		run(); //send to all clients
	}


	private void listenForClients() throws IOException{
		System.out.println("Listeneing for clients");
		//wait for all clients to connect
		while(clientList.size() < maxClients){
			Socket s = serverSocket.accept();
			int clientID = clientList.size();
			ClientConnection client = new ClientConnection(s,clientID);
			clientList.add(client);
		}

	}

	void run(){
		transmitState(); //transmit initially
		System.out.println("Server running fully");
		long lastUpdate = System.currentTimeMillis();
		while(clientList.size() > 0){
			if(System.currentTimeMillis()< lastUpdate+updateFreq){
				//gameLogic.tick()
				transmitState();
			}
			else processAction();
		}
		System.out.println("All clients disconnected \n closing down server");
	}

	private void transmitState(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);
		  out.writeObject(gameLogic.getGameState());
		  byte[] serializedGameState = bos.toByteArray();
		  for(ClientConnection client : clientList){
				client.writeGameStateBytes(serializedGameState);
		  }
		}catch(IOException e){
			System.err.println("failed to write gamestate");
		}finally {
		  try {
			    if (out != null) {
			      out.close();
			    }
			  } catch (IOException ex) {
			    // ignore close exception
			  }
			  try {
			    bos.close();
			  } catch (IOException ex) {
			    // ignore close exception
			  }
		}

	}


	void processAction(){
		NetworkAction action = actionQueue.poll();
		if(action != null)interpreter.interpret(action);
	}

	private class ClientConnection{
		Socket socket;
		ObjectInputStream inputFromClient;
        ObjectOutputStream outputToClient;
		int clientID;
		String username;

		ClientConnection(Socket socket, int id) throws IOException {
			this.socket = socket;
			clientID = id;
			inputFromClient = new ObjectInputStream(socket.getInputStream());
        	outputToClient = new ObjectOutputStream(socket.getOutputStream());
        	// Sleep for a bit
			try{
			    Thread.sleep(500);
			} catch(InterruptedException ex){Thread.currentThread().interrupt();}

			// Read the user name sent from the client
        	try{
				this.username = (String) inputFromClient.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        	System.out.println("Server: new Client: " + username + " "+ clientID);
        	sendID(clientID);

        	System.out.println("wrote id to client" + clientID);

        	// Begin listening to this client
        	new Thread(new Runnable(){ public void run(){
        		listenToClient();
            }}).start();
		}

		public void writeGameStateBytes(byte[] serializedGameState) {
			try {
				outputToClient.write(serializedGameState);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendID(int clientID2) {
			try{
        		outputToClient.writeInt(clientID);//write(clientID); //send clients ID
        		outputToClient.flush();
        	}catch(IOException e){
        		System.err.println("Failed to send ID");
        		e.printStackTrace();
        	}
		}
		/**
		 * Listen to client and add any NetworkActions to the action queue
		 * If an io error occurs, remove client
		 */
		public void listenToClient(){
    		// While the client is sending messages
    		while (true){
    			NetworkAction action = null;
				try {
					System.out.println("attempting to listen to client");
					action = (NetworkAction)inputFromClient.readObject();
				} catch (IOException e) {
					// TODO handle disconnected client - this may not be the right way since an IOException could occur for other reasons
					//e.printStackTrace();
					//removeClient();
					//return;
					//
				} catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				if(action != null)actionQueue.add(action);
    		}
    	}

		/**
    	 * Send a message to the client
    	 */
		public boolean writeObject(Object o){
			if(o != null){
				try {
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
		private void removeClient() {
    		System.out.println("Client " + clientID + "Disconnected");
			try {
				socket.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Unable to close client socket descriptor");
				e1.printStackTrace();
			}
			clientList.remove(this);
		}
	}

	public static void main(String[] args) {
		new Server(2,50);
	}
}
