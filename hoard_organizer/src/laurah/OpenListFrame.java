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
		openButton = new JButton("Open");
		panel = new JPanel();
		listComboBox = new JComboBox();
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
				
			query = "SELECT list_name FROM " + HoardOrganizer.dbName + ".Master_List "
					+ "ORDER BY list_name";
				
			rs = s.executeQuery(query);
				
			while (rs.next())
			{
				listComboBox.addItem(rs.getString(1));
				++i;
			}
			
			if (i == 0)
			{
				listsExist = false;
			}
				
			rs.close();
			s.close();
		
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
	
		if (!listsExist)
		{
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
					ViewListFrame frame = new ViewListFrame(conn, (String) listComboBox.getSelectedItem(), desktop);
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
