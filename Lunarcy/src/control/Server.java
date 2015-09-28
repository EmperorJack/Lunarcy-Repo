package control;

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
		private LinkedBlockingQueue<NetworkAction> messageQueue = new LinkedBlockingQueue<NetworkAction>();

		Server(int maxClients){
			this.maxClients = maxClients;
			try {
				serverSocket = new ServerSocket(PORT);
				listenForClients();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//processActions();
		}

//	private void processActions() {
//			while(true){
//				if(!messageQueue.isEmpty()){
//					System.out.println();
//				}
//			}
//		}

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
        	try{
        		outputToClient.writeInt(clientID);//write(clientID); //send clients ID
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	System.out.println("wrote id to client" + clientID);

        	// Begin listening to this client
        	new Thread(new Runnable(){ public void run(){
        		listenToClient();
            }}).start();
		}

		public void listenToClient(){
    		// While the client is sending messages
    		while (true){
    			NetworkAction action = null;
				try {
					action = (NetworkAction)inputFromClient.readObject();
				} catch (IOException e) {
					// TODO handle disconnected client - this may not be the right way since an IOException could occur for other reasons
					System.err.println("Client " + clientID + "Disconnected");
					e.printStackTrace();
				} catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				if(action != null)messageQueue.add(action);
    			MoveAction a = (MoveAction)action;
    			System.out.println("Server: id "+ a.getPlayerID() + "  " + a.getDirection());
    		}
    	}

    	/**
    	 * Send a message to the client
    	 */
    	public void write(String message)
    	{
    		if (!message.equals(null))
    		{

    			try {
    				outputToClient.writeObject("Yo");
					outputToClient.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
	}

	public static void main(String[] args) {
		new Server(5);
	}
}
