package control;

import game.GameLogic;
import game.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
	// Network related fields
		private ServerSocket serverSocket;
		private int maxClients;
		private static final int PORT = 58627;
		private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
		private LinkedBlockingQueue<NetworkAction> actionQueue = new LinkedBlockingQueue<NetworkAction>();
		private Interpreter interpreter;
		
		Server(int maxClients){
			this.maxClients = maxClients;
			try {
				serverSocket = new ServerSocket(PORT);
				listenForClients();
			} catch (IOException e) {
				e.printStackTrace();
			}
			processActions();
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
		interpreter = new Interpreter(new GameLogic(new GameState(10,10))); //TODO testing
		processActions();
	}

	void processActions(){
		System.out.println("Processing actions");
		while(clientList.size() > 0){
			NetworkAction action = actionQueue.poll();
			if(action != null)interpreter.interpret(action);
		}
		System.out.println("All clients disconnected \n closing down server");
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
					action = (NetworkAction)inputFromClient.readObject();
				} catch (IOException e) {
					// TODO handle disconnected client - this may not be the right way since an IOException could occur for other reasons
					removeClient();
					//e.printStackTrace();
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
		new Server(2);
	}
}
