package linen_app;

import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;

/**
 * @author Prasad Jeyapalan<p>
 * This class will generate a JPanel which will have a JTable to display all orders in the database 
 * @Version 1.0<p>
 * FileName: OrderWindow.java
 */
public class OrderWindow extends JPanel{
	//private JPanel orderPanel;
	public JTable table;
	private TitledBorder tBorder;
	private String title;
	
	/**
	 * The default construction will create a JPanel
	 * Add border and Title to the panel
	 * Will connect to the database through the DBShell and access getAllOrders function
	 * It will create a vector of each record in the database
	 * And pass it to the InfoVecTableGenerator class to produce a JTable and add it to the panel	
	 * @throws SQLException
	 */
	
	
	public OrderWindow(String dbPath) throws SQLException {	
		//orderPanel = new JPanel();
		//orderPanel.setPreferredSize(new Dimension(600,400));	
		this.setLayout(new FlowLayout());
		
		title = "All Orders View";
		tBorder = BorderFactory.createTitledBorder(tBorder, title, Color.TRANSLUCENT, 2);
		tBorder.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(tBorder);

		DBShell orders = new DBShell(dbPath);
		Vector<OrderInfo> orderInfo = orders.getAllOrders();
		InfoVecTableGenerator<OrderInfo> ordertable = new InfoVecTableGenerator<OrderInfo>(orderInfo, new OrderInfo(1, "", 01/01/1970, 3000, true));
		table = ordertable.getTable();
		this.add(new JScrollPane(table));
		//orderPanel.setVisible(true);
	}
}