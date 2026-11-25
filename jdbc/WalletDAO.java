package tollplaza.jdbc;

import tollplaza.member2.Wallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WalletDAO {

    public void saveWallet(Wallet wallet) throws SQLException {
        if (wallet == null) return;

        String sql = "INSERT INTO wallets (wallet_id, vehicle_plate, balance, is_active) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE balance = VALUES(balance), is_active = VALUES(is_active)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, wallet.getWalletId());
            stmt.setString(2, wallet.getVehiclePlate());
            stmt.setDouble(3, wallet.getBalance());
            stmt.setBoolean(4, wallet.isActive());
            stmt.executeUpdate();
        }
    }

    public Wallet findByPlate(String vehiclePlate) throws SQLException {
        if (vehiclePlate == null) return null;

        String sql = "SELECT * FROM wallets WHERE vehicle_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiclePlate.trim().toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Wallet(
                            rs.getString("wallet_id"),
                            rs.getString("vehicle_plate"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_active")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateBalance(String walletId, double newBalance) throws SQLException {
        String sql = "UPDATE wallets SET balance = ? WHERE wallet_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, walletId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Wallet> getAllWallets() throws SQLException {
        List<Wallet> list = new ArrayList<>();
        String sql = "SELECT * FROM wallets ORDER BY wallet_id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Wallet(
                        rs.getString("wallet_id"),
                        rs.getString("vehicle_plate"),
                        rs.getDouble("balance"),
                        rs.getBoolean("is_active")
                ));
            }
        }
        return list;
    }
}
