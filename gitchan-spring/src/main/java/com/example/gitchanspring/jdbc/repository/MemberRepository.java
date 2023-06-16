package com.example.gitchanspring.jdbc.repository;

import com.example.gitchanspring.jdbc.connection.DbConnectionUtil;
import com.example.gitchanspring.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MemberRepository {

    public Member save(final Member member) throws SQLException {
        final String sql = "INSERT INTO member (member_id, money) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DbConnectionUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.execute();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    private void close(final Connection connection, final Statement statement, final ResultSet resultSet) {
        if (resultSet!=null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        try {
            statement.close();
        } catch (SQLException e) {
            log.info("error", e);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            log.info("error", e);
        }
    }
}
