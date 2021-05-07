package macsek;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Card {
	
	private int point;
	private Suit suit;
	private Face face;
	private ImageIcon icon;
	private String kep;
	
	public Card(Suit s, Face f) {
		suit = s;
		face = f;
		if(suit == Suit.HEARTS) 
			point = 1;
		else if(suit == Suit.SPADES && face == Face.QUEEN)
			point = 13;
		else point = 0;
		kep = "cards\\" + face.getValue() + suit.getValue() + ".png";
		icon = Size_card(new ImageIcon(kep));
	}
	
	public int getPoint() {
		return point;
	}

	public Suit getSuit() {
		return suit;
	}
	
	public Face getFace() {
		return face;
	}
	
	public int getOrdinal() {
		return face.ordinal();
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
	
	public boolean equals(Card c) {
		if (face == c.getFace() && suit == c.getSuit())
			return true;
		return false;
	}
	
	public String szoveg() {
		System.out.print(face.getValue() + "of" + suit.getValue() + ": " + point +  " | ");
		return new String(face.getValue() + "-" + suit.getValue());
	}
	
	public static ImageIcon Size_card(ImageIcon ii) {
		BufferedImage bi = new BufferedImage(79, 120, BufferedImage.TYPE_INT_RGB);	
        Graphics2D g = (Graphics2D) bi.createGraphics();
        g.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
        g.drawImage(ii.getImage(), 0, 0, 79, 120, null);
        ii = new ImageIcon(bi);
        return ii;
	}
	
}
