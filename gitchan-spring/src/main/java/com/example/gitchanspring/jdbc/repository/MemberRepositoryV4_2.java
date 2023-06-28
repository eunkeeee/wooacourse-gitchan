package com.example.gitchanspring.jdbc.repository;

import com.example.gitchanspring.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

// SQLExceptionTranslator 추가
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV4_2(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    private Connection getConnection() throws SQLException {
        // create new connection
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}", connection);
        return connection;
    }

    @Override
    public Member save(final Member member) {
        final String sql = "INSERT INTO member (member_id, money) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.execute();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw exTranslator.translate("save", sql, e);
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    @Override
    public Member findById(final String memberId) {
        final String sql = "SELECT * FROM member WHERE member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final Member member = new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("membern ot found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw exTranslator.translate("findById", sql, e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public void update(final String memberId, final int money) {
        final String sql = "UPDATE member SET money = ? WHERE member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);
            final int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw exTranslator.translate("update", sql, e);
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    @Override
    public void delete(final String memberId) {
        final String sql = "DELETE FROM member WHERE member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);
            final int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw exTranslator.translate("delete", sql, e);
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    private void close(final Connection connection, final Statement statement, final ResultSet resultSet) {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(statement);
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}
