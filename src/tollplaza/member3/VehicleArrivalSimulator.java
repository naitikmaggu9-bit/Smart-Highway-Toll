package tollplaza.member3;

import tollplaza.member1.Vehicle;
import tollplaza.member1.VehicleRegistry;

import java.util.List;
import java.util.Random;

public class VehicleArrivalSimulator implements Runnable {

    private final LaneQueue queue;
    private final VehicleRegistry registry;
    private final int totalVehiclesToSimulate;
    private final Random random;
    private volatile boolean running;

    public VehicleArrivalSimulator(LaneQueue queue, VehicleRegistry registry, int totalVehiclesToSimulate) {
        this.queue = queue;
        this.registry = registry;
        this.totalVehiclesToSimulate = totalVehiclesToSimulate;
        this.random = new Random();
        this.running = true;
    }

    @Override
    public void run() {
        List<Vehicle> availableVehicles = registry.getAllVehicles();
        if (availableVehicles.isEmpty()) {
            System.out.println("[SIMULATOR] No vehicles registered to simulate traffic.");
            return;
        }

        System.out.printf("[SIMULATOR] Vehicle arrival simulation initiated for %d vehicles.%n", totalVehiclesToSimulate);
        int dispatched = 0;

        while (running && dispatched < totalVehiclesToSimulate) {
            try {

                Vehicle vehicle = availableVehicles.get(random.nextInt(availableVehicles.size()));

                System.out.printf("  -> [VEHICLE ARRIVED] %s approached the toll plaza gates.%n", vehicle.getPlateNumber());
                queue.enqueueVehicle(vehicle);
                dispatched++;

        
                int delay = 200 + random.nextInt(400);
                Thread.sleep(delay);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.printf("[SIMULATOR] All %d simulated vehicles arrived at the plaza gates.%n", dispatched);
    }

    public void stopSimulation() {
        this.running = false;
    }
}
