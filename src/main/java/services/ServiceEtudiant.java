package services;

import interfaces.IService;
import models.Etudiant;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEtudiant implements IService<Etudiant> {

    private Connection cnx;

    public ServiceEtudiant() {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Etudiant e) {
        String req = "INSERT INTO `etudiant` (`nom`, `prenom`, `email`, `mot_de_passe`, `niveau`, `points`, `est_mentor`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString (1, e.getNom());
            ps.setString (2, e.getPrenom());
            ps.setString (3, e.getEmail());
            ps.setString (4, e.getMotDePasse());
            ps.setInt    (5, e.getNiveau());
            ps.setInt    (6, e.getPoints());
            ps.setBoolean(7, e.isEstMentor());
            ps.executeUpdate();
            System.out.println(" Etudiant ajoute : " + e.getNom() + " " + e.getPrenom());
        } catch (SQLException ex) {
            System.out.println(" Erreur add : " + ex.getMessage());
        }
    }

    @Override
    public List<Etudiant> getAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        String req = "SELECT * FROM `etudiant`";
        try {
            Statement stm  = cnx.createStatement();
            ResultSet rs   = stm.executeQuery(req);
            while (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId        (rs.getInt    ("id"));
                e.setNom       (rs.getString ("nom"));
                e.setPrenom    (rs.getString ("prenom"));
                e.setEmail     (rs.getString ("email"));
                e.setMotDePasse(rs.getString ("mot_de_passe"));
                e.setNiveau    (rs.getInt    ("niveau"));
                e.setPoints    (rs.getInt    ("points"));
                e.setEstMentor (rs.getBoolean("est_mentor"));
                etudiants.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(" Erreur getAll : " + ex.getMessage());
        }
        return etudiants;
    }

    @Override
    public Etudiant getById(int id) {
        String req = "SELECT * FROM `etudiant` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId        (rs.getInt    ("id"));
                e.setNom       (rs.getString ("nom"));
                e.setPrenom    (rs.getString ("prenom"));
                e.setEmail     (rs.getString ("email"));
                e.setMotDePasse(rs.getString ("mot_de_passe"));
                e.setNiveau    (rs.getInt    ("niveau"));
                e.setPoints    (rs.getInt    ("points"));
                e.setEstMentor (rs.getBoolean("est_mentor"));
                return e;
            }
        } catch (SQLException ex) {
            System.out.println(" Erreur getById : " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void update(Etudiant e) {
        String req = "UPDATE `etudiant` SET `nom`=?, `prenom`=?, `email`=?, "
                + "`mot_de_passe`=?, `niveau`=?, `points`=?, `est_mentor`=? "
                + "WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString (1, e.getNom());
            ps.setString (2, e.getPrenom());
            ps.setString (3, e.getEmail());
            ps.setString (4, e.getMotDePasse());
            ps.setInt    (5, e.getNiveau());
            ps.setInt    (6, e.getPoints());
            ps.setBoolean(7, e.isEstMentor());
            ps.setInt    (8, e.getId());
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println(" Etudiant mis a jour (id=" + e.getId() + ")");
            else
                System.out.println(" Aucun etudiant avec id=" + e.getId());
        } catch (SQLException ex) {
            System.out.println(" Erreur update : " + ex.getMessage());
        }
    }

    @Override
    public void delete(Etudiant e) {
        String req = "DELETE FROM `etudiant` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, e.getId());
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println(" Etudiant supprime (id=" + e.getId() + ")");
            else
                System.out.println("⚠ Aucun etudiant avec id=" + e.getId());
        } catch (SQLException ex) {
            System.out.println(" Erreur delete : " + ex.getMessage());
        }
    }
}
