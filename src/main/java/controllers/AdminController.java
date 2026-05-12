package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.*;
import services.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Contrôleur du tableau de bord Admin.
 * Gère : liste des étudiants, blocage/déblocage, ajout/modif/suppression,
 * et la liste des cours.
 */
public class AdminController implements Initializable {

    // ── Sidebar ──────────────────────────────────────────────────────
    @FXML private Button btnEtudiants;
    @FXML private Button btnCours;
    @FXML private Button btnTests;
    @FXML private Button btnCertificats;
    @FXML private Button btnJeux;
    @FXML private Label  titreSection;

    // ── Barre de recherche ───────────────────────────────────────────
    @FXML private TextField rechercheField;
    @FXML private Button    btnAjouter;

    // ── Tableau Étudiants ────────────────────────────────────────────
    @FXML private TableView<Etudiant>       tableEtudiants;
    @FXML private TableColumn<Etudiant,Integer> colId;
    @FXML private TableColumn<Etudiant,String>  colNom;
    @FXML private TableColumn<Etudiant,String>  colPrenom;
    @FXML private TableColumn<Etudiant,String>  colEmail;
    @FXML private TableColumn<Etudiant,String>  colTelephone;
    @FXML private TableColumn<Etudiant,String>  colSexe;
    @FXML private TableColumn<Etudiant,Integer> colNiveau;
    @FXML private TableColumn<Etudiant,Boolean> colBloque;
    @FXML private TableColumn<Etudiant,Void>    colActions;

    // ── Tableau Cours ────────────────────────────────────────────────
    @FXML private TableView<Cours>       tableCours;
    @FXML private TableColumn<Cours,Integer> colCoursId;
    @FXML private TableColumn<Cours,String>  colCoursTitre;
    @FXML private TableColumn<Cours,Integer> colCoursNiveau;
    @FXML private TableColumn<Cours,Integer> colCoursAdmin;
    @FXML private TableColumn<Cours,Void>    colCoursActions;

    // ── Tableau Tests ────────────────────────────────────────────────
    @FXML private TableView<Test>        tableTests;
    @FXML private TableColumn<Test,Integer>  colTestId;
    @FXML private TableColumn<Test,String>   colTestTitre;
    @FXML private TableColumn<Test,Integer>  colTestScore;
    @FXML private TableColumn<Test,Integer>  colTestCours;
    @FXML private TableColumn<Test,Void>     colTestActions;

    // ── Tableau Certificats ──────────────────────────────────────────
    @FXML private TableView<Certificat>   tableCertificats;
    @FXML private TableColumn<Certificat,Integer> colCertifId;
    @FXML private TableColumn<Certificat,String>  colCertifNom;
    @FXML private TableColumn<Certificat,Integer> colCertifTest;
    @FXML private TableColumn<Certificat,Void>    colCertifActions;

    // ── Tableau Jeux ─────────────────────────────────────────────────
    @FXML private TableView<Jeu>         tableJeux;
    @FXML private TableColumn<Jeu,Integer> colJeuId;
    @FXML private TableColumn<Jeu,String>  colJeuNom;
    @FXML private TableColumn<Jeu,String>  colJeuType;
    @FXML private TableColumn<Jeu,Void>    colJeuActions;

    private final ServiceEtudiant   serviceEtudiant   = new ServiceEtudiant();
    private final ServiceCours      serviceCours      = new ServiceCours();
    private final ServiceTest       serviceTest       = new ServiceTest();
    private final ServiceCertificat serviceCertificat = new ServiceCertificat();
    private final ServiceJeu        serviceJeu        = new ServiceJeu();

    private ObservableList<Etudiant>   listeEtudiants   = FXCollections.observableArrayList();
    private ObservableList<Cours>      listeCours       = FXCollections.observableArrayList();
    private ObservableList<Test>       listeTests       = FXCollections.observableArrayList();
    private ObservableList<Certificat> listeCertificats = FXCollections.observableArrayList();
    private ObservableList<Jeu>        listeJeux        = FXCollections.observableArrayList();

    private String currentMode = "ETUDIANTS";

