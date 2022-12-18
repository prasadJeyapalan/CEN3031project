package linen_app;

import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;

/**
 * Creates a new window to add carts with clean linen to be shipped out
 * @author Austin Franklin, Prasad Jeyapalan
 */
public class AddOutbound {

    public AddOutbound(String dbpath){
        JFrame cartF = new JFrame("Add Clean Cart for Outbound");
        cartF.setLocationRelativeTo(null);
        cartF.setSize(400,300);

        JPanel cartP = new JPanel();
		cartP.setPreferredSize(new Dimension(400, 300));
		cartP.setLayout(new GridLayout(5,2,10,10));

        String title = "Cart Information";

        TitledBorder tBorder = BorderFactory.createTitledBorder(null, title, Color.TRANSLUCENT, 2);
        tBorder.setTitleJustification(TitledBorder.CENTER);
		cartP.setBorder(tBorder);
		

        JLabel cartIDL = new JLabel("Cart Number");
		cartIDL.setFont(new Font("Consolas", Font.BOLD, 20));

        JLabel custIDL = new JLabel("Customer ID");
        custIDL.setFont(new Font("Consolas", Font.BOLD, 20));

        JLabel grossWtL = new JLabel("Gross Weight");
        grossWtL.setFont(new Font("Consolas", Font.BOLD, 20));

        JLabel linenTypeL = new JLabel("Linen Type");
        linenTypeL.setFont(new Font("Consolas", Font.BOLD, 20));

        JTextField cartIDTF = new JTextField();
		cartIDTF.setFont(new Font("consolas", Font.BOLD, 20));

        JTextField custIDTF = new JTextField();
		custIDTF.setFont(new Font("consolas", Font.BOLD, 20));

        JTextField grossWtTF = new JTextField();
		grossWtTF.setFont(new Font("consolas", Font.BOLD, 20));

        JTextField linenTypeTF = new JTextField();
		linenTypeTF.setFont(new Font("consolas", Font.BOLD, 20));

        JButton addB = new JButton("Add");
        addB.setSize(100, 30);
        addB.setFont(new Font("Consolas", Font.BOLD, 15));
        addB.addActionListener(e -> {
            DBShell db = new DBShell(dbpath);
            CartInfo info = null;
            int cartID = -1;
            //check good number entered for cart ID
            try {
                cartID = Integer.parseInt(cartIDTF.getText());
            }
            catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "No Cart Entered!");
                return;
            }

            //check good number entered for weight
            int weight = -1; 
            try {
                weight = Integer.parseInt(grossWtTF.getText());
            }
            catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "No Weight Entered!");
            }
            //check cart exists
            try {
                info = db.getCartInfo(cartID);
            }
            catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            //if no cart found, throws an error window and gives an option to add the cart
            if (info == null) {
                int addCartOption = JOptionPane.showConfirmDialog(null, "Do you want to add cart in the system?", "Cart Does Not Exist!", JOptionPane.YES_NO_OPTION);
                if (addCartOption == JOptionPane.YES_OPTION) {
                    new AddCarts(dbpath);
                }
                else {
                    return;
                }
            }

            //check customer exists
            String custID = custIDTF.getText();
            CustomerInfo custInfo = null;
            try {
                custInfo = db.getCustomerInfo(custID);
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            if (custInfo == null) {
                JOptionPane.showMessageDialog(cartF, "Customer Not Found!");
                return;
            }

            //check that the linen type exists
            String linen = linenTypeTF.getText();
            try {
                Vector<LinenInfo> linenVec = db.getAllLinenInfo();
                linenVec = new Vector<>(
                    linenVec
                        .stream()
                        .filter(a -> a.linenType().compareTo(linen) == 0)
                        .toList()
                );
                if (linenVec.size() == 0) {
                    JOptionPane.showMessageDialog(cartF, "Linen Type Not Found!");
                    return;
                }
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                //finds the oldest active order for the customer
                Vector<OrderInfo> activeOrders = db.getActiveOrders();
                activeOrders = new Vector<>(
                    activeOrders
                        .stream()
                        .filter(a -> a.customerID().equals(custID))
                        .toList()
                );
                int orderID;
                if (activeOrders.size() > 0) {
                    activeOrders.sort(OrderInfo.compareDateShippedIn());
                    orderID = activeOrders.firstElement().orderID();
                }
                else {
                    //if no active order, get newest order
                    Vector<OrderInfo> allOrders = db.getAllOrders();
                    allOrders = new Vector<>(
                        allOrders
                            .stream()
                            .filter(a -> a.customerID().equals(custID))
                            .toList()
                    );
                    //unless there are no orders to attach this to
                    if (allOrders.size() == 0) {
                        JOptionPane.showMessageDialog(cartF, "No Orders For This Customer Found!");
                        return;
                    }
                    allOrders.sort(OrderInfo.compareDateShippedIn());
                    orderID = allOrders.lastElement().orderID();
                }
                //adds cart to order history
                db.outboundCart(
                    new OutboundInfoOrder(
                        cartID,
                        orderID,
                        linenTypeTF.getText(),
                        weight,
                        System.currentTimeMillis()
                    )
                );
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }

            //asks user if they want to add aother cart
            //if yes, clears fields
            //else disposes window
            int addAnother = JOptionPane.showConfirmDialog(null, "Do you want to add another clean cart?", "ADD CLEAN", JOptionPane.YES_NO_OPTION);
            if (addAnother == JOptionPane.YES_OPTION) {
                cartIDTF.setText("");
                custIDTF.setText("");
                grossWtTF.setText("");
                linenTypeTF.setText("");
            }
            else {
                cartF.dispose();
            }
        }); 

        JButton cancelB = new JButton("Cancel");
        cancelB.setSize(100, 30);
        cancelB.setFont(new Font("Consolas", Font.BOLD, 15));
        cancelB.addActionListener(e -> {
            cartF.dispose();
        });

        cartP.add(cartIDL);
        cartP.add(cartIDTF);
        cartP.add(custIDL);
        cartP.add(custIDTF);
        cartP.add(linenTypeL);
        cartP.add(linenTypeTF);        
        cartP.add(grossWtL);
        cartP.add(grossWtTF);       
        cartP.add(addB);
        cartP.add(cancelB);
        cartF.add(cartP);
		cartF.setResizable(false);
		cartF.setVisible(true);
		cartF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
    }
}