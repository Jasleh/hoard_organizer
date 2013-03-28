package laurah;

import javax.swing.*;
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
	
	public NewListFrame(Connection c)
	{
		super("Create New List", false, true); // only close option enabled
		
		conn = c;;
		
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
		getContentPane().add(Box.createRigidArea(new Dimension(0,20)));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
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
			
		}
		else if ("clear".equals(e.getActionCommand()))
		{
			clearValues();
		}
	}
	
	// creates the list in the DB
	private void createTable()
	{
		
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
	}
}
