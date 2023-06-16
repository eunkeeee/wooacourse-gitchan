package com.example.gitchanspring.jdbc.service;

import com.example.gitchanspring.jdbc.domain.Member;
import com.example.gitchanspring.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV2 {

    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(final String fromId, final String toId, final int money) throws SQLException {
        final Member fromMember = memberRepository.findById(fromId);
        final Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember); // 예외 상황 테스트용
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(final Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 과정에서 문제 발생!");
        }
    }
}
