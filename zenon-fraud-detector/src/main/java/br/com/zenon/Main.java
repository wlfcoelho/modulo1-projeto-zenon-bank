package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("Inicio==============================");
        List<Transaction> transactions = TransactionIngestor.ingest("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");
        System.out.println("Number of transactions ingested: " + transactions.size());
        System.out.println("First 10 transactions:");
        for (int i = 0; i < 10 && i < transactions.size(); i++) {
            System.out.println(transactions.get(i));
        }
        System.out.println("fim==============================");

    }
}
