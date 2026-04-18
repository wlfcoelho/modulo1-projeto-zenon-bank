package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    public static List<Transaction> ingest( String path) throws FileNotFoundException {


        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        String linha;
        int contador = 0;
        List<Transaction> transactions = new ArrayList<>();
        while (true) {
            
            try {
                if ((linha = bufferedReader.readLine()) == null) break;

                if (contador >= 1 && contador <= 1000){
                    String campo[] = linha.split(",");
                    int step  = Integer.parseInt(campo[0]);
                    String transactionType = campo[1];
                    BigDecimal amount = new BigDecimal(campo[2]);
                    String nameOrig = campo[3];
                    BigDecimal oldbalanceOrg = new BigDecimal(campo[4]);
                    BigDecimal newbalanceOrig = new BigDecimal(campo[5]);
                    String nameDest = campo[6];
                    BigDecimal oldbalanceDest = new BigDecimal(campo[7]);
                    BigDecimal newbalanceDest = new BigDecimal(campo[8]);
                    int isFraud = Integer.parseInt(campo[9]);
                    int isFlaggedFraud = Integer.parseInt(campo[10]);

                    Transaction transaction = new Transaction(
                            step,
                            TransactionType.valueOf(transactionType),
                            amount,
                            nameOrig,
                            oldbalanceOrg,
                            newbalanceOrig,
                            nameDest,
                            oldbalanceDest,
                            newbalanceDest,
                            isFraud,
                            isFlaggedFraud
                    );

                    transactions.add(transaction);
                }

                contador++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return transactions;
    }
}
