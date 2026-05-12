package services;

import interfaces.IService;
import models.Etudiant;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service CRUD pour les Etudiants.
 * Inclut également les opérations de blocage/déblocage (réservées à l'Admin).
 */
public class ServiceEtudiant implements IService<Etudiant> {

    private Connection cnx;

    public ServiceEtudiant() {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // ----------------------------------------------------------------
    // ADD
    // ----------------------------------------------------------------
    @Override
    public void add(Etudiant e) {
        String req = "INSERT INTO `etudiant` "
                + "(`nom`, `prenom`, `email`, `mot_de_passe`, `niveau`, `points`, `est_mentor`, `telephone`, `sexe`, `est_bloque`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString (1, e.getNom());
            ps.setString (2, e.getPrenom());
            ps.setString (3, e.getEmail());
            ps.setString (4, e.getMotDePasse());
            ps.setInt    (5, e.getNiveau());
            ps.setInt    (6, e.getPoints());
            ps.setBoolean(7, e.isEstMentor());
            ps.setString (8, e.getTelephone());
            ps.setString (9, e.getSexe());
            ps.setBoolean(10, e.isEstBloque());
            System.out.println("DEBUG Add Etudiant -> Tel: " + e.getTelephone() + ", Sexe: " + e.getSexe());
            ps.executeUpdate();
            System.out.println("✔ Etudiant ajouté : " + e.getNom() + " " + e.getPrenom());
        } catch (SQLException ex) {
            System.out.println("✘ Erreur add : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // GET ALL
    // ----------------------------------------------------------------
    @Override
    public List<Etudiant> getAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        String req = "SELECT * FROM `etudiant`";
        try {
            Statement  stm = cnx.createStatement();
            ResultSet  rs  = stm.executeQuery(req);
            while (rs.next()) {
                etudiants.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.out.println("✘ Erreur getAll : " + ex.getMessage());
        }
        return etudiants;
    }

    // ----------------------------------------------------------------
    // GET BY ID
    // ----------------------------------------------------------------
    @Override
    public Etudiant getById(int id) {
        String req = "SELECT * FROM `etudiant` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException ex) {
            System.out.println("✘ Erreur getById : " + ex.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    // GET BY EMAIL (utile pour la connexion)
    // ----------------------------------------------------------------
    public Etudiant getByEmail(String email) {
        String req = "SELECT * FROM `etudiant` WHERE `email` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException ex) {
            System.out.println("✘ Erreur getByEmail : " + ex.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------
    @Override
    public void update(Etudiant e) {
        String req = "UPDATE `etudiant` SET "
                + "`nom`=?, `prenom`=?, `email`=?, `mot_de_passe`=?, "
                + "`niveau`=?, `points`=?, `est_mentor`=?, "
                + "`telephone`=?, `sexe`=?, `est_bloque`=? "
                + "WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString (1,  e.getNom());
            ps.setString (2,  e.getPrenom());
            ps.setString (3,  e.getEmail());
            ps.setString (4,  e.getMotDePasse());
            ps.setInt    (5,  e.getNiveau());
            ps.setInt    (6,  e.getPoints());
            ps.setBoolean(7,  e.isEstMentor());
            ps.setString (8,  e.getTelephone());
            ps.setString (9,  e.getSexe());
            ps.setBoolean(10, e.isEstBloque());
            ps.setInt    (11, e.getId());
            System.out.println("DEBUG Update Etudiant (ID=" + e.getId() + ") -> Tel: " + e.getTelephone() + ", Sexe: " + e.getSexe());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✔ Etudiant mis à jour (id=" + e.getId() + ")"
                    : "⚠ Aucun étudiant avec id=" + e.getId());
        } catch (SQLException ex) {
            System.out.println("✘ Erreur update : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    @Override
    public void delete(Etudiant e) {
        String req = "DELETE FROM `etudiant` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, e.getId());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✔ Etudiant supprimé (id=" + e.getId() + ")"
                    : "⚠ Aucun étudiant avec id=" + e.getId());
        } catch (SQLException ex) {
            System.out.println("✘ Erreur delete : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // BLOQUER / DEBLOQUER  (action réservée à l'Admin)
    // ----------------------------------------------------------------

    /** Bloque un étudiant (il ne pourra plus se connecter). */
    public void bloquer(int etudiantId) {
        setBloque(etudiantId, true);
        System.out.println("🔒 Etudiant bloqué (id=" + etudiantId + ")");
    }

    /** Débloque un étudiant. */
    public void debloquer(int etudiantId) {
        setBloque(etudiantId, false);
        System.out.println("🔓 Etudiant débloqué (id=" + etudiantId + ")");
    }

    private void setBloque(int etudiantId, boolean bloque) {
        String req = "UPDATE `etudiant` SET `est_bloque`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setBoolean(1, bloque);
            ps.setInt    (2, etudiantId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("✘ Erreur setBloque : " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // Méthode utilitaire de mapping ResultSet -> Etudiant
    // ----------------------------------------------------------------
    public List<Etudiant> getClassement() {
        List<Etudiant> list = new ArrayList<>();
        String req = "SELECT * FROM `etudiant` ORDER BY `points` DESC LIMIT 50";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.out.println("✘ Erreur getClassement : " + ex.getMessage());
        }
        return list;
    }

    private Etudiant mapRow(ResultSet rs) throws SQLException {
        Etudiant e = new Etudiant();
        e.setId        (rs.getInt    ("id"));
        e.setNom       (rs.getString ("nom"));
        e.setPrenom    (rs.getString ("prenom"));
        e.setEmail     (rs.getString ("email"));
        e.setMotDePasse(rs.getString ("mot_de_passe"));
        e.setNiveau    (rs.getInt    ("niveau"));
        e.setPoints    (rs.getInt    ("points"));
        e.setEstMentor (rs.getBoolean("est_mentor"));
        e.setTelephone (rs.getString ("telephone"));
        e.setSexe      (rs.getString ("sexe"));
        e.setEstBloque (rs.getBoolean("est_bloque"));
        return e;
    }
}
