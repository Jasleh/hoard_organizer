/*
 *  Hoard Organizer - a program to keep track of stuff
 *  Copyright (C) 2012  Laura Herburger
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package laurah;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.sql.*;

public class HoardOrganizer extends JFrame implements ActionListener
{
	private JDesktopPane desktop;
	private int width;
	private int height;
	private static final int INSET = 50;
	private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String dbName="dataDB";
	private static Connection conn;

	public HoardOrganizer()
	{
		super("Hoard Organizer");
		
		// size the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.width - (INSET * 2);
		height = screenSize.height - (INSET * 2);
		setBounds(INSET, INSET, width, height);
		setResizable(false);
		
		// set up GUI
		desktop = new JDesktopPane();
		setContentPane(desktop);
		setJMenuBar(createMenuBar());
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setBackground(Color.lightGray);
		
		// set up window listener
		addWindowListener(new WindowAdapter()
	      {
	         public void windowClosing(WindowEvent e)
	         {
	           quit();
	         }
	      });
	}
	
	private JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		// set up menu
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		
		// set up first menu item
		JMenuItem menuItem = new JMenuItem("New List");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("new");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		// set up second menu item
		menuItem = new JMenuItem("Open List");
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("open");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		// set up third menu item
		menuItem = new JMenuItem("Quit");
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		return menuBar;
	}
	
	// for menu selections
	public void actionPerformed(ActionEvent e)
	{
		if ("new".equals(e.getActionCommand()))
		{
			newList();
		}
		else if ("open".equals(e.getActionCommand()))
		{
			openList();
		}
		else // "quit"
		{
			// exit program
			quit();
		}
	}
	
	private void newList()
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				NewListFrame frame = new NewListFrame(conn, desktop);
				frame.setLocation((width / 2) - (NewListFrame.WIDTH / 2), (height / 2) - (NewListFrame.HEIGHT /2));
				frame.setVisible(true);
				desktop.add(frame);
				try
				{
					frame.setSelected(true);
				} catch(java.beans.PropertyVetoException e) {}
			}
		});
	}
	
	private void openList()
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				OpenListFrame frame = new OpenListFrame(conn, desktop);
				frame.setLocation((width / 2) - (OpenListFrame.WIDTH / 2), (height / 2) - (OpenListFrame.HEIGHT /2));
				frame.setVisible(true);
				desktop.add(frame);
				try
				{
					frame.setSelected(true);
				} catch(java.beans.PropertyVetoException e) {}
			}
		});
	}
	
	private void quit()
	{
		/*
		JOptionPane.showMessageDialog(null, 
				"quit function used to exit", 
				"Test", JOptionPane.INFORMATION_MESSAGE);
		*/
		
		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) 
		{
            boolean gotSQLExc = false;
            try 
            {
               DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se)  
            {	
               if ( se.getSQLState().equals("XJ015") ) 
               {		
                  gotSQLExc = true;
               }
            }
            if (!gotSQLExc) 
            {
            	JOptionPane.showMessageDialog(null, 
    					"Error: Database did not shut down normally", 
    					"Error", JOptionPane.ERROR_MESSAGE);
            }
         }
		System.exit(0);
	}
	
	// set up GUI - use from event-dispatching thread
	private static void createAndShowGUI()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		HoardOrganizer frame = new HoardOrganizer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
		conn = null;
		Statement s;
		String createString = "CREATE TABLE " + dbName +".Master_List  "
		        + "(list_id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
		        + " list_name VARCHAR(64) NOT NULL, " 
		        + " PRIMARY KEY(list_id))";
		
		// start the derby engine/driver
		try
		{
			Class.forName(driver); 
	    } 
		catch(java.lang.ClassNotFoundException e)
	    {
	        // class path not set properly
			JOptionPane.showMessageDialog(null, 
					"Error: Please check your CLASSPATH variable", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
	    }
		
		// connect to DB and set up Master_List table if it does not exist
		try
		{
			conn = DriverManager.getConnection(connectionURL);
			s = conn.createStatement();
			
			try
			{
				s.execute(createString);
			}
			catch (SQLException sqle)
			{
				String theError = (sqle).getSQLState();
			    if (theError.equals("X0Y32"))   // Table already exists
			    {  
			    	// do nothing
			    }  
			    else 
			    { 
			       // unhandled SQL error
			       throw sqle; 
			    }
			}
		}
		catch (Throwable e)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

}
