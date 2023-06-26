package com.example.gitchanspring.jdbc.service;

import com.example.gitchanspring.jdbc.domain.Member;
import com.example.gitchanspring.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/*
 * 트랜잭션 - @Transactional 어노테이션 적용
 */
@Slf4j
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(final MemberRepositoryV3 memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(final String fromId, final String toId, final int money) throws SQLException {
        businessLogic(fromId, toId, money);
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
