package Socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Main.Main;

public class Client {
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 9090;
	public static ObjectInputStream is;
	public static DataOutputStream os;
	private static Socket socket;
	
	public static void main(String[] args) throws IOException{
		socket = new Socket (SERVER_IP, SERVER_PORT);
		is = new ObjectInputStream(socket.getInputStream());
		os = new DataOutputStream(socket.getOutputStream());
		Main.main(args);
	}
}
