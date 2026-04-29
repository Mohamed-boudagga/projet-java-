package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDataBase {

    private static MyDataBase instance;

    // ============================================================
    //  CONFIGURATION WAMP
    // ============================================================
    private static final String HOST     = "127.0.0.1";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "skillquest";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";   // vide par defaut WAMP

    // URL sans nom de base (pour creer la base si elle n'existe pas)
    private static final String URL_NO_DB = "jdbc:mysql://" + HOST + ":" + PORT
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // URL avec nom de la base
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";


    private Connection cnx;

    private MyDataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Etape 1 : creer la base de donnees si elle n'existe pas
            createDatabaseIfNotExists();

            // Etape 2 : se connecter a la base
            this.cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(" Connexion WAMP/MySQL reussie !");

            // Etape 3 : creer la table si elle n'existe pas
            createTableIfNotExists();

        } catch (ClassNotFoundException e) {
            System.out.println(" Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(" Erreur connexion : " + e.getMessage());
            System.out.println("   → Verifiez que WAMP est demarre.");
        }
    }

    /**
     * Cree la base de donnees 'skillquest' si elle n'existe pas encore.
     */
    private void createDatabaseIfNotExists() throws SQLException {
        try (Connection tempCnx = DriverManager.getConnection(URL_NO_DB, USERNAME, PASSWORD);
             Statement stmt = tempCnx.createStatement()) {
            stmt.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "` " +
                "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
            );
            System.out.println(" Base de donnees '" + DB_NAME + "' verifiee/creee.");
        }
    }

    /**
     * Cree la table 'etudiant' si elle n'existe pas encore.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS `etudiant` (" +
                     "  `id`           INT          NOT NULL AUTO_INCREMENT," +
                     "  `nom`          VARCHAR(100) NOT NULL," +
                     "  `prenom`       VARCHAR(100) NOT NULL," +
                     "  `email`        VARCHAR(150) NOT NULL UNIQUE," +
                     "  `mot_de_passe` VARCHAR(255) NOT NULL," +
                     "  `niveau`       INT          NOT NULL DEFAULT 1," +
                     "  `points`       INT          NOT NULL DEFAULT 0," +
                     "  `est_mentor`   TINYINT(1)   NOT NULL DEFAULT 0," +
                     "  PRIMARY KEY (`id`)" +
                     ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        try (Statement stmt = cnx.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println(" Table 'etudiant' verifiee/creee.");
        } catch (SQLException e) {
            System.out.println(" Erreur creation table : " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

    /** Verifie si la connexion est active */
    public boolean isConnected() {
        try {
            return cnx != null && !cnx.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
