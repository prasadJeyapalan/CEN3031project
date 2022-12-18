package linen_app;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.Vector;
import linen_app.message_classes.*;

/**
 * @author Austin Franklin<p>
 * Wide suite of tests for testing DBShell.java
 */
public class DBShellTests {
    public static String DBPath = "test_linen.sqlite3";
    public static DBShell dbShell;
    public static CustomerInfo vlp = new CustomerInfo("VLP", "Vacasa", "40 Hill Ave WN, FL 32548  ", 0.0);

    @BeforeAll
    public static void setup() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
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
        dbShell = new DBShell(DBPath);
    }

    @Test
    public void customerCreation() throws SQLException {
        //delete previous entry
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
        PreparedStatement pstmt = con.prepareStatement(
            "DELETE FROM customer WHERE customer_id = 'VLP'"
        );
        pstmt.execute();
        pstmt.close();
        con.close();
        assertNull(dbShell.getCustomerInfo("VLP"));
        dbShell.addCustomer(vlp);
        assertTrue(dbShell.isCustomerActive("VLP"));
        assertNull(dbShell.getCustomerInfo("ABCD"));
    }

    @Test
    public void customerActivity() throws SQLException {
        CustomerInfo emg = new CustomerInfo("EMG", "Emerald Grande", "Half of destin", 1.0);
        dbShell.addCustomer(emg);
        assertTrue(dbShell.isCustomerActive("EMG"));
        dbShell.makeCustomerInactive("EMG");
        assertFalse(dbShell.isCustomerActive("EMG"));
    }

    @Test
    public void addCart() throws SQLException {
        dbShell.addCart(1, 135);
        CartInfo info = dbShell.getCartInfo(1);
        assertEquals(info.cartID(), 1);
        assertEquals(info.location(), "VLP");
        assertEquals(info.tareWeight(), 135);
        assertEquals(info.state(), CartState.EMPTY);

        dbShell.addCart(2, 100);
        Vector<CartInfo> infoVec = dbShell.getAllCartInfo();
        CartInfo info2 = infoVec.get(1);
        assertEquals(info2.cartID(), 2);
        assertEquals(info2.location(), "VLP");
        assertEquals(info2.tareWeight(), 100);
        assertEquals(info2.state(), CartState.EMPTY);
    }

    @Test
    public void addLinen() throws SQLException {
        int beforeSize = dbShell.getAllLinenInfo().size();
        dbShell.addLinenType(
            new LinenInfo("Pillow", 1.2)
        );
        LinenInfo info = dbShell.getAllLinenInfo().get(beforeSize);
        assertEquals(info.linenType(), "Pillow");
        assertEquals(info.weight(), 1.2);
    }

    @Test
    public void createOrder() throws SQLException {
        if(dbShell.getCustomerInfo("VLP") == null)
            dbShell.addCustomer(vlp);
        dbShell.createOrder("VLP", 1010000); //jan 1, 2000
        dbShell.finishOrder(dbShell.getLastOrderNumber());
        int orderID = dbShell.createOrder("VLP", 1020000); //jan 2, 2000
        dbShell.addCart(3, 100);
        dbShell.soiledCart(3, orderID, 300);
        dbShell.finishOrder(orderID);
        OrderInfo orderInfo = dbShell.getAllOrders().lastElement();
        assertEquals(orderInfo.customerID(), "VLP");
        assertEquals(orderInfo.dateShippedIn(), 1020000);
        assertEquals(orderInfo.orderID(), orderID);
        assertEquals(orderInfo.weight(), 200);
    }

    @Test 
    public void cartStateSoiled() throws SQLException{
        if(dbShell.getCustomerInfo("VLP") == null)
            dbShell.addCustomer(vlp);
        int orderID = dbShell.createOrder("VLP", 1030000); //jan 2, 2000
        dbShell.addCart(4, 100);
        dbShell.soiledCart(4, orderID, 300);
        dbShell.finishOrder(orderID);
        assertEquals(dbShell.getCartState(4), CartState.SOILED);
    }

    @Test
    public void outbound() throws SQLException {
        dbShell.addLinenType(
            new LinenInfo(
                "Bath towel",
                2.1)
        );
        if(dbShell.getCustomerInfo("VLP") == null)
            dbShell.addCustomer(vlp);
        int orderID = dbShell.createOrder("VLP", 1040000); //jan 4, 2000
        dbShell.addCart(5, 100);
        dbShell.soiledCart(5, orderID, 300);
        dbShell.finishOrder(orderID);
        dbShell.outboundCart(
            new OutboundInfoOrder(
                5, 
                orderID,
                "Bath towel",
                200,
                1050000
            )
        );
        dbShell.addCart(6, 130);
        dbShell.outboundCart(
            new OutboundInfoOrder(
                6, 
                orderID,
                "Bath towel",
                200,
                1050000
            )
        );
        OutboundInfoCustomer outboundInfoCustomer = dbShell.getOutboundInfoAllCustomersRange(
            1050000,
            1050000)
            .lastElement();
        assertEquals(outboundInfoCustomer.cartID(), 6);
        assertEquals(outboundInfoCustomer.customerID(), "VLP");
        assertEquals(outboundInfoCustomer.date(), 1050000);
        assertEquals(outboundInfoCustomer.linenType(), "Bath towel");
        assertEquals(outboundInfoCustomer.weight(), 70);
    }

    @Test

    public void totalWeightInAndOut() throws SQLException {
        dbShell.addLinenType(
            new LinenInfo(
                "King set",
                2.8)
        );
        if(dbShell.getCustomerInfo("VLP") == null)
            dbShell.addCustomer(vlp);
        int orderID = dbShell.createOrder("VLP", 1060000); //jan 6, 2000
        dbShell.addCart(7, 100);
        dbShell.addCart(8, 130);
        dbShell.soiledCart(7, orderID, 300);
        dbShell.soiledCart(8, orderID, 300);
        dbShell.finishOrder(orderID);
        dbShell.outboundCart(
            new OutboundInfoOrder(
                7, 
                orderID,
                "King set",
                200,
                1070000
            )
        );
        dbShell.outboundCart(
            new OutboundInfoOrder(
                8, 
                orderID,
                "King set",
                200,
                1070000
            )
        );
        assertEquals(dbShell.totalSoiledWeightIn(1060000), 370);
        assertEquals(dbShell.totalCleanWeightOut(1070000), 170);
    }

    @Test
    public void outboundRange() throws IllegalArgumentException, SQLException {
        dbShell.addLinenType(
            new LinenInfo(
                "Queen set",
                2.3)
        );
        if(dbShell.getCustomerInfo("VLP") == null)
            dbShell.addCustomer(vlp);
        int orderID = dbShell.createOrder("VLP", 1080000L); //jan 8, 2000
        dbShell.addCart(9, 100);
        dbShell.addCart(10, 130);
        dbShell.soiledCart(9, orderID, 300);
        dbShell.finishOrder(orderID);
        dbShell.outboundCart(
            new OutboundInfoOrder(
                9, 
                orderID,
                "Queen set",
                200,
                1080000L
            )
        );
        dbShell.outboundCart(
            new OutboundInfoOrder(
                10, 
                orderID,
                "Queen set",
                200,
                1090000L
            )
        );
        assertEquals(dbShell.getTotalWeightCleanForCustomerRange("VLP", 1080000L, 1080000L), 100);
        assertEquals(dbShell.getTotalWeightCleanForCustomerRange("VLP", 1080000L, 1090000L), 170);

        assertEquals(dbShell.getNumCartsOfTypeOfCustomerRange("VLP", "King set", 1080000L, 1080000L), 0);
        assertEquals(dbShell.getNumCartsOfTypeOfCustomerRange("VLP", "Queen set", 1080000L, 1080000L), 1);
        assertEquals(dbShell.getNumCartsOfTypeOfCustomerRange("VLP", "Queen set", 1080000L, 1090000L), 2);

        assertEquals(dbShell.getNumItemsOfTypeOfCustomerRange("VLP", "Queen set", 1080000L, 1080000L), 43);
        assertEquals(dbShell.getNumItemsOfTypeOfCustomerRange("VLP", "Queen set", 1080000L, 1090000L), 73);
    }

    @Test
    public void orderComplete() throws SQLException {
        dbShell.addLinenType(
            new LinenInfo(
                "Full set",
                1.8)
        );
        if(dbShell.getCustomerInfo("VLP") == null)
            dbShell.addCustomer(vlp);
        int orderID = dbShell.createOrder("VLP", 1100000L); //jan 10, 2000
        dbShell.addCart(11, 100);
        dbShell.addCart(12, 130);
        dbShell.soiledCart(11, orderID, 300);
        dbShell.finishOrder(orderID);
        dbShell.outboundCart(
            new OutboundInfoOrder(
                11, 
                orderID,
                "Full set",
                200,
                1110000L
            )
        );
        dbShell.outboundCart(
            new OutboundInfoOrder(
                12, 
                orderID,
                "Full set",
                200,
                1110000L
            )
        );

        assertFalse(dbShell.checkOrderComplete(orderID));

        dbShell.addCart(13, 100);
        dbShell.outboundCart(
            new OutboundInfoOrder(
                13, orderID, 
                "Full set", 
                110, 
                1110000L)
        );
        assertTrue(dbShell.checkOrderComplete(orderID));
    }
}
