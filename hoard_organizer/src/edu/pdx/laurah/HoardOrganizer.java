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

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.awt.*;

public class HoardOrganizer extends JFrame implements ActionListener
{
	private JDesktopPane desktop;
	private int width;
	private int height;
	private static final int INSET = 50;

	public HoardOrganizer()
	{
		super("Hoard Organizer");
		
		// size the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.width - (INSET * 2);
		height = screenSize.height - (INSET * 2);
		setBounds(INSET, INSET, width, height);
		
		// set up GUI
		desktop = new JDesktopPane();
		setContentPane(desktop);
		setJMenuBar(createMenuBar());
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setBackground(Color.lightGray);
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
			// open existing list
		}
		else // "quit"
		{
			// exit program
			quit();
		}
	}
	
	private void newList()
	{
		NewListFrame frame = new NewListFrame();
		frame.setLocation((width / 2) - (frame.WIDTH / 2), (height / 2) - (frame.HEIGHT /2));
		frame.setVisible(true);
		desktop.add(frame);
		try
		{
			frame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {}
	}
	
	private void quit()
	{
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
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

}
