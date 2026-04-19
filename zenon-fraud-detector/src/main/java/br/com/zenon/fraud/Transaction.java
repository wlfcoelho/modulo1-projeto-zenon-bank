package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;


public record Transaction(
        int step,
        TransactionType type,
        BigDecimal amount,
        String nameOrig,
        BigDecimal oldbalanceOrg,
        BigDecimal newbalanceOrig,
        String nameDest,
        BigDecimal oldbalanceDest,
        BigDecimal newbalanceDest,
        Boolean isFraud,
        Boolean isFlaggedFraud) {

    public Transaction {

        Objects.requireNonNull(type, "type should not be null");
        Objects.requireNonNull(amount, "amount should not be null");
        Objects.requireNonNull(nameOrig, "nameOrig should not be null");
        Objects.requireNonNull(oldbalanceOrg, "oldbalanceOrg should not be null");
        Objects.requireNonNull(newbalanceOrig, "newbalanceOrig should not be null");
        Objects.requireNonNull(nameDest, "nameDest should not be null");
        Objects.requireNonNull(oldbalanceDest, "oldbalanceDest should not be null");
        Objects.requireNonNull(newbalanceDest, "newbalanceDest should not be null");
        Objects.requireNonNull(isFraud, "isFraud should not be null");
        Objects.requireNonNull(isFlaggedFraud, "isFlaggedFraud should not be null");

        if (step <= 0) {
            throw new IllegalArgumentException("step should be positive: " + step);
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }
        if (oldbalanceOrg.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + oldbalanceOrg);
        }
        if (newbalanceOrig.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + newbalanceOrig);
        }
        if (oldbalanceDest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + oldbalanceDest);
        }
        if (newbalanceDest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + newbalanceDest);
        }

        if (nameOrig.isBlank()) {
            throw new IllegalArgumentException("name should not be empty: " + nameOrig);
        }
        if (nameDest.isBlank()) {
            throw new IllegalArgumentException("name should not be empty: " + nameDest);
        }
    }
}

