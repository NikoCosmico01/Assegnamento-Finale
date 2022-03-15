package Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import Socket.Server;

class loginTest {
	
	@Test
	void testLogin() throws ClassNotFoundException, SQLException, IOException {		
		String resultString = Server.checkEvent(null, 30, 101, "ILNTRZZ");
		assertEquals("OK", resultString);
	}

}
