
package br.com.zenon.fraud;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactionsList;

    public TransactionListRepository(List<Transaction> transactionsList) {
        Objects.requireNonNull(transactionsList);
        this.transactionsList = transactionsList;
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {

        return transactionsList.stream()
                .filter(transaction ->
                        transaction.customerOrigin().getName().equals(originName))
                .findFirst();
    }
}