    // ────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurerColonnesEtudiants();
        configurerColonnesCours();
        configurerColonnesTests();
        configurerColonnesCertificats();
        configurerColonnesJeux();
        chargerEtudiants();
    }

    // ================================================================
    // NAVIGATION SIDEBAR
    // ================================================================
    @FXML
    private void afficherEtudiants(ActionEvent e) {
        currentMode = "ETUDIANTS";
        titreSection.setText("Gestion des Étudiants");
        setTablesVisibility(tableEtudiants);
        updateSidebarStyles(btnEtudiants);
        chargerEtudiants();
    }

    @FXML
    private void afficherCours(ActionEvent e) {
        currentMode = "COURS";
        titreSection.setText("Gestion des Cours");
        setTablesVisibility(tableCours);
        updateSidebarStyles(btnCours);
        chargerCours();
    }

    @FXML
    private void afficherTests(ActionEvent e) {
        currentMode = "TESTS";
        titreSection.setText("Gestion des Tests");
        setTablesVisibility(tableTests);
        updateSidebarStyles(btnTests);
        chargerTests();
    }

    @FXML
    private void afficherCertificats(ActionEvent e) {
        currentMode = "CERTIFICATS";
        titreSection.setText("Gestion des Certificats");
        setTablesVisibility(tableCertificats);
        updateSidebarStyles(btnCertificats);
        chargerCertificats();
    }

    @FXML
    private void afficherJeux(ActionEvent e) {
        currentMode = "JEUX";
        titreSection.setText("Gestion des Jeux");
        setTablesVisibility(tableJeux);
        updateSidebarStyles(btnJeux);
        chargerJeux();
    }

    private void setTablesVisibility(TableView<?> activeTable) {
        tableEtudiants.setVisible(activeTable == tableEtudiants);
        tableEtudiants.setManaged(activeTable == tableEtudiants);
        tableCours.setVisible(activeTable == tableCours);
        tableCours.setManaged(activeTable == tableCours);
        tableTests.setVisible(activeTable == tableTests);
        tableTests.setManaged(activeTable == tableTests);
        tableCertificats.setVisible(activeTable == tableCertificats);
        tableCertificats.setManaged(activeTable == tableCertificats);
        tableJeux.setVisible(activeTable == tableJeux);
        tableJeux.setManaged(activeTable == tableJeux);
    }

    private void updateSidebarStyles(Button activeButton) {
        Button[] buttons = {btnEtudiants, btnCours, btnTests, btnCertificats, btnJeux};
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

    // ================================================================
    // AJOUTER (étudiant ou cours selon le mode)
    // ================================================================
    @FXML
    private void handleAjouter(ActionEvent e) {
        switch (currentMode) {
            case "ETUDIANTS":
                App.ouvrirFenetreModal("AjouterEtudiant", "Ajouter un Étudiant");
                chargerEtudiants();
                break;
            case "COURS":
                FXMLLoader loaderC = App.ouvrirFenetreModalAvecLoader("AjouterCours", "Ajouter un Cours");
                if (loaderC != null) {
                    AjouterCoursController ctrl = loaderC.getController();
                    ctrl.setAdminId(Session.getAdminConnecte().getId());
                    App.getStageFromLoader(loaderC).showAndWait();
                    chargerCours();
                }
                break;
            case "TESTS":
                App.ouvrirFenetreModal("AjouterTest", "Ajouter un Test");
                chargerTests();
                break;
            case "CERTIFICATS":
                App.ouvrirFenetreModal("AjouterCertificat", "Ajouter un Certificat");
                chargerCertificats();
                break;
            case "JEUX":
                App.ouvrirFenetreModal("AjouterJeu", "Ajouter un Jeu");
                chargerJeux();
                break;
        }
    }

    // ================================================================
    // RECHERCHE EN TEMPS RÉEL
    // ================================================================
    @FXML
    private void handleRecherche() {
        String filtre = rechercheField.getText().trim().toLowerCase();
        switch (currentMode) {
            case "ETUDIANTS":
                tableEtudiants.setItems(listeEtudiants.filtered(et -> 
                    et.getNom().toLowerCase().contains(filtre) || 
                    et.getPrenom().toLowerCase().contains(filtre) || 
                    et.getEmail().toLowerCase().contains(filtre)));
                break;
            case "COURS":
                tableCours.setItems(listeCours.filtered(c -> 
                    c.getTitre().toLowerCase().contains(filtre)));
                break;
            case "TESTS":
                tableTests.setItems(listeTests.filtered(t -> 
                    t.getTitre().toLowerCase().contains(filtre)));
                break;
            case "CERTIFICATS":
                tableCertificats.setItems(listeCertificats.filtered(c -> 
                    c.getNom().toLowerCase().contains(filtre)));
                break;
            case "JEUX":
                tableJeux.setItems(listeJeux.filtered(j -> 
                    j.getNom().toLowerCase().contains(filtre)));
                break;
        }
    }

    // ================================================================
    // CONFIGURATION TABLEAU ÉTUDIANTS
    // ================================================================
    private void configurerColonnesEtudiants() {
        colId       .setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom      .setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom   .setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail    .setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTelephone()));
        colSexe     .setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getSexe()));
        colNiveau   .setCellValueFactory(new PropertyValueFactory<>("niveau"));
        colBloque   .setCellValueFactory(new PropertyValueFactory<>("estBloque"));

        // Colonne Actions : Modifier | Bloquer/Débloquer | Supprimer
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnModif  = new Button("✏");
            private final Button btnBloc   = new Button("🔒");
            private final Button btnSuppr  = new Button("🗑");
            private final HBox   boite     = new HBox(5, btnModif, btnBloc, btnSuppr);

            {
                String base = "-fx-background-radius:5; -fx-cursor:hand; -fx-padding:4 8 4 8;";
                btnModif.setStyle(base + "-fx-background-color:#0f3460; -fx-text-fill:white;");
                btnBloc .setStyle(base + "-fx-background-color:#e94560; -fx-text-fill:white;");
                btnSuppr.setStyle(base + "-fx-background-color:#c0392b; -fx-text-fill:white;");

                btnModif.setOnAction(ev -> {
                    Etudiant et = getTableView().getItems().get(getIndex());
                    ouvrirFormModifEtudiant(et);
                });
                btnBloc.setOnAction(ev -> {
                    Etudiant et = getTableView().getItems().get(getIndex());
                    if (et.isEstBloque()) {
                        serviceEtudiant.debloquer(et.getId());
                        btnBloc.setText("🔒");
                    } else {
                        serviceEtudiant.bloquer(et.getId());
                        btnBloc.setText("🔓");
                    }
                    chargerEtudiants();
                });
                btnSuppr.setOnAction(ev -> {
                    Etudiant et = getTableView().getItems().get(getIndex());
                    if (confirmerSuppression(et.getNom() + " " + et.getPrenom())) {
                        serviceEtudiant.delete(et);
                        chargerEtudiants();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Etudiant et = getTableView().getItems().get(getIndex());
                    btnBloc.setText(et.isEstBloque() ? "🔓" : "🔒");
                    setGraphic(boite);
                }
            }
        });

        tableEtudiants.setItems(listeEtudiants);
    }

    // ================================================================
    // CONFIGURATION TABLEAU COURS
    // ================================================================
    private void configurerColonnesCours() {
        colCoursId    .setCellValueFactory(new PropertyValueFactory<>("id"));
        colCoursTitre .setCellValueFactory(new PropertyValueFactory<>("titre"));
        colCoursNiveau.setCellValueFactory(new PropertyValueFactory<>("niveauRequis"));
        colCoursAdmin .setCellValueFactory(new PropertyValueFactory<>("adminId"));

        colCoursActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnModif = new Button("✏");
            private final Button btnSuppr = new Button("🗑");
            private final HBox   boite   = new HBox(5, btnModif, btnSuppr);

            {
                String base = "-fx-background-radius:5; -fx-cursor:hand; -fx-padding:4 8 4 8;";
                btnModif.setStyle(base + "-fx-background-color:#0f3460; -fx-text-fill:white;");
                btnSuppr.setStyle(base + "-fx-background-color:#c0392b; -fx-text-fill:white;");

                btnModif.setOnAction(ev -> {
                    Cours c = getTableView().getItems().get(getIndex());
                    ouvrirFormModifCours(c);
                });
                btnSuppr.setOnAction(ev -> {
                    Cours c = getTableView().getItems().get(getIndex());
                    if (confirmerSuppression(c.getTitre())) {
                        serviceCours.delete(c);
                        chargerCours();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : boite);
            }
        });

        tableCours.setItems(listeCours);
    }

    // ================================================================
    // CHARGEMENT DES DONNÉES
    // ================================================================
    private void chargerEtudiants() { listeEtudiants.setAll(serviceEtudiant.getAll()); }
    private void chargerCours()     { listeCours.setAll(serviceCours.getAll()); }
    private void chargerTests()     { listeTests.setAll(serviceTest.getAll()); }
    private void chargerCertificats() { listeCertificats.setAll(serviceCertificat.getAll()); }
    private void chargerJeux()      { listeJeux.setAll(serviceJeu.getAll()); }

    // ================================================================
    // CONFIGURATION NOUVEAUX TABLEAUX
    // ================================================================
    private void configurerColonnesTests() {
        colTestId   .setCellValueFactory(new PropertyValueFactory<>("id"));
        colTestTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colTestScore.setCellValueFactory(new PropertyValueFactory<>("scoreMin"));
        colTestCours.setCellValueFactory(new PropertyValueFactory<>("coursId"));
        colTestActions.setCellFactory(col -> createActionCell(
                this::ouvrirFormModifTest,
                t -> { serviceTest.delete(t); chargerTests(); }
        ));
        tableTests.setItems(listeTests);
    }

    private void configurerColonnesCertificats() {
        colCertifId  .setCellValueFactory(new PropertyValueFactory<>("id"));
        colCertifNom .setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCertifTest.setCellValueFactory(new PropertyValueFactory<>("testId"));
        colCertifActions.setCellFactory(col -> createActionCell(
                this::ouvrirFormModifCertificat,
                c -> { serviceCertificat.delete(c); chargerCertificats(); }
        ));
        tableCertificats.setItems(listeCertificats);
    }

    private void configurerColonnesJeux() {
        colJeuId  .setCellValueFactory(new PropertyValueFactory<>("id"));
        colJeuNom .setCellValueFactory(new PropertyValueFactory<>("nom"));
        colJeuType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colJeuActions.setCellFactory(col -> createActionCell(
                this::ouvrirFormModifJeu,
                j -> { serviceJeu.delete(j); chargerJeux(); }
        ));
        tableJeux.setItems(listeJeux);
    }

    private <T> TableCell<T, Void> createActionCell(java.util.function.Consumer<T> editAction, java.util.function.Consumer<T> deleteAction) {
        return new TableCell<>() {
            private final Button btnModif = new Button("✏");
            private final Button btnSuppr = new Button("🗑");
            private final HBox boite = new HBox(5, btnModif, btnSuppr);
            {
                String base = "-fx-background-radius:5; -fx-cursor:hand; -fx-padding:4 8 4 8;";
                btnModif.setStyle(base + "-fx-background-color:#0f3460; -fx-text-fill:white;");
                btnSuppr.setStyle(base + "-fx-background-color:#c0392b; -fx-text-fill:white;");
                btnModif.setOnAction(ev -> {
                    T item = getTableView().getItems().get(getIndex());
                    editAction.accept(item);
                });
                btnSuppr.setOnAction(ev -> {
                    T item = getTableView().getItems().get(getIndex());
                    if (confirmerSuppression("cet élément")) {
                        deleteAction.accept(item);
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : boite);
            }
        };
    }

    // ================================================================
    // OUVRIR FORMULAIRE MODIFICATION
    // ================================================================
    private void ouvrirFormModifEtudiant(Etudiant et) {
        FXMLLoader loader = App.ouvrirFenetreModalAvecLoader("AjouterEtudiant", "Modifier Étudiant");
        if (loader != null) {
            AjouterEtudiantController ctrl = loader.getController();
            ctrl.chargerEtudiant(et);
            App.getStageFromLoader(loader).showAndWait();
            chargerEtudiants();
        }
    }

    private void ouvrirFormModifCours(Cours c) {
        FXMLLoader loader = App.ouvrirFenetreModalAvecLoader("AjouterCours", "Modifier Cours");
        if (loader != null) {
            AjouterCoursController ctrl = loader.getController();
            ctrl.chargerCours(c);
            App.getStageFromLoader(loader).showAndWait();
            chargerCours();
        }
    }

    private void ouvrirFormModifTest(Test t) {
        FXMLLoader loader = App.ouvrirFenetreModalAvecLoader("AjouterTest", "Modifier Test");
        if (loader != null) {
            AjouterTestController ctrl = loader.getController();
            ctrl.chargerTest(t);
            App.getStageFromLoader(loader).showAndWait();
            chargerTests();
        }
    }

    private void ouvrirFormModifCertificat(Certificat c) {
        FXMLLoader loader = App.ouvrirFenetreModalAvecLoader("AjouterCertificat", "Modifier Certificat");
        if (loader != null) {
            AjouterCertificatController ctrl = loader.getController();
            ctrl.chargerCertificat(c);
            App.getStageFromLoader(loader).showAndWait();
            chargerCertificats();
        }
    }

    private void ouvrirFormModifJeu(Jeu j) {
        FXMLLoader loader = App.ouvrirFenetreModalAvecLoader("AjouterJeu", "Modifier Jeu");
        if (loader != null) {
            AjouterJeuController ctrl = loader.getController();
            ctrl.chargerJeu(j);
            App.getStageFromLoader(loader).showAndWait();
            chargerJeux();
        }
    }

    // ================================================================
    // BOITE DE CONFIRMATION SUPPRESSION
    // ================================================================
    private boolean confirmerSuppression(String nom) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer : " + nom);
        alert.setContentText("Cette action est irréversible. Confirmer ?");
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get() == ButtonType.OK;
    }
}
