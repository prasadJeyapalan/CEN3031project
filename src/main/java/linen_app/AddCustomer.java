package linen_app;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;

/**
 * This function adds new customers to the database
 * @author Prasad Jeyapalan
 * @version 1.0<p>
 * FileName AddCustomer.java
 */
public class AddCustomer extends JFrame implements ActionListener{
	private String name;
	private String address;
	private String custID;
	private String title;
	private String cpp;	
	private JPanel formP;
	private JFrame frame;
	private TitledBorder tBorder;
	private JTextField custIDTF;
	private JTextField custNameTF;
	private JTextField custAddressTF;
	private JTextField custCPPTF;
	private JLabel custNameL;
	private JLabel custIDL;
	private JLabel custAddL;
	private JLabel custCPPL;
	private JButton addB;
	private JButton cancelB;
	private JRadioButton activeRB;
	private JRadioButton inactiveRB;
	private ButtonGroup bg;
	private String dbPath;
	
	public AddCustomer(String dbPath) {		
		this.dbPath = dbPath;
		frame = new JFrame("Add New Customer");
		frame.setSize(600,250);
		frame.setLocationRelativeTo(null);	
		
		formP = new JPanel();
		formP.setPreferredSize(new Dimension(300, 300));
		formP.setLayout(new GridLayout(6,2,10,10));
		
		title = "New Customer Information";
		
		tBorder = BorderFactory.createTitledBorder(tBorder, title, Color.TRANSLUCENT, 2);
		tBorder.setTitleJustification(TitledBorder.CENTER);
		formP.setBorder(tBorder);
		custIDL = new JLabel("Enter customer ID");
		custIDL.setFont(new Font("Consolas", Font.BOLD, 15));		
		
		custIDTF = new JTextField();
		custIDTF.requestFocusInWindow();
		custIDTF.setPreferredSize(new Dimension (70, 30));	
		
		
		custNameL = new JLabel("Custommer Name");
		custNameL.setFont(new Font("Consolas", Font.BOLD, 15));
		
		
		custNameTF = new JTextField();
		custNameTF.setPreferredSize(new Dimension (160, 30));
		
		custAddL = new JLabel("Enter Customer Address");
		custAddL.setFont(new Font("Consolas", Font.BOLD, 15));
		
		
		custAddressTF = new JTextField();
		custAddressTF.setPreferredSize(new Dimension(150,20));	
		
		custCPPL = new JLabel("Customer Cost Per Pounds");
		custCPPL.setFont(new Font("Consolas", Font.BOLD, 15));
		
		custCPPTF = new JTextField();
		custCPPTF.setPreferredSize(new Dimension(150,20));	
		
		
		addB = new JButton("Add Customer");
		addB.setSize(100, 30);
		addB.setFont(new Font("Consolas", Font.BOLD, 15));
		addB.addActionListener(this);
		cancelB = new JButton("Exit");
		cancelB.setSize(100,30);
		cancelB.setFont(new Font("Consolas", Font.BOLD, 15));
		cancelB.addActionListener(this);
		bg = new ButtonGroup();
		activeRB = new JRadioButton("Active");
		activeRB.addActionListener(this);
		inactiveRB = new JRadioButton("Inactive");
		inactiveRB.addActionListener(this);
		bg.add(activeRB);
		bg.add(inactiveRB);
		inactiveRB.setSelected(true);
		
		formP.add(custIDL);
		formP.add(custIDTF);
		formP.add(custNameL);
		formP.add(custNameTF);
		formP.add(custAddL);
		formP.add(custAddressTF);
		formP.add(custCPPL);
		formP.add(custCPPTF);		 
		formP.add(activeRB);
		formP.add(inactiveRB);
		formP.add(addB);
		formP.add(cancelB);	
		
		frame.add(formP);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				
				
	}
	@Override
	public void actionPerformed (ActionEvent e) {
		JFrame existP = new JFrame();
		
		DBShell addCust = new DBShell(dbPath);
		
		if(e.getSource() == addB) {		
			name = custNameTF.getText();
			address = custAddressTF.getText();
			custID = custIDTF.getText();
			cpp = custCPPTF.getText();
			if(custID.isEmpty()) {
				JOptionPane.showMessageDialog(existP, "ID cannot be empty");
				custIDTF.requestFocusInWindow();
				custID = custIDTF.getText();
			}
			else if(name.isEmpty()) {
				JOptionPane.showMessageDialog(existP, "Name cannot be empty");
				custNameTF.requestFocusInWindow();
				custID = custNameTF.getText();
			}
			else if(address.isEmpty()) {
				JOptionPane.showMessageDialog(existP, "Address cannot be empty");
				custAddressTF.requestFocusInWindow();
				address = custAddressTF.getText();
			}
			else if(cpp.isEmpty()) {
				JOptionPane.showMessageDialog(existP, "Cost per pounds cannot be empty");
				custCPPTF.requestFocusInWindow();
				cpp = custCPPTF.getText();
			}
			else {	
				try {
					if (addCust.doesCustomerExists(custID)){
						CustomerInfo info = new CustomerInfo(custID, name, address, Double.parseDouble(cpp));
						addCust.addCustomer(info);						
						JOptionPane.showMessageDialog(existP, "Customer Added Successfuly");
					}			
				}
				catch (SQLException e1) {
					JOptionPane.showMessageDialog(existP, "Customer Exist");		
				}
			}
		}
		else if(e.getSource() == activeRB) {
			try {					
					addCust.makeCustomerActive(custID);
					JOptionPane.showMessageDialog(existP, "Customer Activated");
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == inactiveRB){
			try {
				addCust.makeCustomerInactive(custID);
				JOptionPane.showMessageDialog(existP, "Customer is inactive");
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == cancelB) {
			frame.dispose();				
		}
	}
}