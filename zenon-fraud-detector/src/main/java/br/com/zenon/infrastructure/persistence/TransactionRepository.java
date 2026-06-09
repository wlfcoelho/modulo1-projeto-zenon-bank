package br.com.zenon.infrastructure.persistence;

import br.com.zenon.domain.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByOriginName(String originName);

    void save(Transaction transaction);
}

