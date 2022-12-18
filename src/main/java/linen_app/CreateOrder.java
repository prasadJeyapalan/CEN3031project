package linen_app;

import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import linen_app.message_classes.*;

/**
 * Panel displays customers for reference to input a customer ID to input for an order, spawns a series of windows to put in incoming soiled cart information
 * @author Austin Franklin
 */
public class CreateOrder extends JPanel {
    public CreateOrder(String dbPath) throws SQLException {
        setLayout(new BorderLayout());

        DBShell db = new DBShell(dbPath);
        Vector<CustomerInfo> customerInfo = db.getAllCustomerInfo();
        customerInfo.removeIf(cust -> cust.customerID().equals("VAC")); //VAC isnt a customer
        //create table
        InfoVecTableGenerator<CustomerInfo> customerTable = new InfoVecTableGenerator<CustomerInfo>(
            customerInfo,
            new CustomerInfo("",
				"",
				"",
				1.0
			)
        );
        JScrollPane scrollPane = new JScrollPane(customerTable.getTable());
        TitledBorder tBorder = BorderFactory.createTitledBorder(
            new EmptyBorder(0, 350, 0, 350),
            "Create New Order",
            TitledBorder.CENTER,
            2
        );
        scrollPane.setBorder(tBorder);
        add(scrollPane);
        //create panel for inputting info for filtering
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter a customer ID:"));
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 20));
        inputPanel.add(textField);

        JButton createOrderButton = new JButton("Create New Order");
        createOrderButton.addActionListener(e -> {
            String input = textField.getText();
            CustomerInfo info = null;
            int orderID = 0;
            try {
                info = db.getCustomerInfo(input);
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            //catch if no such customer
            if (info == null) {
                JOptionPane.showMessageDialog(this, "No such customer!");
                return;
            }
            //create the order
            try {
                orderID = db.createOrder(input, System.currentTimeMillis());
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            //something has gone terribly wrong
            if (orderID == 0) {
                JOptionPane.showMessageDialog(this, "Error with creating order!");
                return;
            }
            new AddSoiledCarts(dbPath, orderID);
        });

        inputPanel.add(createOrderButton);
        add(inputPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}

/**
 * Small JFrame created when a new order is created to input incoming carts
 * @author Austin Franklin
 */
class AddSoiledCarts extends JFrame {
    public AddSoiledCarts(String dbPath, int orderID) {
        setTitle("Add Soiled Carts");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //person MUST hit finish order
        setSize(300, 120);
        getContentPane().setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));

        //Adding Cart ID
        inputPanel.add(new JLabel("Cart ID"));
        JTextField cartField = new JTextField();
        cartField.setPreferredSize(new Dimension(150, 20));
        inputPanel.add(cartField);

        //Adding the weight measured
        inputPanel.add(new JLabel("Weight Measured"));
        JTextField weightField = new JTextField();
        weightField.setPreferredSize(new Dimension(150, 20));
        inputPanel.add(weightField);

        getContentPane().add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addCartButton = new JButton("Add Cart");
        addCartButton.addActionListener(e -> {
            DBShell db = new DBShell(dbPath);
            CartInfo cartInfo = null;
            int cartID = 0;
            int weight = 0;
            try { //catch non number entered
                cartID = Integer.parseInt(cartField.getText());
                weight = Integer.parseInt(weightField.getText());
            }
            catch (IllegalArgumentException e1) {
                JOptionPane.showMessageDialog(this, "Not a number!");
                return;
            }
            //check cart exists
            try {
                cartInfo = db.getCartInfo(cartID);
            }
            catch (IllegalArgumentException | SQLException e1) {
                e1.printStackTrace();
            }
            if (cartInfo == null) {
                JOptionPane.showMessageDialog(this, "Cart not found!");
                return;
            }
            //add the soiled cart
            try {
                db.soiledCart(
                    cartInfo.cartID(),
                    orderID,
                    weight
                );
            }
            catch (IllegalArgumentException | SQLException e1) {
                e1.printStackTrace();
            }
            //create new window for next cart
            dispose();
            new AddSoiledCarts(dbPath, orderID); //creates a new window to input information into
        });
        buttonPanel.add(addCartButton);
        
        //sums up the soiled linen weights for the order entry in the db
        JButton finishOrderButton = new JButton("Finish Order Input");
        finishOrderButton.addActionListener(e -> {
            DBShell db = new DBShell(dbPath);
            //runs finishOrder to finalize the total weight of the order
            try {
                db.finishOrder(orderID);
            }
            catch (IllegalArgumentException | SQLException e1) {
                e1.printStackTrace();
            }
            dispose();
        });
        buttonPanel.add(finishOrderButton);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}