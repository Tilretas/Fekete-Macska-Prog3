package macsek;

import java.util.ArrayList;

public class Player {
	
	private String name;
	private int points;	
	private ArrayList<Card> hand;
	private ArrayList<Card> pile;
	
	public Player(String n) {
		name = n;
		points = 0;
		hand = new ArrayList<Card>();
		pile = new ArrayList<Card>();
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}

	public int getPoints() {
		points = 0;
		for (int i = 0; i < pile.size(); i++)
			points += pile.get(i).getPoint();
		return points;
	}
	
	public ArrayList<Card> getPile() {
		return pile;
	}

	public Card getHand(int i) {
		return hand.get(i);
	}
	
	public String getName() {
		return name;
	}	
}
