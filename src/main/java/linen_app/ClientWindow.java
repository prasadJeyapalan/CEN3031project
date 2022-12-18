package linen_app;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

/**
 * JFrame window for the majority of the application
 */
public class ClientWindow extends JFrame {
	private static ClientWindow mainWindow;
	private final int WINDOW_WIDTH = 1280;
	private final int WINDOW_HEIGHT = 720;
	private JPanel currentView;
	private String dbPath;

	public ClientWindow(String dbPath) {
		if (mainWindow == null) {
			mainWindow = this;
		}
		else {
			System.out.println("Attemtped to create another ClientWindow.");
			return;
		}

		this.dbPath = dbPath;
		setTitle("Linen Tracking App");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setLocationRelativeTo(null);	    	   
		getContentPane().setLayout(new BorderLayout());
		setJMenuBar(createMenuBar());
		currentView = new HomeScreen();	//used for a workaround with removing currentView
		setContentViewPanel(currentView);
		setVisible(true);
	}

	/**
	 * Sets whichever panel of content desired to be displayed
	 * @param panel e.g new OrderWindow()
	 */
	public void setContentViewPanel(JPanel panel) {
		getContentPane().remove(currentView);
		getContentPane().add(panel, BorderLayout.CENTER);	//add the provided panel
     	getContentPane().revalidate();
		getContentPane().repaint();
		currentView = panel;								//update the reference to the center panel    	
    	getContentPane().setVisible(true);    	 	
	}

	public JPanel getCurrentView() {
		return currentView;
	}

	/**
     * @return The current JPanel in the center of the UI
     */
	public static ClientWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Creates the menu bar for switching to other windows<p>
	 * Replaces ButtonColumn.java in functionality and placed within ClientWindow for infinite loop and datarace reasons
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		//creating a new order
		JButton newOrder = new JButton("Create New Order");
		newOrder.addActionListener(e -> {
			try {
				setContentViewPanel(new CreateOrder(dbPath));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		newOrder.setFocusable(false);
		menuBar.add(newOrder);

		//outbound clean carts
		JButton outbound = new JButton("Outbound Cart");
		outbound.addActionListener(e -> {
			new AddOutbound(dbPath);
		});
		outbound.setFocusable(false);
		menuBar.add(outbound);

		//viewing tables
		JMenu viewLists = new JMenu("View Lists");
		JMenuItem viewOrders = new JMenuItem("Orders");
		viewOrders.addActionListener(e -> {
			try {
				setContentViewPanel(new OrderWindow(dbPath));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		viewLists.add(viewOrders);
		JMenuItem viewCarts = new JMenuItem("Carts");
		viewCarts.addActionListener(e -> {
			try {
				setContentViewPanel(new CartsWindow(dbPath));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		viewLists.add(viewCarts);
		JMenuItem viewCustomers = new JMenuItem("Customers");
		viewCustomers.addActionListener(e -> {
			try {
				setContentViewPanel(new CustomersWindow(dbPath));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		viewLists.add(viewCustomers);
		JMenuItem viewLinen = new JMenuItem("Linens");
		viewLinen.addActionListener(e -> {
			try {
				setContentViewPanel(new LinenWindow(dbPath));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		viewLists.add(viewLinen);
		//outbound cart history
		JMenuItem viewOutbound = new JMenuItem("Outbound Carts");
		viewOutbound.addActionListener(e -> {
			try {
				setContentViewPanel(new OutboundCartsWindow(dbPath));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		viewLists.add(viewOutbound);
		menuBar.add(viewLists);

		//adding simple items like customers and carts
		JMenu addItems = new JMenu("Add...");
		JMenuItem addCustomer = new JMenuItem("Customer");
		addCustomer.addActionListener(e -> {
			new AddCustomer(dbPath);
		});
		addItems.add(addCustomer);
		JMenuItem addCart = new JMenuItem("Cart");
		addCart.addActionListener(e -> {
			new AddCarts(dbPath);
		});
		addItems.add(addCart);
		menuBar.add(addItems);

		menuBar.add(new JMenuItem()); //dummy item as a spacer
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		exitButton.setFocusable(false);
		menuBar.add(exitButton);
		return menuBar;
	}

	public void close() {
		setVisible(false);
		mainWindow = null;
		dispose();
	}
}