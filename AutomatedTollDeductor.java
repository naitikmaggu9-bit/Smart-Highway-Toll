package tollplaza.member2;

import tollplaza.member1.Vehicle;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;


public class AutomatedTollDeductor implements PaymentProcessor {

    private static final AtomicLong TXN_COUNTER = new AtomicLong(1000);

    @Override
    public boolean validateBalance(Wallet wallet, double requiredAmount) {
        if (wallet == null) return false;
        return wallet.getBalance() >= requiredAmount;
    }

    @Override
    public Transaction processPayment(Wallet wallet, Vehicle vehicle, double amount, String laneId)
            throws InsufficientBalanceException {
        if (wallet == null) {
            throw new IllegalArgumentException("Cannot process payment: Wallet is null.");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Cannot process payment: Vehicle is null.");
        }

        String txnId = "TXN-" + System.currentTimeMillis() % 1000000 + "-" + TXN_COUNTER.incrementAndGet();
        String category = vehicle.getVehicleType();

        if (vehicle.isEmergency() || amount <= 0.0) {
            return new Transaction(txnId, wallet.getWalletId(), vehicle.getPlateNumber(),
                    category, 0.0, Transaction.TYPE_TOLL, Transaction.STATUS_SUCCESS,
                    LocalDateTime.now(), "Emergency/Exempt Vehicle on Lane " + laneId);
        }

        if (wallet.getBalance() < amount) {

            Transaction failedTxn = new Transaction(txnId, wallet.getWalletId(), vehicle.getPlateNumber(),
                    category, amount, Transaction.TYPE_TOLL, Transaction.STATUS_FAILED,
                    LocalDateTime.now(), "Insufficient Funds at Lane " + laneId);
            throw new InsufficientBalanceException(wallet.getWalletId(), wallet.getBalance(), amount);
        }

        wallet.deduct(amount);

        return new Transaction(txnId, wallet.getWalletId(), vehicle.getPlateNumber(),
                category, amount, Transaction.TYPE_TOLL, Transaction.STATUS_SUCCESS,
                LocalDateTime.now(), "Deducted at Lane " + laneId);
    }
}
