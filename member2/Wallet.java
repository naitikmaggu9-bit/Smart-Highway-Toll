package tollplaza.member2;

public class Wallet {

    private final String walletId;
    private final String vehiclePlate;
    private double balance;
    private boolean active;

    public Wallet(String walletId, String vehiclePlate, double initialBalance) {
        this.walletId = (walletId != null) ? walletId.trim() : "W-000";
        this.vehiclePlate = (vehiclePlate != null) ? vehiclePlate.trim().toUpperCase() : "";
        this.balance = Math.max(0.0, initialBalance);
        this.active = true;
    }

    public Wallet(String walletId, String vehiclePlate, double balance, boolean active) {
        this.walletId = walletId;
        this.vehiclePlate = vehiclePlate;
        this.balance = balance;
        this.active = active;
    }

 
    public synchronized void recharge(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Recharge amount must be positive.");
        }
        this.balance += amount;
    }


    public synchronized double recharge(double amount, String promoCode) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Recharge amount must be positive.");
        }
        double bonus = 0.0;
        if (promoCode != null) {
            String code = promoCode.trim().toUpperCase();
            if (code.equals("FASTAG10")) {
                bonus = amount * 0.10; 
            } else if (code.equals("HIGHWAY50") && amount >= 500) {
                bonus = 50.0; 
            }
        }
        this.balance += (amount + bonus);
        return bonus;
    }

  
    public synchronized boolean deduct(double amount) throws InsufficientBalanceException {
        if (!active) {
            throw new IllegalStateException("Wallet " + walletId + " is deactivated.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Deduction amount cannot be negative.");
        }
        if (this.balance < amount) {
            throw new InsufficientBalanceException(this.walletId, this.balance, amount);
        }
        this.balance -= amount;
        return true;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("Wallet [ID: %-8s | Plate: %-10s | Balance: Rs. %8.2f | Status: %s]",
                walletId, vehiclePlate, balance, (active ? "ACTIVE" : "INACTIVE"));
    }
}
