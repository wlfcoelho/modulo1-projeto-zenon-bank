package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    private final List<Transaction> transactionsList;

    public  FraudAnalyzer(List<Transaction> transactionsList) {
        Objects.requireNonNull(transactionsList);
        this.transactionsList = transactionsList;
    }


    public long countFrauds() {
        return getStreamIsFraud()
                .count();
    }


    public List<Transaction> findHighestValueFrauds(int limit) {
        return getStreamIsFraud()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(limit)
                .toList();
    }

    public List<String> findTopSuspiciousClients(int limit) {
        return getStreamIsFraud()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.customerOrigin().getName())
                .distinct()
                .limit(limit)
                .toList();


    }

    public BigDecimal calculateTotalFraudsLoss() {
        return getStreamIsFraud()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<TransactionType, Long> countFraudsType() {
        return getStreamIsFraud()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

    }

    private Stream<Transaction> getStreamIsFraud() {
        return transactionsList.stream()
                .filter(Transaction::isFraud);
    }
}
