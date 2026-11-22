package tollplaza.member4;

import java.io.File;
import java.io.IOException;

/**
 * Replaces DBConnection class!
 * Initializes file directories, verifies storage files, and ensures all text data
 * stores (vehicles.txt, wallets.txt, transactions.txt, blacklist.txt, receipts/) are ready.
 * Concepts: File I/O (File, mkdirs, createNewFile, exists).
 * Member 4 - Class 6 of 8 (Project Class 30 of 32)
 */
public class FileStorageManager {

    private final String baseDataDir;
    private final String vehiclesFilePath;
    private final String walletsFilePath;
    private final String transactionsFilePath;
    private final String blacklistFilePath;
    private final String receiptsDirPath;

    public FileStorageManager(String baseDataDir) {
        this.baseDataDir = baseDataDir;
        this.vehiclesFilePath = baseDataDir + File.separator + "vehicles.txt";
        this.walletsFilePath = baseDataDir + File.separator + "wallets.txt";
        this.transactionsFilePath = baseDataDir + File.separator + "transactions.txt";
        this.blacklistFilePath = baseDataDir + File.separator + "blacklist.txt";
        this.receiptsDirPath = baseDataDir + File.separator + "receipts";
    }

    public boolean initializeStorage() {
        try {
            File dir = new File(baseDataDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File receiptsDir = new File(receiptsDirPath);
            if (!receiptsDir.exists()) {
                receiptsDir.mkdirs();
            }

            createIfMissing(vehiclesFilePath);
            createIfMissing(walletsFilePath);
            createIfMissing(transactionsFilePath);
            createIfMissing(blacklistFilePath);

            return true;
        } catch (IOException e) {
            System.err.println("Fatal: Could not initialize file storage: " + e.getMessage());
            return false;
        }
    }

    private void createIfMissing(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public String getVehiclesFilePath() {
        return vehiclesFilePath;
    }

    public String getWalletsFilePath() {
        return walletsFilePath;
    }

    public String getTransactionsFilePath() {
        return transactionsFilePath;
    }

    public String getBlacklistFilePath() {
        return blacklistFilePath;
    }

    public String getReceiptsDirPath() {
        return receiptsDirPath;
    }

    public void printStorageStatus() {
        System.out.println("==========================================================================================");
        System.out.println("                         FILE STORAGE SYSTEM STATUS                                       ");
        System.out.println("==========================================================================================");
        checkAndPrint("Vehicles Store    ", vehiclesFilePath);
        checkAndPrint("Wallets Store     ", walletsFilePath);
        checkAndPrint("Transactions Store", transactionsFilePath);
        checkAndPrint("Blacklist Store   ", blacklistFilePath);
        checkAndPrint("Receipts Directory", receiptsDirPath);
        System.out.println("==========================================================================================");
    }

    private void checkAndPrint(String label, String path) {
        File f = new File(path);
        System.out.printf("  %s : [%s] Path: %s (Size: %d bytes)%n",
                label, (f.exists() ? "OK" : "MISSING"), path, (f.exists() ? f.length() : 0));
    }
}
