package Socket;

import static Socket.ClientHandler.os;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.PooledConnection;

import Event.Participants;
import People.Person;
import Vehicle.Boat;

public class Server {
	private static final int PORT = 9090;

	private static Connection connection;
	private static Statement statement;

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
				System.out.println("Esegui Quer");
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord, rs.getInt("Manager")));
				os.flush();
				System.out.println("FLusho");
			} else {
				os.writeObject(null);
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("checkLogin Error: " + e.getMessage());
		}
		System.out.println("Disconn");
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
			System.out.println("checkUserExistance Error: " + e.getMessage());
		}
		disconnect();
	}

	//TODO ASK
	public static void addPartner (String name, String surname, String address, String cf, Integer id_club, String username, String password) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Peroson(Name, Surname, Address, CF, ID_Club, UserName, PassWord) VALUES (?,?,?,?,?,?,?)");
			statement.setString(1, name);
			statement.setString(2, surname);
			statement.setString(3, address);
			statement.setString(4, cf);
			statement.setInt(5, id_club);
			statement.setString(6, username);
			statement.setString(7, password);
			statement.execute();
		} catch (SQLException e) {
			System.out.println("addPartner Error: " + e.getMessage());
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
				System.out.println(new Boat(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Length")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrieveBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void retriveCompetitions() throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmtI = connection.createStatement();
		ResultSet rsI = null;
		try {
			rsI = stmtI.executeQuery("Select * FROM Competition");
		} catch (SQLException e) {
			System.out.println("retrieve-ALL-Competitions Error: " + e.getMessage());
		}
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try {
			ResultSet rs = stmt.executeQuery("SELECT C.Name, C.ID, C.Cost, B.Name AS BoatName, B.ID AS BoatID FROM Competition AS C, Boat AS B, Participants AS P WHERE P.ID_Boat = B.ID AND P.ID_Competition = C.ID ;");
			while (rsI.next()) {
				Integer controllerInteger = 0;
				rs.beforeFirst();
				while (rs.next()) {
					if (rs.getInt("ID") == rsI.getInt("ID")) {
						controllerInteger = 1;
						os.writeObject(new Participants(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Cost"),rs.getString("BoatName"),rs.getInt("BoatID")));
						os.flush();
					}
				} 
				if (controllerInteger == 0) {
					os.writeObject(new Participants(rsI.getString("Name"),rsI.getInt("ID"),rsI.getDouble("Cost"),null, null));
					os.flush();
				}
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retriveCompetitions Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void subscriptEvent(Integer eventID, Integer boatID) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		Statement statementI = connection.createStatement();
		try {
			ResultSet rs = statementI.executeQuery("SELECT * FROM Participants");
			while (rs.next() == true) {
				if (eventID.equals(rs.getInt("ID_Competition"))) {
					os.writeBytes("CSE\n");
					os.flush();
					disconnect();
					return;
				} if (boatID.equals(rs.getInt("ID_Boat"))) {
					os.writeBytes("BSE\n");
					os.flush();
					disconnect();
					return;
				}
			}
		} catch (Exception e) {
			System.err.println("sendSubscription Check-Existance Error: " + e.getMessage());
		}
		Statement statement = connection.createStatement();
		try {
			statement.executeUpdate("INSERT INTO Participants VALUES (" + boatID + ", " + eventID + ");");
			os.writeBytes("OK\n");
			os.flush();
		} catch (Exception e) {
			System.err.println("Insert sendSubscription Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void checkBoat(String CF, String boatName) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Boat WHERE CF_Owner = \"" + CF + "\" AND Name = \"" + boatName + "\";");
			if (rs.next()) {
				os.writeBytes("KO\n");
				os.flush();
			}
			os.writeBytes("OK\n");
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrieveBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void removeBoat(Integer ID, Integer Checker) throws ClassNotFoundException, IOException, SQLException, InterruptedException  {
		initializeConnection();
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Participants WHERE ID_Boat = " + ID + ";");
			if (rs.next() && Checker == 0) {
				os.writeBytes("RQDL\n");
				os.flush();
				disconnect();
				return;
			} if (Checker == 1) {
				PreparedStatement stmtI = connection.prepareStatement("DELETE FROM Participants WHERE ID_Boat = " + ID + ";");
				stmtI.executeUpdate();
			}
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM Boat WHERE ID = \""+ ID + "\"");
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("removeBoats Error: " + e.getMessage());
		}
		disconnect();
	}
	
	public static void retrievePerson(String CF) throws ClassNotFoundException, IOException, SQLException, InterruptedException  {
		initializeConnection();
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Person WHERE CF = \"" + CF + "\";");
			if (rs.next()) {
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), rs.getString("UserName"), rs.getString("PassWord"), rs.getInt("Manager")));
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("removeBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void disconnect() throws SQLException {
		connection.close();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		LinkedList<ClientHandler> clients = new LinkedList<>();
		//LoggedUsers = new LinkedList<>();
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			while (true) {
				System.out.println("[SERVER] Server Is Waiting For A Connection");
				Socket client = server.accept();
				System.out.println("[SERVER] Client Connected");
				
				ClientHandler clientThread = new ClientHandler(client);
				clients.add(clientThread);
				new Thread(clientThread).start();
				System.out.println(clients.size());
			}
		} catch (IOException e) {
			System.out.println("[SERVER] Client Disconnected");
		} finally {
			try {
				server.close();
			} catch (Exception e) {
				System.err.println("[SERVER] Closing ERROR " + e.getMessage());
			}
		}
	}
}
