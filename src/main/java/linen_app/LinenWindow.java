package linen_app;

import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import linen_app.message_classes.*;

/**
 * @author Prasad Jeyapalan<p>
 * This class will generate a JPanel which will have a JTable to display the linen type and weight 
 * @version 1.0<p>
 * FileName: LinenWindow.java
 */

public class LinenWindow extends JPanel{
	private JPanel linenPanel;
	public JTable table;
	private TitledBorder tBorder;
	private String title;
		
	/**
	 * The default construction will create a JPanel
	 * Add border and Title to the panel
	 * Will connect to the database through the DBShell and access getAllLinenInfo function
	 * It will create a vector of each record in the database
	 * And pass it to the InfoVecTableGenerator class to produce a JTable and add it to the panel	
	 * @throws SQLException
	 */
	public LinenWindow(String dbPath) throws SQLException{	
		linenPanel = new JPanel();
		linenPanel.setPreferredSize(new Dimension(600,400));	
		this.setLayout(new FlowLayout());
		
		title = "All Linen View";
		tBorder = BorderFactory.createTitledBorder(tBorder, title, Color.TRANSLUCENT, 2);
		tBorder.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(tBorder);
		
		DBShell linen = new DBShell(dbPath);
		Vector<LinenInfo> linenInfo = linen.getAllLinenInfo();
		InfoVecTableGenerator<LinenInfo> linentable = new InfoVecTableGenerator<LinenInfo>(linenInfo, new LinenInfo( "", 0.0));
		table = linentable.getTable();

		this.add(new JScrollPane(table));
		linenPanel.setVisible(true);
	}
}
		




