import java.util.Scanner;
import java.util.ArrayList;

class  Blackjack {
    static Scanner s = new Scanner(System.in);
    public static void main(String args[]) {
        System.out.println("\n"+"Welcome to Blackjack!");
        System.out.println("\n"+"These are the rules: ");
        System.out.println("\n"+"Each player gets a hand of cards. You are aiming for a hand with a score as close to 21, without going over. If you go over 21, you've gone bust and lost.");
        System.out.println("Cards from 2 to 10 score their number value. J, Q or K score 10 each and an A scores 1 or 11 - it's up to the player.");
        System.out.println("Each player will be dealt two cards. On your turn you will be asked to Stick (stop at your current score and get no more cards) or Twist (get another card).");
        System.out.println("If in your starting hand, you have a 10-valued card and an Ace, it is called Blackjack. If any player has Blackjack, all players without Blackjack lose, even if they have a score of 21.");

        System.out.println("\n"+"If you are playing with a dealer, you are trying to match or beat the dealer's score. You are not competing with any other player.");
        System.out.println("Note: if the dealer has Blackjack, you can only match their score by also getting Blackjack.");
        System.out.println("At the beginning of the game they will flip over one card in their hand, but not the other. There is an exception - they will reveal if they have been dealt Blackjack.");
        System.out.println("The dealer will then play their turn after all players have gone. They will continue to Twist (request another card) until they have a score of 17 or higher, or go bust.");


        //boolean checks if everyone has played
        boolean continuePlay = true;
        Game game;

        //asks for number of players and whether you would like to play against a dealer
        int numPlayers = askInt("\n"+"This game plays for 1 to 5 players."+"\n"+"How many people are playing? "+"\n"+"Number of Players: ", 1, 5);
        char dealer = ask("\n"+"Would you like to play against a dealer?"+"\n"+"If playing against a dealer, each player wins only if they beat the dealer."+"\n"+"Type Y for yes or N for no: ", "YN");
        if(dealer=='Y') {
            game = new Game(numPlayers, true);
        } else {
            game = new Game(numPlayers, false);
        }


        //while hand not completed
        while(continuePlay) {
            switch (game.getState()){

                //if table empty, deal
                case CLEAR:
                    game.dealTable();
                break;

                //game in play - should run through all players and dealer (if applicable)
                case INPLAY:
                    System.out.println("\n"+"--------------------------------");
                    int s=0;

                    // if dealer, print dealer's first card
                    if(dealer=='Y') {
                        s = 1;
                        game.printDealer();
                        System.out.println("\n"+"--------------------------------");
                    } 

                    //Loop through all players
                    //If there is a dealer, s shifts the indices of the players by 1
                    for(int i=s; i<game.getPlayerNum()+s; i++) {
                        System.out.println("\n"+"Player "+(i+(s+1)%2)+", it's your turn:");
                        char st = 'Z';
                        Boolean playerCompleted = false;

                        //if Blackjack, auto-stick a player
                        if(game.getHandState(i)==HandState.BLACKJACK) {
                            System.out.println("\n"+"This is your hand: ");
                            game.printHand(i);
                            System.out.println("\n"+"You've been dealt Blackjack!");
                            playerCompleted = true;
                        }

                        while(!playerCompleted) {

                            //print player's hand
                            System.out.println("\n"+"This is your hand:");
                            game.printHand(i);
                            System.out.println("\n"+"This is your hand's score: "+game.getHandScore(i));

                            //ask Stick or Twist
                            Boolean confirmedAnswer = false;
                            while(!confirmedAnswer) {
                                st = ask("\n"+"Would you like to stick or twist?"+"\n"+"Type S to stick or T to twist: ", "ST");
                                confirmedAnswer = confirm("you would like to "+charToString(st));
                            }

                            game.stPlayer(st, i);

                            //if bust, print hand and say "bust"
                            if(game.getHandState(i)==HandState.BUST) {
                                    System.out.println("\n"+"This is your hand:");
                                    game.printHand(i);
                                    System.out.println("\n"+"Oh no, you've gone bust!");
                            }

                            //auto-stick a player if they score 21
                            if(game.getHandScore(i)==21) {
                                System.out.println("\n"+"This is your hand:");
                                game.printHand(i);
                                System.out.println("\n"+"You scored 21!");
                                game.stPlayer('S', i);
                            }

                            //update player complete status
                            playerCompleted = game.checkHandComplete(i);
                        }
                        System.out.println("\n"+"--------------------------------");
                    }

                    //if playing with a dealer, run it!
                    if(dealer=='Y') {
                        game.runDealer();
                    }

                break;

                case ENDED:

                    System.out.println("\n"+"All hands have been played.");
                    ArrayList<Integer> winners;

                    //gets list of winners
                    if(dealer=='Y') {
                        winners = game.getDealerWinners();
                    } else {
                        winners = game.getWinners();
                    }

                    //converts winners array to a string
                    int numWin = winners.size();
                    String w = "";
                    if(numWin==1) {
                        w = "Player "+winners.get(0);
                    } else if (numWin==2){
                        w = "Players "+winners.get(0)+" and "+winners.get(1);
                    } else if(numWin>2) {
                        w = "Players "+(winners.get(0));
                        for(int i=1; i<numWin-1; i++){
                            w = w + ", "+(winners.get(i));
                        }
                        w = w + " and "+(winners.get(numWin-1));
                    }

                    //print appropriate win statement
                    if(dealer=='Y'){
                        if(numWin==0) {
                            System.out.println("Everyone lost to the dealer...");
                        } else {
                            System.out.println(w+" beat the dealer!");
                        }
                    } else {
                        if(numWin==0) {
                            System.out.println("Everyone went bust...");
                        } else if (numWin==1) {
                            System.out.println(w+" wins!");
                        } else {
                            System.out.println(w+" tied.");
                        }
                    }


                    //Play another round?
                    game.clearTable();

                    Boolean confirmedAnswer = false;
                    char yn = 'Z';
                    while(!confirmedAnswer) {
                        yn = ask("\n"+"Would you like to play another hand?"+"\n"+"Type Y for yes or N for no: ", "YN");
                        if(yn=='Y'){
                            confirmedAnswer = confirm("you would like to play again");
                        } else {
                            confirmedAnswer = confirm("you would like to end the game");
                        }
                    }

                    if(yn == 'N') {
                        continuePlay = false;
                    }

                break;
            }
        }

        System.out.println("\n"+"Thanks for playing!");   
        s.close();   
    }

