


// this class represents a match between two players
public class Match {

    // round of the match
    private String round;
    // player one
    private Player player1;
    // player two
    private Player player2;
    // winner of the match
    private Player winner;
    // is the match complete / played
    private boolean played;

    // constructor
    public Match(Player player1, Player player2, String round) {
        this.player1 = player1;
        this.player2 = player2;
        this.round = round;
    }
    
    
    // string representation of the match
    @Override
    public String toString(){

        return String.format("%s vs %s", player1.getName(), player2.getName());
    }

    // get player one
    public Player getPlayer1() {

        return player1;
    }


    // get player two
    public Player getPlayer2() {

        return player2;
    }

    // get winner
    public Player getWinner()
    {
        return winner;
    }
    
    
    
    // set the winner and loser for this match
    public void setWinnerLoser(Player winner, int points) {
        this.winner = winner;
        // update the points of winner
        winner.addPoints(points);
        winner.incrementWins();
        // update the points of loser
        // if player one is winner player two is loser
        if(player1==winner){
            player2.incrementLosses();
            
        }
        // player two is winner and player one in loser
        else{
            player1.incrementLosses();
        }
        setMatchPlayed(true);
    }

    // set the match status
    public void setMatchPlayed(boolean status){

        played = status;
    }

    // check the status of match
    public boolean matchCompleted(){

        return played;
    }

    public String getRound(){
        return round;
    }
    
}
