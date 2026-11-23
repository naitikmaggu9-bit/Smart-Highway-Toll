package tollplaza.member3;

import tollplaza.member1.Vehicle;


public interface TollCalculatable {

    
    double calculateToll(Vehicle vehicle);

    double calculateToll(Vehicle vehicle, boolean isPeakHour, double discountPercentage);
}
