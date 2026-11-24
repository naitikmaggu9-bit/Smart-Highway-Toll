package tollplaza.member3;

import tollplaza.member1.Vehicle;
import java.util.Comparator;

public class VehiclePlateComparator implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle v1, Vehicle v2) {
        if (v1 == null && v2 == null) return 0;
        if (v1 == null) return -1;
        if (v2 == null) return 1;

        if (v1.isEmergency() != v2.isEmergency()) {
            return v1.isEmergency() ? -1 : 1;
        }

        int typeCompare = v1.getVehicleType().compareTo(v2.getVehicleType());
        if (typeCompare != 0) {
            return typeCompare;
        }

        return v1.getPlateNumber().compareTo(v2.getPlateNumber());
    }
}
