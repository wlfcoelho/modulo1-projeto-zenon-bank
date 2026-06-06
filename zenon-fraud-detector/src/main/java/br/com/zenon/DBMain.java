package br.com.zenon;

import br.com.zenon.fraud.*;

import java.math.BigDecimal;
import java.util.List;

public class DBMain {

    void main(String[] args) {
        try (var connection = ConnectionFactory.createConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao estabelecer conexão: " + e.getMessage());
        }

        var repository = new TransactionSQLRepository();

        var trasactionIngestor = new TransactionIngestor();
        long start = System.nanoTime();
        List<Transaction> transactions = trasactionIngestor.read("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");
        System.out.println(transactions.size());


        transactions.forEach(repository::save);
        long endTime = System.nanoTime();
        long elapsedNanos = endTime - start;
        System.out.println("Tempo de busca - ListMap (ms): " + (elapsedNanos / 1000000.0) + " ms");

        repository.findByOriginName("C1231006815").ifPresentOrElse(
                transaction -> System.out.println("Transação encontrada: " + transaction),
                () -> System.out.println("Nenhuma transação encontrada para o nome de origem fornecido.")
        );

    }
}
