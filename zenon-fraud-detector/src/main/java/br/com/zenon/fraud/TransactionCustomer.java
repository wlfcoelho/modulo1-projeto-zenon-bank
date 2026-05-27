package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionCustomer {

    private String name;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;

    public TransactionCustomer(String name, BigDecimal oldBalance, BigDecimal newBalance) {
        Objects.requireNonNull(name, "nameOrig should not be null");
        Objects.requireNonNull(oldBalance, "oldBalance should not be null");
        Objects.requireNonNull(newBalance, "newBalance should not be null");

        if (oldBalance.signum() < 0) {
            throw new IllegalArgumentException("amount should be positive: " + oldBalance);
        }
        if (newBalance.signum() < 0) {
            throw new IllegalArgumentException("amount should be positive: " + newBalance);
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name should not be empty");
        }

        this.name = name;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
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
