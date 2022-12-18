package linen_app;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * JFrame meant for a user to type in carts to be stored in the database
 * @author Prasad Jeyapalan
 */
public class AddCarts extends JFrame  implements ActionListener{
    private JFrame addCartF;
    private JPanel addCartP;
    private JTextField cartIDTF;
    private JTextField tareWtTF;
    private JButton addB;
    private JButton cancelB;
    private JLabel cartIDL;
    private JLabel tareWtL;    
    private String dbPath;
    private String title; 
    private TitledBorder tBorder;
    private int cartID;
    private int tareWt;

    public AddCarts(String dbPath){
        this.dbPath = dbPath;
        addCartF = new JFrame("Add cart");
        addCartF.setSize(500, 250);
        addCartF.setLocationRelativeTo(null);

        
        addCartP = new JPanel();
        addCartP.setPreferredSize(new Dimension (100,100));
        addCartP.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);

        title = "Add New Cart";
        tBorder = BorderFactory.createTitledBorder(tBorder, title, Color.TRANSLUCENT, 2);
        tBorder.setTitleJustification(TitledBorder.CENTER);
		addCartP.setBorder(tBorder);

        cartIDL = new JLabel("Enter Cart ID");
        cartIDL.setFont(new Font("Consolas", Font.BOLD, 15));

        tareWtL = new JLabel("Enter Tare Weight");
        tareWtL.setFont(new Font("Consolas", Font.BOLD, 15));

        cartIDTF = new JTextField();
        cartIDTF.requestFocusInWindow();
        cartIDTF.setFont(new Font("Consolas", Font.BOLD, 15));
		cartIDTF.setPreferredSize(new Dimension (100, 25));

        tareWtTF = new JTextField();
        tareWtTF.setFont(new Font("Consolas", Font.BOLD, 15));
		tareWtTF.setPreferredSize(new Dimension (100, 25));

        addB = new JButton("Add Cart");
        addB.setSize(100, 30);
        addB.setFont(new Font("Consolas", Font.BOLD, 15));
		addB.addActionListener(this);

        cancelB = new JButton("Cancel");
        cancelB.setSize(100, 30); 
        cancelB.setFont(new Font("Consolas", Font.BOLD, 15));
		cancelB.addActionListener(this);
             

        c.gridx = 0;
        c.gridy = 0;
        addCartP.add(cartIDL,c);

        c.gridx = 1;
        c.gridy = 0;
        addCartP.add(cartIDTF,c);

        c.gridx = 0;
        c.gridy = 1;
        addCartP.add(tareWtL,c);

        c.gridx = 1;
        c.gridy = 1;
        addCartP.add(tareWtTF,c);

        c.gridx = 0;
        c.gridy = 2;
        addCartP.add(addB,c);

        c.gridx = 1;
        c.gridy = 2;
        addCartP.add(cancelB,c);
		
        addCartF.add(addCartP);        
        addCartF .setResizable(false);
		addCartF.setVisible(true);
		addCartF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    @Override
    public void actionPerformed (ActionEvent e){
        DBShell addCart = new DBShell(dbPath);

        if(e.getSource() == addB){
            String ID = cartIDTF.getText();
            String weight = tareWtTF.getText();
           
            if(ID.isBlank()){
                JOptionPane.showMessageDialog(null, "Cart ID cannot be empty");
				cartIDTF.requestFocusInWindow();
				ID = cartIDTF.getText();
            }
            else if(weight.isEmpty()){
                JOptionPane.showMessageDialog(null, "Tare Weight cannot be empty");
				tareWtTF.requestFocusInWindow();
				weight = tareWtTF.getText();

            }
            else{
            try {
                cartID = Integer.parseInt(ID);
                tareWt = Integer.parseInt(weight);
                if(addCart.getCartInfo(cartID) == null){
                addCart.addCart(cartID, tareWt);
              
                JOptionPane.showMessageDialog(null, "Cart Added Successfuly");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Cart Exist. Check cart Number");
                }
            } catch (SQLException e1) {                
                JOptionPane.showMessageDialog(null, "Cart Exist. Check cart Number");
            }
        }

        }

        else if (e.getSource() == cancelB){
                addCartF.dispose();
        }

    }

    
}
