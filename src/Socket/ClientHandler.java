package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientHandler(Socket clientSocket) throws IOException {
		this.client = clientSocket;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		boolean autoFlush ;
		out = new PrintWriter(client.getOutputStream(), autoFlush = true);
	}

	@Override
	public void run() {
		try {
			while(true) {
				String request = in.readLine();
				if (request.contains("name")) {
					out.println("Ciao Merda");
				} else {
					out.println("Scrivi Nome");
				}
			}
		} catch (IOException e) {
			System.err.println("IOException Failed");
		} finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
