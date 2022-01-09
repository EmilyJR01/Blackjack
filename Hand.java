import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;
    private HandState state;
    private int score;
    private Boolean soft;

    //Constructor  
    public Hand() {
        this.hand = new ArrayList<Card>();
        this.state = HandState.EMPTY;
        this.score = 0;
        this.soft = false;
    }

    // --------------------------- Functions --------------------------------

    //deals a card, to the hand, scores it and updates the handstate
    public void hit(Card C) {
        this.hand.add(C);
        score();

        if(this.score>21){
            this.state = HandState.BUST;
        } else if (this.score == 21 && this.hand.size()==2) {
            this.state = HandState.BLACKJACK;
        } else {
            this.state = HandState.PLAYING;
        }
    }

    //changes handstate to stick
    public void stick() {
        //should be redundant, but ensures handstate isn't changed if bust or blackjack
        if(this.hand.size()>0 && !(this.state == HandState.BUST) && !(this.state == HandState.BLACKJACK)) {
            this.state = HandState.STICK;
        }
    }

    //scores hand
    public void score() {
        int aceCount = 0;
        int s = 0;

        for(Card c: this.hand) {
            if(c.getValue() == 1) {
                aceCount++;
            } else if(c.getValue()>10) {
                s += 10;
            } else {
                s += c.getValue();
            }
        }

        //if any aces...
        if(aceCount > 0) {
            //2 aces at value 11 then BUST already (score is 22+)
            //check if 1 ace can go to 11 without Bust, otherwise take all aces as 1 for minimal score
            if(s + aceCount + 10 <22) {
                s += aceCount + 10;
                this.soft = true;
            } else {
                s += aceCount;
                this.soft = false;
            }
        }
        this.score = s;
    }

    //prints hand
    public void print() {
        for(Card c: this.hand) {
            System.out.println(c.toString());
        }
    }

    //checks if a hand is complete based on HandState
    public Boolean checkComplete() {
        if(this.state==HandState.PLAYING || this.state==HandState.EMPTY) {
            return false;
        } else {
            return true;
        }
    }

    //--------------------------------- getters & setters -----------------------------

    public int getScore() {
        return this.score;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public HandState getState() {
        return this.state;
    }

    public Boolean getSoft() {
        return this.soft;
    }

    public Card getCard(int card) {
        return this.hand.get(card);
    }
}