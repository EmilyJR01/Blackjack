public enum Suit {
    CLUB,
    DIAMOND,
    HEART,
    SPADE;

    public String toString() {
        switch(this) {
            case CLUB:
                return "Club";
            case DIAMOND:
                return "Diamond";
            case HEART: 
                return "Heart";
            case SPADE:
                return "Spade";
            default:
                return "uhhh is this a suit?";
        }   
    }
}