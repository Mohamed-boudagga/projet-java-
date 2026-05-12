package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import models.Etudiant;
import models.ProgressionCours;
import models.Test;
import services.ServiceBadge;
import services.ServiceEtudiant;
import services.ServiceProgression;
import services.ServiceTest;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur de l'espace Étudiant.
 */
public class EtudiantController implements Initializable {

    @FXML private Label nomComplet, emailEtudiant, labelNiveau, pointsLabel, avatarInitial;
    @FXML private ProgressBar levelProgress;
    @FXML private FlowPane badgesPane;
    @FXML private Button btnProfil, btnCours, btnTests, btnClassement;

    // Sections
    @FXML private VBox sectionProfil, sectionCours, sectionTests, sectionClassement;

    // Tableaux
    @FXML private TableView<ProgressionCours> tableCoursProgression;
    @FXML private TableColumn<ProgressionCours, String> colCoursTitre;
    @FXML private TableColumn<ProgressionCours, Integer> colCoursProgression;
    @FXML private TableColumn<ProgressionCours, String> colCoursStatut;
    @FXML private TableColumn<ProgressionCours, Void> colCoursActions;

    @FXML private TableView<Test> tableTestsDispo;
    @FXML private TableColumn<Test, String> colTestTitre;
    @FXML private TableColumn<Test, Integer> colTestScoreMin;
    @FXML private TableColumn<Test, Void> colTestActions;

    @FXML private TableView<Etudiant> tableClassement;
    @FXML private TableColumn<Etudiant, Integer> colRang;
    @FXML private TableColumn<Etudiant, String> colLeaderNom;
    @FXML private TableColumn<Etudiant, Integer> colLeaderPoints;
    @FXML private TableColumn<Etudiant, Integer> colLeaderNiveau;

    private final ServiceEtudiant   serviceEtudiant   = new ServiceEtudiant();
    private final ServiceProgression serviceProgression = new ServiceProgression();
    private final ServiceBadge       serviceBadge       = new ServiceBadge();
    private final ServiceTest        serviceTest        = new ServiceTest();

    private Etudiant etudiant;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.etudiant = Session.getEtudiantConnecte();
        if (etudiant == null) return;

        chargerDonneesProfil();
        configurerTables();
        
        // Mode par défaut
        afficherProfil(null);
    }

    private void chargerDonneesProfil() {
        nomComplet.setText(etudiant.getPrenom() + " " + etudiant.getNom());
        emailEtudiant.setText(etudiant.getEmail());
        avatarInitial.setText(etudiant.getNom().substring(0, 1).toUpperCase());
        pointsLabel.setText(String.valueOf(etudiant.getPoints()));
        
        // Calcul Niveau & Progrès
        int lvl = (etudiant.getPoints() / 1000) + 1;
        double progress = (etudiant.getPoints() % 1000) / 1000.0;
        
        labelNiveau.setText("Lvl " + lvl);
        levelProgress.setProgress(progress);

        // Charger Badges
        badgesPane.getChildren().clear();
        serviceBadge.getBadgesEtudiant(etudiant.getId()).forEach(b -> {
            Label badgeLabel = new Label(b.getNom());
            badgeLabel.setStyle("-fx-background-color:#0f3460; -fx-text-fill:white; -fx-padding:5 10; -fx-background-radius:15; -fx-font-size:11px;");
            badgesPane.getChildren().add(badgeLabel);
        });
    }

    private void configurerTables() {
        // Table Cours
        colCoursTitre.setCellValueFactory(new PropertyValueFactory<>("titreCours"));
        colCoursStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        colCoursProgression.setCellFactory(column -> new TableCell<>() {
            private final ProgressBar pb = new ProgressBar();
            { pb.setPrefWidth(100); pb.setStyle("-fx-accent: #27ae60;"); }
            @Override protected void updateItem(Integer progress, boolean empty) {
                super.updateItem(progress, empty);
                if (empty) setGraphic(null);
                else {
                    ProgressionCours p = getTableView().getItems().get(getIndex());
                    pb.setProgress(p.getPourcentage() / 100.0);
                    setGraphic(pb);
                }
            }
        });

        colCoursActions.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Continuer");
            {
                btn.setStyle("-fx-background-color:#e94560; -fx-text-fill:white; -fx-background-radius:5; -fx-cursor:hand;");
                btn.setOnAction(ev -> System.out.println("Lancer cours..."));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Table Tests
        colTestTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colTestScoreMin.setCellValueFactory(new PropertyValueFactory<>("scoreMin"));
        colTestActions.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Lancer");
            {
                btn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-background-radius:5; -fx-cursor:hand;");
                btn.setOnAction(ev -> System.out.println("Lancer test..."));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Table Classement
        colRang.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else setText(String.valueOf(getIndex() + 1));
            }
        });
        colLeaderNom.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getPrenom() + " " + e.getValue().getNom()));
        colLeaderPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        colLeaderNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
    }

    @FXML
    private void afficherProfil(ActionEvent e) {
        setSectionsVisibility(sectionProfil);
        updateSidebarStyles(btnProfil);
    }

    @FXML
    private void ouvrirFormInscription(ActionEvent e) {
        App.ouvrirFenetreModal("InscrireCours", "Inscription à un cours");
        afficherCours(null); // Rafraîchir après inscription
    }

    @FXML
    private void afficherCours(ActionEvent e) {
        setSectionsVisibility(sectionCours);
        updateSidebarStyles(btnCours);
        tableCoursProgression.setItems(FXCollections.observableArrayList(serviceProgression.getProgressionByEtudiant(etudiant.getId())));
    }

    @FXML
    private void afficherTests(ActionEvent e) {
        setSectionsVisibility(sectionTests);
        updateSidebarStyles(btnTests);
        tableTestsDispo.setItems(FXCollections.observableArrayList(serviceTest.getAll()));
    }

    @FXML
    private void afficherClassement(ActionEvent e) {
        setSectionsVisibility(sectionClassement);
        updateSidebarStyles(btnClassement);
        tableClassement.setItems(FXCollections.observableArrayList(serviceEtudiant.getClassement()));
    }

    private void setSectionsVisibility(VBox activeSection) {
        sectionProfil.setVisible(activeSection == sectionProfil);
        sectionProfil.setManaged(activeSection == sectionProfil);
        sectionCours.setVisible(activeSection == sectionCours);
        sectionCours.setManaged(activeSection == sectionCours);
        sectionTests.setVisible(activeSection == sectionTests);
        sectionTests.setManaged(activeSection == sectionTests);
        sectionClassement.setVisible(activeSection == sectionClassement);
        sectionClassement.setManaged(activeSection == sectionClassement);
    }

    private void updateSidebarStyles(Button activeBtn) {
        Button[] buttons = {btnProfil, btnCours, btnTests, btnClassement};
        for (Button b : buttons) {
            if (b == null) continue;
            if (b == activeBtn) {
                b.setStyle("-fx-background-color:#e94560; -fx-text-fill:white; -fx-alignment:CENTER_LEFT; -fx-padding:12 20;");
            } else {
                b.setStyle("-fx-background-color:transparent; -fx-text-fill:#a8a8b3; -fx-alignment:CENTER_LEFT; -fx-padding:12 20;");
            }
        }
    }

    @FXML
    private void handleDeconnexion(ActionEvent e) {
        Session.setEtudiantConnecte(null);
        App.ouvrirScene("Login");
    }
}
