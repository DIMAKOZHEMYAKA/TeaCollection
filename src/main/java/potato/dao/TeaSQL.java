package potato.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import potato.models.Tea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeaSQL implements TeaDAO {
    private static final String URL = "jdbc:postgresql://localhost:5430/postgres_db";
    private static final String USER = "postgres_user";
    private static final String PASSWORD = "postgres_password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public List<Tea> getAllTeas() throws SQLException {
        List<Tea> teas = new ArrayList<>();
        String sql = "SELECT * FROM teas";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teas.add(mapRowToTea(rs));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return teas;
    }

    @Override
    public Tea getTeaById(int id) throws SQLException {
        String sql = "SELECT * FROM teas WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTea(rs);
                } else {
                    return null; // Чай с таким ID не найден
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addTea(Tea tea) throws SQLException {
        String sql = "INSERT INTO teas (name, type, description, flavor_profile, quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setTeaParameters(stmt, tea);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tea.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateTea(Tea tea) throws SQLException {
        String sql = "UPDATE teas SET name = ?, type = ?, description = ?, flavor_profile = ?, quantity = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setTeaParameters(stmt, tea);
            stmt.setInt(6, tea.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteTea(int id) throws Exception {

    }

    private void setTeaParameters(PreparedStatement stmt, Tea tea) throws SQLException {
        stmt.setString(1, tea.getName());
        stmt.setString(2, tea.getType());
        stmt.setString(3, tea.getDescription());
        stmt.setString(4, tea.getFlavor_profile());
        stmt.setInt(5, tea.getQuantity());
    }

    private Tea mapRowToTea(ResultSet rs) throws SQLException, JsonProcessingException {
        Tea tea = new Tea();
        tea.setId(rs.getInt("id"));
        tea.setName(rs.getString("name"));
        tea.setType(rs.getString("type"));
        tea.setDescription(rs.getString("description"));
        tea.setFlavor_profile(rs.getString("flavor_profile"));
        tea.setQuantity(rs.getInt("quantity"));
        return tea;
    }

}