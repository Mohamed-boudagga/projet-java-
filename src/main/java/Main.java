import models.Etudiant;
import services.ServiceEtudiant;
import utils.MyDataBase;

import java.sql.Statement;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        System.out.println("============================================");
        System.out.println("   SkillQuest — Module Gestion Etudiants   ");
        System.out.println("============================================");

        if (!MyDataBase.getInstance().isConnected()) {
            System.err.println(" Impossible de continuer sans connexion a la base de donnees.");
            return;
        }


        try {
            Statement stmt = MyDataBase.getInstance().getCnx().createStatement();
            stmt.executeUpdate("TRUNCATE TABLE etudiant");
            System.out.println(" Base de donnees reinitialisee pour le test.");
        } catch (SQLException e) {
            System.out.println("Erreur de nettoyage : " + e.getMessage());
        }

        ServiceEtudiant service = new ServiceEtudiant();

        // ADD
        System.out.println("\n===== ADD =====");
        service.add(new Etudiant("Ben Ali",  "Ahmed",  "ahmed.benali@esprit.tn",  "pass1*2*3*", 1,   0, false));
        service.add(new Etudiant("Trabelsi", "Sarra",    "sarra@esprit.tn",           "pass4*5*6*", 2, 150, false));
        service.add(new Etudiant("Mansouri", "Jihed",    "jihed@esprit.tn",           "pass7*8*9*", 3, 500, true));
        service.add(new Etudiant("briki",    "oussama",  "oussama@esprit.tn",         "pass1*2*3*44", 1,   0, false));
        service.add(new Etudiant("Boudagga",  "Mohamed",  "Mohamed.Boudagga@esprit.tn",  "pass12345678", 5,   1000, true));
        //  GET ALL
        System.out.println("\n===== GET ALL =====");
        service.getAll().forEach(System.out::println);

        // GET BY ID
        System.out.println("\n===== GET BY ID (id=1) =====");
        Etudiant trouve = service.getById(1);
        System.out.println(trouve);

        //  UPDATE
        System.out.println("\n===== UPDATE =====");
        if (trouve != null) {
            trouve.setPoints(300);
            trouve.setNiveau(2);
            service.update(trouve);
        }

        // GET ALL apres UPDATE
        System.out.println("\n===== GET ALL apres UPDATE =====");
        service.getAll().forEach(System.out::println);

        // DELETE
        /*
        System.out.println("\n===== DELETE (id=1) =====");
        if (trouve != null) service.delete(trouve);
        
        System.out.println("\n===== GET ALL apres DELETE =====");
        service.getAll().forEach(System.out::println);
        */

        System.out.println("\n=========================================");
        System.out.println("   Test CRUD termine avec succes !         ");
        System.out.println("   Vérifiez phpMyAdmin, les données sont là !");
        System.out.println("===========================================");
    }
}