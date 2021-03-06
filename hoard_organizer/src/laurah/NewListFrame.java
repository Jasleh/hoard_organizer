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
import javax.swing.border.Border;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class NewListFrame extends JInternalFrame implements ActionListener
{
	public static final int HEIGHT = 500;
	public static final int WIDTH = 700;
	
	private Connection conn;	// connection to DB
	
	// GUI components for user input
	private JTextField listName;
	
	private NewListColumn one;
	private NewListColumn two;
	private NewListColumn three;
	private NewListColumn four;
	private NewListColumn five;
	
	private JButton createButton;
	private JButton clearButton;
	
	private JPanel namePanel;
	private JPanel buttonPanel;
	
	private JLabel nameLabel;
	private JLabel columnLabel;
	
	private Border defaultTextBorder;
	
	private JDesktopPane desktop;
	
	public NewListFrame(Connection c, JDesktopPane d)
	{
		super("Create New List", false, true); // only close option enabled
		
		conn = c;
		desktop = d;
		
		// create GUI
		this.setSize(WIDTH, HEIGHT);
		
		// components
		namePanel = new JPanel();
		buttonPanel = new JPanel();
		nameLabel = new JLabel("List Name");
		columnLabel = new JLabel("List Columns");
		
		listName = new JTextField();
		one = new NewListColumn();
		two = new NewListColumn();
		three = new NewListColumn();
		four = new NewListColumn();
		five = new NewListColumn();
		createButton = new JButton("Create");
		clearButton = new JButton("Clear");
		
		defaultTextBorder = listName.getBorder();
		
		listName.setColumns(32);
		
		namePanel.add(nameLabel);
		namePanel.add(listName);
		
		createButton.setActionCommand("create");
		clearButton.setActionCommand("clear");
		
		createButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		buttonPanel.add(createButton);
		buttonPanel.add(clearButton);
		
		// layout
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		getContentPane().add(Box.createRigidArea(new Dimension(0,20)));
		getContentPane().add(namePanel);
		getContentPane().add(Box.createRigidArea(new Dimension(0,10)));
		getContentPane().add(columnLabel);
		getContentPane().add(Box.createRigidArea(new Dimension(0,5)));
		getContentPane().add(one);
		getContentPane().add(two);
		getContentPane().add(three);
		getContentPane().add(four);
		getContentPane().add(five);
		getContentPane().add(Box.createRigidArea(new Dimension(0,10)));
		getContentPane().add(buttonPanel);
	}

	public void actionPerformed(ActionEvent e) 
	{
		// respond to button pushes
		if ("create".equals(e.getActionCommand()))
		{
			if (createTable())
			{
				// table created, close frame and open viewer frame
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						ViewListFrame frame = new ViewListFrame(conn, listName.getText(), desktop);
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
		else if ("clear".equals(e.getActionCommand()))
		{
			clearValues();
		}
	}
	
	// creates the list in the DB
	// returns false if unable to create table
	// reasons given in dialogs
	private boolean createTable()
	{
		int count = 0; // number of attributes collected
		String ln; // list name
		String temp;
		Border textRed = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red), defaultTextBorder);
		Border red = BorderFactory.createLineBorder(Color.red);
		Border none = BorderFactory.createEmptyBorder();
		String createString = "";
		String createRefString = "";
		Statement s;
		PreparedStatement ps;
		
		// remove any red borders previously placed
		one.setBorder(none);
		listName.setBorder(defaultTextBorder);
		
		// check that there is a table name
		ln = getListName();
		
		if (ln == null)
		{
			// error, no list name
			// alert user, mark field, and return false
			
			JOptionPane.showMessageDialog(getContentPane(), 
					"A list name is needed", 
					"Warning", JOptionPane.INFORMATION_MESSAGE);
			
			listName.setBorder(textRed);
			
			return false;
		}
		
		// begin constructing the statements
		temp = "\"" + ln + " ref\"";
		createRefString = createRefString + "CREATE TABLE "
				+ HoardOrganizer.dbName + "." + temp + " "
				+ "(attribute_id INTEGER NOT NULL,"
				+ " attribute_name VARCHAR(64) NOT NULL,"
				+ " attribute_type VARCHAR(16) NOT NULL,"
				+ " PRIMARY KEY(attribute_id))";
		
		temp = "\"" + ln + "\"";
		
		createString = createString + "CREATE TABLE " 
				+ HoardOrganizer.dbName + "." + temp
				+ " (item_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, ";
		
		temp = one.getValues();
		
		if (!(temp == null))
		{
			createString = createString + temp + ", ";
			++count;
		}
		
		temp = two.getValues();
		
		if (!(temp == null))
		{
			createString = createString + temp + ", ";
			++count;
		}
		
		temp = three.getValues();
		
		if (!(temp == null))
		{
			createString = createString + temp + ", ";
			++count;
		}
		
		temp = four.getValues();
		
		if (!(temp == null))
		{
			createString = createString + temp + ", ";
			++count;
		}
		
		temp = five.getValues();
		
		if (!(temp == null))
		{
			createString = createString + temp + ", ";
			++count;
		}
		
		if (count == 0)
		{
			// error, no attributes
			// alert user, mark field, and return false
						
			JOptionPane.showMessageDialog(getContentPane(), 
					"At least one column is needed", 
					"Warning", JOptionPane.INFORMATION_MESSAGE);
						
			one.setBorder(red);
						
			return false;
		}
		
		createString = createString + " PRIMARY KEY(item_id))";
		
		// attempt to create the table
		try
		{
			s = conn.createStatement();
			s.execute(createString);
			s.close();
		}
		catch (SQLException sqle)
		{
			String theError = (sqle).getSQLState();
		    if (theError.equals("X0Y32"))   // Table already exists
		    {  
		    	JOptionPane.showMessageDialog(getContentPane(), 
						"A list of that name already exists", 
						"Warning", JOptionPane.INFORMATION_MESSAGE);
				
				listName.setBorder(textRed);
				
				return false;
		    }  
		    else 
		    { 
		        // unhandled SQL error, show message and shut down
		    	JOptionPane.showMessageDialog(null, 
						"An Error Occurred", 
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
		    }
		}
		
		// table created, add supporting tables and information to database
		try
		{
			// add list name to master list table
			temp = "INSERT INTO " + HoardOrganizer.dbName + "." +
					"Master_List(list_name) VALUES ('" + ln + "')";
			s = conn.createStatement();
			s.executeUpdate(temp);
			
			// create and fill the ref table
			s.execute(createRefString);
			
			temp = "\"" + ln + " ref\"";
			ps = conn.prepareStatement("insert into " + HoardOrganizer.dbName + "." 
					+ temp + "(attribute_id, attribute_name, attribute_type) values (?, ?, ?)");
			
			int i = 1;
			
			if (!(one.getText() == null))
			{
				ps.setInt(1, i);
				ps.setString(2, one.getText());
				ps.setString(3, one.getType());
				ps.executeUpdate();
				++i;
			}
			
			if (!(two.getText() == null))
			{
				ps.setInt(1, i);
				ps.setString(2, two.getText());
				ps.setString(3, two.getType());
				ps.executeUpdate();
				++i;
			}
			
			if (!(three.getText() == null))
			{
				ps.setInt(1, i);
				ps.setString(2, three.getText());
				ps.setString(3, three.getType());
				ps.executeUpdate();
				++i;
			}
			
			if (!(four.getText() == null))
			{
				ps.setInt(1, i);
				ps.setString(2, four.getText());
				ps.setString(3, four.getType());
				ps.executeUpdate();
				++i;
			}
			
			if (!(five.getText() == null))
			{
				ps.setInt(1, i);
				ps.setString(2, five.getText());
				ps.setString(3, five.getType());
				ps.executeUpdate();
				++i;
			}
		}
		catch (SQLException sqle)
		{
			// unhandled SQL error, show message and shut down
	    	JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// table created successfully
		
		return true;
	}
	
	// returns components to default values
	private void clearValues()
	{
		listName.setText("");
		one.clearValues();
		two.clearValues();
		three.clearValues();
		four.clearValues();
		five.clearValues();
		listName.setBorder(defaultTextBorder);
	}
	
	// returns the value of the listName text field
	// returns null if the text field is blank
	// doubles any "
	private String getListName()
	{
		String result = null;
		
		try
		{
			result = listName.getText();
			result = result.trim();
				
			if (result.equals(""))
			{
				return null;
			}
				
			result.replace("\"", "\"\"");
		}
		catch (NullPointerException e) {}
			
		return result;
	}
}
