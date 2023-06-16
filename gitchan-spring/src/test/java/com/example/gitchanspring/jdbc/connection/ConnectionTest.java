package com.example.gitchanspring.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.gitchanspring.jdbc.connection.ConnectionConst.*;

@Slf4j
class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        final Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        final Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection={}, class={}", connection1, connection1.getClass());
        log.info("connection={}, class={}", connection2, connection2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        final DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }

    private void useDataSource(final DataSource dataSource) throws SQLException {
        final Connection connection1 = dataSource.getConnection();
        final Connection connection2 = dataSource.getConnection();

        log.info("connection={}, class={}", connection1, connection1.getClass());
        log.info("connection={}, class={}", connection2, connection2.getClass());
    }
}
