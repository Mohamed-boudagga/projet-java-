package services;

import entities.Partie;
import interfaces.IService;
import tools.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ServicePartie implements IService<Partie> {

    private Connection connection = Mydb.getInstance().getConnection();

    @Override
    public void add(Partie p) {
        try {
            String sql = "INSERT INTO partie(score, datee, joueur_id) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, p.getScore());
            ps.setDate(2, p.getDatee());
            ps.setInt(3, p.getJoueur().getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Partie p) {}
    @Override
    public void delete(Partie p) {}
    @Override
    public java.util.List<Partie> getAll() { return new java.util.ArrayList<>(); }
    @Override
    public Partie getById(int id) { return null; }
}