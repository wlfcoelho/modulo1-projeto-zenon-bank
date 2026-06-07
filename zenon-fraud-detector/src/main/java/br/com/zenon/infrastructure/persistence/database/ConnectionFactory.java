package br.com.zenon.infrastructure.persistence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private ConnectionFactory() {}

    public static Connection createConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/zenon_frauds", "root", "123");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao BD", e);
        }
    }
}

