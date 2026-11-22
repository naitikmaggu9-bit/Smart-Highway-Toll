package tollplaza.member4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Receipt implements Comparable<Receipt> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String receiptNumber;
    private final String laneId;
    private final String vehiclePlate;
    private final String vehicleCategory;
    private final double tollAmount;
    private final double remainingBalance;
    private final LocalDateTime issueTime;

    public Receipt(String receiptNumber, String laneId, String vehiclePlate,
                   String vehicleCategory, double tollAmount, double remainingBalance,
                   LocalDateTime issueTime) {
        this.receiptNumber = receiptNumber;
        this.laneId = laneId;
        this.vehiclePlate = vehiclePlate;
        this.vehicleCategory = vehicleCategory;
        this.tollAmount = tollAmount;
        this.remainingBalance = remainingBalance;
        this.issueTime = (issueTime != null) ? issueTime : LocalDateTime.now();
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public String getLaneId() {
        return laneId;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public double getTollAmount() {
        return tollAmount;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public String getFormattedIssueTime() {
        return issueTime.format(FORMATTER);
    }

    @Override
    public int compareTo(Receipt other) {
        if (other == null) return 1;
   
        return this.receiptNumber.compareTo(other.receiptNumber);
    }

    @Override
    public String toString() {
        return String.format("Receipt #%-12s | Lane: %-6s | Plate: %-10s | Paid: Rs. %7.2f | Bal: Rs. %8.2f | Time: %s",
                receiptNumber, laneId, vehiclePlate, tollAmount, remainingBalance, getFormattedIssueTime());
    }
}
