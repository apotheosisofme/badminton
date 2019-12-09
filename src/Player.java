

// this class represents a Player of the Tournament
public class Player {
    // name of the player
    private String name;
    // total number of games won by this player
    private int wins;
    // total number of games lost by this player
    private int losses;
    // total points of the player
    private int points;

    // constructor
    public Player(String name)
    {
        this.name = name;
    }

    // get the name of player
    public String getName()
    {
        return name;
    }

    // set the name of the player
    public void setName(String name) {

        this.name = name;
    }

    // increment wins of this player
    public void incrementWins(){

        wins++;
    }

    // increments games lost of this player
    public void incrementLosses(){

        losses++;
    }

    // update points
    public void addPoints(int points){

        this.points += points;
    }

    // return the short details of this player
    public String details()
    {
        return String.format("%s [W: %d, L:%d, P: %d]", name, wins, losses, points);
    }

    // return the descriptive details of this player
    public String getFulldetails(){

        return String.format("Win: %d\nLost:%d\nPoints: %d", wins, losses, points);
    }

    // string representation of the player
    @Override
    public String toString()
    {
        return String.format("%s", name);
    }
    
}
