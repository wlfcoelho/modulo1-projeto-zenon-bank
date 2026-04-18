package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionType;

import java.math.BigDecimal;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Transaction transaction1 = new Transaction(
                1,
                TransactionType.PAYMENT,
                new BigDecimal("9839.64"),
                "C1231006815",
                new BigDecimal("170136.0"),
                new BigDecimal("160296.36"),
                "M1979787155",
                new BigDecimal("0.0"),
                new BigDecimal("0.0"),
                0,
                0



        );

        Transaction transaction2 = new Transaction(
                743,
                TransactionType.CASH_OUT,
                new BigDecimal("850002.52"),
                "C1280323807",
                new BigDecimal("850002.52"),
                new BigDecimal("0.0"),
                "C873221189",
                new BigDecimal("6510099.11"),
                new BigDecimal("7360101.63"),
                1,
                0
        );


        System.out.println(String.format("transaction1 = " + transaction1));

        System.out.println(String.format("transaction2 = " + transaction2));

    }
}
