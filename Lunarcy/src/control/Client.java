package control;

import game.GameState;
import ui.Frame;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



//import com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase;

public class Client {
	// Network related fields
		private String serverAddr;
		private static final int DEFAULT_PORT = 58627;
		private Socket socket;
		private ObjectInputStream inputFromServer;
		private ObjectOutputStream outputToServer;
		private int id;
		private String name;
		private Frame frame;
		private Color colour;




		public Client(String serverAddr, String name, Color colour, boolean hardwareRenderer){
			this.serverAddr = serverAddr;
			this.colour = colour;
			try {
				socket = new Socket(serverAddr, DEFAULT_PORT);
				outputToServer = new ObjectOutputStream(socket.getOutputStream());
				inputFromServer = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				System.out.println("Couldn't establish connection");
				e.printStackTrace();
			}
			writeObject(name);
			//Send hex colour
			String hexColour = String.format("#%02x%02x%02x", this.colour.getRed(), this.colour.getGreen(), this.colour.getBlue());
			System.out.println("hex colour  " + hexColour);
			writeObject(hexColour);
			System.out.println("Name sent to server: " + name);
			readInt();
			//get gamestate setup game
			GameState initialGameState = null;
			while(initialGameState == null){
				initialGameState = getGameState(); //wait for the initial gamestate
			}
			this.frame = new Frame(this,initialGameState, hardwareRenderer);

			System.out.println("Listening for gamestate");
			//listenForGameUpdates(); //listen for gamestates from the server
		}

		private void writeBytes(String str) {
			try {
				outputToServer.writeBytes(str);
			} catch (IOException e) {
				System.out.println("Failed to send: " + str);
			}
		}

		public void listenForGameUpdates() {
			while(true){
				GameState state = getGameState();
				if(state != null){
					frame.getCanvas().setGameState(state);
				}
			}
		}

		private GameState getGameState() {
			GameState state = null;
			try {
				//System.out.println("attempting to listen for game update");
				state = (GameState)inputFromServer.readObject();
				return state;
			} catch (IOException e) {

			} catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			return null;
		}

		public void sendAction(NetworkAction action) {
			writeObject(action);
		}

		private void readInt() {
			System.out.println("trying to read ID");
			try {
				id = inputFromServer.readInt();
				System.out.println("My clientID is: " + id);
			} catch (IOException e) {
				System.err.println("cant read ID");
				e.printStackTrace();
			}
		}

		private boolean writeObject(Object o){
			if(o != null){
				try {
					outputToServer.writeObject(o);
					outputToServer.flush();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		public int getPlayerID() {
			return id;
		}

		public static void main(String[] args){
			new Client("localhost","JP" + (int)(Math.random()*100),Color.RED,true);
		}
}

