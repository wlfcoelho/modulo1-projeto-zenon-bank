package br.com.zenon;

import br.com.zenon.fraud.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Inicio==============================");

        TransactionIngestor transactionIngestor = new TransactionIngestor();

        List<Transaction> transactionsList = transactionIngestor.read("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");

        transactionsList.stream().limit(10).forEach(System.out::println);


        List<Transaction> transactionsBadList = transactionIngestor.read("zenon-fraud-detector/data/paysim_with_bad_data.csv");
        System.out.println("Number of transactions ingested from paysim_with_bad_data.cvs: " + transactionsBadList.size());
        transactionsBadList.stream().limit(10).forEach(System.out::println);

        System.out.println("Inicio==============================");
        var fraudAnalizer = new FraudAnalyzer(transactionsList);
        long fraudeCounts = fraudAnalizer.countFrauds();
        System.out.println("Total de fraudes: " + fraudeCounts);

        System.out.println("Inicio==============================");
        List<Transaction> listHighestValueFrauds = fraudAnalizer.findHighestValueFrauds(3);
        listHighestValueFrauds.stream().map(Transaction::amount).forEach(amount -> System.out.printf("- %.2f%n", amount));

        System.out.println("Inicio==============================");
        List<String> suspiciousCLients = fraudAnalizer.findTopSuspiciousClients(5);
        System.out.println("Top 5 clientes suspeitos:");
        suspiciousCLients.forEach(System.out::println);

        System.out.println("Inicio==============================");
        BigDecimal totalLoss = fraudAnalizer.calculateTotalFraudsLoss();
        System.out.println("Total de perdas: " + totalLoss);

        System.out.println("Inicio==============================");
        Map<TransactionType, Long> fraudCountByType =  fraudAnalizer.countFraudsType();
        System.out.println("Contagem de fraudes por tipo:");
        fraudCountByType.forEach((type, count) -> System.out.println(type + ": " + count));

        System.out.println("Inicio==============================");
        TransactionRepository transactionRepository;

        transactionRepository = new TransactionListRepository(transactionsList);
        String nameNotExist = "NonExistingClient";
        transactionRepository.findByOriginName(nameNotExist).ifPresentOrElse(System.out::println, () -> System.out.println("Transação não encontrada para o cliente " + nameNotExist));

        long start = System.nanoTime();
        String nameExist = "C1868032458";
        transactionRepository.findByOriginName(nameExist).ifPresentOrElse(System.out::println, () -> System.out.println("Transação não encontrada para o cliente " + nameExist));
        long endTime = System.nanoTime();
        long elapsedNanos = endTime - start;
        System.out.println("Tempo de busca - List (ms): " + (elapsedNanos / 1000000.0) + " ms");


        transactionRepository = new TransactionMapRepository(transactionsList);
        start = System.nanoTime();
        transactionRepository.findByOriginName(nameExist).ifPresentOrElse(System.out::println, () -> System.out.println("Transação não encontrada para o cliente " + nameExist));
        endTime = System.nanoTime();
        elapsedNanos = endTime - start;
        System.out.println("Tempo de busca - ListMap (ms): " + (elapsedNanos / 1000000.0) + " ms");
    }

    private static void printResult(
            String estrutura,
            String originName,
            Optional<Transaction> result,
            long elapsedNanos
    ) {
        if (result.isPresent()) {
            System.out.println("[" + estrutura + "] Encontrada para " + originName + ":");
            System.out.println(result.get());
        } else {
            System.out.println("[" + estrutura + "] Transação não encontrada para o cliente " + originName);
        }

        System.out.println("[" + estrutura + "] Tempo de busca: " + elapsedNanos + " ns");
    }

}

