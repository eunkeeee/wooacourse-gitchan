package com.example.gitchanspring.jdbc.service;

import com.example.gitchanspring.jdbc.domain.Member;
import com.example.gitchanspring.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    public MemberServiceV4(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(final String fromId, final String toId, final int money) {
        businessLogic(fromId, toId, money);
    }

    private void businessLogic(final String fromId, final String toId, final int money) {
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
