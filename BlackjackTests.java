import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

public class BlackjackTests {
    Game game;
    Hand empty, playing, bust, stick;

    @Before
    public void setUp() {
        game = new Game(1,false);

        empty = new Hand();

        playing = new Hand();
        playing.hit(new Card(4, Suit.HEART));
        playing.hit(new Card(9, Suit.DIAMOND));

        stick = new Hand();
        stick.hit(new Card(9, Suit.HEART));
        stick.hit(new Card(9, Suit.DIAMOND));
        stick.stick();

        bust = new Hand();
        bust.hit(new Card(11, Suit.DIAMOND));
        bust.hit(new Card(12, Suit.SPADE));
        bust.hit(new Card(13, Suit.DIAMOND));
    }

    //testing hand -----------------------------------------
    @Test
    public void testHit() {
        playing.hit(new Card(5, Suit.DIAMOND));
        assertEquals(playing.getHand().size(), 3, "hit is not working");
    }

    @Test
    public void testStick() {
        bust.stick();
        assertEquals(bust.getState(), HandState.BUST, "Stick overrides bust hand");

        empty.stick();
        assertEquals(empty.getState(), HandState.EMPTY, "Stick overrides empty hand");
    }

    @Test
    public void testComplete() {
        assertEquals(empty.getState(), HandState.EMPTY, "empty hand isn't empty!");
        assertEquals(empty.checkComplete(), false, "empty hand is complete?");

        assertEquals(playing.getState(), HandState.PLAYING, "in-play hand isn't playing");
        assertEquals(playing.checkComplete(), false, "in-play hand is complete?");

        assertEquals(stick.getState(), HandState.STICK, "stuck hand isn't stuck");
        assertEquals(stick.checkComplete(), true, "Stuck hand is marked incomplete");

        assertEquals(bust.getState(), HandState.BUST, "bust hand isn't bust");
        assertEquals(bust.checkComplete(), true, "bust hand is marked incomplete");
    }

    //testing game -----------------------------------------------------------
    @Test
    public void testDeckSize() {
        System.out.println(game.getHands().size());
        assertEquals(game.getDeck().size(), 52, "Deck size is incorrect");
    }

    //Scenario 1
    @Test
    @DisplayName("Compulsory Test: Starting hand is 2 cards")
    public void testHandSize() {
        game.dealTable();
        Hand h = game.getHand(0);
        assertEquals(h.getHand().size(), 2, "starting hand is not 2 cards");
    }

    //Scenario 2
    @RepeatedTest(20)
    @DisplayName("Compulsory Test: When a player chooses to hit (twist), they receive a new card and their score is updated")
    public void testGameHit() {
        game.dealTable();
        Hand h = game.getHand(0);
        Boolean softBefore = h.getSoft();
        int score1 = h.getScore();
        game.stPlayer('T', 0);
        assertEquals(h.getHand().size(), 3, "hit is not working");
        int score2 = h.getScore();
        int val = h.getHand().get(2).getValue();
        //if hand included an Ace valued at 11, and gains a face card, the new score would be the same
        if(!((val == 10 || val == 11 || val == 12 || val == 13) && softBefore)) {
            assertEquals(score2!=score1, true, "score has not been updated");
        } 
    }

    //Scenario 3
    @Test
    @DisplayName("Compulsory Test: When a player chooses to stand (stick), they receive no more cards (score is updated when a card is received)")
    public void testGameStick() {
        game.dealTable();
        Hand h = game.getHand(0);
        int score1 = h.getScore();
        game.stPlayer('S', 0);
        assertEquals(h.getHand().size(), 2, "received another card when stick chosen");
        int score2 = h.getScore();
        assertEquals(score1, score2, "score has changed");

        if(score1 == 21) {
            assertEquals(h.getState(), HandState.BLACKJACK, "Wrong HandState");
        } else {
            assertEquals(h.getState(), HandState.STICK, "Wrong HandState");
        }
    }

    //Scenario 4
    @Test
    @DisplayName("Compulsory Test: With a score of 21 or less, hand is still valid (aka playing state)")
    public void testLess21Valid() {
        //Score only evaluated when a new card is dealt
        playing.hit(new Card(5, Suit.DIAMOND));
        assertTrue(playing.getScore() <= 21 , "score is not less than 21!");
        assertEquals(playing.getState(), HandState.PLAYING, "Hand less than 21 has changed state");
    }

    //Sceario 5
    @Test
    @DisplayName("Compulsory Test: With a score of 22 or more, hand is invalid (aka bust state)")
    public void testGreater22Invalid() {
        //Score only evaluated when a new card is dealt
        playing.hit(new Card(12, Suit.DIAMOND));
        assertTrue(playing.getScore() >= 22 , "score is not greater than 22!");
        assertEquals(playing.getState(), HandState.BUST, "Hand should be bust");
    }

    //Scenario 6
    @Test
    @DisplayName("Compulsory Test: KA = 21")
    public void testKAScore() {
        empty.hit(new Card(13, Suit.HEART));
        empty.hit(new Card(1, Suit.CLUB));
        assertEquals(empty.getScore(), 21, "KA Score is not 21");
        assertEquals(empty.getState(), HandState.BLACKJACK);
    }

    //Scenario 7
    @Test
    @DisplayName("Compulsory Test: KQA = 21")
    public void testKQAScore() {
        empty.hit(new Card(13, Suit.HEART));
        empty.hit(new Card(12, Suit.CLUB));
        empty.hit(new Card(1, Suit.CLUB));
        assertEquals(empty.getScore(), 21, "KQA Score is not 21");
    }

    //Scenario 8
    @Test
    @DisplayName("Compulsory Test: 9AA = 21")
    public void test9AAScore() {
        empty.hit(new Card(9, Suit.HEART));
        empty.hit(new Card(1, Suit.HEART));
        empty.hit(new Card(1, Suit.CLUB));
        assertEquals(empty.getScore(), 21, "9AA Score is not 21");
    }

}
