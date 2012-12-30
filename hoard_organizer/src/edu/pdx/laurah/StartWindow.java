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

package edu.pdx.laurah;

import javax.swing.*;
import java.awt.*;

public class StartWindow extends JFrame 
{
	private JButton new_list;
	private JButton open_list;
	int f_height;
	int f_width;
	int button_height;
	int button_width;
	
	public StartWindow()
	{
		button_height = 100;
		button_width = 200;
		f_height = (int) ((button_height * 2.5) + (button_width/2));
		f_width = (int) (button_width * 1.5);
		
		new_list = new JButton("Create a New List");
		open_list = new JButton("Open a List");
		
		setLayout(null);
		
		this.add(new_list);
		this.add(open_list);
		this.setSize(f_width, f_height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Hoard Organizer");
		this.setLocationRelativeTo(null);
		
		new_list.setBounds((f_width/2)-(button_width/2), (f_width/2)-(button_width/2), button_width, button_height);
		open_list.setBounds((f_width/2)-(button_width/2), (int) (((f_width/2)-(button_width/2))+(button_height*1.25)), button_width, button_height);
	}
	
	public void run()
	{
		this.setVisible(true);
	}
	
	public void hide()
	{
		this.setVisible(false);
	}
}
