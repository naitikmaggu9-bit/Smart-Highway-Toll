 package tollplaza.member2;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class TransactionFileManager {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filePath;

    public TransactionFileManager(String filePath) {
        this.filePath = filePath;
    }

    public synchronized void appendTransaction(Transaction txn) {
        if (txn == null) return;
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        boolean fileExists = file.exists() && file.length() > 0;
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            if (!fileExists) {
                writer.println("# TRANSACTIONS AUDIT LOG");
                writer.println("# Format: TXN_ID|WALLET_ID|PLATE|CATEGORY|AMOUNT|TYPE|STATUS|TIMESTAMP|REMARKS");
            }
            writer.printf("%s|%s|%s|%s|%.2f|%s|%s|%s|%s%n",
                    txn.getTransactionId(),
                    txn.getWalletId(),
                    txn.getVehiclePlate(),
                    txn.getVehicleCategory(),
                    txn.getAmount(),
                    txn.getType(),
                    txn.getStatus(),
                    txn.getFormattedTimestamp(),
                    txn.getRemarks());
        } catch (IOException e) {
            System.err.println("Error appending transaction to file: " + e.getMessage());
        }
    }

    public synchronized List<Transaction> loadTransactions() {
        List<Transaction> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] p = line.split("\\|");
                if (p.length < 8) continue;

                String txnId = p[0].trim();
                String walletId = p[1].trim();
                String plate = p[2].trim().toUpperCase();
                String category = p[3].trim().toUpperCase();
                double amount = Double.parseDouble(p[4].trim());
                String type = p[5].trim();
                String status = p[6].trim();
                LocalDateTime timestamp = LocalDateTime.parse(p[7].trim(), FORMATTER);
                String remarks = (p.length > 8) ? p[8].trim() : "";

                list.add(new Transaction(txnId, walletId, plate, category, amount, type, status, timestamp, remarks));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading transactions from file: " + e.getMessage());
        }
        return list;
    }
}
