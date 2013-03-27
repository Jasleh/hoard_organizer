package laurah;

import javax.swing.JInternalFrame;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class NewListFrame extends JInternalFrame
{
	public static final int HEIGHT = 500;
	public static final int WIDTH = 700;
	
	public NewListFrame(Connection conn)
	{
		super("Create New List", false, true); // only close option enabled
		
		// create GUI
		
		
		this.setSize(WIDTH, HEIGHT);
	}
}
