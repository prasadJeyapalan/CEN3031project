package linen_app;


import java.sql.*;
import java.util.Vector;

import linen_app.message_classes.*;

/**
 * Basically a bunch of functions that connect to a saved DB directory
 * Connections are made at the start of each function/method call and closed at the end
 *  otherwise the DB will hang.<p>
 *
 * Dates are stored as unix millis, through System.currentTimeMillis()
 * Dates are shown as java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy");
 * format.format(new java.util.Date(date));<p>
 * 
 * checking if something exists is to be checked by the code using this class
 * @author Austin Franklin
 */

public class DBShell {
    private String DBName;

    //for future use
    //private String username = null;
    //private String password = null;

    //basically used as a macro
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DBName);
    }

    /** 
     * object holds the database directory and methods connect to it
     */
    public DBShell(String DBName) {
        this.DBName = DBName;
    }
    
    
    
    //===================
    //	Customer Info
    //===================
    public CustomerInfo getCustomerInfo(String customerID) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM customer "
            + "WHERE customer_id = ?"
        );
        pstmt.setString(1, customerID);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        CustomerInfo info = new CustomerInfo(
            rs.getString("customer_ID"), 
            rs.getString("name"), 
            rs.getString("address"), 
            rs.getDouble("cost_per_pound")
        );
        rs.close();
        pstmt.close();
        con.close();
        return info;
    }

    /**
     * Accepts a customerInfo object and adds it to the database
     * @param customerInfo
     * @throws SQLException
     */
    public void addCustomer(CustomerInfo customerInfo) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "INSERT INTO customer (customer_ID, name, address, cost_per_pound) "
            + "VALUES (?, ?, ?, ?)"
        );
        pstmt.setString(1, customerInfo.customerID());
        pstmt.setString(2, customerInfo.customerName());
        pstmt.setString(3, customerInfo.address());
        pstmt.setDouble(4, customerInfo.costPerPound());
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    public Vector<CustomerInfo> getAllCustomerInfo() throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM customer"
        );
        ResultSet rs = pstmt.executeQuery();
        Vector<CustomerInfo> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new CustomerInfo(
                    rs.getString("customer_ID"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("cost_per_pound"))
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }
    
    
    
    //===================
    //		CART INFO
    //===================
    /**
     * Returns a CartInfo object pulled from the database with a matching cart ID.
     * @param cartID
     * @return CartInfo, null if empty.
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public CartInfo getCartInfo(int cartID) throws SQLException, IllegalArgumentException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM cart "
            + "WHERE cart_id = ?"
        );
        pstmt.setInt(1, cartID);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        CartInfo info = new CartInfo(
            rs.getInt("cart_ID"), 
            rs.getString("location"), 
            rs.getInt("tare_weight"), 
            CartState.valueOf(
                rs.getString("state")
                .toUpperCase()
            )
        );
        rs.close();
        pstmt.close();
        con.close();
        return info;
    }

    /**
     * Adds a cart to the database with provided settings.
     * @param cartID
     * @param tareWeight
     * @throws SQLException
     */
    public void addCart(int cartID, int tareWeight) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "INSERT INTO cart "
            + "VALUES (?, 'VLP', ?, 'empty')"
        );
        pstmt.setInt(1, cartID);
        pstmt.setInt(2, tareWeight);
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    /**
     * Returns a Vector List off all the carts from the database
     * @return
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public Vector<CartInfo> getAllCartInfo() throws SQLException, IllegalArgumentException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM cart"
        );
        ResultSet rs = pstmt.executeQuery();
        Vector<CartInfo> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new CartInfo(
                    rs.getInt("cart_id"),
                    rs.getString("location"),
                    rs.getInt("tare_weight"),
                    CartState.valueOf(
                        rs.getString("state")
                        .toUpperCase()
                    )
                )
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }
    
    
    
    
    
    //===================
    //	ORDER INFO
    //===================
    /**
     * Creates a new order in the database and returns it's ID number
     * @param customerID
     * @param date
     * @return order ID number
     * @throws SQLException
     */
    public int createOrder(String customerID, long date) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "INSERT INTO orders (customer_ID, date_shipped_in) "
            + "VALUES (?, ?)"
        );
        pstmt.setString(1, customerID);
        pstmt.setLong(2, date);
        pstmt.execute();
        pstmt.close();
        con.close();
        return getLastOrderNumber();
    }

    /**
     * Deletes an order. DO NOT USE WHEN CARTS HAVE BEEN ADDED TO THE ORDER
     * @param orderID
     * @throws SQLException
     */
    public void deleteOrder(int orderID) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "DELETE FROM orders "
            + " WHERE order_ID = ?"
        );
        pstmt.setInt(1, orderID);
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    /**
     * Used at the end of adding soiled carts to an order to calculate the total soiled weight of the order
     * @param orderID
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public void finishOrder(int orderID) throws SQLException, IllegalArgumentException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(weight) FROM soiled "
            + "WHERE order_ID = ?"
        );
        pstmt.setInt(1, orderID);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            throw new IllegalArgumentException("No such order");
        }
        int totalWeight = rs.getInt(1);
        rs.close();
        if (totalWeight == 0) {
            pstmt.close();
            con.close();
            deleteOrder(orderID);
            return;
        }
        pstmt = con.prepareStatement(
            "UPDATE orders "
            + "SET weight = ?"
            + "WHERE order_id = ?"
        );
        pstmt.setInt(1, totalWeight);
        pstmt.setInt(2, orderID);
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    /**
     * Returns a Vector list of all active orders.
     * @return A Vector<OrderInfo> list of all active orders.
     * @throws SQLException
     */
    public Vector<OrderInfo> getActiveOrders() throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM orders WHERE active = 1"
        );
        ResultSet rs = pstmt.executeQuery();
        Vector<OrderInfo> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new OrderInfo(
                    rs.getInt("order_ID"),
                    rs.getString("customer_ID"),
                    rs.getLong("date_shipped_in"),
                    rs.getInt("weight"), 
                    rs.getInt("active") == 1)
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }

    /**
     * Returns an OrderInfo for a matching order ID
     * @param orderID
     * @return OrderInfo
     * @throws SQLException
     */
    public OrderInfo getOrderInfo(int orderID) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM orders WHERE order_ID = ?"
        );
        pstmt.setInt(1, orderID);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next())
            return null;
        OrderInfo orderInfo = new OrderInfo(
            orderID, 
            rs.getString("customer_ID"), 
            rs.getLong("date_shipped_in"), 
            rs.getInt("weight"), 
            rs.getInt("active") == 1
        );
        rs.close();
        pstmt.close();
        con.close();
        return orderInfo;
    }

    public Vector<OrderInfo> getAllOrders() throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM orders"
        );
        ResultSet rs = pstmt.executeQuery();
        Vector<OrderInfo> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new OrderInfo(
                    rs.getInt("order_ID"),
                    rs.getString("customer_ID"),
                    rs.getLong("date_shipped_in"),
                    rs.getInt("weight"), 
                    rs.getInt("active") == 1)
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }
    
    /** will change order.active to 0 if SUM(outbound.weight) >= 0.9 * orders.weight
     */
    public boolean checkOrderComplete(int orderID) throws SQLException {
        double soliedIn = (double) getOrderInfo(orderID).weight();
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(weight) FROM outbound_history "
            + "WHERE order_ID = ?"
        );
        pstmt.setInt(1, orderID);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            return false;
        }
        double cleanOut = (double) rs.getInt(1);
        rs.close();
        if (cleanOut >= 0.9 * soliedIn) {
            pstmt = con.prepareStatement(
                "UPDATE orders "
                + "SET active = 0 "
                + "WHERE order_ID = ?"
            );
            pstmt.setInt(1, orderID);
            pstmt.execute();
            pstmt.close();
            con.close();
            return true;
        }
        else {
            pstmt.close();
            con.close();
            return false;
        }
    }
    
    
    
    
    
    //===================
    //		LinenInfo
    //===================
    public void addLinenType(LinenInfo linenInfo) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "INSERT INTO linen (type, weight) "
            + "VALUES (?, ?)"
        );
        pstmt.setString(1, linenInfo.linenType());
        pstmt.setDouble(2, linenInfo.weight());
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    public Vector<LinenInfo> getAllLinenInfo() throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM linen"
        );
        ResultSet rs = pstmt.executeQuery();
        Vector<LinenInfo> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new LinenInfo(
                    rs.getString("type"),
                    rs.getDouble("weight")
                )
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }
    
    
    
    
    //===================
    //		Carts
    //===================
    
    /**
     * @return a CartState enum
     */
    public CartState getCartState(int cartID) throws SQLException, IllegalArgumentException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT state FROM cart WHERE cart_ID = ?"
        );
        pstmt.setInt(1, cartID);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        String stateStr = rs.getString("state");
        rs.close();
        pstmt.close();
        con.close();
        return CartState.valueOf(stateStr.toUpperCase()); //match to valid cart states, will throw exception if not
    }

    private void clearCart(Connection con, int cartID)  throws SQLException, IllegalArgumentException {
        CartState state = getCartState(cartID);
        String stateStr = state.toString().toLowerCase();
        if (stateStr.equals("empty"))
            return;
        PreparedStatement pstmt = con.prepareStatement(
            "DELETE from " + stateStr +" WHERE cart_ID = ?"
        );
        pstmt.setInt(1, cartID);
        pstmt = con.prepareStatement(
            "UPDATE cart "
            + "SET state = 'empty' "
            + "WHERE cart_ID = ?"
        );
        pstmt.setInt(1, cartID);
        pstmt.execute();
        pstmt.close();
    }

    /**
     * Moves a cart into soiled state
     * @param weightMeasured is the weight given by the scale with both the cart and linen in it
     */
    public void soiledCart(int cartID, int orderID, int weightMeasured) throws SQLException, IllegalArgumentException {
        Connection con = connect();
        clearCart(con, cartID);
        int tareWeight = getCartInfo(cartID).tareWeight();

        PreparedStatement pstmt = con.prepareStatement(
            "INSERT INTO soiled "
            + "VALUES (?, ?, ?)"
        );
        pstmt.setInt(1, cartID);
        pstmt.setInt(2, orderID);
        pstmt.setInt(3, weightMeasured - tareWeight);
        pstmt.execute();
        pstmt = con.prepareStatement(
            "UPDATE cart "
            + "SET state = 'soiled' "
            + "WHERE cart_ID = ?"
        );
        pstmt.setInt(1, cartID);
        pstmt.execute();
        pstmt.close();
        con.close();
    }
    
    
    
    
    
    
    
    //===================
    //OUTBOUND INFO ORDER
    //===================
    /**
     * Moves a cart into outbound state
     * @param weightMeasured is the weight given by the scale with both the cart and linen in it
     */
    public void outboundCart(OutboundInfoOrder outboundInfo) throws SQLException, IllegalArgumentException {
        Connection con = connect();
        clearCart(con, outboundInfo.cartID());
        int tareWeight = getCartInfo(outboundInfo.cartID()).tareWeight();
        PreparedStatement pstmt = con.prepareStatement(
            "INSERT INTO outbound "
            + "VALUES (?, ?, ?, ?, ?)"
        );
        pstmt.setInt(1, outboundInfo.cartID());
        pstmt.setInt(2, outboundInfo.orderID());
        pstmt.setString(3, outboundInfo.linenType());
        pstmt.setInt(4, outboundInfo.weightMeasured() - tareWeight);
        pstmt.setLong(5, outboundInfo.date());
        pstmt.execute();
        pstmt = con.prepareStatement(
            "INSERT INTO outbound_history "
            + "VALUES (?, ?, ?, ?, ?)"
        );
        pstmt.setInt(1, outboundInfo.cartID());
        pstmt.setInt(2, outboundInfo.orderID());
        pstmt.setString(3, outboundInfo.linenType());
        pstmt.setInt(4, outboundInfo.weightMeasured() - tareWeight);
        pstmt.setLong(5, outboundInfo.date());
        pstmt.execute();
        pstmt = con.prepareStatement(
            "UPDATE cart "
            + "SET state = 'outbound' "
            + "WHERE cart_ID = ?"
        );
        pstmt.setInt(1, outboundInfo.cartID());
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    

    public Vector<OutboundInfoOrder> getOutboundInfoCustomerRange(String customerID, long dateStart, long dateEnd) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM outbound_history "
            + "WHERE customer_id = ? AND date >= ? AND date <= ?"
        );
        pstmt.setString(1, customerID);
        pstmt.setLong(2, dateStart);
        pstmt.setLong(3, dateEnd);
        ResultSet rs = pstmt.executeQuery();
        Vector<OutboundInfoOrder> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new OutboundInfoOrder(
                    rs.getInt("cart_id"),
                    rs.getInt("order_id"),
                    rs.getString("type"),
                    rs.getInt("weight"),
                    rs.getLong("date")
                )
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }

    /**
     * All outbound info for ALL customers in a range of dates
     */
    public Vector<OutboundInfoCustomer> getOutboundInfoAllCustomersRange(long dateStart, long dateEnd) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT * FROM outbound_history oh, orders od "
            + "WHERE oh.date >= ? AND oh.date <= ? AND oh.order_id = od.order_id"
        );
        pstmt.setLong(1, dateStart);
        pstmt.setLong(2, dateEnd);
        ResultSet rs = pstmt.executeQuery();
        Vector<OutboundInfoCustomer> infoVec = new Vector<>();
        while(rs.next()) {
            infoVec.add(
                new OutboundInfoCustomer(
                    rs.getString("customer_id"),
                    rs.getInt("cart_id"),
                    rs.getString("linen_type"),
                    rs.getInt("weight"),
                    rs.getLong("date")
                )
            );
        }
        rs.close();
        pstmt.close();
        con.close();
        return infoVec;
    }

    
    
    
    
    
    
    
    //===================
    //	STATISTICS
    //===================
    /**
     * Used to check how much linen was sent out on a specific date
     */
    public int totalCleanWeightOut(long date) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(weight) FROM outbound_history WHERE date = ?"
        );
        pstmt.setLong(1, date);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int sum = rs.getInt(1);
        rs.close();
        pstmt.close();
        con.close();
        return sum;
    }

    /**
     * Used to check how much linen was sent in on a specific date
     */
    public int totalSoiledWeightIn(long date) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(weight) FROM orders WHERE date_shipped_in = ?"
        );
        pstmt.setLong(1, date);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        pstmt.close();
        con.close();
        return count;
    }

    /** see that x number of pillow carts got sent to y over the last week
     */ 
    public int getNumCartsOfTypeOfCustomerRange(String customerID, String linenType, Long dateStart, long dateEnd) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT COUNT(*) FROM outbound_history oh, orders od "
            + "WHERE od.customer_ID = ? AND oh.order_ID = od.order_id AND oh.linen_type = ? "
            + "AND oh.date >= ? AND oh.date <= ?"
        );
        pstmt.setString(1, customerID);
        pstmt.setString(2, linenType);
        pstmt.setLong(3, dateStart);
        pstmt.setLong(4, dateEnd);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        pstmt.close();
        con.close();
        return count;
    }

    /** see that approximately x number of pillows got sent to y over the last week
     */ 
    public int getNumItemsOfTypeOfCustomerRange(String customerID, String linenType, long dateStart, long dateEnd) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(oh.weight / ln.weight) FROM outbound_history oh, linen ln, orders od "
            + "WHERE oh.linen_type = ln.type "
			+ "AND oh.order_ID = od.order_ID "
			+ "AND od.customer_ID = ? "
			+ "AND oh.date >= ? AND oh.date <= ? "
			+ "AND ln.type = ?"
        );
        pstmt.setString(1, customerID);
        pstmt.setLong(2, dateStart);
        pstmt.setLong(3, dateEnd);
        pstmt.setString(4, linenType);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int sum = rs.getInt(1);
        rs.close();
        pstmt.close();
        con.close();
        return sum;
    }

    public double getTotalWeightCleanForCustomerRange(String customerID, long dateStart, long dateEnd) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(oh.weight) FROM orders od, outbound_history oh "
            + "WHERE od.order_id = oh.order_id AND od.customer_id = ? "
            + "AND oh.date >= ? AND oh.date <= ?"
        );
        pstmt.setString(1, customerID);
        pstmt.setLong(2, dateStart);
        pstmt.setLong(3, dateEnd);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        double totalWeight = rs.getDouble(1);
        rs.close();
        pstmt.close();
        con.close();
        return totalWeight;
    }

    public int getTotalWeightSoiledForCustomerRange(String customerID, long dateStart, long dateEnd) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT SUM(weight) FROM orders "
            + "WHERE customer_id = ? AND date_shipped_in >= ? AND date_shipped_in <= ?"
        );
        pstmt.setString(1, customerID);
        pstmt.setLong(2, dateStart);
        pstmt.setLong(3, dateEnd);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int totalWeight = rs.getInt(1);
        rs.close();
        pstmt.close();
        con.close();
        return totalWeight;
    }
    
    
    
    
    
    
    
    
    //===================
    // CUSTOMER ACTIVITY
    //===================
    /**
     * Queries the database to check whether or not the specified customer is active.
     * @param customerID
     * @return customer's Active State
     * @throws SQLException
     */
    public boolean isCustomerActive(String customerID) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT COUNT(*) FROM customer WHERE customer_ID = ? AND active = 1"
        );
        pstmt.setString(1, customerID);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        boolean active = rs.getInt(1) == 1 ;
        rs.close();
        pstmt.close();
        con.close();
        return active;
    }

    public void makeCustomerActive(String customerID) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "UPDATE customer SET active = 1 "
            + "WHERE customer_ID = ?"
        );
        pstmt.setString(1, customerID);
        pstmt.execute();
        pstmt.close();
        con.close();
    }

    public void makeCustomerInactive(String customerID) throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "UPDATE customer SET active = 0 "
            + "WHERE customer_ID = ?"
        );
        pstmt.setString(1, customerID);
        pstmt.execute();
        pstmt.close();
        con.close();
    }
    
    public boolean doesCustomerExists(String customerID) throws SQLException{
    	Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT COUNT(*) FROM customer WHERE customer_ID = ?"
        );
        pstmt.setString(1, customerID);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        boolean customerExist = rs.getString(1) != null;
        rs.close();
        pstmt.close();
        con.close();
        return customerExist;
    	
    }


     
    
    
    //===================
    //		UTILITY
    //===================

    public int getLastOrderNumber() throws SQLException {
        Connection con = connect();
        PreparedStatement pstmt = con.prepareStatement(
            "SELECT seq FROM sqlite_sequence WHERE name = 'orders'"
        );
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int lastOrder = rs.getInt(1);
        rs.close();
        pstmt.close();
        con.close();
        return lastOrder;
    }

    
}