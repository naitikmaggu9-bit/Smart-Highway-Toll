package tollplaza.member2;

import tollplaza.member1.Vehicle;


public interface PaymentProcessor {

  
    Transaction processPayment(Wallet wallet, Vehicle vehicle, double amount, String laneId)
            throws InsufficientBalanceException;

    boolean validateBalance(Wallet wallet, double requiredAmount);
}
