package services;

import tools.Mydb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {
    private Connection connection = Mydb.getInstance().getConnection();

    /**
     * Récupère tous les emails des étudiants inscrits (comptes Google)
     */
    public List<String> getAllStudentEmails() {
        List<String> emails = new ArrayList<>();
        // On suppose que la table s'appelle 'user' et contient une colonne 'email'
        // Si votre collègue a nommé la table autrement (ex: 'utilisateur'), il faudra ajuster ici.
        String sql = "SELECT email FROM user WHERE email IS NOT NULL AND email LIKE '%@%'";
        
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emails : " + e.getMessage());
            // fallback : on essaie la table 'joueur' au cas où les emails y seraient
            try {
                Statement stFallback = connection.createStatement();
                String sqlFallback = "SELECT email FROM joueur WHERE email IS NOT NULL";
                ResultSet rs2 = stFallback.executeQuery(sqlFallback);
                while (rs2.next()) {
                    emails.add(rs2.getString("email"));
                }
            } catch (Exception ex) {}
        }
        return emails;
    }
}
