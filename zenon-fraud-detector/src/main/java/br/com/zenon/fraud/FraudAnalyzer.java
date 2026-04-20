package br.com.zenon.fraud;

import java.util.List;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public static void isFraud(List<Transaction> transactionsList) {
        System.out.println("Início da conta de fraudes==============================");
        System.out.println("1. Total de fraudes: " + transactionsList.stream().filter(transaction -> transaction.isFraud().equals(true)).count());
        System.out.println("Início da conta de fraudes==============================");

        System.out.println("Início da analise das maiores fraudes==============================");
        System.out.println("2. Top 3 Fraudes de maior valor: ==============================");
        transactionsList.stream()
                .filter(transaction -> transaction.isFraud().equals(true))
                .sorted((t1, t2) -> t2.amount().compareTo(t1.amount()))
                .limit(3)
                .forEach(transaction -> System.out.println(String.format("%.2f", transaction.amount())));
        System.out.println("Fim da analise das maiores fraudes==============================");

        System.out.println("Início das analises dos clientes suspeitos de fraude==============================");
        System.out.println("3. Clientes Suspeitos:");
        transactionsList.stream()
                .filter(transaction -> transaction.isFraud().equals(true))
                .sorted((t1, t2) -> t2.amount().compareTo(t1.amount()))
                .map(Transaction::nameOrig)
                .distinct()
                .limit(5)
                .forEach(System.out::println);
        System.out.println("Fim da analise dos suspeitos de fraudes==============================");

        System.out.println("Calculo do valor das fraudes==============================");
        transactionsList.stream()
                .filter(transaction -> transaction.isFraud().equals(true))
                .map(Transaction::amount)
                .reduce((a, b) -> a.add(b))
                .ifPresent(totalFraud -> System.out.println("4. Prejuízo Total: " + String.format("%.2f", totalFraud)));
        System.out.println("Fim do calculo do valor das fraudes==============================");

        System.out.println("Contagem por tipos de fraude==============================");
        System.out.println("5. Fraudes por Tipo: ");
        transactionsList.stream()
                .filter(transaction -> transaction.isFraud().equals(true))
                .map(Transaction::type)
                .collect(Collectors.groupingBy(type -> type))
                .forEach((type, transactions) -> System.out.println(type + ": " + transactions.size()));


    }


}
