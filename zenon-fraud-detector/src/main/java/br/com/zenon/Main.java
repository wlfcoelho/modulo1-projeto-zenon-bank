package br.com.zenon;

import br.com.zenon.fraud.FraudAnalyzer;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Inicio==============================");
        List<Transaction> transactionsList = TransactionIngestor.ingest("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");
        System.out.println("Number of transactions ingested: " + transactionsList.size());
        FraudAnalyzer.isFraud(transactionsList);
        //System.out.println("First 10 transactions:");
        for (int i = 0; i < 10 && i < transactionsList.size(); i++) {
            System.out.println(transactionsList.get(i));
        }
        /*for (int i = 0; i < transactions.size(); i++) {
            System.out.println(transactions.get(i));
        }*/
        System.out.println("Fim==============================");

    }
}
