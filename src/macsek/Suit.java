package macsek;

public enum Suit {
	CLUBS("C"), DIAMONDS("D"), HEARTS("H"), SPADES("S");

    private String value;

    private Suit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
