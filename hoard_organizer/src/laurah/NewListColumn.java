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

public class NewListColumn extends JPanel implements ActionListener
{
	private JTextField name;
	private JComboBox type;
	private JComboBox size;
	
	public NewListColumn()
	{
		String[] types = {"Text", "Integer", "Decimal"};
		String[] sizes = {"8", "16", "32", "64"};
		name = new JTextField();
		type = new JComboBox(types);
		size = new JComboBox(sizes);
		
		name.setColumns(32);
		type.setSelectedIndex(0);
		size.setSelectedIndex(2);
		
		type.addActionListener(this);
		
		this.add(name);
		this.add(type);
		this.add(size);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		// hide the size combo box if text is not selected
		String selected = (String) type.getSelectedItem();
		if (selected.equals("Text"))
		{
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					size.setEnabled(true);
				}
			});
		}
		else
		{
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					size.setEnabled(false);
				}
			});
		}
		
	}
	
	// returns null if the text field is blank
	// otherwise returns a string suitable for adding to
	// a CREATE TABLE statement using delimited identifiers
	// for example: "[cName]" VARCHAR (32)
	public String getValues()
	{
		String result = "";
		String cName = getText();
		String cType = (String) type.getSelectedItem();
		String cSize = (String) size.getSelectedItem();
		
		if (cName == null)
		{
			return null;
		}
		
		result = result + "\"" + cName + "\" ";
		
		if (cType.equals("Text"))
		{
			result = result + "VARCHAR (" + cSize + ")";
		}
		else if (cType.equals("Integer"))
		{
			result = result + "INTEGER";
		}
		else if (cType.equals("Decimal"))
		{
			result = result + "DOUBLE";
		}
		else // this should not be reached
		{
			return null;
		}
		
		return result;
	}
	
	// returns the value of the text field
	// returns null if the text field is blank
	// doubles any "
	public String getText()
	{
		String result = null;
		
		try
		{
			result = name.getText();
			result = result.trim();
			
			if (result.equals(""))
			{
				return null;
			}
			
			if (result.length() > 60)
			{
				result = result.substring(0, 59);
			}
			
			// escape " for Derby
			result = result.replace("\"", "\"\"");
		}
		catch (NullPointerException e) {}
		
		return result;
	}
	
	public String getType()
	{
		String result = "";
		String cType = (String) type.getSelectedItem();
		String cSize = (String) size.getSelectedItem();
		
		if (getText() == null)
		{
			return null;
		}
		else
		{
			if (cType.equals("Text"))
			{
				result = result + "VARCHAR (" + cSize + ")";
			}
			else if (cType.equals("Integer"))
			{
				result = result + "INTEGER";
			}
			else if (cType.equals("Decimal"))
			{
				result = result + "DOUBLE";
			}
			else // this should not be reached
			{
				return null;
			}
		}
		return result;
	}
	
	// returns components to default values
	public void clearValues()
	{
		name.setText("");
		type.setSelectedIndex(0);
		size.setSelectedIndex(2);
		size.setEnabled(true);
	}
}
