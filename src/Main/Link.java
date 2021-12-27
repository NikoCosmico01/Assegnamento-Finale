package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Person.Partner;
import Vehicle.Boat;

public class Link {
	
	private static Connection cn;
	
	public static void initialize() throws SQLException {
		cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
	}
	
	public static Partner checkLogin(String userName, String passWord) throws SQLException {
		Statement stmt = cn.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Partner;");
			while (rs.next() == true) {
				if (userName.equals(rs.getString("UserName")) && passWord.equals(rs.getString("PassWord"))) {
					System.out.println("Login Succeded");
					return new Partner(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord);
				}
			}
			System.out.println("Login Non Avvenuto");
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		//cn.close();
		return null;
	}
	
	public static ArrayList<Boat> retrieveBoats(String CF) throws SQLException {
		Statement stmt = cn.createStatement();
		try {
			ArrayList<Boat> List = new ArrayList<Boat>();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Boat WHERE CF_Owner = \""+ CF + "\"");
			while (rs.next() == true) {
				Boat B = new Boat(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Length"),rs.getString("Subscription"));
				List.add(B);
			}
			return List;
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		//cn.close();
		return null;
	}

}
