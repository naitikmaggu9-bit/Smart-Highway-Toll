package tollplaza.member4;

import tollplaza.member1.*;
import tollplaza.member2.*;
import tollplaza.member3.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    private static final String DATA_DIRECTORY = "data";
    private final TollPlazaManager plazaManager;
    private final Scanner scanner;

    public MainApp() {
        this.plazaManager = new TollPlazaManager(DATA_DIRECTORY);
        this.scanner = new Scanner(System.in);

        plazaManager.seedDemoDataIfEmpty();
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.run();
    }

    public void run() {
        printBanner();
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your choice (0-9): ", 0, 9);
            System.out.println();

            switch (choice) {
                case 1:
                    handleVehicleMenu();
                    break;
                case 2:
                    handleWalletMenu();
                    break;
                case 3:
                    handleManualTollCrossing();
                    break;
                case 4:
                    handleBlacklistMenu();
                    break;
                case 5:
                    handleLaneSimulation();
                    break;
                case 6:
                    handleAnalyticsDashboard();
                    break;
                case 7:
                    handleTransactionAudit();
                    break;
                case 8:
                    handleFileStorageInspection();
                    break;
                case 9:
                    handleSeedDemoData();
                    break;
                case 0:
                    running = false;
                    System.out.println("Saving all data to permanent storage...");
                    plazaManager.saveAllData();
                    System.out.println("==========================================================================================");
                    System.out.println("   Thank you for using the Smart Highway Toll & Automated Wallet Manager System!          ");
                    System.out.println("==========================================================================================");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printBanner() {
        System.out.println("******************************************************************************************");
        System.out.println("*              SMART HIGHWAY TOLL & AUTOMATED WALLET MANAGER                             *");
        System.out.println("*            BTech 3rd Sem Core Java Academic Project (Console Edition)                  *");
        System.out.println("*           Zero External Libraries | File I/O Storage | Multithreading                  *");
        System.out.println("******************************************************************************************");
    }

    private void printMainMenu() {
        System.out.println("\n================================ MAIN MENU ===============================================");
        System.out.println("  1. Vehicle Registration & Fleet Management (Member 1)");
        System.out.println("  2. Prepaid FASTag Wallet Operations (Member 2)");
        System.out.println("  3. Manual Toll Booth Crossing & Receipt Generation (Member 3 & 4)");
        System.out.println("  4. Vehicle Blacklist Management (Member 1)");
        System.out.println("  5. Multi-Lane Concurrent Simulation [Multithreading] (Member 3)");
        System.out.println("  6. Toll Collection Analytics Dashboard [Stream API] (Member 4)");
        System.out.println("  7. Transaction Audit & Bidirectional History [Iterator] (Member 2 & 4)");
        System.out.println("  8. File Storage Status & Generated Receipts Inspection (Member 4)");
        System.out.println("  9. Seed / Refresh Academic Demo Data");
        System.out.println("  0. Save & Exit Program");
        System.out.println("==========================================================================================");
    }

  
    private void handleVehicleMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------------- VEHICLE & FLEET MANAGEMENT -----------------");
            System.out.println("  1. View All Registered Vehicles");
            System.out.println("  2. Register New Car");
            System.out.println("  3. Register New Bike");
            System.out.println("  4. Register New Commercial Truck");
            System.out.println("  5. View Fleet Sorted by Category & Plate (VehiclePlateComparator)");
            System.out.println("  6. Search Vehicle by Number Plate");
            System.out.println("  0. Back to Main Menu");
            int sub = readIntInput("Select an option (0-6): ", 0, 6);

            switch (sub) {
                case 1:
                    plazaManager.getVehicleRegistry().displayAllVehicles();
                    break;
                case 2:
                    registerCarPrompt();
                    break;
                case 3:
                    registerBikePrompt();
                    break;
                case 4:
                    registerTruckPrompt();
                    break;
                case 5:
                    sortAndDisplayFleet();
                    break;
                case 6:
                    searchVehiclePrompt();
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void registerCarPrompt() {
        System.out.println("\n--- Register New Car ---");
        String plate = readStringInput("Enter Plate Number (e.g. DL01AB9999): ").toUpperCase();
        String owner = readStringInput("Enter Owner Name: ");
        int seats = readIntInput("Enter Seating Capacity (1-10): ", 1, 10);
        boolean comm = readBooleanInput("Is this a commercial taxi? (yes/no): ");
        boolean emerg = readBooleanInput("Is this an emergency vehicle? (yes/no): ");
        double balance = readDoubleInput("Enter Initial FASTag Wallet Balance (Rs.): ", 0.0, 50000.0);

        try {
            Car car = new Car(plate, owner, LocalDate.now(), emerg, seats, comm);
            plazaManager.registerVehicleWithWallet(car, balance);
            System.out.printf("[SUCCESS] Car [%s] registered with FASTag Wallet Balance: Rs. %.2f%n", plate, balance);
        } catch (InvalidVehicleDataException e) {
            System.err.println("[ERROR] Failed to register car: " + e.getMessage());
        }
    }

    private void registerBikePrompt() {
        System.out.println("\n--- Register New Motorcycle / Bike ---");
        String plate = readStringInput("Enter Plate Number (e.g. PB65XY1234): ").toUpperCase();
        String owner = readStringInput("Enter Owner Name: ");
        int cc = readIntInput("Enter Engine Capacity in CC (50-2000): ", 50, 2000);
        boolean emerg = readBooleanInput("Is this a police emergency bike? (yes/no): ");
        double balance = readDoubleInput("Enter Initial FASTag Wallet Balance (Rs.): ", 0.0, 50000.0);

        try {
            Bike bike = new Bike(plate, owner, LocalDate.now(), emerg, cc);
            plazaManager.registerVehicleWithWallet(bike, balance);
            System.out.printf("[SUCCESS] Bike [%s] registered with FASTag Wallet Balance: Rs. %.2f%n", plate, balance);
        } catch (InvalidVehicleDataException e) {
            System.err.println("[ERROR] Failed to register bike: " + e.getMessage());
        }
    }

    private void registerTruckPrompt() {
        System.out.println("\n--- Register New Commercial Truck ---");
        String plate = readStringInput("Enter Plate Number (e.g. HR26TR7890): ").toUpperCase();
        String owner = readStringInput("Enter Transporter Name: ");
        int axles = readIntInput("Enter Axle Count (2-12): ", 2, 12);
        double weight = readDoubleInput("Enter Gross Load Weight in Tons (1.0-60.0): ", 1.0, 60.0);
        boolean emerg = readBooleanInput("Is this a fire brigade truck? (yes/no): ");
        double balance = readDoubleInput("Enter Initial FASTag Wallet Balance (Rs.): ", 0.0, 100000.0);

        try {
            Truck truck = new Truck(plate, owner, LocalDate.now(), emerg, axles, weight);
            plazaManager.registerVehicleWithWallet(truck, balance);
            System.out.printf("[SUCCESS] Truck [%s] registered with FASTag Wallet Balance: Rs. %.2f%n", plate, balance);
        } catch (InvalidVehicleDataException e) {
            System.err.println("[ERROR] Failed to register truck: " + e.getMessage());
        }
    }

    private void sortAndDisplayFleet() {
        List<Vehicle> list = plazaManager.getVehicleRegistry().getAllVehicles();
        if (list.isEmpty()) {
            System.out.println("Fleet is empty.");
            return;
        }

        Collections.sort(list, new VehiclePlateComparator());
        System.out.println("==========================================================================================");
        System.out.println("       FLEET SORTED BY CATEGORY & PLATE (VehiclePlateComparator)                          ");
        System.out.println("==========================================================================================");
        int i = 1;
        for (Vehicle v : list) {
            System.out.printf("%2d. %s%n", i++, v.toString());
        }
    }

    private void searchVehiclePrompt() {
        String plate = readStringInput("Enter Plate Number to search: ").toUpperCase();
        Vehicle v = plazaManager.getVehicleRegistry().findVehicle(plate);
        if (v != null) {
            System.out.println("\n[FOUND] " + v.toString());
            System.out.printf("  Standard Toll Fee : Rs. %.2f%n", v.calculateStandardToll());
            Wallet w = plazaManager.getWalletManager().getWallet(plate);
            if (w != null) {
                System.out.printf("  Wallet ID         : %s%n", w.getWalletId());
                System.out.printf("  Wallet Balance    : Rs. %.2f%n", w.getBalance());
            } else {
                System.out.println("  Wallet            : No wallet linked.");
            }
            if (plazaManager.getBlacklistManager().isBlacklisted(plate)) {
                System.out.println("  Blacklist Status  : ** BLACKLISTED ** (" + plazaManager.getBlacklistManager().getBlacklistReason(plate) + ")");
            } else {
                System.out.println("  Blacklist Status  : Clean");
            }
        } else {
            System.out.println("[NOT FOUND] No vehicle registered with plate: " + plate);
        }
    }

    private void handleWalletMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------------- PREPAID WALLET OPERATIONS ------------------");
            System.out.println("  1. View All Registered Wallets");
            System.out.println("  2. Check Specific Wallet Balance");
            System.out.println("  3. Recharge Wallet (with Optional Promo Code)");
            System.out.println("  4. Create Wallet for Registered Vehicle");
            System.out.println("  0. Back to Main Menu");
            int sub = readIntInput("Select an option (0-4): ", 0, 4);

            switch (sub) {
                case 1:
                    plazaManager.getWalletManager().displayAllWallets();
                    break;
                case 2: {
                    String plate = readStringInput("Enter Vehicle Plate: ").toUpperCase();
                    Wallet w = plazaManager.getWalletManager().getWallet(plate);
                    if (w != null) {
                        System.out.printf("[WALLET FOUND] ID: %s | Plate: %s | Balance: Rs. %.2f | Active: %s%n",
                                w.getWalletId(), w.getVehiclePlate(), w.getBalance(), w.isActive());
                    } else {
                        System.out.println("[NOT FOUND] No wallet found for vehicle: " + plate);
                    }
                    break;
                }
                case 3: {
                    String plate = readStringInput("Enter Vehicle Plate: ").toUpperCase();
                    if (!plazaManager.getWalletManager().hasWallet(plate)) {
                        System.out.println("[ERROR] No wallet exists for plate: " + plate);
                        break;
                    }
                    double amount = readDoubleInput("Enter Recharge Amount (Rs.): ", 1.0, 50000.0);
                    System.out.println("Available Promos: FASTAG10 (10% bonus), HIGHWAY50 (Rs. 50 bonus on >= Rs. 500)");
                    String promo = readStringInput("Enter Promo Code (or press Enter to skip): ");

                    try {
                        Transaction txn = plazaManager.rechargeWallet(plate, amount, promo);
                        Wallet w = plazaManager.getWalletManager().getWallet(plate);
                        System.out.printf("[SUCCESS] %s! New Wallet Balance: Rs. %.2f%n", txn.getRemarks(), w.getBalance());
                    } catch (Exception e) {
                        System.err.println("[ERROR] Recharge failed: " + e.getMessage());
                    }
                    break;
                }
                case 4: {
                    String plate = readStringInput("Enter Vehicle Plate: ").toUpperCase();
                    if (!plazaManager.getVehicleRegistry().containsVehicle(plate)) {
                        System.out.println("[ERROR] Vehicle must be registered first before creating wallet.");
                        break;
                    }
                    if (plazaManager.getWalletManager().hasWallet(plate)) {
                        System.out.println("[INFO] Wallet already exists for plate: " + plate);
                        break;
                    }
                    double init = readDoubleInput("Enter Initial Balance (Rs.): ", 0.0, 50000.0);
                    Wallet w = plazaManager.getWalletManager().createWallet(plate, init);
                    plazaManager.saveAllData();
                    System.out.println("[SUCCESS] Created: " + w.toString());
                    break;
                }
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void handleManualTollCrossing() {
        System.out.println("----------------- MANUAL TOLL BOOTH CROSSING -----------------");
        String plate = readStringInput("Enter Vehicle Plate Number: ").toUpperCase();
        Vehicle v = plazaManager.getVehicleRegistry().findVehicle(plate);
        if (v == null) {
            System.out.println("[ERROR] Vehicle is not registered in the system.");
            return;
        }

        double fee = plazaManager.getTollCalculator().calculateToll(v);
        System.out.printf("Vehicle Details : %s%n", v.toString());
        System.out.printf("Toll Fee Due    : Rs. %.2f%n", fee);

        boolean confirm = readBooleanInput("Proceed with automatic FASTag deduction and receipt generation? (yes/no): ");
        if (!confirm) {
            System.out.println("Crossing cancelled.");
            return;
        }

        try {
            Receipt receipt = plazaManager.processManualCrossing(plate, "BOOTH-MANUAL");
            System.out.println("\n[BARRIER OPEN] Transaction Successful! Official Receipt Generated:");
            System.out.println(receipt.toString());
            System.out.printf("Receipt file saved permanently to: data/receipts/%s.txt%n", receipt.getReceiptNumber());
        } catch (VehicleBlacklistedException e) {
            System.err.println("\n**************************************************************");
            System.err.println("SECURITY ALERT: " + e.getMessage());
            System.err.println("The vehicle has been detained at the toll barrier!");
            System.err.println("**************************************************************");
        } catch (InsufficientBalanceException e) {
            System.err.println("\n[TRANSACTION DECLINED] " + e.getMessage());
            System.err.println("Please recharge the wallet before the toll barrier opens.");
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleBlacklistMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------------- VEHICLE BLACKLIST MANAGEMENT ---------------");
            System.out.println("  1. View All Blacklisted Vehicles (Iterator)");
            System.out.println("  2. Add Vehicle to Blacklist");
            System.out.println("  3. Remove Vehicle from Blacklist");
            System.out.println("  0. Back to Main Menu");
            int sub = readIntInput("Select an option (0-3): ", 0, 3);

            switch (sub) {
                case 1:
                    plazaManager.getBlacklistManager().displayAllBlacklisted();
                    break;
                case 2: {
                    String plate = readStringInput("Enter Plate Number to Blacklist: ").toUpperCase();
                    String reason = readStringInput("Enter Reason for Blacklisting: ");
                    plazaManager.getBlacklistManager().addBlacklist(plate, reason);
                    plazaManager.saveAllData();
                    System.out.printf("[SUCCESS] Vehicle [%s] added to security blacklist.%n", plate);
                    break;
                }
                case 3: {
                    String plate = readStringInput("Enter Plate Number to Un-blacklist: ").toUpperCase();
                    boolean removed = plazaManager.getBlacklistManager().removeBlacklist(plate);
                    if (removed) {
                        plazaManager.saveAllData();
                        System.out.printf("[SUCCESS] Vehicle [%s] removed from blacklist.%n", plate);
                    } else {
                        System.out.printf("[NOT FOUND] Vehicle [%s] was not on the blacklist.%n", plate);
                    }
                    break;
                }
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void handleLaneSimulation() {
        System.out.println("----------- MULTITHREADED MULTI-LANE TOLL SIMULATION ----------");
        System.out.println("Demonstrates: Thread, Runnable, wait(), notify(), notifyAll(),");
        System.out.println("              PriorityQueue for emergency vehicles, and concurrent lanes.");
        int lanes = readIntInput("Enter number of parallel toll lanes to operate (1-4): ", 1, 4);
        int vehicles = readIntInput("Enter number of incoming vehicles to simulate (3-20): ", 3, 20);

        plazaManager.getSimulationManager().runSimulation(lanes, vehicles);
        plazaManager.saveAllData();
    }

    private void handleAnalyticsDashboard() {
        TollAnalytics analytics = plazaManager.getAnalytics();
        analytics.printAnalyticsDashboard();
    }

    private void handleTransactionAudit() {
        boolean back = false;
        while (!back) {
            List<Transaction> txns = plazaManager.getWalletManager().getAllTransactions();
            System.out.println("\n----------------- TRANSACTION AUDIT & TRAVERSAL --------------");
            System.out.println("  1. Forward Traversal [Chronological] (ListIterator.next)");
            System.out.println("  2. Reverse Traversal [Newest First] (ListIterator.previous)");
            System.out.println("  3. Sort by Transaction Amount (TollAmountComparator - Descending)");
            System.out.println("  4. Filter History by Vehicle Plate (Iterator)");
            System.out.println("  0. Back to Main Menu");
            int sub = readIntInput("Select an option (0-4): ", 0, 4);

            switch (sub) {
                case 1:
                    TransactionHistoryViewer.displayForward(txns);
                    break;
                case 2:
                    TransactionHistoryViewer.displayBackward(txns);
                    break;
                case 3:
                    Collections.sort(txns, new TollAmountComparator(true));
                    System.out.println("==========================================================================================");
                    System.out.println("         TRANSACTIONS SORTED BY AMOUNT DESCENDING (TollAmountComparator)                  ");
                    System.out.println("==========================================================================================");
                    int i = 1;
                    for (Transaction t : txns) {
                        System.out.printf("%2d. %s%n", i++, t.toString());
                    }
                    break;
                case 4: {
                    String plate = readStringInput("Enter Vehicle Plate: ");
                    TransactionHistoryViewer.displayByVehicle(txns, plate);
                    break;
                }
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void handleFileStorageInspection() {
        plazaManager.getFileStorageManager().printStorageStatus();
        plazaManager.getReceiptGenerator().displayAllReceipts();

        File dir = new File(plazaManager.getFileStorageManager().getReceiptsDirPath());
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        System.out.println("\nPhysical Receipt Files on Disk (" + (files != null ? files.length : 0) + " files):");
        if (files != null && files.length > 0) {
            for (File f : files) {
                System.out.printf("  - %-20s (%d bytes)%n", f.getName(), f.length());
            }
        } else {
            System.out.println("  (No receipt files currently generated on disk)");
        }
    }

    private void handleSeedDemoData() {
        boolean confirm = readBooleanInput("Reset and seed fresh academic demo data? (yes/no): ");
        if (confirm) {
            plazaManager.getVehicleRegistry().clear();
            plazaManager.getWalletManager().clear();
            plazaManager.seedDemoDataIfEmpty();
            System.out.println("[SUCCESS] Demo data re-initialized successfully!");
        }
    }

  
    private String readStringInput(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        while (line == null || line.trim().isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            line = scanner.nextLine();
        }
        return line.trim();
    }

    private int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                int val = Integer.parseInt(line.trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter a valid integer between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter an integer.");
            }
        }
    }

    private double readDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                double val = Double.parseDouble(line.trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter a valid number between %.2f and %.2f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid decimal number.");
            }
        }
    }

    private boolean readBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.equals("yes") || line.equals("y") || line.equals("true")) {
                return true;
            }
            if (line.equals("no") || line.equals("n") || line.equals("false")) {
                return false;
            }
            System.out.println("Please enter 'yes' or 'no'.");
        }
    }
}
