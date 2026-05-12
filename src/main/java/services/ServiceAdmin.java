package services;

import interfaces.IService;
import models.Admin;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service CRUD pour les Admins.
 * L'admin contrôle l'application, gère les cours et les étudiants.
 */
public class ServiceAdmin implements IService<Admin> {

    private Connection cnx;

    public ServiceAdmin() {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // ----------------------------------------------------------------
    // ADD
    // ----------------------------------------------------------------
    @Override
    public void add(Admin a) {
        String req = "INSERT INTO `admin` (`nom`, `prenom`, `email`, `mot_de_passe`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getMotDePasse());
            ps.executeUpdate();
            System.out.println("✔ Admin ajouté : " + a.getNom() + " " + a.getPrenom());
        } catch (SQLException ex) {
            System.out.println("✘ Erreur add Admin : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // GET ALL
    // ----------------------------------------------------------------
    @Override
    public List<Admin> getAll() {
        List<Admin> admins = new ArrayList<>();
        String req = "SELECT * FROM `admin`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                admins.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.out.println("✘ Erreur getAll Admin : " + ex.getMessage());
        }
        return admins;
    }

    // ----------------------------------------------------------------
    // GET BY ID
    // ----------------------------------------------------------------
    @Override
    public Admin getById(int id) {
        String req = "SELECT * FROM `admin` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException ex) {
            System.out.println("✘ Erreur getById Admin : " + ex.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    // AUTHENTIFICATION (login admin)
    // ----------------------------------------------------------------
    public Admin login(String email, String motDePasse) {
        String req = "SELECT * FROM `admin` WHERE `email` = ? AND `mot_de_passe` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("✔ Connexion admin réussie !");
                return mapRow(rs);
            } else {
                System.out.println("✘ Email ou mot de passe incorrect.");
            }
        } catch (SQLException ex) {
            System.out.println("✘ Erreur login Admin : " + ex.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------
    @Override
    public void update(Admin a) {
        String req = "UPDATE `admin` SET `nom`=?, `prenom`=?, `email`=?, `mot_de_passe`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getMotDePasse());
            ps.setInt   (5, a.getId());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✔ Admin mis à jour (id=" + a.getId() + ")"
                    : "⚠ Aucun admin avec id=" + a.getId());
        } catch (SQLException ex) {
            System.out.println("✘ Erreur update Admin : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    @Override
    public void delete(Admin a) {
        String req = "DELETE FROM `admin` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, a.getId());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✔ Admin supprimé (id=" + a.getId() + ")"
                    : "⚠ Aucun admin avec id=" + a.getId());
        } catch (SQLException ex) {
            System.out.println("✘ Erreur delete Admin : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // Mapping ResultSet -> Admin
    // ----------------------------------------------------------------
    private Admin mapRow(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getInt   ("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("mot_de_passe")
        );
    }
}
