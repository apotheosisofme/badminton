/*
 Jeff Schott and Atika Ishtiaq
 CIS 2348
 Dr. Peggy Lindner
 Wed. 1-4 pm
 */


//necessary imports

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


// this is the Main class which runs the application

public class Main extends Application {

    // the Stage of the JavaFX Application
    public static Stage mainStage;


    // the start provided by Application class which is called when the JavaFX application starts
    @Override
    public void start(Stage stage) throws Exception {
        
        mainStage = stage;

        // Load the "Main.fxml" file which contains the GUI Layout of the Home Screen
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        // create the scene
        Scene scene = new Scene(root);
        // set the stage title
        stage.setTitle("Badminton Tournament");
        // set the scene on stage
        stage.setScene(scene);
        // set the stage to be visible to the user
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
