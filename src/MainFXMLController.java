/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MainFXMLController implements Initializable {

    // this table displays all the players currently playing in this tournament
    @FXML
    private TableView<Player> allPlayersTable;
    // the column name which represents player name
    @FXML
    private TableColumn<Player, String> playerNameCol;

    // list of players in this tournament
    private ObservableList<Player> playersList;
    // text field to input player name
    @FXML
    private TextField inputPlayerName;

    // initializes the controller class
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize the list, table and column
        playersList = FXCollections.observableArrayList();
        allPlayersTable.setItems(playersList);
        playerNameCol.setCellValueFactory(new PropertyValueFactory("name"));
    }

    // add a new player to the tournament
    @FXML
    private void addPlayerButtonPressed(ActionEvent event) {
        // ask for player`s name
        String name = inputPlayerName.getText().trim();
        if (!name.isEmpty()) {
            // add player to the players list
            playersList.add(new Player(name));
        } else {
            // show error message if name is empty
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Player Name");
            alert.setHeaderText("Empty Player Name");
            alert.setContentText("Player name can not be empty.");
            alert.showAndWait();
        }
    }

    // delete the player from the tournament
    @FXML
    private void deletePlayerButtonPressed(ActionEvent event) {
        // get selected player
        Player tempPlayer = allPlayersTable.getSelectionModel().getSelectedItem();
        // if a player is selected, remove him from tournament
        if (tempPlayer != null) {
            playersList.remove(tempPlayer);
        }
    }

    // this method is called when single elimination button is pressed
    @FXML
    private void singleEliminationButtonPressed(ActionEvent event) {
        // get list of all players
        Player players[] = new Player[playersList.size()];
        players = playersList.toArray(players);
        // total number of players
        int totalPlayers = players.length;

        // make sure we have enough player to schedule a tournament using Elimination Algorithm
        if (enoughPlayersForTournament(totalPlayers)) {
            // shuffle the players
            ArrayList<Player> allPlayers = randomizePlayers(players);
            // assign the matches for first round
            ArrayList<Match> matches = singleElimination(allPlayers);
            // start the tournament
            startTournament(matches);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Single Elimination");
            alert.setHeaderText("Single Elimination");
            alert.setContentText("Not enought Players for this Tournament.");
            alert.showAndWait();
        }
    }

    // this method checks whether we have enough player for a tournament or not.
    private boolean enoughPlayersForTournament(int players) {
        // a elimination bracket tournament can have players with the increasing power of two
        // For example, 2, 4, 8, 16, 32 etc.
        if (players < 2) {
            return false;
        }
        while (players > 1) {
            if (players % 2 == 0) {
                players = players / 2;
            } else {
                return false;
            }
        }
        return true;
    }

    // schedule the single elimination bracket tournament between the given list of players
    private ArrayList<Match> singleElimination(ArrayList<Player> allPlayers) {

        ArrayList<Match> matches = new ArrayList<>();
        while (!allPlayers.isEmpty()) {
            matches.add(new Match(allPlayers.remove(0), allPlayers.remove(0), "Round 1"));
        }
        return matches;

    }

    // this method randomizes / shuffles the players names in the given list
    private ArrayList<Player> randomizePlayers(Player[] players) {

        ArrayList<Player> temp = new ArrayList<>();
        temp.addAll(Arrays.asList(players));
        ArrayList<Player> randomPlayers = new ArrayList<>();
        Random rand = new Random();
        int index;
        while (!temp.isEmpty()) {
            index = rand.nextInt(temp.size());
            randomPlayers.add(temp.remove(index));
        }

        return randomPlayers;
    }


    // this method is called when round robbin button is pressed
    @FXML
    private void roundRobbinButtonPressed(ActionEvent event) {
        // get the players
        Player players[] = new Player[playersList.size()];
        players = playersList.toArray(players);
        int totalPlayers = players.length;
        if (totalPlayers>3) {
            // shuffle the players
            ArrayList<Player> allPlayers = randomizePlayers(players);
            // assign the matches for first round
            ArrayList<Match> matches = roundRobbin(allPlayers);
            // start the tournament
            startTournament(matches);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Round Robbin");
            alert.setHeaderText("Round Robbin");
            alert.setContentText("There should be at least 3 players in a Round Robbin Tournament.");
            alert.showAndWait();
        }
    }

    // schedule the round robbin bracket tournament between the given list of players
    private ArrayList<Match> roundRobbin(ArrayList<Player> allPlayers) {

        ArrayList<Match> matches
                = new ArrayList<>(allPlayers.size() * allPlayers.size() - 1);
        
        for (int i = 0; i < allPlayers.size(); i++) {
            for (int j = 0; j < allPlayers.size(); j++) {
                if (i != j) {
                    matches.add(new Match(allPlayers.get(i), allPlayers.get(j), "Round 1"));
                }
            }
        }

        return matches;
    }

    // this method starts the tournament
    private void startTournament(ArrayList<Match> matches) {
        try {
            // load the second screen .i.e. "Tournament.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tournament.fxml"));
            Parent root = loader.load();
            TournamentController controller = loader.getController();
            controller.setMatches(matches);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.WARNING, ex.getMessage());
            alert.showAndWait();
        }
    }

    // show scores of a player in a pop up dialog when "Show Score" button is pressed
    @FXML
    private void showScoresButtonPressed(ActionEvent event) {
        Player player = allPlayersTable.getSelectionModel().getSelectedItem();
        if (player != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Player`s Info");
            alert.setHeaderText(player.getName());
            alert.setContentText(player.getFulldetails());
            alert.showAndWait();
        }
    }

}
