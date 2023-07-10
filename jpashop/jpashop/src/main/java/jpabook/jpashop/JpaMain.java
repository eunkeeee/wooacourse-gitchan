package jpabook.jpashop;

import jpabook.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        final EntityManager em = emf.createEntityManager();

        final EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member1 = new Member();
            member1.setName("깃짱");
            em.persist(member1);

            List<Member> result = em.createNativeQuery(
                    "select MEMBER_ID, city, street, zipcode, name FROM member",
                            Member.class
                    )
                    .getResultList();
            for (Member member : result) {
                System.out.println("member = " + member);
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
