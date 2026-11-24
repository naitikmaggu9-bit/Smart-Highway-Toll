package tollplaza.jdbc;

import tollplaza.member1.Car;
import tollplaza.member1.Vehicle;
import tollplaza.member2.Wallet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TollPlazaJDBCDemo {

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("   SMART HIGHWAY TOLL PLAZA - JDBC / MySQL LIVE CONNECTOR     ");
        System.out.println("==============================================================");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MySQL root password (press Enter for 'root'): ");
        String pass = scanner.nextLine().trim();
        if (pass.isEmpty()) {
            pass = "root";
        }

        DBConnection.setCredentials(
                "jdbc:mysql://localhost:3306/tollplaza_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                pass
        );

        System.out.println("\n[1/3] Testing database connection to localhost:3306/tollplaza_db...");
        if (!DBConnection.testConnection()) {
            System.err.println("[ERROR] Unable to connect to MySQL database.");
            System.err.println("Please verify:");
            System.err.println(" 1. MySQL Server service is running.");
            System.err.println(" 2. Database 'tollplaza_db' exists (run 'database/schema.sql').");
            System.err.println(" 3. Your password matches.");
            return;
        }
        System.out.println("[SUCCESS] Connected to MySQL database!");

        VehicleDAO vehicleDAO = new VehicleDAO();
        WalletDAO walletDAO = new WalletDAO();
        BlacklistDAO blacklistDAO = new BlacklistDAO();

        try {
            System.out.println("\n[2/3] Fetching Registered Fleet from 'vehicles' table via JDBC...");
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            System.out.println("Found " + vehicles.size() + " vehicles in database:");
            for (Vehicle v : vehicles) {
                System.out.printf("  - [%s] Plate: %-12s | Owner: %-18s | Toll: Rs. %.2f%n",
                        v.getVehicleType(), v.getPlateNumber(), v.getOwnerName(), v.calculateStandardToll());
            }

            System.out.println("\n[3/3] Fetching FASTag Wallets from 'wallets' table via JDBC...");
            List<Wallet> wallets = walletDAO.getAllWallets();
            System.out.println("Found " + wallets.size() + " active wallets:");
            for (Wallet w : wallets) {
                System.out.printf("  - Wallet ID: %-8s | Plate: %-12s | Balance: Rs. %.2f%n",
                        w.getWalletId(), w.getVehiclePlate(), w.getBalance());
            }

            System.out.println("\n[DEMO] Inserting sample new vehicle via VehicleDAO...");
            String testPlate = "UP16DEMO" + (int)(Math.random() * 1000);
            Vehicle testCar = new Car(testPlate, "Test Highway Driver", LocalDate.now(), false, 5, false);
            vehicleDAO.saveVehicle(testCar);
            System.out.println("[SUCCESS] Inserted vehicle " + testPlate + " successfully into MySQL!");

            System.out.println("\n==============================================================");
            System.out.println("  JDBC Demonstration completed successfully!");
            System.out.println("==============================================================");

        } catch (SQLException e) {
            System.err.println("[SQL ERROR] " + e.getMessage());
        }
    }
}
