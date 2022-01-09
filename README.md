Hello and welcome to Blackjack! 
This has been written as a command line game in java.

To play, type "java Blackjack" in your command window from the folder containing this code and it should run automatically.

-------------------------------------------------
== File Index ==
Blackjack - deals with all the player input and the game rules
Game - holds all the player hands, scores and keeps track of the deck
Hand - structure for each players hand - holds player cards, score and state

GameState - enum class of possible game states
HandState - enum class of possible hand states
Suit - enum class of suits
Card - defines the structure of a card (suit and value)

BlackjackTests - holds all the required unit tests
TestRunner - command line confirmation of tests passing

-------------------------------------------------
== Game Details ==

This game is set to run for 1 to 5 players.
(These limits have been hardcoded in Blackjack.java, but theoretically could be expanded to any integer, though you would need also edit the number of decks in the game to have enough cards for everyone!)
This also deals all hands from one deck and has a discard pile, so card counting is possible.

At the beginning of the game, you can choose which of the two gamemodes you would like to play:
 - 1: all players against each other. The game prints out the winning players at the end of each hand, or all players bust.
 - 2: each player individually against a dealer 'bot'. The game prints out each player who beat the dealer, 
 the dealer winning against everybody, or everyone going bust.

If any player has Blackjack (a 10-valued card and an Ace), only players who get Blackjack can win.
If playing with a dealer, the dealer flips their first card at the beginning of the hand unless they get dealt Blackjack, in which case they announce that they have Blackjack.

--------------------------------------------------
== Testing ==

These tests have been set up using JUnit's console standalone version
"junit-platform-console-standalone-1.7.0-M1.jar"

To run the tests from the console, type "java TestRunner".
This will print a simple confirmation that all tests have passed.

To see the tests it runs, look in BlackjackTests.java


---------------------------------------------------
Other Notes:

 This code has been structured with the following possible future features in mind:
    - adding/removing players between hands
    - keeping track of score (or possibly net betting winnings) across several hands
    - adding optional player names instead of referring to players by number
    - additional Blackjack variants: multiple decks, splitting hands
    - dealer 'bot' variants: hitting on soft 17 instead of sticking
