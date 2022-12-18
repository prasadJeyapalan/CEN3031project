package linen_app;




import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


import javax.swing.JButton;
import javax.swing.JPanel;

@Deprecated
public class ButtonColumn extends JPanel {

	final int MAX_BUTTONS = 20;
	int numButtons = 0;
	private JButton exitButton;
	private JButton orderButton;
	private GridLayout myLayout;
	ClientWindow cw;
	String dbPath;
	
	@Deprecated
	public ButtonColumn() throws SQLException {
		myLayout = new GridLayout(MAX_BUTTONS + 1,0);
		setLayout(myLayout);
		numButtons = 0;		
		
	}
	
	@Deprecated
	public void addButton (JButton buttonIn) {
		add(buttonIn,numButtons,0);
		numButtons++;
		createExitButton();
				
	}

	/**
	 * Gets the JPanel from CLientWindow and calls the function to add the button to the panel 
	 * @param panel
	 * @throws SQLException
	 */
	@Deprecated
	public ButtonColumn(ClientWindow panel, String dbPath) throws SQLException {
		this.dbPath = dbPath;
		myLayout = new GridLayout(MAX_BUTTONS + 1,0);
		setLayout(myLayout);
		numButtons = 0;
		this.cw = panel;
		createExitButton(); 
		createOrderButton();
		createCartsButton();
		createCustomerViewButton();
		createLinenButton();	
		addCustomerButton();
		addOutboundButton();
		addNewCartsButton();
		
	}
	
	/**
	 * creates "Exit" button which will close the UI
	 */
	@Deprecated
	public void createExitButton() {
    	exitButton = new JButton("Exit");		
    	exitButton.setBounds(180,200, 70, 30);	    
	    //Action listener methods is added to button1 to exit when the exit button is pressed.
    	exitButton.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
    	});
    	add(exitButton,MAX_BUTTONS,-1); //Add the exit button *specifically* to the bottom of the list.
	}
	
	/**
	 * "Orders" button will display all the orders in the database
	 * Will call OrderWindow and pass it to swapCentralPane function in ClientWIndow Class 
	 */
	@Deprecated
	public void createOrderButton() {
		orderButton = new JButton("Orders");
		orderButton.setBounds(180, 230, 70, 30);
		orderButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e)  {
				try {
					cw.setContentViewPanel(new OrderWindow(dbPath));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}		
		});
		add(orderButton,MAX_BUTTONS,0);
	}
	
	/**
	 * This function will create "Carts" button which will dispaly all the carts in the system
	 */
	@Deprecated
	public void createCartsButton() {
		orderButton = new JButton("Carts");
		orderButton.setBounds(180, 260, 70, 30);
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)  {
				try {
					cw.setContentViewPanel(new CartsWindow(dbPath));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(orderButton,MAX_BUTTONS,1);
	}
	
	/**
	 * This function will create "Customers" button which will display all customers in the system
	 */
	@Deprecated
	public void createCustomerViewButton() {
		orderButton = new JButton("Customers");
		orderButton.setBounds(180, 290, 70, 30);
		orderButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e)  {
				try {
					cw.setContentViewPanel(new CustomersWindow(dbPath));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(orderButton,MAX_BUTTONS,2);
	}
	
	/**
	 * This function will create "Linen" button which will display all the linen types used in the system
	 */
	@Deprecated
	public void createLinenButton() {
		orderButton = new JButton("Linen");
		orderButton.setBounds(180, 290, 70, 30);
		orderButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e)  {
				try {
					cw.setContentViewPanel(new LinenWindow(dbPath));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(orderButton,MAX_BUTTONS,3);
	}
	@Deprecated
	public void addCustomerButton() {
		orderButton = new JButton("Add Customer");
		orderButton.setBounds(180, 290, 70, 30);
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)  {
				new AddCustomer(dbPath);
			}
		});
		add(orderButton,MAX_BUTTONS,4);
	}
	@Deprecated
	public void addNewCartsButton() {
		orderButton = new JButton("Add Carts");
		orderButton.setBounds(180, 290, 70, 30);
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)  {
				new AddCarts(dbPath);
			}
		});
		add(orderButton,MAX_BUTTONS,5);
	}
	@Deprecated
	public void addOutboundButton() {
		orderButton = new JButton("Outbound");
		orderButton.setBounds(180, 290, 70, 30);
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)  {
				new AddOutbound(dbPath);
			}
		});
		add(orderButton,MAX_BUTTONS,4);
	}

	//needed so unit testing can confirm it exists and has been added.
	@Deprecated
	public JButton getExitButton() {
		return exitButton;
	}
}