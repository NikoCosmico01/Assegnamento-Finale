package Socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Person.Competition;
import Person.Partner;
import Vehicle.Boat;

public class Server {
	private static final int PORT = 9090;
	
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static Connection connection;
	private static Statement statement;
	
	private static ObjectOutputStream os;
	
	public static void initializeConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
		statement = connection.createStatement();
	}
	
	public static void checkLogin(String userName, String passWord) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try { 
			ResultSet rs = statement.executeQuery("SELECT * FROM Partner WHERE UserName =\"" + userName + "\" AND PassWord = \"" + passWord + "\";");
			if (rs.next()) {
				System.out.println("Login Succeded");
				os.writeObject(new Partner(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord));
				os.flush();
				//return new Partner(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord);
			}
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		disconnect();
	}
	
	public static void retrieveBoats(String CF) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Boat WHERE CF_Owner = \""+ CF + "\";");
			//TODO Grandezza
			Integer i = 0;
			while (rs.next()) {
				rs.setFetchSize(++i);
				os.writeObject(new Boat(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Length"),rs.getString("Subscription")));
				os.flush();
			}
			System.out.println(rs.getFetchSize());
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		disconnect();
	}
	
	public static ArrayList<Competition> retriveEvents() throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			ArrayList<Competition> competitionList = new ArrayList<Competition>();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Competition;");
			while (rs.next() == true) {
				Competition C = new Competition(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Cost"));
				competitionList.add(C);
			}
			return competitionList;
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		//cn.close();
		return null;
	}
	
	public static void disconnect() throws SQLException {
		connection.close();
	}
		
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		ServerSocket server = new ServerSocket(PORT);
		
		while (true) {
			System.out.println("[SERVER] Server Is Waiting For A Connection");
			Socket client = server.accept();
			System.out.println("[SERVER] Client Connected");
			
			BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
			os = new ObjectOutputStream(client.getOutputStream());
			//DataOutputStream os = new DataOutputStream(client.getOutputStream());
			
			ClientHandler clientThread = new ClientHandler(client);
			clients.add(clientThread);
			clientThread.run();
			System.out.println(clients.size());
			System.out.println(is.readLine());
		}
	}
	
}
