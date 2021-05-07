package macsek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Game {

	private ArrayList<CardButton> selected;
	private ArrayList<Player> players;
	private ArrayList<Card> table;
	private Deck deck;
	private GameFrame frame;
	private boolean turn_end;
	private boolean hearts;
	private boolean game_end;	
	private int round;
	private int phase;
	private int nextP;
	public Game(String name) {
		
		players = new ArrayList<Player>();
		players.add(new Player(name));
		players.add(new Player("Kapi"));
		players.add(new Player("Slendy"));
		players.add(new Player("Grõbi"));
		
		deck = new Deck();
		Collections.shuffle(deck);
		deck.deal(players);
		turn_end = false;
		game_end = false;
		hearts = false;
		round = 1;
		phase = 0;
		selected = new ArrayList<CardButton>();
		table = new ArrayList<Card>();
		
		
		
		frame = new GameFrame();	
		
	}
		
	private void addSelected(CardButton cb) {
		if(phase == 0) {
			if(selected.size() >= 3) {
				selected.get(0).setSelected(false);
				selected.get(0).setBorderPainted(false);
				selected.get(0).setContentAreaFilled(false);
				selected.remove(0);
			}
			selected.add(cb);
		}
		if(phase == 1) {
			if(selected.size() >= 1) {
				selected.get(0).setSelected(false);
				selected.get(0).setBorderPainted(false);
				selected.get(0).setContentAreaFilled(false);
				selected.remove(0);
			}
			selected.add(cb);
		}
	}
	
	private Card aiCard(Player ai) {
		for(int i = 0; i < ai.getHand().size(); i++) {
			if(check(ai.getHand(i), ai)) {
				return ai.getHand(i);
			}
		}
		System.out.println("Shit, nem tud kártyát rakni!");
		return null;
	}
	
	private void ai() {
//		while(table.size() != 4 && nextP != 0) { //teszt verzió
		while(table.size() != 4) {
			Card c = aiCard(players.get(nextP));
			table.add(c);
			frame.labels.get(nextP).setIcon(c.getIcon());
			nextP++;
			if(nextP >= 4) 
				nextP = 0;
		}
//		if(nextP == 0 && table.size() != 4)
//			return;
		endRound();
	}
	
	private void endRound() {
		Card hit = hit();
		nextP = findCard(hit);
		for(int i = 0; i < table.size(); i++) {
			players.get(nextP).getPile().add(table.get(i));
			players.get(findCard(table.get(i))).getHand().remove(table.get(i));
		}
		frame.labels.get(nextP).setOpaque(true);
		frame.panelT.revalidate();
		turn_end = true;
	}
	
	private void giveCards() {
		for (int p = 3; p > 0; p--) {
			for (int c = 0; c < 3; c++) {
				players.get(p-1).getHand().add(players.get(p).getHand().remove(0));
			}
			players.get(3).getHand().add(selected.get(0).getCard());
			players.get(0).getHand().remove(selected.get(0).getCard());
			selected.remove(0);
		}
	}
	
	private boolean check(Card c, Player p) {
		boolean okay = true;
		boolean onlyHearts = true;
		boolean onlyPoints = true;
		boolean gotSuit = false;
		
		for (int i = 0; i < p.getHand().size(); i++) {
			if(p.getHand(i).getPoint() == 0)
				onlyPoints = false;
			if(p.getHand(i).getSuit() == Suit.HEARTS)
				onlyHearts = false;
			if(table.size() != 0)
				if(p.getHand(i).getSuit() == table.get(0).getSuit())
					gotSuit = true;
		}
		
		
		if (round == 1 && c.getPoint() != 0 && !onlyPoints)
			okay = false; //elsõ kör -> nem érhet pontot
		
		if (round == 1 && table.size() == 0 && !(c.getSuit() == Suit.CLUBS && c.getFace() == Face.TWO))
			okay = false; //treff2 kezd
		
		if (!hearts && c.getSuit() == Suit.HEARTS && !onlyHearts && table.size() == 0)
			okay = false; //nem lehet kõr kezdés, amíg nem dobtak kõrt
		
		if (table.size() != 0)
			if(gotSuit && c.getSuit() != table.get(0).getSuit())
				okay = false; 

		if (okay && c.getSuit() == Suit.HEARTS)
			hearts = true;
		
		return okay;
	}
	
	public Card hit() {
		Card c = table.get(0);
		for (int i = 1; i < table.size(); i++) {
			if (c.getOrdinal() < table.get(i).getOrdinal() && c.getSuit() == table.get(i).getSuit())
				c = table.get(i);
		}
		return c;
	}
	
	public void drawButtons() {
		JButton cButton;
		ClickCard cc = new ClickCard();
		frame.panelP.setVisible(false);
		frame.panelP.removeAll();

		for (int i = 0; i < players.get(0).getHand().size(); i++) {
			cButton = new CardButton(players.get(0).getHand(i).getIcon(), players.get(0).getHand(i));
			cButton.addActionListener(cc);
			frame.panelP.add(cButton);
		}
		frame.panelP.revalidate();
		frame.panelP.setVisible(true);
	}
	
	public int findCard(Card card) {
			for (int p = 0; p < players.size(); p++)
				for (int c = 0; c < players.get(p).getHand().size(); c++)
					if(players.get(p).getHand(c).equals(card)) {
						return p;
					}
			return 5;
	}
	
	@SuppressWarnings("serial")
	private class GameFrame extends JFrame{
		
		private JPanel panelT;
		private JPanel panelP;
		private ArrayList<JLabel> labels;
		private ArrayList<CardButton> buttons;
		private ImageIcon back;
		
		public GameFrame() {
			
			super("Fekete Macska");
			back = Card.Size_card(new ImageIcon("cards\\back.png"));
			getContentPane().setLayout(new BorderLayout());
			
			panelP = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
			JButton cButton;
			buttons = new ArrayList<CardButton>();
			ClickCard cc = new ClickCard();
			for (int i = 0; i < players.get(1).getHand().size(); i++) {
				cButton = new CardButton(players.get(0).getHand(i).getIcon(), players.get(0).getHand(i));
				buttons.add((CardButton) cButton);
				cButton.addActionListener(cc);
				panelP.add(cButton);
			}
			panelT = new JPanel(new BorderLayout());
	        panelT.setBorder(BorderFactory.createEtchedBorder());

			JButton ok = new JButton(new ImageIcon("cards\\macska_txt.jpg"));
			ok.setHorizontalAlignment(SwingConstants.CENTER);
			ok.addActionListener(new ClickOK());
			ok.setContentAreaFilled(false);
			panelT.add(ok, BorderLayout.CENTER);
			
			labels = new ArrayList<JLabel>();
			for(int i = 0; i < 4; i++) {
				labels.add(new JLabel());
				labels.get(i).setIcon(back);
				labels.get(i).setHorizontalAlignment(SwingConstants.CENTER);
				labels.get(i).setPreferredSize(new Dimension(259, 130));
				labels.get(i).setBackground(Color.GREEN);
			}
			
			panelT.add(labels.get(0), BorderLayout.SOUTH);
			panelT.add(labels.get(1), BorderLayout.WEST);
			panelT.add(labels.get(2), BorderLayout.NORTH);
			panelT.add(labels.get(3), BorderLayout.EAST); 
			
	        getContentPane().add(panelP, BorderLayout.SOUTH);
	        getContentPane().add(panelT, BorderLayout.CENTER);
	        
	        setMinimumSize(new Dimension(1200, 600));
	        
	        setLocation(360, 215);
	        pack();
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
		}
		
	}
	
	private void playCard() {
		table.add(selected.get(0).getCard());
		frame.labels.get(0).setIcon(selected.get(0).getCard().getIcon());
		selected.remove(0);
	}
	
	private class ClickCard implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CardButton cb = (CardButton) e.getSource();
			if (!cb.getSelected()) {
				cb.setBorderPainted(true);
				cb.setContentAreaFilled(true);
				cb.setBackground(Color.RED);
				addSelected(cb);
			}
			else {
				cb.setBorderPainted(false);
				cb.setContentAreaFilled(false);
				selected.remove(cb);
			}
			cb.setSelected(!cb.getSelected());
			frame.panelP.revalidate();
		}
		
	}
	
	private class ClickOK implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(game_end) {
				String n = new String();
				int p;
				int g;
				boolean found = false;
				Scanner sc;
				try {
					File origfile = new File("cards\\scores.txt");
					File newfile = new File("file.txt");
					newfile.createNewFile();
		            FileWriter fw = new FileWriter(newfile, true);
		            BufferedWriter bw = new BufferedWriter(fw);
		            PrintWriter pw = new PrintWriter(bw);
		            sc = new Scanner(new File("cards\\scores.txt"));
				
				while(sc.hasNext()) {
					n = sc.next();
					p = sc.nextInt();
					g = sc.nextInt();
					if(n.equals(players.get(0).getName())) {
						pw.println(n + " " + (p+players.get(0).getPoints()) + " " + (g+1));
						found = true;
					}
					else pw.println(n + " " + p + " " + g);
				}
				
				if(!found)
					pw.println(players.get(0).getName() + " " + players.get(0).getPoints() + " " + 1);
				sc.close();
				pw.flush();
				pw.close();
				origfile.delete();
				File temp = new File("cards\\scores.txt");
				newfile.renameTo(temp);
				
				} catch (IOException e1) {}
				
				frame.dispose();
			}
			
			else if(turn_end) {
				table = new ArrayList<Card>();
				for (int i = 0; i < frame.labels.size(); i++)
					frame.labels.get(i).setIcon(frame.back);
				for (int i = 0; i < frame.buttons.size(); i++) 
					frame.buttons.get(i).setSelected(false);
				selected = new ArrayList<CardButton>();
				round++;
				turn_end = false;
				frame.labels.get(nextP).setOpaque(false);
				drawButtons();
				if (round == 14) {
					game_end = true;
					for (int i = 0; i < players.size(); i++) {
						frame.panelP.add(new TextField(players.get(i).getName() + " got " + players.get(i).getPoints() + " points!"));						
					}
					return;
				}
				ai();
			}
			
			else if(phase == 0 && selected.size() == 3) {
				giveCards();
				phase = 1;
				nextP = findCard(new Card(Suit.CLUBS, Face.TWO));
				drawButtons();
				ai();
			}
			
			else if(phase == 1 && selected.size() == 1 && nextP == 0 && check(selected.get(0).getCard(), players.get(0))) {
				playCard();
				nextP++;
				if(table.size() == 4)
					endRound();
				else ai();
			}
		}
	}
}

