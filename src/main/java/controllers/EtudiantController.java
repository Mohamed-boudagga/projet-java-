package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Cours;
import models.Etudiant;
import services.ServiceCours;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur de l'espace Étudiant.
 * Affiche le profil et les cours accessibles selon le niveau.
 */
public class EtudiantController implements Initializable {

    // ── Sidebar ──────────────────────────────────────────────────────
    @FXML private Button btnProfil;
    @FXML private Button btnCours;
    @FXML private Label labelBienvenue;

    // ── Panels ───────────────────────────────────────────────────────
    @FXML private VBox panelProfil;
    @FXML private VBox panelCours;

    // ── Labels Profil ────────────────────────────────────────────────
    @FXML private Label valNom;
    @FXML private Label valPrenom;
    @FXML private Label valEmail;
    @FXML private Label valTelephone;
    @FXML private Label valSexe;
    @FXML private Label valNiveau;
    @FXML private Label valPoints;

    // ── Tableau Cours ────────────────────────────────────────────────
    @FXML private TableView<Cours>       tableCours;
    @FXML private TableColumn<Cours,String>  colTitre;
    @FXML private TableColumn<Cours,String>  colDescription;
    @FXML private TableColumn<Cours,Integer> colNiveau;

    private final ServiceCours serviceCours = new ServiceCours();
    private Etudiant etudiant;

    // ────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        etudiant = Session.getEtudiantConnecte();
        if (etudiant == null) {
            App.ouvrirScene("Login");
            return;
        }

        // Remplir le profil
        labelBienvenue.setText("Bonjour, " + etudiant.getPrenom() + " !");
        valNom      .setText(etudiant.getNom());
        valPrenom   .setText(etudiant.getPrenom());
        valEmail    .setText(etudiant.getEmail());
        valTelephone.setText(etudiant.getTelephone() != null ? etudiant.getTelephone() : "—");
        valSexe     .setText(
            "M".equals(etudiant.getSexe()) ? "Masculin" :
            "F".equals(etudiant.getSexe()) ? "Féminin"  : "—"
        );
        valNiveau   .setText(String.valueOf(etudiant.getNiveau()));
        valPoints   .setText(etudiant.getPoints() + " pts");

        // Configurer tableau des cours
        colTitre      .setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colNiveau     .setCellValueFactory(new PropertyValueFactory<>("niveauRequis"));
    }

    // ================================================================
    // NAVIGATION
    // ================================================================
    @FXML
    private void afficherProfil(ActionEvent e) {
        panelProfil.setVisible(true);  panelProfil.setManaged(true);
        panelCours .setVisible(false); panelCours .setManaged(false);
        updateSidebarStyles(btnProfil);
    }

    @FXML
    private void afficherCours(ActionEvent e) {
        panelProfil.setVisible(false); panelProfil.setManaged(false);
        panelCours .setVisible(true);  panelCours .setManaged(true);
        updateSidebarStyles(btnCours);

        // Charger les cours accessibles au niveau de l'étudiant
        tableCours.setItems(FXCollections.observableArrayList(
                serviceCours.getCoursAccessibles(etudiant.getNiveau())
        ));
    }

    private void updateSidebarStyles(Button activeButton) {
        Button[] buttons = {btnProfil, btnCours};
        for (Button b : buttons) {
            if (b == activeButton) {
                b.setStyle("-fx-background-color:#e94560; -fx-text-fill:white; " +
                           "-fx-font-size:13px; -fx-background-radius:6; -fx-padding:10; " +
                           "-fx-cursor:hand; -fx-alignment:CENTER-LEFT;");
            } else {
                b.setStyle("-fx-background-color:transparent; -fx-text-fill:#a8a8b3; " +
                           "-fx-font-size:13px; -fx-background-radius:6; -fx-padding:10; " +
                           "-fx-cursor:hand; -fx-alignment:CENTER-LEFT;");
            }
        }
    }

    @FXML
    private void handleDeconnexion(ActionEvent e) {
        Session.vider();
        App.ouvrirScene("Login");
    }
}
