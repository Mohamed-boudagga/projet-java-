package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminDashboardController {

    // --- FONCTIONNALITÉS SKILLQUEST ---
    
    @FXML
    private void handleGoToGames(ActionEvent event) {
        openGamesView((Node) event.getSource());
    }

    @FXML
    private void handleGoToGamesFromMouse(MouseEvent event) {
        openGamesView((Node) event.getSource());
    }

    private void openGamesView(Node source) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminGames.fxml"));
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setTitle("SkillQuest - Gestion des Jeux");
            stage.setScene(new Scene(root, 1200, 800));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- FONCTIONNALITÉS EXAMENS & CERTIFICATIONS (MOHAMED) ---

    @FXML
    public void ouvrirGestionExam(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GestionExam.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ouvrirGestionCert(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GestionCertification.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ouvrirVueEtudiant(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SelectionNiveau.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deconnexion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
