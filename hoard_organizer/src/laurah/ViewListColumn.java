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
import java.awt.Component;
import java.text.NumberFormat;

public class ViewListColumn extends JPanel
{
	private String colName;
	private String colType;
	private int maxStrLength;
	
	private JLabel label;
	private JTextField textType;
	private JFormattedTextField integerType;
	private JFormattedTextField decimalType;
	
	// makes one with nothing enabled or visible
	// generally useless, but needed for current implementation
	// will be removed when implementation is improved
	public ViewListColumn() 
	{
		label = new JLabel("");
		label.setVisible(false);
		
		colName = "";
		colType = "";
		maxStrLength = 0;
		
		setUpComponents();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(label);
		add(textType);
		add(integerType);
		add(decimalType);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	// type should come from the list's "ref" table
	public ViewListColumn(String name, String type)
	{
		String temp = "";
		String temp2;
		
		colName = name;
		colType = type;
		maxStrLength = 0;
		
		setUpComponents();
		
		temp = temp + colName + " (";
		
		if (colType.contains("VARCHAR"))
		{
			temp = temp + "Text";
			textType.setVisible(true);
			textType.setEnabled(true);
			
			temp2 = colType.substring(colType.indexOf('(') + 1, colType.indexOf(')'));
			maxStrLength = Integer.parseInt(temp2);
		}
		else if (colType.equals("INTEGER"))
		{
			temp = temp + "Integer";
			integerType.setVisible(true);
			integerType.setEnabled(true);
		}
		else if (colType.equals("DOUBLE"))
		{
			temp = temp + "Decimal";
			decimalType.setVisible(true);
			decimalType.setEnabled(true);
		}
		else {} // should not be reached
		
		temp = temp + ")";
		
		label = new JLabel(temp);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(label);
		add(textType);
		add(integerType);
		add(decimalType);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.setMaximumSize(getPreferredSize());
	}
	
	// does not include the label
	private void setUpComponents()
	{
		textType = new JTextField();
		textType.setColumns(20);
		textType.setVisible(false);
		textType.setEnabled(false);
		
		integerType = new JFormattedTextField(NumberFormat.getIntegerInstance());
		integerType.setColumns(20);
		integerType.setVisible(false);
		integerType.setEnabled(false);
		
		decimalType = new JFormattedTextField(NumberFormat.getNumberInstance());
		decimalType.setColumns(20);
		decimalType.setVisible(false);
		decimalType.setEnabled(false);
	}
	
	public String getValue()
	{
		String value = null;
		
		try
		{
			if (textType.isEnabled())
			{
				value = textType.getText();
				value.trim();
				
				if (value.length() > maxStrLength)
				{
					value = value.substring(0, maxStrLength);
				}
				
				// escape ' for Derby
				value = value.replace("'", "''");
				
				if (value.equals(""))
				{
					value = null;
				}
			}
			else if (integerType.isEnabled())
			{
				value = integerType.getText();
				value.trim();
				
				if (value.equals(""))
				{
					value = null;
				}
			}
			else if (decimalType.isEnabled())
			{
				value = decimalType.getText();
				value.trim();
				
				if (value.equals(""))
				{
					value = null;
				}
			}
		
			return value;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
	
	public String getColName()
	{
		return colName;
	}
	
	public boolean isTextField()
	{
		return textType.isEnabled();
	}
	
	public void clearValue()
	{
		if (textType.isEnabled())
		{
			textType.setText("");
		}
		else if (integerType.isEnabled())
		{
			integerType.setValue(null);
		}
		else if (decimalType.isEnabled())
		{
			decimalType.setValue(null);
		}
	}
	
	public void setValue(String v)
	{
		if (textType.isEnabled())
		{
			if (v == null)
			{
				textType.setText("");
			}
			else
			{
				textType.setText(v);
			}
		}
		else if (integerType.isEnabled())
		{
			if (v == null)
			{
				integerType.setValue(null);
			}
			else
			{
				integerType.setValue(Integer.parseInt(v));
			}
		}
		else if (decimalType.isEnabled())
		{
			if (v == null)
			{
				decimalType.setValue(null);
			}
			else
			{
				decimalType.setValue(Double.parseDouble(v));
			}
		}
	}
}
