package br.com.zenon.infrastructure.persistence.repository;

import br.com.zenon.domain.model.Transaction;
import br.com.zenon.domain.model.TransactionCustomer;
import br.com.zenon.domain.model.TransactionType;
import br.com.zenon.infrastructure.persistence.database.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TransactionSQLRepository implements TransactionRepository {

    Logger logger = Logger.getLogger(TransactionSQLRepository.class.getName());
    public static Integer SQL_BATCH_SIZE = 1000;

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        String sql = """
                SELECT id, step, `type`, amount, 
                       name_orig, old_balance_origin, new_balance_origin, 
                       name_recipient, old_balance_recipient, new_balance_recipient,
                       is_fraud, is_flagged_fraud
                FROM zenon_frauds.`transaction`
                WHERE name_orig = ?
                LIMIT 1
                """;

        try (Connection conn = ConnectionFactory.createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, originName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    return Optional.of(transaction);
                } else {
                    System.out.println("Não encontrado: " + originName);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Transaction transaction) {
        String sql = """
                insert into transaction
                (step,`type`, amount,
                 name_orig, old_balance_origin, new_balance_origin, 
                 name_recipient, old_balance_recipient, new_balance_recipient,
                 is_fraud, is_flagged_fraud)
                VALUES
                (?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (Connection conn = ConnectionFactory.createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());
            ps.setString(4, transaction.customerOrigin().getName());
            ps.setBigDecimal(5, transaction.customerOrigin().getOldBalance());
            ps.setBigDecimal(6, transaction.customerOrigin().getNewBalance());
            ps.setString(7, transaction.customerDestination().getName());
            ps.setBigDecimal(8, transaction.customerDestination().getOldBalance());
            ps.setBigDecimal(9, transaction.customerDestination().getNewBalance());
            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggedFraud());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar nova transação", e);
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) {
        try {
            int step = rs.getInt("step");
            TransactionType type = TransactionType.valueOf(rs.getString("type"));
            BigDecimal amount = rs.getBigDecimal("amount");

            String originName = rs.getString("name_orig");
            BigDecimal oldBalanceOrigin = rs.getBigDecimal("old_balance_origin");
            BigDecimal newBalanceOrigin = rs.getBigDecimal("new_balance_origin");
            TransactionCustomer customerOrigin = new TransactionCustomer(originName, oldBalanceOrigin, newBalanceOrigin);

            String recipientName = rs.getString("name_recipient");
            BigDecimal oldBalanceRecipient = rs.getBigDecimal("old_balance_recipient");
            BigDecimal newBalanceRecipient = rs.getBigDecimal("new_balance_recipient");
            TransactionCustomer recipient = new TransactionCustomer(recipientName, oldBalanceRecipient, newBalanceRecipient);

            Boolean isFraud = rs.getBoolean("is_fraud");
            Boolean isFlaggedFraud = rs.getBoolean("is_flagged_fraud");

            return new Transaction(step, type, amount, customerOrigin, recipient, isFraud, isFlaggedFraud);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Transaction", e);
        }
    }

    public void saveAll(List<Transaction> transactions) {
        String sql = """
                insert into transaction
                (step,`type`, amount,
                 name_orig, old_balance_origin, new_balance_origin, 
                 name_recipient, old_balance_recipient, new_balance_recipient,
                 is_fraud, is_flagged_fraud)
                VALUES
                (?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (Connection conn = ConnectionFactory.createConnection()) {
            conn.setAutoCommit(false);

            int count = 0;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Transaction transaction : transactions) {
                    ps.setInt(1, transaction.step());
                    ps.setString(2, transaction.type().name());
                    ps.setBigDecimal(3, transaction.amount());
                    ps.setString(4, transaction.customerOrigin().getName());
                    ps.setBigDecimal(5, transaction.customerOrigin().getOldBalance());
                    ps.setBigDecimal(6, transaction.customerOrigin().getNewBalance());
                    ps.setString(7, transaction.customerDestination().getName());
                    ps.setBigDecimal(8, transaction.customerDestination().getOldBalance());
                    ps.setBigDecimal(9, transaction.customerDestination().getNewBalance());
                    ps.setBoolean(10, transaction.isFraud());
                    ps.setBoolean(11, transaction.isFlaggedFraud());

                    ps.addBatch();
                    count++;

                    if (count % SQL_BATCH_SIZE == 0) {
                        logger.info("Executando batch JDBC...");
                        ps.executeBatch();
                        conn.commit();
                    }
                }

                logger.info("Executando batch final JDBC...");
                ps.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.severe("Erro ao realizar rollback: " + ex.getMessage());
                }
                throw new RuntimeException("Erro ao salvar nova transação", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o BD...", e);
        }
    }
}

