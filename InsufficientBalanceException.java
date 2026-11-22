package tollplaza.member2;

public class InsufficientBalanceException extends Exception {

    private final String walletId;
    private final double availableBalance;
    private final double requiredAmount;

    public InsufficientBalanceException(String walletId, double availableBalance, double requiredAmount) {
        super(String.format("Wallet [%s] has insufficient funds. Available: Rs. %.2f, Required: Rs. %.2f (Shortage: Rs. %.2f)",
                walletId, availableBalance, requiredAmount, (requiredAmount - availableBalance)));
        this.walletId = walletId;
        this.availableBalance = availableBalance;
        this.requiredAmount = requiredAmount;
    }

    public String getWalletId() {
        return walletId;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public double getRequiredAmount() {
        return requiredAmount;
    }

    public double getShortage() {
        return requiredAmount - availableBalance;
    }
}
