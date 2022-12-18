package linen_app;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import javax.swing.JPanel;

public class ClientWindowTester {
	private final String dbPath = "test-linen.sqlite3";
	
	@Test
	public void testSwapCentralPanel() throws SQLException {
		ClientWindow clientWindow = new ClientWindow(dbPath);
		JPanel testPanel = new JPanel();

		clientWindow.setContentViewPanel(testPanel);
		assertEquals(testPanel,clientWindow.getCurrentView());
		clientWindow.close();
	}
	
	
	@Test
	public void testGetMainWindow() throws SQLException {
		ClientWindow clientWindow = new ClientWindow(dbPath);
		assertEquals(clientWindow, ClientWindow.getMainWindow());
		clientWindow.close();
	}
	
	@Test
	public void testClose() throws SQLException {
		ClientWindow clientWindow = new ClientWindow(dbPath);
		clientWindow.close();
		assertEquals(null, ClientWindow.getMainWindow());
	}
}
