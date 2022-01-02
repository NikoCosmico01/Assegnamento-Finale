package Socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Event.Participants;
import People.Person;
import Vehicle.Boat;

public class Server {
	private static final int PORT = 9090;
	
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static Connection connection;
	private static Statement statement;
	
	private static ObjectOutputStream os;

	private static ServerSocket server;
	
	public static void initializeConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
		statement = connection.createStatement();
	}
	
	public static void checkLogin(String userName, String passWord) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try { 
			ResultSet rs = statement.executeQuery("SELECT * FROM Person WHERE UserName =\"" + userName + "\" AND PassWord = \"" + passWord + "\";");
			if (rs.next()) {
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord, rs.getInt("Manager")));
				os.flush();
			} else {
				os.writeObject(null);
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		disconnect();
	}
	
	public static void checkUserExistance(String userName) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try { 
			ResultSet rs = statement.executeQuery("SELECT * FROM Person WHERE UserName =\"" + userName + "\";");
			if (rs.next()) {
				os.writeByte(1);
				os.flush();
			} else {
				os.writeByte(0);
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		disconnect();
	}
	
	//TODO ASK
	public static void addPartner (String name, String surname, String address, String cf, Integer id_club, String username, String password) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Partner(Name, Surname, Address, CF, ID_Club, UserName, PassWord) VALUES (?,?,?,?,?,?,?)");
			statement.setString(1, name);
			statement.setString(2, surname);
			statement.setString(3, address);
			statement.setString(4, cf);
			statement.setInt(5, id_club);
			statement.setString(6, username);
			statement.setString(7, password);
			statement.execute();
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
			while (rs.next()) {
				os.writeObject(new Boat(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Length")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		disconnect();
	}
	
	public static void retriveCompetitionsParticipants() throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT C.Name, C.ID, C.Cost, B.Name AS BoatName, B.ID AS BoatID FROM Competition AS C, Boat AS B, Participants AS P WHERE P.ID_Boat = B.ID AND P.ID_Competition = C.ID ;");
			while (rs.next()) {
				os.writeObject(new Participants(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Cost"),rs.getString("BoatName"),rs.getInt("BoatID")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		disconnect();
	}
	
	
	public static void disconnect() throws SQLException {
		connection.close();
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		try {
			server = new ServerSocket(PORT);
			while (true) {
				System.out.println("[SERVER] Server Is Waiting For A Connection");
				Socket client = server.accept();
				System.out.println("[SERVER] Client Connected");

				//new BufferedReader(new InputStreamReader(client.getInputStream()));
				os = new ObjectOutputStream(client.getOutputStream());

				ClientHandler clientThread = new ClientHandler(client);
				clients.add(clientThread);
				clientThread.run();
				System.out.println(clients.size());
			}
		} catch (IOException e) {
			System.err.println("Closing Server");
		} finally {
			try {
				server.close();
			} catch (Exception e) {
				System.err.println("ERROR Closing Server");
			}
		}
	}
}
