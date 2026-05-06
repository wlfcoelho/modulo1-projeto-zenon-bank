package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionCustomer {

    private String name;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;

    public TransactionCustomer() {
        Objects.requireNonNull(name, "nameOrig should not be null");
        Objects.requireNonNull(oldBalance, "oldBalance should not be null");
        Objects.requireNonNull(newBalance, "newBalance should not be null");

        if (oldBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + oldBalance);
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + newBalance);
        }
    }

    public TransactionCustomer(String field, BigDecimal bigDecimal, BigDecimal bigDecimal1) {
    }

    public String getName() {
        return name;
    }
    public BigDecimal getOldBalance() {
        return oldBalance;
    }
    public BigDecimal getNewBalance() {
        return newBalance;
    }


}
