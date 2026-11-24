package tollplaza.member4;

import tollplaza.member2.Transaction;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class TransactionHistoryViewer {

 
    public static void displayForward(List<Transaction> transactions) {
        System.out.println("==========================================================================================");
        System.out.println("            TRANSACTION AUDIT (FORWARD TRAVERSAL - ListIterator.next())                  ");
        System.out.println("==========================================================================================");
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }

        ListIterator<Transaction> listIterator = transactions.listIterator();
        int index = 1;
        while (listIterator.hasNext()) {
            Transaction t = listIterator.next();
            System.out.printf("%2d. %s%n", index++, t.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total Records: " + transactions.size());
    }

    
    public static void displayBackward(List<Transaction> transactions) {
        System.out.println("==========================================================================================");
        System.out.println("          TRANSACTION AUDIT (REVERSE TRAVERSAL - ListIterator.previous())                ");
        System.out.println("==========================================================================================");
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }

        
        ListIterator<Transaction> listIterator = transactions.listIterator(transactions.size());
        int index = 1;
        while (listIterator.hasPrevious()) {
            Transaction t = listIterator.previous();
            System.out.printf("%2d. %s%n", index++, t.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total Records: " + transactions.size());
    }

  
    public static void displayByVehicle(List<Transaction> transactions, String plateNumber) {
        if (plateNumber == null) return;
        String targetPlate = plateNumber.trim().toUpperCase();

        System.out.println("==========================================================================================");
        System.out.printf("           TRANSACTION HISTORY FOR VEHICLE [%s] (Iterator)             %n", targetPlate);
        System.out.println("==========================================================================================");

        Iterator<Transaction> it = transactions.iterator();
        int count = 0;
        while (it.hasNext()) {
            Transaction t = it.next();
            if (targetPlate.equalsIgnoreCase(t.getVehiclePlate())) {
                System.out.printf("%2d. %s%n", ++count, t.toString());
            }
        }

        if (count == 0) {
            System.out.println("No transactions found for plate: " + targetPlate);
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Matched Records: " + count);
    }
}
