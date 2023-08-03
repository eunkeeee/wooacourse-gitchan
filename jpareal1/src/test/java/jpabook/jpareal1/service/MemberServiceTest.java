package jpabook.jpareal1.service;

import jpabook.jpareal1.domain.Member;
import jpabook.jpareal1.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        Member member = new Member();
        member.setName("깃짱");

        Long savedId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 중복회원_예외() {
        Member member1 = new Member();
        member1.setName("깃짱");

        Member member2 = new Member();
        member2.setName("깃짱");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외가 발생해야 한다.");
    }
}
