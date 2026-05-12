package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Admin;
import models.Etudiant;
import services.ServiceAdmin;
import services.ServiceEtudiant;
import utils.PasswordUtils;

/**
 * Contrôleur de la page de connexion.
 * Gère le login pour les deux rôles : Étudiant et Admin.
 */
public class LoginController {

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton   radioEtudiant;
    @FXML private RadioButton   radioAdmin;
    @FXML private Label         erreurLabel;
    @FXML private CheckBox      captchaCheckBox;

    private final ServiceAdmin    serviceAdmin    = new ServiceAdmin();
    private final ServiceEtudiant serviceEtudiant = new ServiceEtudiant();

    // ----------------------------------------------------------------
    // Connexion
    // ----------------------------------------------------------------
    @FXML
    private void handleConnexion(ActionEvent event) {
        String email = emailField.getText().trim();
        String mdp   = passwordField.getText().trim();

        // Vérification CAPTCHA
        if (!captchaCheckBox.isSelected()) {
            afficherErreur("Veuillez confirmer que vous n'êtes pas un robot.");
            return;
        }

        // Validation de base
        if (email.isEmpty() || mdp.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        if (radioAdmin.isSelected()) {
            // --- Connexion Admin ---
            Admin admin = serviceAdmin.getByEmail(email);
            if (admin != null && PasswordUtils.checkPassword(mdp, admin.getMotDePasse())) {
                // Stocker l'admin connecté dans la session
                Session.setAdminConnecte(admin);
                App.ouvrirScene("AdminDashboard");
            } else {
                afficherErreur("Email ou mot de passe incorrect (Admin).");
            }

        } else {
            // --- Connexion Étudiant ---
            Etudiant etudiant = serviceEtudiant.getByEmail(email);
            if (etudiant == null) {
                afficherErreur("Aucun compte trouvé avec cet email.");
                return;
            }
            if (etudiant.isEstBloque()) {
                afficherErreur("Votre compte est bloqué. Contactez l'administrateur.");
                return;
            }
            if (!PasswordUtils.checkPassword(mdp, etudiant.getMotDePasse())) {
                afficherErreur("Mot de passe incorrect.");
                return;
            }
            // Connexion réussie
            Session.setEtudiantConnecte(etudiant);
            App.ouvrirScene("EtudiantDashboard");
        }
    }

    // ----------------------------------------------------------------
    // Inscription → ouvre le formulaire d'ajout étudiant
    // ----------------------------------------------------------------
    @FXML
    private void handleInscription(ActionEvent event) {
        // Ouvrir en modal pour ne PAS fermer la scène de connexion
        App.ouvrirFenetreModal("AjouterEtudiant", "Inscription Étudiant");
    }

    // ----------------------------------------------------------------
    // Utilitaire
    // ----------------------------------------------------------------
    private void afficherErreur(String msg) {
        erreurLabel.setText(msg);
        erreurLabel.setStyle("-fx-text-fill:#e94560; -fx-font-size:12px;");
    }
}
