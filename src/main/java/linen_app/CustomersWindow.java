package linen_app;

import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;

/**
 * @author Prasad Jeyapalan<p>
 * This class will generate a JPanel which will have a JTable to display all the customers in the system
 * @version 1.0<p>
 * FileName: CustomerWindow.java
 */
public class CustomersWindow extends JPanel{
	private JPanel customersPanel;
	public JTable table;
	private TitledBorder tBorder;
	private String title;
	
	/**
	 * The default construction will create a JPanel
	 * Add border and Title to the panel
	 * Will connect to the database through the DBShell and access getAllCustomerInfo function
	 * It will create a vector of each record in the database
	 * And pass it to the InfoVecTableGenerator class to produce a JTable and add it to the panel	
	 * @throws SQLException
	 */
	
	public CustomersWindow(String dbPath) throws SQLException {
		customersPanel = new JPanel();
		customersPanel.setPreferredSize(new Dimension(600,400));	
		this.setLayout(new FlowLayout());
		
		title = "All Customers View";
		tBorder = BorderFactory.createTitledBorder(tBorder, title, Color.TRANSLUCENT, 2);
		tBorder.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(tBorder);

		DBShell db = new DBShell(dbPath);
		Vector<CustomerInfo> customerInfo = db.getAllCustomerInfo();
		InfoVecTableGenerator<CustomerInfo> customertable = new InfoVecTableGenerator<CustomerInfo>(
			customerInfo,
			 new CustomerInfo("",
				"",
				"",
				1.0
			)
		);
		table = customertable.getTable();			
		this.add(new JScrollPane(table));
		customersPanel.setVisible(true);
	}
}			