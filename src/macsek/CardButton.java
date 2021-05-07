package macsek;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class CardButton extends JButton{
	
	private boolean selected;
	private Card card;
	
	public CardButton(ImageIcon ic, Card c) {
		super(ic);
		card = c;
		selected = false;
		setBorderPainted(false);
		setContentAreaFilled(false);
		setPreferredSize(new Dimension(89, 140));
	}
	
	public void setSelected (boolean s) {
		selected = s;
	}
	
	public boolean getSelected() {
		return selected;
	}
	
	public Card getCard() {
		return card;
	}
	
}
