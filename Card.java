public class Card {
    private Suit suit;
    //value is from 1 to 13
    //Jack = 11, Queen = 12, King = 13
    private int value;

    public Card(int v, Suit s) {
        this.suit = s;
        this.value = v;
    }

    public String toString() {
        String v;
        switch(this.value) {
            case 1:
                v = "Ace";
                break;
            case 2:
                v = "Two";
                break;
            case 3:
                v = "Three";
                break;
            case 4:
                v = "Four";
                break;
            case 5:
                v = "Five";
                break;
            case 6:
                v = "Six";
                break;
            case 7:
                v = "Seven";
                break;
            case 8:
                v = "Eight";
                break;
            case 9:
                v = "Nine";
                break;
            case 10:
                v = "Ten";
                break;
            case 11:
                v = "Jack";
                break;
            case 12:
                v = "Queen";
                break;
            case 13:
                v = "King";
                break;
            default:
                v = "whaaaaaaaaaaaa?";
        }

        return v+" of "+this.suit.toString()+"s";
    }

    public int getValue() {
        return this.value;
    }

    public Suit getSuit() {
        return this.suit;
    }

}
