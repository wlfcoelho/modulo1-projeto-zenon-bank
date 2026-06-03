package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud) {}

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {

    private final static Statistics ZERO = new Statistics(0, 0, BigDecimal.ZERO);

    private Statistics addReportTransaction(ReportTransaction rt) {
        return new Statistics(
                totalTransactions() + 1,
                totalFrauds() + (rt.isFraud ? 1 : 0),
                totalAmount().add(rt.amount));
    }

    private Statistics add (Statistics other) {
        return new Statistics(
                totalTransactions() + other.totalTransactions(),
                totalFrauds() + other.totalFrauds(),
                totalAmount().add(other.totalAmount()));

    }
}
    public TransactionReport.Statistics genereteReport(String filePath) {
        Path path = Path.of(filePath);
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .map(this::parseReportTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            Statistics.ZERO,
                            Statistics::addReportTransaction,
                            Statistics::add);



        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    private Optional<TransactionReport.ReportTransaction> parseReportTransaction(String s) {

        try {
            String[] chunks = s.split(",");

            if (chunks[2] == null || chunks[2].trim().isEmpty())
                throw new IllegalArgumentException("amount should not be empty");

            BigDecimal amount = new BigDecimal(chunks[2]);

            boolean isFraud = "1".equals(chunks[9]);


            return Optional.of(new TransactionReport.ReportTransaction(amount, isFraud));
        } catch (Exception e) {
            System.err.println("Error parsing transaction: " + s + " - " + e.getMessage());
            return Optional.empty();

        }
    }
}

