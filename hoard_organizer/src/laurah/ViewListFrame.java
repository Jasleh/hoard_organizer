package laurah;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class ViewListFrame extends JInternalFrame implements ActionListener
{
	public static final int HEIGHT = 500;
	public static final int WIDTH = 700;
	
	private Connection conn;	// connection to DB
	private String listName;
	
	public ViewListFrame(Connection c, String n)
	{
		super(n, false, true); // only close option enabled
		
		conn = c;
		listName = n;
		
		// create GUI
		this.setSize(WIDTH, HEIGHT);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
	}

}
