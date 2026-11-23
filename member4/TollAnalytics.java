package tollplaza.member4;

import tollplaza.member2.Transaction;

import java.util.*;
import java.util.stream.Collectors;


public class TollAnalytics {

    private final List<Transaction> transactions;

    public TollAnalytics(List<Transaction> transactions) {
        this.transactions = (transactions != null) ? transactions : new ArrayList<>();
    }

  
    public double getTotalTollRevenue() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_TOLL.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalRechargeVolume() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_RECHARGE.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

   
    public long getSuccessfulTollCount() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_TOLL.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .count();
    }

  
    public long getFailedTransactionCount() {
        return transactions.stream()
                .filter(t -> Transaction.STATUS_FAILED.equals(t.getStatus()))
                .count();
    }

   
    public Map<String, Double> getRevenueByCategory() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_TOLL.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .collect(Collectors.groupingBy(
                        Transaction::getVehicleCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

  
    public Map<String, Long> getVehicleCountByCategory() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_TOLL.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .collect(Collectors.groupingBy(
                        Transaction::getVehicleCategory,
                        Collectors.counting()
                ));
    }

   
    public double getAverageTollFee() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_TOLL.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0.0);
    }

  
    public double getMaxTollFee() {
        return transactions.stream()
                .filter(t -> Transaction.TYPE_TOLL.equals(t.getType()))
                .filter(t -> Transaction.STATUS_SUCCESS.equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);
    }

   
    public void printAnalyticsDashboard() {
        System.out.println("==========================================================================================");
        System.out.println("                     HIGHWAY TOLL ANALYTICS DASHBOARD (STREAM API)                        ");
        System.out.println("==========================================================================================");
        System.out.printf("  Total Toll Revenue Collected : Rs. %10.2f%n", getTotalTollRevenue());
        System.out.printf("  Total Wallet Recharges       : Rs. %10.2f%n", getTotalRechargeVolume());
        System.out.printf("  Successful Toll Crossings    : %10d%n", getSuccessfulTollCount());
        System.out.printf("  Failed Transaction Attempts  : %10d%n", getFailedTransactionCount());
        System.out.printf("  Average Toll per Vehicle     : Rs. %10.2f%n", getAverageTollFee());
        System.out.printf("  Highest Single Toll Charged  : Rs. %10.2f%n", getMaxTollFee());

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("  REVENUE BREAKDOWN BY VEHICLE CATEGORY (Collectors.groupingBy):");
        Map<String, Double> revMap = getRevenueByCategory();
        Map<String, Long> countMap = getVehicleCountByCategory();

        if (revMap.isEmpty()) {
            System.out.println("  No toll payment data recorded yet.");
        } else {
            revMap.forEach((category, revenue) -> {
                long count = countMap.getOrDefault(category, 0L);
                System.out.printf("   - %-8s : %3d vehicles | Revenue: Rs. %8.2f%n", category, count, revenue);
            });
        }
        System.out.println("==========================================================================================");
    }
}
