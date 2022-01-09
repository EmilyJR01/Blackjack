import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private ArrayList<Card> deck;
    private ArrayList<Hand> hands;
    private ArrayList<Card> discards;
    private GameState state;
    private int players;
    private Boolean dealer;

    public Game(int players, Boolean dealer) {
        //populates deck
        this.deck = new ArrayList<Card>();
        for(Suit s: Suit.values()) {
            for(int i=1; i<14; i++){
                this.deck.add(new Card(i,s));
            }
        }
        Collections.shuffle(this.deck);
        this.hands = new ArrayList<Hand>();
        this.discards = new ArrayList<Card>();
        this.state = GameState.CLEAR;
        this.players = players;
        this.dealer = dealer;
    }  

    //---------------------- functions -----------------------

    //check deck has a card to deal
    //if not, add discards to deck and shuffle
    public void checkDeck(int i) {
        if(this.deck.size() < i) {
            deck.addAll(discards);
            discards.clear();
            Collections.shuffle(this.deck);
        }
    }

    //creates hands for each player and deals each hand 2 cards
    public void dealTable() {
        int s = this.players;
        if(this.dealer) {
            s++;
        }
        System.out.println(this.players);
        for(int i=0; i<s; i++) {
            this.hands.add(new Hand());
            this.hands.get(i).hit(dealCard());
        }
        for(Hand h: hands) {
            h.hit(dealCard());
        }
        this.state = GameState.INPLAY;
    }

    //clears all hands from the table and adds them to the discards pile
    public void clearTable() {
        for(Hand h: hands) {
            discards.addAll(h.getHand());
        }
        hands.clear();
        this.state = GameState.CLEAR;
    }

    //returns the top card off the deck
    public Card dealCard() {
        checkDeck(1);
        Card C = this.deck.get(0);
        this.deck.remove(0);
        return C;
    }

    //sticks or twists a player as determined by the input
    public void stPlayer(char c, int p) {
        Hand h = hands.get(p);
        if(c=='T'){
            checkDeck(1);
            h.hit(dealCard());
        } else {
            h.stick();
        }
        
        //checks if all players have finished their turn
        if(checkAllComplete()) {
            this.state = GameState.ENDED;
        }
    }

    //checks if all players have finished
    public Boolean checkAllComplete() {
        for(Hand h: hands) {
            if(!h.checkComplete()) {
                return false;
            }
        }
        return true;
    }

    //prints a players hand
    public void printHand(int p) {
        Hand h = hands.get(p);
        h.print();
    }

    //checks if this players hand has finished
    public Boolean checkHandComplete(int p) {
        Hand h = hands.get(p);
        return h.checkComplete();
    }

    //checks for the winning player(s) if playing without a dealer
    public ArrayList<Integer> getWinners() {
        ArrayList<Integer> winners = new ArrayList<Integer>();
        int maxScore = 0;
        Boolean blackjack = false;
        for(int i=0; i<hands.size(); i++) {
            Hand h = hands.get(i);
            if(blackjack) {
                if(h.getState()==HandState.BLACKJACK){
                    winners.add(i+1);
                }
            } else {
                if(h.getState()!=HandState.BUST) {
                    if(h.getState()==HandState.BLACKJACK){
                        blackjack=true;
                        winners.clear();
                        winners.add(i+1);
                        break;
                    }

                    int score = h.getScore();
                    if(score > maxScore) {
                        winners.clear();
                        maxScore = score;
                        winners.add(i+1);
                    } else if (score == maxScore) {
                        winners.add(i+1);
                    }
                }
            }
        }
        return winners;
    }

    //returns a list of players who beat the dealer
    public ArrayList<Integer> getDealerWinners() {
        ArrayList<Integer> winners = new ArrayList<Integer>();
        int dealerScore = getHandScore(0);
        Boolean blackjack = false;

        if(getHandState(0)==HandState.BUST){
            dealerScore = 0;
        } else if (getHandState(0)==HandState.BLACKJACK) {
            blackjack = true;
        }

        for(int i=1; i<hands.size(); i++) {
            Hand h = hands.get(i);
            if(blackjack){
                if(h.getState()==HandState.BLACKJACK){
                    winners.add(i);
                }
            } else {
                if(h.getState()!=HandState.BUST) {
                    int score = h.getScore();
                    if(score >= dealerScore) {
                        winners.add(i);
                    }
                }
            }
        }
        return winners;
    }

    //prints the dealers hand at the start of the game
    public void printDealer() {
        if(this.dealer){
            if(hands.get(0).getState()==HandState.BLACKJACK){
                System.out.println("The dealer has Blackjack!");
                printHand(0);
            } else {
                System.out.println("\n"+"This is the dealer's hand: ");
                System.out.println(hands.get(0).getCard(0).toString());
                System.out.println("?? of ??");
            }
        } else {
            System.out.println("This game doesn't have a dealer...");
        }
    }

    //runs the dealer 'bot'
    public void runDealer() {
        //prints dealers starting hand
        System.out.println("\n"+"Dealer reveals their hand: ");
        printHand(0);

        while(checkHandComplete(0)==false) {
            Hand h = this.hands.get(0);
            int x = getHandScore(0);
            if(x<17) { // if score is less than 17, dealer hits
                System.out.println("\n"+"Dealer hits");
                h.hit(dealCard());
                System.out.println("\n"+"This is the dealer's hand: ");
                printHand(0);
            } else { // if score>=17, dealer sticks
                h.stick();
            }
        }

        if(getHandState(0)==HandState.BUST){
            System.out.println("\n"+"Dealer has gone bust.");
        } else if(getHandState(0)==HandState.STICK) {
            System.out.println("\n"+"Dealer sticks at "+getHandScore(0));
        }

        if(checkAllComplete()) {
            this.state = GameState.ENDED;
        }
    }

    //------------------- getters & setters ---------------------------

    public GameState getState() {
        return this.state;
    }

    public Hand getHand(int p) {
        return this.hands.get(p);
    }

    public int getHandScore(int p) {
        return this.hands.get(p).getScore();
    }

    public int getPlayerNum() {
        return
        this.players;
    }

    public HandState getHandState(int p){
        return this.hands.get(p).getState();
    }

    //==================== FOR TESTING ONLY ========================

    public ArrayList<Card> getDeck() {
        return this.deck;
    }

    public ArrayList<Hand> getHands() {
        return this.hands;
    }

}
