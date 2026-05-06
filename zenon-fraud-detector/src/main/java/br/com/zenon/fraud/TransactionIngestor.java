package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.foreign.SymbolLookup;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransactionIngestor {

    private static final Logger logger = Logger.getLogger(TransactionIngestor.class.getName());
    
    public List<Transaction> read(String filePath) {
        Path path = Path.of(filePath);
        try {

            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::parseTransaction)
                    .toList();
            
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
}

    private Transaction parseTransaction(String s) {

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

        return new Transaction(step, type, amount, customerOrigin, customerDestination, isFraud, isFlaggedFraud);

    }
    }
