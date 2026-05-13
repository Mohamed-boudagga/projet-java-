package controllers;

import entities.Games;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.MailingService;
import services.ServiceUser;
import java.util.List;

public class EmailNotificationController {

    @FXML private TextField txtRecipientEmail;
    @FXML private Label lblStatus;

    private Games game;

    public void setGameData(Games game) {
        this.game = game;
        txtRecipientEmail.setText("amalghali.2004@gmail.com"); // Pré-rempli pour le test
    }

    @FXML
    private void handleSend() {
        String email = txtRecipientEmail.getText().trim();
        if (email.isEmpty() || !email.contains("@")) {
            lblStatus.setText("❌ Veuillez saisir un e-mail valide.");
            lblStatus.setStyle("-fx-text-fill: #ef4444;");
            return;
        }

        lblStatus.setText("⏳ Envoi en cours...");
        lblStatus.setStyle("-fx-text-fill: #3a86ff;");

        // Envoi avec l'objet game complet
        MailingService.sendNewGameNotification(email, game);

        lblStatus.setText("✅ Notification envoyée !");
        lblStatus.setStyle("-fx-text-fill: #10b981;");
        
        // Fermeture automatique après 1.5s
        new Thread(() -> {
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            javafx.application.Platform.runLater(() -> ((Stage) txtRecipientEmail.getScene().getWindow()).close());
        }).start();
    }

    @FXML
    private void handleNotifyAll() {
        lblStatus.setText("⏳ Récupération des étudiants...");
        lblStatus.setStyle("-fx-text-fill: #3a86ff;");

        ServiceUser userService = new ServiceUser();
        List<String> emails = userService.getAllStudentEmails();

        if (emails.isEmpty()) {
            lblStatus.setText("⚠️ Aucun étudiant trouvé dans la base.");
            lblStatus.setStyle("-fx-text-fill: #f59e0b;");
            return;
        }

        lblStatus.setText("⏳ Envoi à " + emails.size() + " étudiants...");
        MailingService.sendBulkNotification(emails, (entities.Games) game);

        lblStatus.setText("✅ Notifications groupées envoyées !");
        lblStatus.setStyle("-fx-text-fill: #10b981;");
    }

    @FXML
    private void handleCancel() {
        ((Stage) txtRecipientEmail.getScene().getWindow()).close();
    }
}
