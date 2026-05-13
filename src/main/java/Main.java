import entities.Games;
import models.Certification;
import models.Exam;
import services.ServiceGames;
import services.ServiceCertification;
import services.ServiceExam;
import tools.Mydb;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   SkillQuest — Tests d'Intégration        ");
        System.out.println("============================================");

        // --- TEST CONNEXION ---
        if (Mydb.getInstance().getConnection() == null) {
            System.err.println(" Impossible de continuer sans connexion à la base de données.");
            return;
        }
        System.out.println(" Connexion MySQL établie.");

        // --- TEST MODULE JEUX (VOTRE TRAVAIL) ---
        ServiceGames serviceG = new ServiceGames();
        System.out.println("Nombre de jeux en base : " + serviceG.getAll().size());

        // --- TEST MODULE EXAMENS (MOHAMED) ---
        ServiceExam se = new ServiceExam();
        ServiceCertification sc = new ServiceCertification();

        System.out.println("Module Examens de Mohamed chargé.");

        System.out.println("\n=========================================");
        System.out.println("   Compilation réussie !                 ");
        System.out.println("===========================================");
    }
}