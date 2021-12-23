package Main;

import java.sql.*;

public class DBConnection {

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}
		//Creo Connessione Al DB
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");

		try {
			ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM Partner;");
			while (rs.next() == true) {
				System.out.println(rs.getString("Name"));
			}
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		cn.close(); //Chiudo DB
	}
}

