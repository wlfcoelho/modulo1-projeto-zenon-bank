package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.foreign.SymbolLookup;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransactionIngestor {

    private static final Logger logger = Logger.getLogger(TransactionIngestor.class.getName());

    public static List<Transaction> ingest(String path) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            int contador = 0;
            while ((line = bufferedReader.readLine()) != null) {
                contador++;
                if (contador > 50000) break;
                if (contador == 1) continue;

                try {
                    String[] campo = line.split(",");
                    int step = Integer.parseInt(campo[0].trim());
                    TransactionType transactionType = TransactionType.valueOf(campo[1].trim());
                    BigDecimal amount = new BigDecimal(campo[2].trim());
                    String nameOrig = campo[3].trim();
                    BigDecimal oldbalanceOrg = new BigDecimal(campo[4].trim());
                    BigDecimal newbalanceOrig = new BigDecimal(campo[5].trim());
                    String nameDest = campo[6].trim();
                    BigDecimal oldbalanceDest = new BigDecimal(campo[7].trim());
                    BigDecimal newbalanceDest = new BigDecimal(campo[8].trim());
                    int isFraud = Integer.parseInt(campo[9].trim());
                    int isFlaggedFraud = Integer.parseInt(campo[10].trim());

                    Transaction transaction = new Transaction(
                            step,
                            transactionType,
                            amount,
                            nameOrig,
                            oldbalanceOrg,
                            newbalanceOrig,
                            nameDest,
                            oldbalanceDest,
                            newbalanceDest,
                            verifFraud(isFraud),
                            verifFraud(isFlaggedFraud)
                    );

                    transactions.add(transaction);
                } catch (Exception e) {
                    logger.warning("Error: " + line + ": " + e);
                }
            }
        }
        return transactions;
    }

    private static Boolean verifFraud(int value) {
        if (value == 1) {
            return true;
        }
        return false;
    }
}
