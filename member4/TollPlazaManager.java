package tollplaza.member4;

import tollplaza.member1.*;
import tollplaza.member2.*;
import tollplaza.member3.*;

import java.time.LocalDate;
import java.util.List;


public class TollPlazaManager {

  
    private final VehicleRegistry vehicleRegistry;
    private final BlacklistManager blacklistManager;
    private final VehicleFileManager vehicleFileManager;


    private final WalletManager walletManager;
    private final WalletFileManager walletFileManager;
    private final TransactionFileManager transactionFileManager;
    private final PaymentProcessor paymentProcessor;

    
    private final TollCalculatable tollCalculator;
    private final LaneSimulationManager simulationManager;

   
    private final ReceiptGenerator receiptGenerator;
    private final FileStorageManager fileStorageManager;

    public TollPlazaManager(String dataDirectory) {
    
        this.fileStorageManager = new FileStorageManager(dataDirectory);
        this.fileStorageManager.initializeStorage();

     
        this.vehicleRegistry = new VehicleRegistry();
        this.blacklistManager = new BlacklistManager(fileStorageManager.getBlacklistFilePath());
        this.vehicleFileManager = new VehicleFileManager(fileStorageManager.getVehiclesFilePath());


        this.walletManager = new WalletManager();
        this.walletFileManager = new WalletFileManager(fileStorageManager.getWalletsFilePath());
        this.transactionFileManager = new TransactionFileManager(fileStorageManager.getTransactionsFilePath());
        this.paymentProcessor = new AutomatedTollDeductor();


        this.receiptGenerator = new ReceiptGenerator(fileStorageManager.getReceiptsDirPath());


        this.tollCalculator = new StandardTollCalculator();
        this.simulationManager = new LaneSimulationManager(
                vehicleRegistry, tollCalculator, paymentProcessor,
                walletManager, blacklistManager, transactionFileManager
        );

        loadAllData();
    }

    
    public synchronized void loadAllData() {
        vehicleFileManager.loadVehicles(vehicleRegistry);
        walletFileManager.loadWallets(walletManager);
        List<Transaction> loadedTxns = transactionFileManager.loadTransactions();
        for (Transaction t : loadedTxns) {
            walletManager.recordTransaction(t);
        }
    }

 
    public synchronized void saveAllData() {
        vehicleFileManager.saveVehicles(vehicleRegistry.getAllVehicles());
        walletFileManager.saveWallets(walletManager.getAllWallets());
        blacklistManager.saveBlacklistToFile();
    }

  
    public boolean registerVehicleWithWallet(Vehicle vehicle, double initialWalletBalance)
            throws InvalidVehicleDataException {
        vehicleRegistry.registerVehicle(vehicle);
        walletManager.createWallet(vehicle.getPlateNumber(), initialWalletBalance);
        saveAllData();
        return true;
    }

 
    public Receipt processManualCrossing(String plateNumber, String laneId)
            throws VehicleBlacklistedException, InsufficientBalanceException {
        String cleanPlate = (plateNumber != null) ? plateNumber.trim().toUpperCase() : "";

        Vehicle vehicle = vehicleRegistry.findVehicle(cleanPlate);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle [" + cleanPlate + "] is not registered in the system.");
        }

        if (blacklistManager.isBlacklisted(cleanPlate)) {
            String reason = blacklistManager.getBlacklistReason(cleanPlate);
            throw new VehicleBlacklistedException(cleanPlate, reason);
        }

        Wallet wallet = walletManager.getWallet(cleanPlate);
        if (wallet == null) {
            throw new IllegalArgumentException("No wallet associated with vehicle [" + cleanPlate + "].");
        }

        double toll = tollCalculator.calculateToll(vehicle);

        Transaction txn = paymentProcessor.processPayment(wallet, vehicle, toll, laneId);
        walletManager.recordTransaction(txn);
        transactionFileManager.appendTransaction(txn);

        Receipt receipt = receiptGenerator.generateReceipt(
                laneId, cleanPlate, vehicle.getVehicleType(), toll, wallet.getBalance()
        );

        saveAllData();
        return receipt;
    }

    public Transaction rechargeWallet(String plateNumber, double amount, String promoCode) {
        Transaction txn = walletManager.rechargeWallet(plateNumber, amount, promoCode);
        transactionFileManager.appendTransaction(txn);
        saveAllData();
        return txn;
    }

    public void seedDemoDataIfEmpty() {
        if (vehicleRegistry.getVehicleCount() > 0) {
            return;
        }

        try {
           
            registerVehicleWithWallet(new Car("DL01AB1234", "Rahul Sharma", LocalDate.of(2025, 1, 10), false, 5, false), 500.0);
            registerVehicleWithWallet(new Car("MH02CD5678", "Priya Verma", LocalDate.of(2025, 3, 15), false, 4, true), 300.0);
            registerVehicleWithWallet(new Car("KA03EF9012", "Emergency Ambulance", LocalDate.of(2025, 2, 20), true, 6, false), 0.0);

           
            registerVehicleWithWallet(new Bike("PB65CD1122", "Simran Kaur", LocalDate.of(2025, 4, 5), false, 150), 200.0);
            registerVehicleWithWallet(new Bike("CH01GH3344", "Arjun Singh", LocalDate.of(2025, 5, 12), false, 650), 100.0);

         
            registerVehicleWithWallet(new Truck("HR26IJ5566", "National Logistics", LocalDate.of(2024, 11, 1), false, 4, 18.5), 1500.0);
            registerVehicleWithWallet(new Truck("RJ14KL7788", "Express Cargo", LocalDate.of(2024, 12, 18), false, 2, 8.0), 50.0); // Low balance to trigger InsufficientBalanceException

      
            blacklistManager.addBlacklist("DL09ZZ9999", "Reported Stolen by Delhi Traffic Police");

            saveAllData();
            System.out.println(">>> Seeded academic demo data: 7 Vehicles, 7 Wallets, 1 Blacklist record.");
        } catch (InvalidVehicleDataException e) {
            System.err.println("Error seeding demo data: " + e.getMessage());
        }
    }

    public VehicleRegistry getVehicleRegistry() {
        return vehicleRegistry;
    }

    public BlacklistManager getBlacklistManager() {
        return blacklistManager;
    }

    public WalletManager getWalletManager() {
        return walletManager;
    }

    public TollCalculatable getTollCalculator() {
        return tollCalculator;
    }

    public LaneSimulationManager getSimulationManager() {
        return simulationManager;
    }

    public ReceiptGenerator getReceiptGenerator() {
        return receiptGenerator;
    }

    public FileStorageManager getFileStorageManager() {
        return fileStorageManager;
    }

    public TollAnalytics getAnalytics() {
        return new TollAnalytics(walletManager.getAllTransactions());
    }
}
