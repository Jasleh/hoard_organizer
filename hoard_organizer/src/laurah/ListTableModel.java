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

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class ListTableModel extends AbstractTableModel 
{
	private ResultSet rs;
	private ResultSetMetaData metadata;
	private int numcols, numrows;
	
	// result set must be scrollable
	public ListTableModel(ResultSet r) throws SQLException
	{
		rs = r;
		
		metadata = rs.getMetaData();
	    numcols = metadata.getColumnCount();
	    
	    rs.beforeFirst();
	    numrows = 0;
	    while (rs.next()) {
	        ++numrows;
	    }
	    rs.beforeFirst();
	}
	
	public int getColumnCount() 
	{
		return numcols;
	}

	public int getRowCount() 
	{
		return numrows;
	}

	public Object getValueAt(int row, int col) 
	{
		try {
		      rs.absolute(row + 1);
		      Object o = rs.getObject(col + 1);
		      if (o == null)
		        return null;
		      else
		        return o.toString();
		    } catch (SQLException e) {
		    	JOptionPane.showMessageDialog(null, 
						"An Error Occurred", 
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
				return null;
		    }
	}
	
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	public ResultSet getListResultSet() 
	{
	    return rs;
	}
	
	public String getColumnName(int column) 
	{
	    try 
	    {
	      return this.metadata.getColumnLabel(column + 1);
	    } 
	    catch (SQLException e) 
	    {
	    	JOptionPane.showMessageDialog(null, 
					"An Error Occurred", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return null;
	    }
	    
	  }
}
