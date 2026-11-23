package tollplaza.member3;

import tollplaza.member1.BlacklistManager;
import tollplaza.member1.Vehicle;
import tollplaza.member2.*;


public class TollBoothLane implements Runnable {

    private final String laneId;
    private final String laneName;
    private final LaneQueue sharedQueue;
    private final TollCalculatable tollCalculator;
    private final PaymentProcessor paymentProcessor;
    private final WalletManager walletManager;
    private final BlacklistManager blacklistManager;
    private final TransactionFileManager txnFileManager;

    private double totalTollCollected;
    private int vehiclesProcessedCount;
    private volatile boolean running;

    public TollBoothLane(String laneId, String laneName, LaneQueue sharedQueue,
                         TollCalculatable tollCalculator, PaymentProcessor paymentProcessor,
                         WalletManager walletManager, BlacklistManager blacklistManager,
                         TransactionFileManager txnFileManager) {
        this.laneId = laneId;
        this.laneName = laneName;
        this.sharedQueue = sharedQueue;
        this.tollCalculator = tollCalculator;
        this.paymentProcessor = paymentProcessor;
        this.walletManager = walletManager;
        this.blacklistManager = blacklistManager;
        this.txnFileManager = txnFileManager;
        this.totalTollCollected = 0.0;
        this.vehiclesProcessedCount = 0;
        this.running = true;
    }

    @Override
    public void run() {
        System.out.printf("[%s - %s] Lane operational on thread: %s%n",
                laneId, laneName, Thread.currentThread().getName());

        while (running && (sharedQueue.isOpen() || !sharedQueue.isEmpty())) {
            try {
                Vehicle vehicle = sharedQueue.dequeueVehicle();
                if (vehicle == null) {
                    continue; 
                }

                processVehicle(vehicle);

        
                Thread.sleep(300);

            } catch (InterruptedException e) {
           
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.printf("[%s] Lane closed. Processed: %d vehicles | Collected: Rs. %.2f%n",
                laneId, vehiclesProcessedCount, totalTollCollected);
    }

    private void processVehicle(Vehicle vehicle) {
        String plate = vehicle.getPlateNumber();
        System.out.printf("[%s] Processing vehicle: %s (%s)...%n", laneId, plate, vehicle.getVehicleType());

       
        if (blacklistManager.isBlacklisted(plate)) {
            String reason = blacklistManager.getBlacklistReason(plate);
            System.err.printf("  *** [%s ALARM] BARRIER LOCKED! Vehicle %s is BLACKLISTED! Reason: %s ***%n",
                    laneId, plate, reason);
            return;
        }

  
        double toll = tollCalculator.calculateToll(vehicle);

        Wallet wallet = walletManager.getWallet(plate);
        if (wallet == null) {
            System.err.printf("  [%s WARNING] No FASTag wallet linked for %s! Cash manual collection required: Rs. %.2f%n",
                    laneId, plate, toll);
            return;
        }

        try {
            Transaction txn = paymentProcessor.processPayment(wallet, vehicle, toll, laneId);
            walletManager.recordTransaction(txn);
            if (txnFileManager != null) {
                txnFileManager.appendTransaction(txn);
            }

            totalTollCollected += toll;
            vehiclesProcessedCount++;

            System.out.printf("  [%s SUCCESS] Toll Rs. %.2f deducted for %s. Wallet Balance: Rs. %.2f%n",
                    laneId, toll, plate, wallet.getBalance());

        } catch (InsufficientBalanceException ex) {
            System.err.printf("  [%s DECLINED] %s for vehicle %s! Barrier remains DOWN.%n",
                    laneId, ex.getMessage(), plate);
        }
    }

    public void stopLane() {
        this.running = false;
    }

    public String getLaneId() {
        return laneId;
    }

    public String getLaneName() {
        return laneName;
    }

    public double getTotalTollCollected() {
        return totalTollCollected;
    }

    public int getVehiclesProcessedCount() {
        return vehiclesProcessedCount;
    }
}
