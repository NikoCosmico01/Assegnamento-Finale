package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

	private Socket client;
	private PrintWriter out;
	private static BufferedReader is;
	
	public ClientHandler(Socket clientSocket) throws IOException {
		this.client = clientSocket;
		is = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream());
	}

	@Override
	public void run() {
		try {
			while(true) {
				String[] comando = is.readLine().split("#");
				if (comando[0].equals("connect")) {
					Server.checkLogin(comando[1], comando[2]);
				} if (comando[0].equals("retrieveBoats")) {
					Server.retrieveBoats(comando[1]);
				}
			}
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.err.println("IOException Failed");
		} finally {
			out.close();
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
