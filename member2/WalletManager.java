package tollplaza.member2;

import java.time.LocalDateTime;
import java.util.*;

public class WalletManager {

    private final Map<String, Wallet> walletByPlate;
    private final List<Transaction> transactionList;

    public WalletManager() {
        this.walletByPlate = new HashMap<>();
        this.transactionList = new LinkedList<>();
    }

    public synchronized Wallet createWallet(String plateNumber, double initialBalance) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be empty.");
        }
        String cleanPlate = plateNumber.trim().toUpperCase();
        if (walletByPlate.containsKey(cleanPlate)) {
            return walletByPlate.get(cleanPlate);
        }

        String walletId = "W-" + (1001 + walletByPlate.size());
        Wallet wallet = new Wallet(walletId, cleanPlate, initialBalance);
        walletByPlate.put(cleanPlate, wallet);

        if (initialBalance > 0) {
            Transaction initialTxn = new Transaction(
                    "TXN-INIT-" + walletId,
                    walletId, cleanPlate, "GENERAL", initialBalance,
                    Transaction.TYPE_RECHARGE, Transaction.STATUS_SUCCESS,
                    LocalDateTime.now(), "Initial Wallet Opening Balance"
            );
            transactionList.add(initialTxn);
        }

        return wallet;
    }

    public synchronized void registerExistingWallet(Wallet wallet) {
        if (wallet != null) {
            walletByPlate.put(wallet.getVehiclePlate(), wallet);
        }
    }

    public synchronized Wallet getWallet(String plateNumber) {
        if (plateNumber == null) return null;
        return walletByPlate.get(plateNumber.trim().toUpperCase());
    }

    public synchronized boolean hasWallet(String plateNumber) {
        if (plateNumber == null) return false;
        return walletByPlate.containsKey(plateNumber.trim().toUpperCase());
    }

    public synchronized Transaction rechargeWallet(String plateNumber, double amount, String promoCode) {
        Wallet wallet = getWallet(plateNumber);
        if (wallet == null) {
            throw new IllegalArgumentException("No wallet found for plate: " + plateNumber);
        }

        double bonus = wallet.recharge(amount, promoCode);
        double totalCredited = amount + bonus;

        String txnId = "TXN-REC-" + System.currentTimeMillis() % 1000000;
        String remarks = (bonus > 0) ? ("Recharged Rs. " + amount + " + Bonus Rs. " + bonus + " [" + promoCode + "]")
                                     : ("Recharged Rs. " + amount);

        Transaction txn = new Transaction(
                txnId, wallet.getWalletId(), wallet.getVehiclePlate(), "GENERAL",
                totalCredited, Transaction.TYPE_RECHARGE, Transaction.STATUS_SUCCESS,
                LocalDateTime.now(), remarks
        );
        transactionList.add(txn);
        return txn;
    }

    public synchronized void recordTransaction(Transaction txn) {
        if (txn != null) {
            transactionList.add(txn);
        }
    }

    public synchronized List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionList);
    }

    public synchronized List<Wallet> getAllWallets() {
        return new ArrayList<>(walletByPlate.values());
    }

    public synchronized void clear() {
        walletByPlate.clear();
        transactionList.clear();
    }

    public synchronized void displayAllWallets() {
        System.out.println("==========================================================================================");
        System.out.println("                                REGISTERED PREPAID WALLETS                                ");
        System.out.println("==========================================================================================");
        if (walletByPlate.isEmpty()) {
            System.out.println("No wallets currently registered.");
            return;
        }

        int index = 1;
        for (Wallet w : walletByPlate.values()) {
            System.out.printf("%2d. %s%n", index++, w.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total Active Wallets: " + walletByPlate.size());
    }
}
