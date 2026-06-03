package br.com.zenon;

import br.com.zenon.fraud.TransactionReport;

public class ReportMain {

    void main(String[] args) {
        var report = new TransactionReport();
        TransactionReport.Statistics statistics = report.genereteReport("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");
        System.out.println("===================== Starting ===========================");
        System.out.println("Total number lines in the file: " + statistics.totalTransactions());
        System.out.println("Total number of frauds: " + statistics.totalFrauds());
        System.out.println("Total amount of transactions: " + statistics.totalAmount());
    }
}
