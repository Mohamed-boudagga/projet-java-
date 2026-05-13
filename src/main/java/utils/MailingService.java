package utils;

import entities.Games;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.List;

public class MailingService {

    // --- CONFIGURATION SMTP ---
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "amalghali.2004@gmail.com";
    private static final String SENDER_PASSWORD = "ghalighali22032004";

    public static void sendNewGameNotification(String recipientEmail, Games game) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("🎖️ SkillQuest : Nouveau Défi - " + game.getTypeJeux());

            String description = (game.getDescription() != null) ? game.getDescription() : "Préparez-vous au combat !";
            String timeStr = (game.getTimeLimit() > 0) ? game.getTimeLimit() + " secondes par question" : "Illimité";

            String htmlContent = "<html><body style='font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif; background-color: #0a1628; padding: 20px; color: white;'>"
                + "<div style='max-width: 600px; margin: auto; background-color: #141e33; border: 2px solid #f45b69; border-radius: 15px; overflow: hidden;'>"
                + "  <div style='background-color: #f45b69; padding: 20px; text-align: center;'>"
                + "    <h1 style='margin: 0; color: white; font-size: 24px;'>NOUVEAU DÉFI DISPONIBLE</h1>"
                + "  </div>"
                + "  <div style='padding: 30px;'>"
                + "    <p style='font-size: 18px;'>Bonjour Soldat,</p>"
                + "    <p>L'administration vient de publier un nouvel exercice pour votre niveau :</p>"
                + "    <div style='background-color: #0a1628; padding: 20px; border-left: 4px solid #3a86ff; margin: 20px 0;'>"
                + "      <h2 style='color: #3a86ff; margin-top: 0;'>" + game.getTypeJeux() + "</h2>"
                + "      <p><strong>Difficulté :</strong> " + game.getDifficulte() + "</p>"
                + "      <p><strong>Score Max :</strong> " + game.getScoreMax() + " pts</p>"
                + "      <p><strong>Temps Limite :</strong> " + timeStr + "</p>"
                + "      <p style='border-top: 1px solid #2d3f6b; padding-top: 10px;'><i>" + description + "</i></p>"
                + "    </div>"
                + "    <p style='text-align: center; margin-top: 30px;'>"
                + "      <a href='#' style='background-color: #f45b69; color: white; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px;'>ACCÉDER AU DASHBOARD</a>"
                + "    </p>"
                + "  </div>"
                + "  <div style='background-color: #0a1628; padding: 15px; text-align: center; font-size: 12px; color: #4a6090;'>"
                + "    © 2026 SkillQuest Battle Arena - Système de Notification Automatique"
                + "  </div>"
                + "</div>"
                + "</body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            new Thread(() -> {
                try {
                    Transport.send(message);
                    System.out.println("✅ Notification envoyée avec succès à " + recipientEmail);
                } catch (MessagingException e) {
                    System.err.println("❌ ÉCHEC DE L'ENVOI : " + e.getMessage());
                    if (e.getMessage().contains("535")) {
                        System.err.println("Note: Google a refusé votre mot de passe habituel. Utilisez un 'Mot de passe d'application'.");
                    }
                }
            }).start();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendBulkNotification(List<String> recipientEmails, Games game) {
        if (recipientEmails == null || recipientEmails.isEmpty()) return;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        new Thread(() -> {
            try {
                for (String email : recipientEmails) {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(SENDER_EMAIL));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("🎖️ SkillQuest : Nouveau Défi - " + game.getTypeJeux());

                    String description = (game.getDescription() != null) ? game.getDescription() : "Préparez-vous au combat !";
                    String timeStr = (game.getTimeLimit() > 0) ? game.getTimeLimit() + " secondes par question" : "Illimité";

                    String htmlContent = "<html><body style='font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif; background-color: #0a1628; padding: 20px; color: white;'>"
                        + "<div style='max-width: 600px; margin: auto; background-color: #141e33; border: 2px solid #f45b69; border-radius: 15px; overflow: hidden;'>"
                        + "  <div style='background-color: #f45b69; padding: 20px; text-align: center;'>"
                        + "    <h1 style='margin: 0; color: white; font-size: 24px;'>ALERTE NOUVEAU JEU</h1>"
                        + "  </div>"
                        + "  <div style='padding: 30px;'>"
                        + "    <p style='font-size: 18px;'>Bonjour Soldat,</p>"
                        + "    <p>Un nouveau défi vient d'être ajouté à l'arène :</p>"
                        + "    <div style='background-color: #0a1628; padding: 20px; border-left: 4px solid #3a86ff; margin: 20px 0;'>"
                        + "      <h2 style='color: #3a86ff; margin-top: 0;'>" + game.getTypeJeux() + "</h2>"
                        + "      <p><strong>Difficulté :</strong> " + game.getDifficulte() + "</p>"
                        + "      <p><strong>Score Max :</strong> " + game.getScoreMax() + " pts</p>"
                        + "      <p><i>" + description + "</i></p>"
                        + "    </div>"
                        + "    <p style='text-align: center; margin-top: 30px;'>"
                        + "      <a href='#' style='background-color: #f45b69; color: white; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px;'>REJOINDRE LE DASHBOARD</a>"
                        + "    </p>"
                        + "  </div>"
                        + "</div>"
                        + "</body></html>";

                    message.setContent(htmlContent, "text/html; charset=utf-8");
                    Transport.send(message);
                    System.out.println("✅ Bulk Email envoyé à : " + email);
                }
                System.out.println("🎖️ Toutes les notifications envoyées aux " + recipientEmails.size() + " étudiants !");
            } catch (MessagingException e) {
                System.err.println("❌ Erreur lors de l'envoi groupé : " + e.getMessage());
            }
        }).start();
    }
}
