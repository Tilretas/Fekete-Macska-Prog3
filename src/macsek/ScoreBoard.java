package macsek;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ScoreBoard extends JFrame{
	
	private JButton bBack;
	private scoreClick al;
	private JTextField tf;
	private JPanel panel;
	private String name;
	private int points;
	private int games;
	private Scanner sc;
	
	public ScoreBoard(String n) {
		
		super("FeketeMacska ScoreBoard");
		panel = new JPanel();
		bBack = new JButton("Back");
		al = new scoreClick();
		name = new String();
		
		
		try {
			sc = new Scanner(new File("cards\\scores.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(sc.hasNext() && !name.equals(n)) {
			name = sc.next();
			points = sc.nextInt();
			games = sc.nextInt();
		}
		if (name.equals(n)) {
			tf = new JTextField(name + " has " + points + " with " + games + " games.");
		}
		else
			tf = new JTextField(n + " has 0 games :(");
		
		
		bBack.addActionListener(al);
		panel.add(tf);
		panel.add(bBack);
		add(panel);
		sc.close();
		
        setLocation(750, 315);
        setMinimumSize(new Dimension(350, 300));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
	}

	private class scoreClick implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == bBack)
				dispose();
		}	
	}
}
