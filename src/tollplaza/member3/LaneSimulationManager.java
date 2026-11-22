package tollplaza.member3;

import tollplaza.member1.BlacklistManager;
import tollplaza.member1.VehicleRegistry;
import tollplaza.member2.PaymentProcessor;
import tollplaza.member2.TransactionFileManager;
import tollplaza.member2.WalletManager;

import java.util.ArrayList;
import java.util.List;


public class LaneSimulationManager {

    private final LaneQueue sharedQueue;
    private final List<TollBoothLane> lanes;
    private final List<Thread> laneThreads;
    private final VehicleRegistry vehicleRegistry;
    private final TollCalculatable tollCalculator;
    private final PaymentProcessor paymentProcessor;
    private final WalletManager walletManager;
    private final BlacklistManager blacklistManager;
    private final TransactionFileManager txnFileManager;

    public LaneSimulationManager(VehicleRegistry vehicleRegistry, TollCalculatable tollCalculator,
                                 PaymentProcessor paymentProcessor, WalletManager walletManager,
                                 BlacklistManager blacklistManager, TransactionFileManager txnFileManager) {
        this.sharedQueue = new LaneQueue(25);
        this.lanes = new ArrayList<>();
        this.laneThreads = new ArrayList<>();
        this.vehicleRegistry = vehicleRegistry;
        this.tollCalculator = tollCalculator;
        this.paymentProcessor = paymentProcessor;
        this.walletManager = walletManager;
        this.blacklistManager = blacklistManager;
        this.txnFileManager = txnFileManager;
    }


 
    public void runSimulation(int numberOfLanes, int vehiclesToSimulate) {
        System.out.println("==========================================================================================");
        System.out.printf("          STARTING MULTITHREADED TOLL SIMULATION (%d LANES, %d VEHICLES)           %n",
                numberOfLanes, vehiclesToSimulate);
        System.out.println("==========================================================================================");

        lanes.clear();
        laneThreads.clear();


        for (int i = 1; i <= numberOfLanes; i++) {
            String laneId = "LANE-0" + i;
            String laneName = (i == 1) ? "FASTag Express" : (i == 2 ? "Commercial Heavy" : "Standard Multi-Axle");
            TollBoothLane lane = new TollBoothLane(laneId, laneName, sharedQueue, tollCalculator,
                    paymentProcessor, walletManager, blacklistManager, txnFileManager);
            lanes.add(lane);

            Thread thread = new Thread(lane, "Thread-" + laneId);
            laneThreads.add(thread);
            thread.start();
        }

        VehicleArrivalSimulator simulator = new VehicleArrivalSimulator(sharedQueue, vehicleRegistry, vehiclesToSimulate);
        Thread simThread = new Thread(simulator, "Thread-ArrivalSimulator");
        simThread.start();

        try {
            simThread.join();

            while (!sharedQueue.isEmpty()) {
                Thread.sleep(200);
            }

            sharedQueue.close();
            for (TollBoothLane lane : lanes) {
                lane.stopLane();
            }

            for (Thread t : laneThreads) {
                t.join(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("==========================================================================================");
        System.out.println("                         SIMULATION SUMMARY REPORT                                        ");
        System.out.println("==========================================================================================");
        double grandTotal = 0.0;
        int totalVehicles = 0;
        for (TollBoothLane lane : lanes) {
            System.out.printf("  Lane: %-8s (%-20s) | Vehicles: %2d | Revenue: Rs. %8.2f%n",
                    lane.getLaneId(), lane.getLaneName(),
                    lane.getVehiclesProcessedCount(), lane.getTotalTollCollected());
            grandTotal += lane.getTotalTollCollected();
            totalVehicles += lane.getVehiclesProcessedCount();
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("  TOTAL PROCESSED: %d vehicles | TOTAL REVENUE GENERATED: Rs. %.2f%n", totalVehicles, grandTotal);
        System.out.println("==========================================================================================");
    }
}
