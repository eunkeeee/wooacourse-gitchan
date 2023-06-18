package com.example.gitchanspring.jdbc.service;

import com.example.gitchanspring.jdbc.domain.Member;
import com.example.gitchanspring.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(final String fromId, final String toId, final int money) throws SQLException {
        final Connection connection = dataSource.getConnection();

        try {
            // 트랜잭션 시작
            connection.setAutoCommit(false);

            // 비즈니스 로직
            businessLogic(connection, fromId, toId, money);

            // 정상 수행 시 commit
            connection.commit();
        } catch (Exception exception) {
            // 예외 발생 시 rollback
            connection.rollback();
            throw new IllegalStateException(exception);
        } finally {
            // connection 정리하기
            release(connection);
        }
    }

    private void businessLogic(final Connection connection, final String fromId, final String toId, final int money) throws SQLException {
        final Member fromMember = memberRepository.findById(connection, fromId);
        final Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromId, fromMember.getMoney() - money);
        validation(toMember); // 예외 상황 테스트용
        memberRepository.update(connection, toId, toMember.getMoney() + money);
    }

    private void release(final Connection connection) {
        if (connection != null) {
            try {
                // 커넥션 풀 고려해서 기본 설정으로 되돌린다.
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception exception) {
                log.info("error", exception);
            }
        }
    }

    private static void validation(final Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 과정에서 문제 발생!");
        }
    }
}
