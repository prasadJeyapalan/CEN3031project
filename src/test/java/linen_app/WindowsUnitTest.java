package linen_app;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import org.junit.jupiter.api.*;

import linen_app.message_classes.*;

/**
 * @author Prasad Jeyapalan, Austin Franklin<p>
 * Integration test from DB to displaying a table of information
 */
class WindowsUnitTest {
	private static final String dbPath = "test-linen.sqlite3";

	//clear DB before use to prevent old data messing this up
	@BeforeAll
	public static void clearDB() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        String[] tables = {"week", "soiled", "outbound", "outbound_history", "orders", "linen", "cart", "customer", "sqlite_sequence"};
        PreparedStatement pstmt = con.prepareStatement("DELETE FROM week");
        for (String table: tables) {
            pstmt = con.prepareStatement(
                "DELETE FROM " + table
            );
            pstmt.execute();
        }
        pstmt.close();
        con.close();
		//add mock data for use
		DBShell db = new DBShell(dbPath);
		db.addCustomer(new CustomerInfo(
			"VAC",
			"Vacasa",
			"40 Hill Ave NW, FL 32548",
			0.36
		));
		db.addCustomer(new CustomerInfo(
			"PBR",
			"Perdido Beach Resort",
			"27200 Perdido Beach Blvd, AL, 36561",
				0.75
		));
		db.addCustomer(new CustomerInfo(
			"ABC",
			"AlphaBet Condos",
			"123 Main Str, NY 12345",
			0.64
		));
		db.addCart(1000, 125);
		db.addCart(1001, 125);
		db.addCart(1002, 125);
		db.createOrder("PBR", 1);
		db.soiledCart(0, db.getLastOrderNumber(), 3125);
		db.addLinenType(new LinenInfo("Queen Set", 2.1));
		db.addLinenType(new LinenInfo("King Set", 2.3));
	}

	@Test
	public static void testOrderWindow() throws SQLException {
		OrderWindow ow = new OrderWindow(dbPath);
		//Test number of rows
		int rows = ow.table.getRowCount();
		assertEquals(4,rows); 
		
		//Test number of columns
		int columns = ow.table.getColumnCount();
		assertEquals(5,columns);
		
		//Testing values in rows and columns
		
		String orderID = (String) ow.table.getValueAt(0, 0);
		assertEquals("1", orderID);
		
		String custID = (String) ow.table.getValueAt(1, 1);
		assertEquals("PBR", custID);
		
		String weight = (String) ow.table.getValueAt(0,3);
		assertEquals("3000", weight);
		
		String active = (String) ow.table.getValueAt(1, 4);
		assertEquals("Active", active);
		
	}
	
	@Test
	public static void testCartsWindow() throws SQLException {
		CartsWindow cw = new CartsWindow(dbPath);
		
		//Testing number of rows
		int rows = cw.table.getRowCount();
		assertEquals(3, rows);
		
		//Testing number of columns
		int columns = cw.table.getColumnCount();
		assertEquals(4, columns);
		
		//Testing values in rows and columns
		
		String cartID = (String) cw.table.getValueAt(0, 0);
		assertEquals("1000", cartID);
		
		String tareWeight = (String) cw.table.getValueAt(1, 2);
		assertEquals("125", tareWeight);
		
		String cState = (String) cw.table.getValueAt(0, 3);
		assertEquals("EMPTY", cState);
		
		String cLocation = (String) cw.table.getValueAt(0,1);
		assertEquals("VAC", cLocation);
		
		
	}
	
	@Test 
	public static void testCustomerWindow() throws SQLException {
		CustomersWindow cw = new CustomersWindow(dbPath) ;
		
		//Testing number of rows
		int rows = cw.table.getRowCount();
		assertEquals(4, rows);
		
		//Testing number of columns
		int columns = cw.table.getColumnCount();
		assertEquals(4, columns);
		
		//testing values in rows and columns
		String custID = (String)cw.table.getValueAt(0, 0);
		assertEquals("VAC", custID);
		
		String custName = (String)cw.table.getValueAt(2, 1);
		assertEquals("AlphaBet Condos", custName);
		
		String address = (String) cw.table.getValueAt(0,2);
		assertEquals("40 Hill Ave NW, FL 32548", address);
		
		String cpp = (String) cw.table.getValueAt(0,3);
		assertEquals("0.36", cpp);
		
	}
	
	@Test
	public static void testLinenWindow() throws SQLException {
		LinenWindow lw = new LinenWindow(dbPath);
		
		//Test number of rows
		int rows = lw.table.getRowCount();
		assertEquals(2, rows);
		
		//Test number of columns
		int columns = lw.table.getColumnCount();
		assertEquals(2,columns);
		
		//testing values in rows and cols
		
		String type = (String)lw.table.getValueAt(1, 0);
		assertEquals("King Set", type);
		
		String weight = (String) lw.table.getValueAt(1, 1);
		assertEquals("2.3", weight);
	}
}
