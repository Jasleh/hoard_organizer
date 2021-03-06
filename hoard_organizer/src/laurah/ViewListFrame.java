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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class ViewListFrame extends JInternalFrame implements ActionListener, ListSelectionListener
{
	private Connection conn;	// connection to DB
	private String listName;
	
	private String getTableString;
	
	// components
	private JLabel idLabel;
	private JTextField idTextField;
	private JPanel idPanel;
	
	private ViewListColumn one;
	private ViewListColumn two;
	private ViewListColumn three;
	private ViewListColumn four;
	private ViewListColumn five;
	
	private JButton addChangeButton;
	private JButton clearDeleteButton;
	private JButton clearSelectionButton;
	private JButton deleteListButton;
	
	private JTable listTable;
	private ListTableModel tableModel;
	
	public ViewListFrame(Connection c, String n, JDesktopPane d)
	{
		super(n, false, true); // only close option enabled
		
		conn = c;
		listName = n;
		
		// set up table
		getTableString = "SELECT * FROM " + HoardOrganizer.dbName + ".\"" + listName + "\"";
		
		try
		{
			Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(getTableString);
			
			tableModel = new ListTableModel(rs);
			listTable = new JTable();
			listTable.setModel(tableModel);
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		listTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTable.setRowSelectionAllowed(true);
		listTable.setColumnSelectionAllowed(false);
		listTable.getSelectionModel().addListSelectionListener(this);
		
		// create GUI
		this.setSize(d.getSize());
		
		// set up other components
		idLabel = new JLabel("row ID");
		idTextField = new JTextField();
		idTextField.setColumns(10);
		idTextField.setEditable(false);
		idTextField.setMaximumSize(idTextField.getPreferredSize());
		
		idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.PAGE_AXIS));
		
		idPanel.add(idLabel);
		idPanel.add(idTextField);
		idPanel.setMaximumSize(idPanel.getPreferredSize());
		
		int col = (tableModel.getColumnCount()) - 1;
		int i = 1;
		String aName;
		String aType;
		
		try
		{
			PreparedStatement ps = conn.prepareStatement("SELECT attribute_type FROM " + HoardOrganizer.dbName + ".\"" + listName + " ref\" "
					+ "WHERE attribute_name = ?");
			ResultSet rs2;
		
			if (i <= col)
			{
				aName = tableModel.getColumnName(i);
				ps.setString(i, aName);
				rs2 = ps.executeQuery();
				rs2.next();
				aType = rs2.getString(1);
				
				one = new ViewListColumn(aName, aType);
				
				++i;
			}
			else
			{
				one = new ViewListColumn();
			}
		
			if (i <= col)
			{
				aName = tableModel.getColumnName(i);
				ps.setString(1, aName);
				rs2 = ps.executeQuery();
				rs2.next();
				aType = rs2.getString(1);
				
				two = new ViewListColumn(aName, aType);
				
				++i;
			}
			else
			{
				two = new ViewListColumn();
			}
			
			if (i <= col)
			{
				aName = tableModel.getColumnName(i);
				ps.setString(1, aName);
				rs2 = ps.executeQuery();
				rs2.next();
				aType = rs2.getString(1);
				
				three = new ViewListColumn(aName, aType);
				
				++i;
			}
			else
			{
				three = new ViewListColumn();
			}
		
			if (i <= col)
			{
				aName = tableModel.getColumnName(i);
				ps.setString(1, aName);
				rs2 = ps.executeQuery();
				rs2.next();
				aType = rs2.getString(1);
				
				four = new ViewListColumn(aName, aType);
				
				++i;
			}
			else
			{
				four = new ViewListColumn();
			}
		
			if (i <= col)
			{
				aName = tableModel.getColumnName(i);
				ps.setString(1, aName);
				rs2 = ps.executeQuery();
				rs2.next();
				aType = rs2.getString(1);
				
				five = new ViewListColumn(aName, aType);
				
				++i;
			}
			else
			{
				five = new ViewListColumn();
			}
			
			ps.close();
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		addChangeButton = new JButton("Add");
		addChangeButton.setActionCommand("add");
		addChangeButton.addActionListener(this);
		
		clearDeleteButton = new JButton("Clear");
		clearDeleteButton.setActionCommand("clear");
		clearDeleteButton.addActionListener(this);
		
		clearSelectionButton = new JButton("Clear Selection");
		clearSelectionButton.setActionCommand("clear selection");
		clearSelectionButton.addActionListener(this);
		clearSelectionButton.setVisible(false);
		clearSelectionButton.setEnabled(false);
		
		deleteListButton = new JButton("Delete List");
		deleteListButton.setActionCommand("delete list");
		deleteListButton.addActionListener(this);
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.PAGE_AXIS));
		
		getContentPane().add(Box.createRigidArea(new Dimension(0,50)));
		editPanel.add(idPanel);
		editPanel.add(one);
		editPanel.add(two);
		editPanel.add(three);
		editPanel.add(four);
		editPanel.add(five);
		editPanel.add(Box.createRigidArea(new Dimension(0,15)));
		editPanel.add(addChangeButton);
		editPanel.add(Box.createRigidArea(new Dimension(0,5)));
		editPanel.add(clearDeleteButton);
		editPanel.add(Box.createRigidArea(new Dimension(0,5)));
		editPanel.add(clearSelectionButton);
		editPanel.add(Box.createRigidArea(new Dimension(0,15)));
		editPanel.add(deleteListButton);
		
		JScrollPane editPane = new JScrollPane(editPanel);
		
		editPane.setMinimumSize(editPanel.getMinimumSize());
		
		JScrollPane tablePane = new JScrollPane(listTable);
		tablePane.setMinimumSize(new Dimension(100,100));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane, editPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(this.getWidth() - 300);
		
		getContentPane().add(splitPane);
	}
	
	// button press actions
	public void actionPerformed(ActionEvent e) 
	{
		// add
		if ("add".equals(e.getActionCommand()))
		{
			addRow();
		}
		else if ("clear".equals(e.getActionCommand()))
		{
			clearValues();
		}
		else if ("change".equals(e.getActionCommand()))
		{
			changeRow();
		}
		else if ("delete".equals(e.getActionCommand()))
		{
			deleteRow();
		}
		else if ("clear selection".equals(e.getActionCommand()))
		{
			clearSelection();
		}
		else if ("delete list".equals(e.getActionCommand()))
		{
			deleteList();
		}
	}

	// row selected actions
	public void valueChanged(ListSelectionEvent e) 
	{
		// return if nothing selected
		if (listTable.getSelectedRow() == -1)
		{
			return;
		}
		
		// fill in edit fields with the selected row's values
		int row = listTable.convertRowIndexToModel(listTable.getSelectedRow());
		int cols = tableModel.getColumnCount();
		int i = 1;
		
		idTextField.setText((String) tableModel.getValueAt(row, 0));
		
		if (i < cols)
		{
			one.setValue((String) tableModel.getValueAt(row, i));
			++i;
		}
		if (i < cols)
		{
			two.setValue((String) tableModel.getValueAt(row, i));
			++i;
		}
		if (i < cols)
		{
			three.setValue((String) tableModel.getValueAt(row, i));
			++i;
		}
		if (i < cols)
		{
			four.setValue((String) tableModel.getValueAt(row, i));
			++i;
		}
		if (i < cols)
		{
			five.setValue((String) tableModel.getValueAt(row, i));
			++i;
		}
		
		// change buttons
		addChangeButton.setText("Change Row");
		addChangeButton.setActionCommand("change");
		
		clearDeleteButton.setText("Delete Row");
		clearDeleteButton.setActionCommand("delete");
		
		clearSelectionButton.setVisible(true);
		clearSelectionButton.setEnabled(true);
	}
	
	private void addRow()
	{
		// build insert string
		String insertString = "INSERT INTO " + HoardOrganizer.dbName + ".\"" + listName + "\"(";
		String valuesString = "VALUES (";
		int i = 0;
		String temp;
		
		temp = one.getValue();
		
		if (temp != null)
		{
			insertString = insertString + "\"" + one.getColName() + "\"";
			
			if (one.isTextField())
			{
				valuesString = valuesString + "'" + temp + "'";
			}
			else
			{
				valuesString = valuesString + temp;
			}
			
			++i;
		}
		
		temp = two.getValue();
		
		if (temp != null)
		{
			if (i > 0)
			{
				insertString = insertString + ", ";
				valuesString = valuesString + ", ";
			}
			
			insertString = insertString + "\"" + two.getColName() + "\"";
			
			if (two.isTextField())
			{
				valuesString = valuesString + "'" + temp + "'";
			}
			else
			{
				valuesString = valuesString + temp;
			}
			
			++i;
		}
		
		temp = three.getValue();
		
		if (temp != null)
		{
			if (i > 0)
			{
				insertString = insertString + ", ";
				valuesString = valuesString + ", ";
			}
			
			insertString = insertString + "\"" + three.getColName() + "\"";
			
			if (three.isTextField())
			{
				valuesString = valuesString + "'" + temp + "'";
			}
			else
			{
				valuesString = valuesString + temp;
			}
			
			++i;
		}
		
		temp = four.getValue();
		
		if (temp != null)
		{
			if (i > 0)
			{
				insertString = insertString + ", ";
				valuesString = valuesString + ", ";
			}
			
			insertString = insertString + "\"" + four.getColName() + "\"";
			
			if (four.isTextField())
			{
				valuesString = valuesString + "'" + temp + "'";
			}
			else
			{
				valuesString = valuesString + temp;
			}
			
			++i;
		}
		
		temp = five.getValue();
		
		if (temp != null)
		{
			if (i > 0)
			{
				insertString = insertString + ", ";
				valuesString = valuesString + ", ";
			}
			
			insertString = insertString + "\"" + five.getColName() + "\"";
			
			if (five.isTextField())
			{
				valuesString = valuesString + "'" + temp + "'";
			}
			else
			{
				valuesString = valuesString + temp;
			}
			
			++i;
		}
		
		if (i == 0)
		{
			JOptionPane.showMessageDialog(getContentPane(), 
					"At least one value is needed", 
					"Warning", JOptionPane.INFORMATION_MESSAGE);
			
			return;
		}
		
		insertString = insertString + ")";
		valuesString = valuesString + ")";
		insertString = insertString + " " + valuesString;
		
		// insert into DB
		try
		{
			Statement s = conn.createStatement();
			s.executeUpdate(insertString);
			
			// update table
			s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(getTableString);
			
			tableModel = new ListTableModel(rs);
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					listTable.setModel(tableModel);
					listTable.invalidate();
				}
			});
			
			clearValues();
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void clearValues()
	{
		idTextField.setText("");
		one.clearValue();
		two.clearValue();
		three.clearValue();
		four.clearValue();
		five.clearValue();
	}
	
	private void changeRow()
	{
		String updateString = "UPDATE " + HoardOrganizer.dbName + ".\"" + listName + "\" SET ";
		String temp;
		int i = 1;
		int cols = tableModel.getColumnCount();
		
		if (i < cols)
		{
			updateString = updateString + "\"" + one.getColName() + "\" = ";
			
			temp = one.getValue();
			
			if (temp == null)
			{
				updateString = updateString + "NULL";
			}
			else
			{
				if (one.isTextField())
				{
					updateString = updateString + "'" + one.getValue() + "'";
				}
				else
				{
					updateString = updateString + one.getValue();
				}
			}
			++i;
		}
		if (i < cols)
		{
			updateString = updateString + ", \"" + two.getColName() + "\" = ";
			
			temp = two.getValue();
			
			if (temp == null)
			{
				updateString = updateString + "NULL";
			}
			else
			{
				if (two.isTextField())
				{
					updateString = updateString + "'" + two.getValue() + "'";
				}
				else
				{
					updateString = updateString + two.getValue();
				}
			}
			++i;
		}
		if (i < cols)
		{
			updateString = updateString + ", \"" + three.getColName() + "\" = ";
			
			temp = three.getValue();
			
			if (temp == null)
			{
				updateString = updateString + "NULL";
			}
			else
			{
				if (three.isTextField())
				{
					updateString = updateString + "'" + three.getValue() + "'";
				}
				else
				{
					updateString = updateString + three.getValue();
				}
			}
			++i;
		}
		if (i < cols)
		{
			updateString = updateString + ", \"" + four.getColName() + "\" = ";
			
			temp = four.getValue();
			
			if (temp == null)
			{
				updateString = updateString + "NULL";
			}
			else
			{
				if (four.isTextField())
				{
					updateString = updateString + "'" + four.getValue() + "'";
				}
				else
				{
					updateString = updateString + four.getValue();
				}
			}
			++i;
		}
		if (i < cols)
		{
			updateString = updateString + ", \"" + five.getColName() + "\" = ";
			
			temp = five.getValue();
			
			if (temp == null)
			{
				updateString = updateString + "NULL";
			}
			else
			{
				if (five.isTextField())
				{
					updateString = updateString + "'" + five.getValue() + "'";
				}
				else
				{
					updateString = updateString + five.getValue();
				}
			}
			++i;
		}
		
		updateString = updateString + " WHERE item_id = " + Integer.parseInt(idTextField.getText());
		
		try
		{
			Statement s = conn.createStatement();
			s.executeUpdate(updateString);
			
			clearSelection();
			
			// update table
			s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(getTableString);
			
			tableModel = new ListTableModel(rs);
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					listTable.setModel(tableModel);
					listTable.invalidate();
				}
			});
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void deleteRow()
	{
		String deleteString = "DELETE FROM " + HoardOrganizer.dbName + ".\"" + listName
				+ "\" WHERE item_id = " + Integer.parseInt(idTextField.getText());
		
		try
		{
			Statement s = conn.createStatement();
			s.executeUpdate(deleteString);
			
			clearSelection();
			
			// update table
			s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(getTableString);
			
			tableModel = new ListTableModel(rs);
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					listTable.setModel(tableModel);
					listTable.invalidate();
				}
			});
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void clearSelection()
	{
		clearValues();
		listTable.clearSelection();
		
		addChangeButton.setText("Add");
		addChangeButton.setActionCommand("add");
		
		clearDeleteButton.setText("Clear");
		clearDeleteButton.setActionCommand("clear");
		
		clearSelectionButton.setVisible(false);
		clearSelectionButton.setEnabled(false);
	}
	
	private void deleteList()
	{
		// confirmation dialog
		int n = JOptionPane.showConfirmDialog(null,
			    "Are you sure you want to delete this list?",
			    "Please Confirm",
			    JOptionPane.YES_NO_OPTION);
		
		if (n == JOptionPane.NO_OPTION)
		{
			return;
		}
		
		String deleteString = "DROP TABLE " + HoardOrganizer.dbName + ".\"" + listName + "\"";
		
		try 
		{
			tableModel.getListResultSet().close();
			
			// delete table
			Statement s = conn.createStatement();
			s.executeUpdate(deleteString);
			
			// remove supporting tables/entries
			deleteString = "DROP TABLE " + HoardOrganizer.dbName + ".\"" + listName + " ref\"";
			s.executeUpdate(deleteString);
			
			deleteString = "DELETE FROM " + HoardOrganizer.dbName + ".Master_List WHERE list_name = '" + listName + "'";
			s.executeUpdate(deleteString);
		} 
		catch (SQLException sqle) 
		{
			JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		this.dispose();
	}

}
