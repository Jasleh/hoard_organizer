package laurah;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class OpenListFrame extends JInternalFrame implements ActionListener
{
	public static final int HEIGHT = 300;
	public static final int WIDTH = 400;
	
	private JComboBox listComboBox;
	private JButton openButton;
	private JPanel panel;
	
	private Connection conn;
	private JDesktopPane desktop;
	
	public OpenListFrame(Connection c, JDesktopPane d)
	{
		super("Open a List", false, true); // only close option enabled
		
		conn = c;
		desktop = d;
		String [] lists = new String[1];	// initialized so compiler doesn't complain
		Statement s = null;
		ResultSet rs = null;
		String query;
		int i = 0;
		boolean listsExist = true;
		
		// get list names from DB
		try
		{
			query = "SELECT COUNT(*) FROM " + HoardOrganizer.dbName + ".Master_List";
			
			s = conn.createStatement();
			rs = s.executeQuery(query);
			
			if (rs.next())
			{
				i = rs.getInt(1);
			}
			
			if (i == 0)
			{
				// no lists currently exist - need to make one first
				listsExist = false;
			}
			else
			{
				lists = new String[i];
				i = 0;
				
				query = "SELECT list_name FROM " + HoardOrganizer.dbName + ".Master_List "
						+ "ORDER BY list_name";
				
				rs = s.executeQuery(query);
				
				while (rs.next() && i < lists.length)
				{
					lists[i] = rs.getString(1);
					++i;
				}
				
				rs.close();
				s.close();
			}
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// create GUI
		
		this.setSize(WIDTH, HEIGHT);
				
		// components

		openButton = new JButton("Open");
		panel = new JPanel();
	
		if (listsExist)
		{
			listComboBox = new JComboBox(lists);
		}
		else
		{
			listComboBox = new JComboBox();
			openButton.setEnabled(false);
		}
		
		openButton.addActionListener(this);
		
		listComboBox.setSelectedIndex(-1);
		
		panel.add(listComboBox);
		
		// layout
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		getContentPane().add(Box.createRigidArea(new Dimension(0,50)));
		getContentPane().add(panel);
		getContentPane().add(Box.createRigidArea(new Dimension(0,20)));
		getContentPane().add(openButton);
		getContentPane().add(Box.createRigidArea(new Dimension(0,75)));
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (listComboBox.getSelectedIndex() == -1)
		{
			// no list selected
			JOptionPane.showMessageDialog(getContentPane(), 
					"Please select a list.", 
					"No List Selected", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			// open selected list
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					ViewListFrame frame = new ViewListFrame(conn, (String) listComboBox.getSelectedItem());
					frame.setLocation((desktop.getWidth() / 2) - (ViewListFrame.WIDTH / 2),
							(desktop.getHeight() / 2) - (ViewListFrame.HEIGHT /2));
					frame.setVisible(true);
					desktop.add(frame);
					try
					{
						frame.setSelected(true);
					} catch(java.beans.PropertyVetoException e) {}
				}
			});
			this.dispose();
		}
	}
}
