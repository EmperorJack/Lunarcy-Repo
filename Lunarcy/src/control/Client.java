package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase;

public class Client {
	// Network related fields
		private String serverAddr;
		private static final int DEFAULT_PORT = 58627;
		private Socket socket;
		private ObjectInputStream inputFromServer;
		private ObjectOutputStream outputToServer;
		
		
		Client(String serverAddr, String name){
			this.serverAddr = serverAddr;
			try {
				socket = new Socket(serverAddr, DEFAULT_PORT);
				outputToServer = new ObjectOutputStream(socket.getOutputStream());
				inputFromServer = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				System.out.println("Couldn't establish connection");
				e.printStackTrace();
			}
			writeObject("Shrek is love");
			
			Scanner sc = new Scanner(System.in);
			outerloop:
			while(true){
				while(sc.hasNext()){
					String input = sc.next();
					if(input.equals("q"))break outerloop;
					switch (input) {
					case "w":
						writeObject(new MoveAction(5,0,1));
						break;
					case "a":
						writeObject(new MoveAction(5,-1,0));
						break;
					case "s":
						writeObject(new MoveAction(5,0,-1));
						break;
					case "d":
						writeObject(new MoveAction(5,1,0));
						break;

					default:
						break;
					}
							
				}
				
			}
			//close socket
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		boolean writeObject(Object o){
			if(o != null){
				try {
					outputToServer.writeObject(o);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		
		public static void main(String[] args){
			new Client("localhost","JP" + Math.random());
		}
}

