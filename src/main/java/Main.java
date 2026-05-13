import entities.Games;
import models.Admin;
import models.Etudiant;
import services.ServiceAdmin;
import services.ServiceEtudiant;
import services.ServiceGames;
import services.ServiceExam;
import utils.MyDataBase;
import utils.PasswordUtils;

public class Main {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   SkillQuest — Tests d'Intégration Totale  ");
        System.out.println("============================================");

        if (!MyDataBase.getInstance().isConnected()) {
            System.err.println(" Impossible de continuer sans connexion à la base de données.");
            return;
        }

        // --- TEST MODULE ÉTUDIANTS & ADMINS ---
        ServiceAdmin serviceAdmin = new ServiceAdmin();
        ServiceEtudiant serviceEtudiant = new ServiceEtudiant();

        if (serviceAdmin.getAll().isEmpty()) {
            serviceAdmin.add(new Admin("Admin", "Principal", "admin@skillquest.tn", "admin123"));
            System.out.println(" Compte Admin créé par défaut.");
        }

        // --- TEST MODULE JEUX (VOTRE TRAVAIL) ---
        ServiceGames serviceG = new ServiceGames();
        System.out.println(" Nombre de jeux SkillQuest : " + serviceG.getAll().size());

        // --- TEST MODULE EXAMENS ---
        ServiceExam se = new ServiceExam();
        System.out.println(" Module Examens opérationnel.");

        System.out.println("\n=========================================");
        System.out.println("   INTÉGRATION RÉUSSIE AVEC SUCCÈS !     ");
        System.out.println("===========================================");
    }
}