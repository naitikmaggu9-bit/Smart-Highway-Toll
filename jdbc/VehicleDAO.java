package tollplaza.jdbc;

import tollplaza.member1.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class VehicleDAO {

    public void saveVehicle(Vehicle vehicle) throws SQLException {
        if (vehicle == null) return;

        String sql = "INSERT INTO vehicles (plate_number, owner_name, vehicle_type, registration_date, is_emergency, extra_field1, extra_field2) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE owner_name = VALUES(owner_name), is_emergency = VALUES(is_emergency), " +
                     "extra_field1 = VALUES(extra_field1), extra_field2 = VALUES(extra_field2)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getPlateNumber());
            stmt.setString(2, vehicle.getOwnerName());
            stmt.setString(3, vehicle.getVehicleType());
            stmt.setDate(4, Date.valueOf(vehicle.getRegistrationDate()));
            stmt.setBoolean(5, vehicle.isEmergency());

            if (vehicle instanceof Car) {
                Car c = (Car) vehicle;
                stmt.setInt(6, c.getSeatingCapacity());
                stmt.setDouble(7, c.isCommercial() ? 1.0 : 0.0);
            } else if (vehicle instanceof Bike) {
                Bike b = (Bike) vehicle;
                stmt.setInt(6, b.getEngineCapacityCC());
                stmt.setDouble(7, 0.0);
            } else if (vehicle instanceof Truck) {
                Truck t = (Truck) vehicle;
                stmt.setInt(6, t.getAxleCount());
                stmt.setDouble(7, t.getLoadWeightTons());
            } else {
                stmt.setInt(6, 0);
                stmt.setDouble(7, 0.0);
            }

            stmt.executeUpdate();
        }
    }

    public Vehicle findByPlate(String plateNumber) throws SQLException {
        if (plateNumber == null) return null;

        String sql = "SELECT * FROM vehicles WHERE plate_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber.trim().toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVehicle(rs);
                }
            }
        }
        return null;
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY registration_date DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }
        }
        return list;
    }

    public boolean deleteVehicle(String plateNumber) throws SQLException {
        if (plateNumber == null) return false;
        String sql = "DELETE FROM vehicles WHERE plate_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, plateNumber.trim().toUpperCase());
            return stmt.executeUpdate() > 0;
        }
    }

    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        String plate = rs.getString("plate_number");
        String owner = rs.getString("owner_name");
        String type = rs.getString("vehicle_type");
        LocalDate regDate = rs.getDate("registration_date").toLocalDate();
        boolean isEmergency = rs.getBoolean("is_emergency");
        int extra1 = rs.getInt("extra_field1");
        double extra2 = rs.getDouble("extra_field2");

        if ("CAR".equalsIgnoreCase(type)) {
            return new Car(plate, owner, regDate, isEmergency, extra1, extra2 > 0.5);
        } else if ("BIKE".equalsIgnoreCase(type)) {
            return new Bike(plate, owner, regDate, isEmergency, extra1);
        } else if ("TRUCK".equalsIgnoreCase(type)) {
            return new Truck(plate, owner, regDate, isEmergency, extra1, extra2);
        } else {
            return new Car(plate, owner, regDate, isEmergency, 5, false);
        }
    }
}
