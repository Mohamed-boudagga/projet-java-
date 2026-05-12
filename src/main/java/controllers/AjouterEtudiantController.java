package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Etudiant;
import services.ServiceEtudiant;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur du formulaire Ajouter / Modifier un Étudiant.
 * Utilisé par l'Admin (modification) et pour l'inscription libre.
 */
public class AjouterEtudiantController implements Initializable {

    @FXML private Label            titreForm;
    @FXML private TextField        nomField;
    @FXML private TextField        prenomField;
    @FXML private TextField        emailField;
    @FXML private PasswordField    passwordField;
    @FXML private TextField        telephoneField;
    @FXML private RadioButton      radioM;
    @FXML private RadioButton      radioF;
    @FXML private Spinner<Integer> niveauSpinner;
    @FXML private Label            messageLabel;
    @FXML private Button           btnSauvegarder;

    private final ServiceEtudiant service = new ServiceEtudiant();

    /** null = mode création / non-null = mode modification */
    private Etudiant etudiantEnModification = null;

    // ────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        niveauSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    }

    // ================================================================
    // CHARGEMENT EN MODE MODIFICATION
    // ================================================================
    /**
     * Appelé par AdminController pour pré-remplir le formulaire.
     */
    public void chargerEtudiant(Etudiant e) {
        this.etudiantEnModification = e;
        titreForm.setText("Modifier l'Étudiant");
        btnSauvegarder.setText("Mettre à jour");

        nomField      .setText(e.getNom());
        prenomField   .setText(e.getPrenom());
        emailField    .setText(e.getEmail());
        passwordField .setText(e.getMotDePasse());
        telephoneField.setText(e.getTelephone() != null ? e.getTelephone() : "");
        niveauSpinner .getValueFactory().setValue(e.getNiveau());

        if ("F".equals(e.getSexe())) radioF.setSelected(true);
        else                         radioM.setSelected(true);
    }

    // ================================================================
    // SAUVEGARDER (Ajouter OU Modifier)
    // ================================================================
    @FXML
    private void handleSauvegarder(ActionEvent e) {
        String nom       = nomField.getText().trim();
        String prenom    = prenomField.getText().trim();
        String email     = emailField.getText().trim();
        String mdp       = passwordField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String sexe      = radioF.isSelected() ? "F" : "M";
        int    niveau    = niveauSpinner.getValue();

        // ── Validations ──────────────────────────────────────────────
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            afficherErreur("Les champs Nom, Prénom, Email et Mot de passe sont obligatoires.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            afficherErreur("Email invalide.");
            return;
        }
        if (mdp.length() < 6) {
            afficherErreur("Le mot de passe doit avoir au moins 6 caractères.");
            return;
        }
        if (!telephone.isEmpty() && !telephone.matches("\\d{8}")) {
            afficherErreur("Le téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        // ── Sauvegarde ───────────────────────────────────────────────
        if (etudiantEnModification == null) {
            // MODE CRÉATION
            Etudiant nouvel = new Etudiant(
                    nom, prenom, email, mdp, niveau, 0, false,
                    telephone.isEmpty() ? null : telephone, sexe);
            service.add(nouvel);
            afficherSucces("Étudiant ajouté avec succès !");
        } else {
            // MODE MODIFICATION
            etudiantEnModification.setNom(nom);
            etudiantEnModification.setPrenom(prenom);
            etudiantEnModification.setEmail(email);
            etudiantEnModification.setMotDePasse(mdp);
            etudiantEnModification.setTelephone(telephone.isEmpty() ? null : telephone);
            etudiantEnModification.setSexe(sexe);
            etudiantEnModification.setNiveau(niveau);
            service.update(etudiantEnModification);
            afficherSucces("Étudiant mis à jour !");
        }

        fermerFenetre();
    }

    // ================================================================
    // ANNULER
    // ================================================================
    @FXML
    private void handleAnnuler(ActionEvent e) {
        fermerFenetre();
    }

    // ================================================================
    // UTILITAIRES
    // ================================================================
    private void afficherErreur(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill:#e94560; -fx-font-size:12px;");
    }

    private void afficherSucces(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill:#27ae60; -fx-font-size:12px;");
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnSauvegarder.getScene().getWindow();
        stage.close();
    }
}
