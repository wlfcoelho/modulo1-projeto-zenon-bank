package br.com.zenon.fraud;

import java.util.List;

public class FraudAnalyzer {

    public static void isFraud(List<Transaction> transactionsList) {
        System.out.println("Início da conta de fraudes==============================");
        System.out.println("Total de fraudes: " + transactionsList.stream().filter(transaction -> transaction.isFraud().equals(true)).count());
        System.out.println("Início da conta de fraudes==============================");
    }
}
