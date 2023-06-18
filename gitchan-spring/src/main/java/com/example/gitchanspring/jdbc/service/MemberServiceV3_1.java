package com.example.gitchanspring.jdbc.service;

import com.example.gitchanspring.jdbc.domain.Member;
import com.example.gitchanspring.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/*
 * 트랜잭션 - 트랜잭션 매니저
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {

    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(final String fromId, final String toId, final int money) throws SQLException {
        final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            businessLogic(fromId, toId, money);
            transactionManager.commit(status);
        } catch (Exception exception) {
            transactionManager.rollback(status);
            throw new IllegalStateException(exception);
        }
    }

    private void businessLogic(final String fromId, final String toId, final int money) throws SQLException {
        final Member fromMember = memberRepository.findById(fromId);
        final Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember); // 예외 상황 테스트용
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(final Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 과정에서 문제 발생!");
        }
    }
}
