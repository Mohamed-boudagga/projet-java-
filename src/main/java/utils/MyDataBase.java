package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDataBase {

    private static MyDataBase instance;

    // CONFIGURATION WAMP/MySQL
    private static final String HOST     = "127.0.0.1";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "skillquest";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; 

    private static final String URL_NO_DB = "jdbc:mysql://" + HOST + ":" + PORT
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private Connection cnx;

    private MyDataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Étape 1 : Créer la base si elle n'existe pas
            createDatabaseIfNotExists();

            // Étape 2 : Connexion
            this.cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("🚀 Connexion MySQL (SkillQuest) réussie !");

            // Étape 3 : Créer les tables
            createTablesIfNotExist();
            migrateColumnsIfNeeded();

        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur connexion : " + e.getMessage());
        }
    }

    private void createDatabaseIfNotExists() throws SQLException {
        try (Connection tmp = DriverManager.getConnection(URL_NO_DB, USERNAME, PASSWORD);
             Statement st = tmp.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "` CHARACTER SET utf8mb4");
        }
    }

    private void createTablesIfNotExist() {
        try (Statement st = cnx.createStatement()) {
            // Table etudiant (Utilisée par vos collègues)
            st.executeUpdate("CREATE TABLE IF NOT EXISTS `etudiant` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `nom` VARCHAR(100) NOT NULL," +
                "  `prenom` VARCHAR(100) NOT NULL," +
                "  `email` VARCHAR(150) NOT NULL UNIQUE," +
                "  `mot_de_passe` VARCHAR(255) NOT NULL," +
                "  `niveau` INT NOT NULL DEFAULT 1," +
                "  `points` INT NOT NULL DEFAULT 0," +
                "  PRIMARY KEY (`id`)) ENGINE=InnoDB;");
            
            // Table admin
            st.executeUpdate("CREATE TABLE IF NOT EXISTS `admin` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `nom` VARCHAR(100) NOT NULL," +
                "  `prenom` VARCHAR(100) NOT NULL," +
                "  `email` VARCHAR(150) NOT NULL UNIQUE," +
                "  `mot_de_passe` VARCHAR(255) NOT NULL," +
                "  PRIMARY KEY (`id`)) ENGINE=InnoDB;");
        } catch (SQLException e) {
            System.out.println("Erreur création tables : " + e.getMessage());
        }
    }

    private void migrateColumnsIfNeeded() {
        // Ajout de colonnes pour la compatibilité si nécessaire
    }

    public static MyDataBase getInstance() {
        if (instance == null) instance = new MyDataBase();
        return instance;
    }

    public Connection getCnx() { return cnx; }

    public boolean isConnected() {
        try {
            return cnx != null && !cnx.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
