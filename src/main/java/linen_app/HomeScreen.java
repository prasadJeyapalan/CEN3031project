package linen_app;


import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;

public class HomeScreen extends JPanel {
	private TitledBorder lineBorder;
	private String name;
	private int borderHeight;
	private JLabel welcomeLabel;
	
	public HomeScreen() {
		final int width = 600;    
	    final int height = 400; 
	    this.borderHeight = 2;
		Dimension panelDimension = new Dimension(width, height);
		this.setPreferredSize(panelDimension);
		this.setLayout(new FlowLayout());
		this.welcomeLabel = new JLabel("Welcome User");
		
		//BorderFactory is imported in order to create the transparent border around the panel as well as include a title for the panel. 
		this.name = "Linen Distribution System";
		this.lineBorder = BorderFactory.createTitledBorder(lineBorder, name, Color.TRANSLUCENT, borderHeight);
		lineBorder.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(lineBorder);
		this.add(welcomeLabel);
		
	}
	
	
	public JLabel getWelcomeLabel() {
		return welcomeLabel;
	}


	public void setWelcomeLabel(JLabel welcomeLabel) {
		this.welcomeLabel = welcomeLabel;
	}


	//Getters and setters
	public Border getLineBorder() {
		return lineBorder;
	}


	public void setLineBorder(TitledBorder lineBorder) {
		this.lineBorder = lineBorder;
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getBorderHeight() {
		return borderHeight;
	}


	public void setBorderHeight(int borderHeight) {
		this.borderHeight = borderHeight;
	}


}