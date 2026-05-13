package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Point d'entrée original : portail de sélection (contient connexion Google)
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginSelection.fxml"));
        primaryStage.setTitle("SkillQuest - Portail de Connexion");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
