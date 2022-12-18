package linen_app;

import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;


public class CartsWindow extends JPanel{
	private JPanel cartsPanel;
	public JTable table;
	private TitledBorder tBorder;
	private String title;
	
	public CartsWindow(String dbPath) throws SQLException {		
		cartsPanel = new JPanel();
		cartsPanel.setPreferredSize(new Dimension(600,400));
		this.setLayout(new FlowLayout());
		title = "All Carts View";
		tBorder = BorderFactory.createTitledBorder(tBorder, title, Color.TRANSLUCENT, 2);
		tBorder.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(tBorder);
		DBShell carts = new DBShell(dbPath);
		Vector<CartInfo> cartInfo = carts.getAllCartInfo();
		InfoVecTableGenerator<CartInfo> customertable = new InfoVecTableGenerator<CartInfo>(cartInfo, new CartInfo(1, "", 1, null ));
		table = customertable.getTable();			
		this.add(new JScrollPane(table));
		cartsPanel.setVisible(true);
	}
}	