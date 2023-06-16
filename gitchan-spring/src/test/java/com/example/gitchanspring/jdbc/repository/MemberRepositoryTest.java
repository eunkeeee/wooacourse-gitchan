package com.example.gitchanspring.jdbc.repository;

import com.example.gitchanspring.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class MemberRepositoryTest {
    MemberRepository repository = new MemberRepository();

    @Test
    void crud() throws SQLException {
        final Member member = new Member("memberV0", 10000);
        repository.save(member);
    }
}
