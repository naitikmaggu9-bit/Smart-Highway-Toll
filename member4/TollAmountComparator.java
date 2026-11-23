package tollplaza.member4;

import tollplaza.member2.Transaction;
import java.util.Comparator;

public class TollAmountComparator implements Comparator<Transaction> {

    private final boolean descending;

    public TollAmountComparator() {
        this(true);
    }

    public TollAmountComparator(boolean descending) {
        this.descending = descending;
    }

    @Override
    public int compare(Transaction t1, Transaction t2) {
        if (t1 == null && t2 == null) return 0;
        if (t1 == null) return -1;
        if (t2 == null) return 1;

        int comparison = Double.compare(t1.getAmount(), t2.getAmount());
        return descending ? -comparison : comparison;
    }
}
