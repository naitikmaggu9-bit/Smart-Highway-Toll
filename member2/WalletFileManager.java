package tollplaza.member2;

import java.io.*;
import java.util.List;


public class WalletFileManager {

    private final String filePath;

    public WalletFileManager(String filePath) {
        this.filePath = filePath;
    }

    public synchronized void saveWallets(List<Wallet> wallets) {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            writer.println("# WALLETS DATA FILE");
            writer.println("# Format: WALLET_ID|PLATE|BALANCE|ACTIVE");
            for (Wallet w : wallets) {
                writer.printf("%s|%s|%.2f|%b%n",
                        w.getWalletId(), w.getVehiclePlate(), w.getBalance(), w.isActive());
            }
        } catch (IOException e) {
            System.err.println("Error saving wallets to file: " + e.getMessage());
        }
    }

    public synchronized void loadWallets(WalletManager walletManager) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] p = line.split("\\|");
                if (p.length < 4) continue;

                String walletId = p[0].trim();
                String plate = p[1].trim().toUpperCase();
                double balance = Double.parseDouble(p[2].trim());
                boolean active = Boolean.parseBoolean(p[3].trim());

                Wallet wallet = new Wallet(walletId, plate, balance, active);
                walletManager.registerExistingWallet(wallet);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading wallets from file: " + e.getMessage());
        }
    }
}
