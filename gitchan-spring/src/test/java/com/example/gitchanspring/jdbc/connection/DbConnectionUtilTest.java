package com.example.gitchanspring.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class DbConnectionUtilTest {
    @Test
    void connection() {
        final Connection connection = DbConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}
