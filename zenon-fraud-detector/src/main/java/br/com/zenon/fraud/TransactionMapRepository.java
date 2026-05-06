/*
package br.com.zenon.fraud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionMap;

    public TransactionMapRepository(List<Transaction> transactions) {
        this.transactionMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            transactionMap.put(transaction.nameOrig(), transaction);
        }
    }

    @Override
    public Optional<Transaction> findByNameOrig(String originName) {
        return Optional.ofNullable(transactionMap.get(originName));
    }
}
*/
