package br.com.zenon.fraud;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionsMap;

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactionsMap =
                transactions.stream().
                        collect(Collectors.toMap(
                                transaction -> transaction.customerOrigin().getName(),
                                //Mesma função no caso abaixo os dois devolvem a transação acima.
                                Function.identity()));
                                // transaction -> transaction));
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        return Optional.ofNullable(transactionsMap.get(originName));
    }
}


