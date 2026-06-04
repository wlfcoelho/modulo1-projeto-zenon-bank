package br.com.zenon;

import br.com.zenon.fraud.TransactionReport;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    public static void main(String[] args) {

        var locale = Locale.of("en", "US");

        var integerFormater = NumberFormat.getIntegerInstance(locale);
        var currencyFormater = DecimalFormat.getCurrencyInstance(locale);
        currencyFormater.setCurrency(Currency.getInstance("USD"));

        var resourceBundle = ResourceBundle.getBundle("report", locale);


        var report = new TransactionReport();
        TransactionReport.Statistics statistics = report.genereteReport("zenon-fraud-detector/data/PS_20174392719_1491204439457_log.csv");

        String formaterTotalTransactions = integerFormater.format(statistics.totalTransactions());
        String formaterTotalFrauds = integerFormater.format(statistics.totalFrauds());
        String formaterTotalAmount = currencyFormater.format(statistics.totalAmount());

        String msgTotalTransaction = resourceBundle.getString("label.total.trasactions");
        String msgTotalFrauds = resourceBundle.getString("label.total.frauds");
        String msgTotalAmount = resourceBundle.getString("label.total.amount");

        System.out.println("===================== Starting ===========================");
        System.out.println("""
                %s: %S
                %s: %S
                %s: %S
                """.formatted(
                msgTotalTransaction, formaterTotalTransactions,
                msgTotalFrauds, formaterTotalFrauds,
                msgTotalAmount, formaterTotalAmount));

    }
}
