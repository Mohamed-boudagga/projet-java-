package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Admin;
import models.Etudiant;
import services.ServiceAdmin;
import services.ServiceEtudiant;
import utils.PasswordUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton radioEtudiant;
    @FXML private RadioButton radioAdmin;
    @FXML private Label erreurLabel;
    @FXML private CheckBox captchaCheckBox;

    private final ServiceAdmin serviceAdmin = new ServiceAdmin();
    private final ServiceEtudiant serviceEtudiant = new ServiceEtudiant();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation si nécessaire
    }

    @FXML
    private void handleConnexion(ActionEvent event) {
        String email = emailField.getText().trim();
        String mdp = passwordField.getText().trim();

        if (!captchaCheckBox.isSelected()) {
            afficherErreur("Veuillez confirmer que vous n'êtes pas un robot.");
            return;
        }

        if (email.isEmpty() || mdp.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        if (radioAdmin.isSelected()) {
            Admin admin = serviceAdmin.getByEmail(email);
            if (admin != null && PasswordUtils.checkPassword(mdp, admin.getMotDePasse())) {
                Session.setAdminConnecte(admin);
                App.ouvrirScene("AdminDashboard");
            } else {
                afficherErreur("Email ou mot de passe incorrect (Admin).");
            }
        } else {
            Etudiant etudiant = serviceEtudiant.getByEmail(email);
            if (etudiant == null) {
                afficherErreur("Aucun compte trouvé.");
                return;
            }
            if (!PasswordUtils.checkPassword(mdp, etudiant.getMotDePasse())) {
                afficherErreur("Mot de passe incorrect.");
                return;
            }
            Session.setEtudiantConnecte(etudiant);
            App.ouvrirScene("SelectionNiveau");
        }
    }

    private void afficherErreur(String msg) {
        erreurLabel.setText(msg);
        erreurLabel.setStyle("-fx-text-fill:#f45b69;");
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        App.ouvrirFenetreModal("AjouterEtudiant", "Inscription");
    }
}
