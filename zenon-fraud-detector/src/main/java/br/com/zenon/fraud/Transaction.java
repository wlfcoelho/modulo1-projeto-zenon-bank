package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(
        int step,
        TransactionType type,
        BigDecimal amount,
        TransactionCustomer customerOrigin,
        TransactionCustomer customerDestination,
        Boolean isFraud,
        Boolean isFlaggedFraud) {

    public Transaction {

        Objects.requireNonNull(type, "type should not be null");
        Objects.requireNonNull(amount, "amount should not be null");
        Objects.requireNonNull(isFlaggedFraud, "isFlaggedFraud should not be null");

        if (step <= 0) {
            throw new IllegalArgumentException("step should be positive: " + step);
        }

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }
    }
}

