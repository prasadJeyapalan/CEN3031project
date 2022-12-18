package linen_app;

import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;

/**
 * @author Austin Franklin<p>
 * This class will generate a JPanel displaying outbound carts and able to filter by customer ID
 * @version 1.0
 */
public class OutboundCartsWindow extends JPanel{

    JTable table;
    JScrollPane scrollPane;
    JPanel thisPanel;
		
	/**
	 * The default construction will create a JPanel
	 * Add border and Title to the panel
	 * Will connect to the database through the DBShell and access getOutboundInfoAllCustomerRange function
	 * It will create a vector of each record in the database
	 * And pass it to the InfoVecTableGenerator class to produce a JTable and add it to the panel
     * Field and button to filter by ID and recreate the table	
	 * @throws SQLException
	 */
	public OutboundCartsWindow(String dbPath) throws SQLException{	
		setLayout(new BorderLayout());
		thisPanel = this;
		String title = "Outbound Cart History";
		TitledBorder tBorder = BorderFactory.createTitledBorder(new EmptyBorder(0, 350, 0, 350), title, Color.TRANSLUCENT, 2);
		tBorder.setTitleJustification(TitledBorder.CENTER);
		setBorder(tBorder);
		//get all OutboundInfoCustomer rows
		DBShell db = new DBShell(dbPath);
        Vector<OutboundInfoCustomer> outboundHistoryFull = db.getOutboundInfoAllCustomersRange(Long.MIN_VALUE, Long.MAX_VALUE);

		InfoVecTableGenerator<OutboundInfoCustomer> tableGen = new InfoVecTableGenerator<OutboundInfoCustomer>(outboundHistoryFull, 
            new OutboundInfoCustomer("", 0, "", 0, 0));
		table = tableGen.getTable();
        scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter an ID to filter by: ");
        filterPanel.add(label);
        JTextField filterInput = new JTextField();
        filterInput.setPreferredSize(new Dimension(150, 20));
        filterPanel.add(filterInput);
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            Vector<OutboundInfoCustomer> outboundHistory;
            //resets filter if field is empty
            if (filterInput.getText().equals("")) {
                outboundHistory = new Vector<>(outboundHistoryFull);
            }
            else {
                outboundHistory = new Vector<>(
                    outboundHistoryFull
                    .stream()
                    .filter(a -> a.customerID().equals(filterInput.getText()))
                    .toList()
                );
            }
            //create new table with filtered info
            InfoVecTableGenerator<OutboundInfoCustomer> tableGenNew = new InfoVecTableGenerator<OutboundInfoCustomer>(outboundHistory, 
                new OutboundInfoCustomer("", 0, "", 0, 0));
            
            table = tableGenNew.getTable();
            //replace table and repaint
            thisPanel.remove(scrollPane);
            scrollPane = new JScrollPane(table);
            thisPanel.add(scrollPane, BorderLayout.CENTER);
            thisPanel.revalidate();
            thisPanel.repaint();
        });
        filterPanel.add(filterButton);
        add(filterPanel, BorderLayout.SOUTH);
	}
}
		




