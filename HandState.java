public enum HandState {
    //hand hasn't been dealt yet
    EMPTY,
    //mid-way through hand
    PLAYING,
    //player had blackjack -> 10 + A
    BLACKJACK,
    //player has chosen to stick with their current score,
    STICK,
    //game-over, player's score is over 21
    BUST
}
