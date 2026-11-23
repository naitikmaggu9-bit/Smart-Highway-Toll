package tollplaza.member4;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;


public class ReceiptGenerator {

    private static final AtomicLong RECEIPT_SEQUENCE = new AtomicLong(5000);
    private final Set<Receipt> receiptSet; // TreeSet maintains natural sort order
    private final String receiptsDirectory;

    public ReceiptGenerator(String receiptsDirectory) {
        this.receiptSet = new TreeSet<>();
        this.receiptsDirectory = receiptsDirectory;
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File dir = new File(receiptsDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public synchronized Receipt generateReceipt(String laneId, String vehiclePlate,
                                                String category, double tollPaid,
                                                double remainingBalance) {
        String receiptNo = "RCP-" + RECEIPT_SEQUENCE.incrementAndGet();
        Receipt receipt = new Receipt(receiptNo, laneId, vehiclePlate, category, tollPaid, remainingBalance, LocalDateTime.now());

        receiptSet.add(receipt);
        saveReceiptToFile(receipt);
        return receipt;
    }

    private void saveReceiptToFile(Receipt r) {
        ensureDirectoryExists();
        File file = new File(receiptsDirectory, r.getReceiptNumber() + ".txt");

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            pw.println("************************************************************");
            pw.println("          NATIONAL HIGHWAYS TOLL PLAZA AUTHORITY            ");
            pw.println("             ELECTRONIC TOLL COLLECTION RECEIPT             ");
            pw.println("************************************************************");
            pw.printf(" Receipt Number    : %s%n", r.getReceiptNumber());
            pw.printf(" Date & Time       : %s%n", r.getFormattedIssueTime());
            pw.printf(" Lane ID           : %s%n", r.getLaneId());
            pw.printf(" Vehicle Plate     : %s%n", r.getVehiclePlate());
            pw.printf(" Vehicle Category  : %s%n", r.getVehicleCategory());
            pw.println("------------------------------------------------------------");
            pw.printf(" TOLL FEE DEDUCTED : Rs. %.2f%n", r.getTollAmount());
            pw.printf(" REMAINING BALANCE : Rs. %.2f%n", r.getRemainingBalance());
            pw.println("------------------------------------------------------------");
            pw.println(" Payment Status    : PAID VIA AUTOMATED FASTAG WALLET");
            pw.println(" Wish you a safe and pleasant journey!");
            pw.println("************************************************************");
        } catch (IOException e) {
            System.err.println("Error saving receipt to file: " + e.getMessage());
        }
    }

    public synchronized Set<Receipt> getAllReceipts() {
        return new TreeSet<>(receiptSet);
    }

    public synchronized void displayAllReceipts() {
        System.out.println("==========================================================================================");
        System.out.println("                             PRINTED TOLL RECEIPTS AUDIT                                  ");
        System.out.println("==========================================================================================");
        if (receiptSet.isEmpty()) {
            System.out.println("No receipts have been generated yet.");
            return;
        }

        for (Receipt r : receiptSet) {
            System.out.println(r.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total Receipts Generated: " + receiptSet.size());
    }
}
