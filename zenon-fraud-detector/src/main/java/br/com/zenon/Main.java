package br.com.zenon;

import br.com.zenon.fraud.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Inicio==============================");
        List<Transaction> transactionsList = TransactionIngestor.ingest("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");
        System.out.println("Number of transactions ingested: " + transactionsList.size());
        //FraudAnalyzer.isFraud(transactionsList);
        //System.out.println("First 10 transactions:");

        TransactionRepository repository = new TransactionListRepository(transactionsList);

        testSearch(repository, "C1231006815");
        testSearch(repository, "C12345");

        String worstCaseOrigin = "C1868032458";

        long startList = System.nanoTime();
        Optional<Transaction> listResult = repository.findByNameOrig(worstCaseOrigin);
        long endList = System.nanoTime();

        printResult("Lista", worstCaseOrigin, listResult, endList - startList);

        TransactionRepository mapRepository = new TransactionMapRepository(transactionsList);

        long startMap = System.nanoTime();
        Optional<Transaction> mapResult = mapRepository.findByNameOrig(worstCaseOrigin);
        long endMap = System.nanoTime();

        printResult("Map", worstCaseOrigin, mapResult, endMap - startMap);

        /*for (int i = 0; i < 100000 && i < transactionsList.size(); i++) {
            System.out.println(transactionsList.get(i));
        }*/
        /*for (int i = 0; i < transactions.size(); i++) {
            System.out.println(transactions.get(i));
        }*/
        System.out.println("Fim==============================");
    }

    private static void testSearch(TransactionRepository repository, String originName) {
        Optional<Transaction> result = repository.findByNameOrig(originName);

        if (result.isPresent()) {
            System.out.println(result.get());
        } else {
            System.out.println("Transação não encontrada para o cliente " + originName);
        }
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

