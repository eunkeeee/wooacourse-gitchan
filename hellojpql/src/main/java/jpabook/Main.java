package jpabook;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        final EntityManager em = emf.createEntityManager();

        final EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("gitchan");
            member.setAge(23);
            em.persist(member);

            List<MemberDto> result = em.createQuery(
                            "select new jpabook.MemberDto(m.username, m.age) from Member m",
                            MemberDto.class
                    )
                    .getResultList();

            for (MemberDto memberDto : result) {
                System.out.println("memberDto = " + memberDto);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
