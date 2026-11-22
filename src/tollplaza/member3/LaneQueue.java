package tollplaza.member3;

import tollplaza.member1.Vehicle;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class LaneQueue {

    private final LinkedList<Vehicle> regularQueue;
    private final PriorityQueue<Vehicle> emergencyQueue;
    private final int capacity;
    private boolean open;

    public LaneQueue(int capacity) {
        this.capacity = capacity;
        this.regularQueue = new LinkedList<>();
    
        this.emergencyQueue = new PriorityQueue<>((v1, v2) -> {
          
            return v1.getPlateNumber().compareTo(v2.getPlateNumber());
        });
        this.open = true;
    }

  
    public synchronized void enqueueVehicle(Vehicle vehicle) throws InterruptedException {
        while (open && (regularQueue.size() + emergencyQueue.size()) >= capacity) {
            wait();
        }

        if (!open) {
            return;
        }

        if (vehicle.isEmergency()) {
            emergencyQueue.offer(vehicle);
            System.out.printf("  [LANE QUEUE] >>> EMERGENCY PRIORITY: %s jumped the queue!%n", vehicle.getPlateNumber());
        } else {
            regularQueue.offer(vehicle);
        }
        notifyAll();
    }

 
    public synchronized Vehicle dequeueVehicle() throws InterruptedException {
        while (open && emergencyQueue.isEmpty() && regularQueue.isEmpty()) {
        
            wait();
        }

        Vehicle vehicle = null;
        if (!emergencyQueue.isEmpty()) {
            vehicle = emergencyQueue.poll();
        } else if (!regularQueue.isEmpty()) {
            vehicle = regularQueue.poll();
        }

        notifyAll();
        return vehicle;
    }

    public synchronized int size() {
        return regularQueue.size() + emergencyQueue.size();
    }

    public synchronized boolean isEmpty() {
        return regularQueue.isEmpty() && emergencyQueue.isEmpty();
    }

    public synchronized void close() {
        this.open = false;
        notifyAll(); 
    }

    public synchronized boolean isOpen() {
        return open;
    }
}
