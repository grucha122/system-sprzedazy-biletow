package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.jdbc.DatabaseConnector;

public class Main extends Application {

    // Aby móc pobrać primaryStage w oknie otwieranym z poziomu kontrolera
    //utworzono zmienną i funkcję statyczną

    private static Stage ps;

    public static Stage getPrimaryStage(){
        return ps;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        ps = primaryStage;

        primaryStage.setTitle("CineTic Dealer");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1024,768));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!DatabaseConnector.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseConnector.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
