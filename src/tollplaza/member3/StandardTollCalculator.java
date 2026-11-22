package tollplaza.member3;

import tollplaza.member1.Vehicle;


public class StandardTollCalculator implements TollCalculatable {

    @Override
    public double calculateToll(Vehicle vehicle) {
        if (vehicle == null) {
            return 0.0;
        }

        if (vehicle.isEmergency()) {
            return 0.0;
        }
 
        return vehicle.calculateStandardToll();
    }

    @Override
    public double calculateToll(Vehicle vehicle, boolean isPeakHour, double discountPercentage) {
        double baseToll = calculateToll(vehicle);
        if (baseToll <= 0.0) {
            return 0.0; 
        }

        if (isPeakHour) {
            baseToll += (baseToll * 0.20);
        }

        if (discountPercentage > 0.0) {
            double discount = baseToll * (discountPercentage / 100.0);
            baseToll -= discount;
        }

        return Math.max(0.0, baseToll);
    }
}
