// necessary imports

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

// this is the controller for "Tournament.fxml" screen
public class TournamentController implements Initializable {

    // the table which displays the matches of this tournament
    @FXML
    private TableView<Match> matchesTable;
    // column to display the first player of the match
    @FXML
    private TableColumn<Match, String> oneCol;
    // column to display the second player of the match
    @FXML
    private TableColumn<Match, String> twoCol;
    // displays the winner of a match
    @FXML
    private TableColumn<Match, String> wonByCol;
    // display the current round of the tournament
    @FXML
    private TableColumn<Match, String> roundCol;
    // move to the next round of the tournament
    @FXML
    private Button nextButton;
    // displays the current round as a Heading on the screen
    @FXML
    private Label roundLabel;

    // matches played up till now in this tournament
    private ArrayList<Match> matchesPlayed;
    // matches to be played in this round
    private ObservableList<Match> currentRound;


    // this variable keeps track of the current round of the tournament
    int round = 1;

    // displays the result of the tournament
    @FXML
    private Button resultButton;
    

    // set the matches for this tournament
    public void setMatches(ArrayList<Match> matches) {
        this.currentRound.addAll(matches);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize the instance variables
        matchesPlayed = new ArrayList<>();
        currentRound = FXCollections.observableArrayList();
        matchesTable.setItems(currentRound);
        // bind the columns with the data
        oneCol.setCellValueFactory(column -> {
            return new SimpleStringProperty(column.getValue().getPlayer1().details());

        });
        twoCol.setCellValueFactory(column -> {
            return new SimpleStringProperty(column.getValue().getPlayer2().details());
        });
        wonByCol.setCellValueFactory(column -> {

            Player winner = column.getValue().getWinner();
            if (winner != null) {
                return new SimpleStringProperty(winner.getName());
            }
            return new SimpleStringProperty("No Result Yet");
        });
        roundCol.setCellValueFactory(new PropertyValueFactory("round"));
    }

    // sets the winner of a particular match of a tournament

    @FXML
    private void announceResultButtonPressed(ActionEvent event) {
        // get selected match
        Match match = matchesTable.getSelectionModel().getSelectedItem();

        if (match != null) {
            // create a PopUp Dialog and ask user about the winner and get his points
            GridPane root = new GridPane();
            root.setVgap(20);
            root.setHgap(20);
            Label winnerLabel = new Label("Winner: ");
            ComboBox<Player> players = new ComboBox<>();
            players.setPromptText("Please Select a Winner");
            players.getItems().addAll(match.getPlayer1(), match.getPlayer2());
            
            Label pointsLabel = new Label("Points: ");
            TextField inputPoints = new TextField();
            
            root.addRow(0, winnerLabel,players);
            root.addRow(1, pointsLabel, inputPoints);
            
            Alert selectWinner = new Alert(AlertType.CONFIRMATION);
            selectWinner.getDialogPane().setContent(root);
            selectWinner.setHeaderText("Select Winner");
            selectWinner.showAndWait().ifPresent(response -> {
                // when the user presses the OK Button
                if (response == ButtonType.OK) {
                    if (players.getValue() != null) {
                        // set the selected player as winner for this match
                        Player winner = players.getValue();
                        // set the points of winner of this match
                        int points = Integer.parseInt(inputPoints.getText());
                        match.setWinnerLoser(winner, points);
                        // update the table
                        refreshTable();
                    }
                }
            });

        }
    }

    // this method updates the table after each match
    private void refreshTable() {
        // get all matches
        Match matchesList[] = new Match[currentRound.size()];
        matchesList = currentRound.toArray(matchesList);
        // clear the list which is shown in the table
        currentRound.clear();
        // add the matches again to the list, the Table is automatically updated
        currentRound.addAll(matchesList);
    }

    // this method is called when next Round Button is pressed
    @FXML
    private void nextRoundButtonPressed(ActionEvent event) {

        // get list of matches for current round
        Match matchesList[] = new Match[currentRound.size()];
        matchesList = currentRound.toArray(matchesList);

        // check if the matches of current round have been completed
        boolean matchesComplete = true;

        for (int i = 0; matchesComplete && i < matchesList.length; i++) {
            if(!matchesList[i].matchCompleted()){
                matchesComplete = false;
            }
        }

        // if matches are to be played yet then don`t proceed to next round
        if(!matchesComplete){
            
        }
        // if there is only one match, this means that the tournament has ended
        else if (matchesList.length == 1) {
            roundLabel.setText("Final Results");
            matchesPlayed.addAll(Arrays.asList(matchesList));
            currentRound.clear();
            currentRound.addAll(matchesPlayed);
            resultButton.setDisable(true);
            nextButton.setDisable(true);
        } 
        // other wise proceed to the next round
        else {
            
            if(matchesList.length == 2){
                nextButton.setText("Results");
            
            }
            // save the matches played in this round
            matchesPlayed.addAll(Arrays.asList(matchesList));
            currentRound.clear();
            // update the round
            String roundText = String.format("Round %d", ++round);
            roundLabel.setText(roundText);
            // remove losers and schedule the next round matches between winners of this round
            for (int i = 0; i < matchesList.length - 1; i++) {
                currentRound.add(new Match(matchesList[i].getWinner(), matchesList[i + 1].getWinner(), roundText));
            }
        }
    }

}
