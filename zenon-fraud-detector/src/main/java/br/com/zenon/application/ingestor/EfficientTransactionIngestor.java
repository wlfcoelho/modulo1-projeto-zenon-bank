package br.com.zenon.application.ingestor;

import br.com.zenon.domain.model.Transaction;
import br.com.zenon.domain.model.TransactionCustomer;
import br.com.zenon.domain.model.TransactionType;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    private static final Logger logger = Logger.getLogger(EfficientTransactionIngestor.class.getName());
    private static final int MAX_SIZE = 10_000;
    private static final int BATCH_SIZE = 5_000;

    public void readAsBatch(String filePath, Consumer<List<Transaction>> consumerList) {
        Path path = Path.of(filePath);
        try (ExecutorService executor = Executors.newFixedThreadPool(10);
             Stream<String> lines = Files.lines(path).skip(1)) {

            var interator = lines.iterator();

            List<String> lineBatch = new ArrayList<>(BATCH_SIZE);
            while (interator.hasNext()) {

                String line = interator.next();
                lineBatch.add(line);

                if (lineBatch.size() >= BATCH_SIZE) {
                    logger.info("Executando batch ingestor...");
                    final List<String> lineBatchCopy = List.copyOf(lineBatch);
                    executor.submit(() -> executeBatch(lineBatchCopy, consumerList));
                    lineBatch.clear();
                }
            }
            if (!lineBatch.isEmpty()) {
                logger.info("Executando batch final ingestor...");
                final List<String> lineBatchCopy = List.copyOf(lineBatch);
                executor.submit(() -> executeBatch(lineBatchCopy, consumerList));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    private void executeBatch(List<String> lineBatch, Consumer<List<Transaction>> consumerList) {

        List<Transaction> transactionList =
                lineBatch
                        .stream()
                        .map(this::parseTransaction)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

        consumerList.accept(transactionList);
    }

    public void readAsStream(String filePath, Consumer<Transaction> consumer) {
        Path path = Path.of(filePath);
        try (Stream<String> lines = Files.lines(path)) {

            lines
                    .skip(1)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(consumer);

        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    private Optional<Transaction> parseTransaction(String s) {
        try {
            String[] fields = s.split(",");

            int step = Integer.parseInt(fields[0]);
            TransactionType type = TransactionType.valueOf(fields[1]);
            BigDecimal amount = new BigDecimal(fields[2]);
            TransactionCustomer customerOrigin = new TransactionCustomer(
                    fields[3], new BigDecimal(fields[4]), new BigDecimal(fields[5]));
            TransactionCustomer customerDestination = new TransactionCustomer(
                    fields[6], new BigDecimal(fields[7]), new BigDecimal(fields[8]));
            boolean isFraud = "1".equals(fields[9]);
            boolean isFlaggedFraud = "1".equals(fields[10]);

            return Optional.of(new Transaction(step, type, amount, customerOrigin, customerDestination, isFraud, isFlaggedFraud));
        } catch (Exception e) {
            System.err.println("Error parsing transaction: " + s + " - " + e.getMessage());
            return Optional.empty();
        }
    }
}