    //========================  functions  ===========================

    //test if input is one of the accepted options
    public static Boolean acceptInput(String input, String options) {
        if(input.isEmpty()){
            System.out.println("\n"+"You do need to input something before you press Enter...");
            return false;
        } else { 
            char x = Character.toUpperCase(input.charAt(0));
            if (options.indexOf(x)!=-1){
                return true;
            } else {
                System.out.println("\n"+"That isn't one of the accepted inputs. Have another go: ");
                return false;
            }
        }
    }

    //ask a question, and only accept an answer in one of the possible options
    public static char ask(String question, String options) {
        Boolean gotAnswer = false;
        String input ="";

        while(!gotAnswer) {
            System.out.print(question);
            input = s.nextLine();
            if(acceptInput(input, options)) {
                gotAnswer = true;
            }
        } 
        return input.toUpperCase().charAt(0);
    }

    //bounds are inclusive, i.e 1 to 5, answer could be 1 2 3 4 or 5
    //ask for an input number between the bounds, only takes an acceptable answer
    public static int askInt(String question, int lowerBound, int upperBound) {
        Boolean gotAnswer = false;
        int number = lowerBound - 100;

        while(!gotAnswer) {
            System.out.print(question);
            String input = s.nextLine();
            if(!isNumber(input)) {
                System.out.println("\n"+"That's not a number! Let's try again.");
            } else {
                number = Integer.parseInt(input);
                if(number>=lowerBound && number <= upperBound) {
                    gotAnswer = true;
                } else {
                    System.out.println("\n"+"That number isn't in the right range. Have another go:");
                }
            }
        } 
        return number;
    }

    //asks the user to confirm their input
    public static Boolean confirm(String checkString) {
        String q = "\n"+"To confirm, "+checkString+"?"+"\n"+"Type Y for yes, or N for no: ";
        char I = ask(q, "YN");

        if(I=='Y') {
            return true;
        } else {
            return false;
        }
    }


    public static String charToString(char input) {
        switch(input) {
            case 'S': 
                return "stick";
            case 'T':
                return "twist";
            case 'Y':
                return "yes";
            case 'N':
                return "no";
            default: 
                return "";      
        }
    }

    //confirms an input is an integer
    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
        }
        catch(NumberFormatException ex) {
            return false;
        }
        return true;
    }   
}



