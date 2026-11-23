package tollplaza.member2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Transaction implements Comparable<Transaction> {

    public static final String TYPE_TOLL = "TOLL_PAYMENT";
    public static final String TYPE_RECHARGE = "WALLET_RECHARGE";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String transactionId;
    private final String walletId;
    private final String vehiclePlate;
    private final String vehicleCategory; 
    private final double amount;
    private final String type;            
    private final String status;          
    private final LocalDateTime timestamp;
    private final String remarks;

    public Transaction(String transactionId, String walletId, String vehiclePlate,
                       String vehicleCategory, double amount, String type,
                       String status, LocalDateTime timestamp, String remarks) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.vehiclePlate = vehiclePlate;
        this.vehicleCategory = (vehicleCategory != null) ? vehicleCategory.toUpperCase() : "GENERAL";
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.timestamp = (timestamp != null) ? timestamp : LocalDateTime.now();
        this.remarks = (remarks != null) ? remarks : "";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public int compareTo(Transaction other) {
        if (other == null) return 1;
  
        return other.timestamp.compareTo(this.timestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s] ID: %-12s | Plate: %-10s | Cat: %-5s | Amt: Rs. %7.2f | Type: %-14s | Status: %-7s | Time: %s",
                (STATUS_SUCCESS.equals(status) ? "OK" : "XX"),
                transactionId, vehiclePlate, vehicleCategory, amount, type, status, getFormattedTimestamp());
    }
}
