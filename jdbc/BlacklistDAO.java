package tollplaza.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BlacklistDAO {

    public void addBlacklist(String plateNumber, String reason) throws SQLException {
        if (plateNumber == null || plateNumber.trim().isEmpty()) return;

        String sql = "INSERT INTO blacklist (plate_number, reason) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE reason = VALUES(reason)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber.trim().toUpperCase());
            stmt.setString(2, (reason != null && !reason.trim().isEmpty()) ? reason.trim() : "Unspecified violation");
            stmt.executeUpdate();
        }
    }

    public boolean isBlacklisted(String plateNumber) throws SQLException {
        if (plateNumber == null) return false;

        String sql = "SELECT COUNT(*) FROM blacklist WHERE plate_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber.trim().toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public String getBlacklistReason(String plateNumber) throws SQLException {
        if (plateNumber == null) return null;

        String sql = "SELECT reason FROM blacklist WHERE plate_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber.trim().toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reason");
                }
            }
        }
        return null;
    }

    public boolean removeBlacklist(String plateNumber) throws SQLException {
        if (plateNumber == null) return false;

        String sql = "DELETE FROM blacklist WHERE plate_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber.trim().toUpperCase());
            return stmt.executeUpdate() > 0;
        }
    }

    public Map<String, String> getAllBlacklisted() throws SQLException {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT plate_number, reason FROM blacklist";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                map.put(rs.getString("plate_number"), rs.getString("reason"));
            }
        }
        return map;
    }
}
