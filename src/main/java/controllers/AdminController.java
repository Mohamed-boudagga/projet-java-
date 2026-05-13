package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Etudiant;
import services.ServiceEtudiant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private StackPane contentArea;
    @FXML private VBox viewEtudiants;
    @FXML private TableView<Etudiant> tableEtudiants;
    @FXML private TableColumn<Etudiant, String> colNom;
    @FXML private TableColumn<Etudiant, String> colPrenom;
    @FXML private TableColumn<Etudiant, String> colEmail;
    @FXML private TableColumn<Etudiant, Integer> colNiveau;
    @FXML private TableColumn<Etudiant, Integer> colPoints;

    private final ServiceEtudiant serviceEtudiant = new ServiceEtudiant();
    private ObservableList<Etudiant> listeEtudiants = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));

        chargerEtudiants();
    }

    private void chargerEtudiants() {
        listeEtudiants.setAll(serviceEtudiant.getAll());
        tableEtudiants.setItems(listeEtudiants);
    }

    @FXML
    private void afficherEtudiants(ActionEvent event) {
        viewEtudiants.setVisible(true);
        viewEtudiants.setManaged(true);
    }

    // --- NAVIGATION VERS VOS JEUX SKILLQUEST ---
    @FXML
    private void handleGoToGames(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminGames.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("SkillQuest - Gestion des Jeux");
            stage.setScene(new Scene(root, 1200, 800));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- NAVIGATION VERS EXAMENS DE MOHAMED ---
    @FXML
    private void handleGoToExams(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterEtudiant(ActionEvent event) {
        // Logique d'ajout étudiant de Mohamed
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
