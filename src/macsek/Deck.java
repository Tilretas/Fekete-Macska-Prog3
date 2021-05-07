package macsek;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Deck extends ArrayList<Card>{
	
	public Deck() {
		for (Suit s : Suit.values())
			for(Face f : Face.values()) {
				add(new Card(s, f));
			}
	}
	
	public void deal(ArrayList<Player> p) {
		int a = 0;
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 4; j++) {
				p.get(j).getHand().add(get(a++));
			}
	}
	
}
