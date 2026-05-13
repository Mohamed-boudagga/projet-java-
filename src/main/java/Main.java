package main;

import models.*;
import services.*;
import utils.MyDataBase;
import utils.PasswordUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        banner("SkillQuest v2 — Module Integration");

        if (!MyDataBase.getInstance().isConnected()) {
            System.err.println("Impossible de continuer sans connexion à la base de données.");
            return;
        }

        // Nettoyage des tables pour un test propre
        try {
            Statement stmt = MyDataBase.getInstance().getCnx().createStatement();
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            stmt.executeUpdate("TRUNCATE TABLE cours");
            stmt.executeUpdate("TRUNCATE TABLE etudiant");
            stmt.executeUpdate("TRUNCATE TABLE admin");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
            System.out.println("✔ Tables réinitialisées.\n");
        } catch (SQLException e) {
            System.out.println("Erreur nettoyage : " + e.getMessage());
        }

        // MODULE ADMIN
        banner("MODULE ADMIN");
        ServiceAdmin serviceAdmin = new ServiceAdmin();
        serviceAdmin.add(new Admin("Admin", "Principal", "admin@skillquest.tn", "admin123"));
        serviceAdmin.add(new Admin("Directeur", "Sami", "sami@skillquest.tn", "sami2024"));
        System.out.println("\n--- Tous les admins ---");
        serviceAdmin.getAll().forEach(System.out::println);

        // MODULE ETUDIANT
        banner("MODULE ETUDIANT");
        ServiceEtudiant serviceEtudiant = new ServiceEtudiant();
        serviceEtudiant.add(new Etudiant("Ben Ali", "Ahmed", "ahmed@esprit.tn", "pass123", 1, 0, false, "20123456", "M"));
        serviceEtudiant.add(new Etudiant("Trabelsi", "Sarra", "sarra@esprit.tn", "pass456", 2, 150, false, "22334455", "F"));
        serviceEtudiant.add(new Etudiant("Boudagga", "Mohamed", "mohamed@esprit.tn", "pass12345678", 5, 1000, true, "27182818", "M"));
        System.out.println("\n--- Tous les étudiants ---");
        serviceEtudiant.getAll().forEach(System.out::println);

        // MODULE COURS
        banner("MODULE COURS");
        ServiceCours serviceCours = new ServiceCours();
        Admin admin = serviceAdmin.getByEmail("admin@skillquest.tn");
        int adminId = admin != null ? admin.getId() : 1;
        serviceCours.add(new Cours("Java Débutant", "Introduction à Java.", 1, adminId));
        serviceCours.add(new Cours("Base de Données SQL", "Requêtes SQL avec MySQL.", 2, adminId));
        System.out.println("\n--- Tous les cours ---");
        serviceCours.getAll().forEach(System.out::println);

        // MODULE TESTS / CERTIF (Other members work)
        banner("MODULE TESTS / CERTIF");
        ServiceExam se = new ServiceExam();
        ServiceCertification sc = new ServiceCertification();

        Exam eTest = new Exam();
        eTest.setNom("Java Mastery Exam");
        eTest.setLevel(1);
        eTest.setDureeMinutes(45);
        se.add(eTest);

        Certification cTest = new Certification();
        cTest.setTitle("Oracle Certified Associate");
        cTest.setLevel(1);
        cTest.setDescription("Expert en programmation Java.");
        cTest.setDateObtention(new Date());
        sc.add(cTest);

        System.out.println("Vérification Exams : " + se.getAll());
        System.out.println("Vérification Certifs : " + sc.getAll());

        banner("Tous les tests d'intégration terminés avec succès !");
    }

    private static void banner(String title) {
        String line = "=".repeat(55);
        System.out.println("\n" + line);
        System.out.printf("  %s%n", title);
        System.out.println(line);
    }
}