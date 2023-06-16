package com.example.gitchanspring.jdbc.repository;

import com.example.gitchanspring.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class MemberRepositoryTest {
    MemberRepository repository = new MemberRepository();

    @Test
    void crud() throws SQLException {
        // save
        final Member member = new Member("memberV2", 10000);
        repository.save(member);

        // find
        final Member foundMember = repository.findById(member.getMemberId());
        log.info("foundMember={}", foundMember);
        assertThat(member).isEqualTo(foundMember);

        // update
        repository.update(member.getMemberId(), 20000);
        final Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
    }
}
