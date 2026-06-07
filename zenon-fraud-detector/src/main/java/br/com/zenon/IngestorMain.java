package br.com.zenon;

import br.com.zenon.application.ingestor.EfficientTransactionIngestor;
import br.com.zenon.infrastructure.persistence.database.ConnectionFactory;
import br.com.zenon.infrastructure.persistence.repository.TransactionSQLRepository;

import java.util.List;

public class IngestorMain {

    void main(String[] args) {
        try (var connection = ConnectionFactory.createConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao estabelecer conexão: " + e.getMessage());
        }

        var repository = new TransactionSQLRepository();

        var trasactionIngestor = new EfficientTransactionIngestor();
        long start = System.nanoTime();

        trasactionIngestor.readAsBatch("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv",
                repository::saveAll);

        long endTime = System.nanoTime();
        long elapsedNanos = endTime - start;
        System.out.println("Tempo de insgestão no BD (ms): " + (elapsedNanos / 1000000.0) + " ms");
    }
}
