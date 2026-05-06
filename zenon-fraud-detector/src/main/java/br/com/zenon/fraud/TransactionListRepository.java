/*
package br.com.zenon.fraud;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactionsList;

    public TransactionListRepository(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @Override
    public Optional<Transaction> findByNameOrig(String nameOrig) {

        return transactionsList.stream()
                .filter(transaction ->
                        transaction.nameOrig().equals(nameOrig))
                .findFirst();
    }
}
*/
