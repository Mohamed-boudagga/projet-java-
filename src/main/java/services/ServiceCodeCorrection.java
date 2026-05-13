package services;

import entities.CodeCorrection;
import interfaces.IService;
import tools.Mydb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCodeCorrection implements IService<CodeCorrection> {

    private Connection connection = Mydb.getInstance().getConnection();

    @Override
    public void add(CodeCorrection c) {
        try {
            String sql = "INSERT INTO code_corrections (game_id, instructions, buggy_code, correct_code) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, c.getGameId());
            ps.setString(2, c.getInstructions());
            ps.setString(3, c.getBuggyCode());
            ps.setString(4, c.getCorrectCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouter(CodeCorrection c) { add(c); }

    @Override
    public void update(CodeCorrection c) {
        try {
            String sql = "UPDATE code_corrections SET instructions=?, buggy_code=?, correct_code=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, c.getInstructions());
            ps.setString(2, c.getBuggyCode());
            ps.setString(3, c.getCorrectCode());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(CodeCorrection c) {
        supprimer(c.getId());
    }

    @Override
    public List<CodeCorrection> getAll() {
        return new ArrayList<>();
    }

    @Override
    public CodeCorrection getById(int id) {
        return null;
    }

    public List<CodeCorrection> getByGameId(int gameId) {
        List<CodeCorrection> list = new ArrayList<CodeCorrection>();
        try {
            String sql = "SELECT * FROM code_corrections WHERE game_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, gameId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CodeCorrection(
                    rs.getInt("id"),
                    rs.getInt("game_id"),
                    rs.getString("instructions"),
                    rs.getString("buggy_code"),
                    rs.getString("correct_code")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void supprimer(int id) {
        try {
            String sql = "DELETE FROM code_corrections WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
