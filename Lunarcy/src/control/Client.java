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
		private static final int DEFAULT_PORT = 6667;
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		
		
		Client(String serverAddr, String name){
			this.serverAddr = serverAddr;
			try {
				socket = new Socket(serverAddr, DEFAULT_PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//serverIn = new ObjectOutputStream(socket.getOutputStream());
		}
		
		
		
		public static void main(String[] args){
			new Client("localhost","JP" + Math.random());
		}
}

