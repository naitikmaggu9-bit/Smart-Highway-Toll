package tollplaza.jdbc;

import tollplaza.member2.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransactionDAO {

    public void recordTransaction(Transaction txn) throws SQLException {
        if (txn == null) return;

        String sql = "INSERT INTO transactions (transaction_id, wallet_id, vehicle_plate, category, amount, type, status, timestamp, remarks) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, txn.getTransactionId());
            stmt.setString(2, txn.getWalletId());
            stmt.setString(3, txn.getVehiclePlate());
            stmt.setString(4, txn.getVehicleCategory());
            stmt.setDouble(5, txn.getAmount());
            stmt.setString(6, txn.getType());
            stmt.setString(7, txn.getStatus());
            stmt.setTimestamp(8, Timestamp.valueOf(txn.getTimestamp()));
            stmt.setString(9, txn.getRemarks());

            stmt.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToTransaction(rs));
            }
        }
        return list;
    }

    public List<Transaction> findByWalletId(String walletId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE wallet_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, walletId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToTransaction(rs));
                }
            }
        }
        return list;
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getString("transaction_id"),
                rs.getString("wallet_id"),
                rs.getString("vehicle_plate"),
                rs.getString("category"),
                rs.getDouble("amount"),
                rs.getString("type"),
                rs.getString("status"),
                rs.getTimestamp("timestamp").toLocalDateTime(),
                rs.getString("remarks")
        );
    }
}
